package com.team.shopping.Services;


import com.team.shopping.DTOs.*;
import com.team.shopping.Domains.Auth;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Domains.WishList;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Securities.CustomUserDetails;
import com.team.shopping.Securities.JWT.JwtTokenProvider;
import com.team.shopping.Services.Module.AuthService;
import com.team.shopping.Services.Module.ProductService;
import com.team.shopping.Services.Module.UserService;
import com.team.shopping.Services.Module.WishListService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MultiService {
    private final AuthService authService;
    private final UserService userService;
    private final WishListService wishListService;
    private final ProductService productService;
    //
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Auth
     */

    public TokenRecord checkToken(String accessToken) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        String username = null;
        if (accessToken != null && accessToken.length() > 7) {
            String token = accessToken.substring(7);
            if (this.jwtTokenProvider.validateToken(token)) {
                httpStatus = HttpStatus.OK;
                username = this.jwtTokenProvider.getUsernameFromToken(token);
            } else httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return TokenRecord.builder().httpStatus(httpStatus).username(username).build();
    }

    @Transactional
    public String refreshToken(String refreshToken) {
        if (this.jwtTokenProvider.validateToken(refreshToken)) {
            Auth auth = this.authService.get(refreshToken);
            String newAccessToken = this.jwtTokenProvider //
                    .generateAccessToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(auth.getUser()), auth.getUser().getPassword()));
            auth.setAccessToken(newAccessToken);
            return newAccessToken;
        }
        return null;
    }

    @Transactional
    public AuthResponseDTO login(AuthRequestDTO requestDto) {
        SiteUser user = this.userService.get(requestDto.getUsername());
        if (!this.userService.isMatch(requestDto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. username = " + requestDto.getUsername());
        String accessToken = this.jwtTokenProvider //
                .generateAccessToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), user.getPassword()));
        String refreshToken = this.jwtTokenProvider //
                .generateRefreshToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), user.getPassword()));
        if (this.authService.isExist(user)) {
            user.getAuth().setAccessToken(accessToken);
            user.getAuth().setRefreshToken(refreshToken);
            return new AuthResponseDTO(user.getAuth());
        }
        return new AuthResponseDTO(authService.save(user, accessToken, refreshToken));
    }

    /**
     * User
     */

    @Transactional
    public SiteUser signup(SignupRequestDTO signupRequestDTO) throws DataDuplicateException{
        userService.check(signupRequestDTO);
        SiteUser user = userService.save(signupRequestDTO);
        WishList wishList = WishList.builder()
                .user(user)
                .build();
        this.wishListService.save(wishList);
        return user;
    }

    @Transactional
    public UserResponseDTO getProfile (String username) {
        SiteUser siteUser = this.userService.get(username);
        return UserResponseDTO.builder()
                .username(siteUser.getUsername())
                .gender(siteUser.getGender().toString())
                .email(siteUser.getEmail())
                .point(siteUser.getPoint())
                .phoneNumber(siteUser.getPhoneNumber())
                .nickname(siteUser.getNickname())
                .birthday(siteUser.getBirthday())
                .createDate(siteUser.getCreateDate())
                .modifyDate(siteUser.getModifyDate())
                .name(siteUser.getName())
                .build();
    }

    /**
    * WishList
    * */

    @Transactional
    public List<ProductResponseDTO> getWishList (String username) throws NoSuchElementException {
        SiteUser user = this.userService.get(username);
        WishList wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    @Transactional
    public List<ProductResponseDTO> addToWishList(String username, ProductRequestDTO productRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productRequestDTO);
        WishList wishList = this.wishListService.addToWishList(user, product);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    @Transactional
    public List<ProductResponseDTO> deleteToWishList (String username, ProductRequestDTO productRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productRequestDTO);
        WishList wishList = this.wishListService.deleteToWishList(user, product);
        return DTOConverter.toProductResponseDTOList(wishList);
    }
}
