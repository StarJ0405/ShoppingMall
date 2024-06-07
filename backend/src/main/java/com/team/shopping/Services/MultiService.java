package com.team.shopping.Services;


import com.team.shopping.DTOs.*;
import com.team.shopping.Domains.*;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Securities.CustomUserDetails;
import com.team.shopping.Securities.JWT.JwtTokenProvider;
import com.team.shopping.Services.Module.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultiService {
    private final AuthService authService;
    private final UserService userService;
    private final WishListService wishListService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CartItemDetailService cartItemDetailService;
    private final OptionsService optionsService;
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
        List<WishList> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    @Transactional
    public List<ProductResponseDTO> addToWishList(String username, ProductRequestDTO productRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productRequestDTO.getProductId());
        this.wishListService.addToWishList(user, product);
        List<WishList> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    @Transactional
    public List<ProductResponseDTO> deleteToWishList (String username, ProductRequestDTO productRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productRequestDTO.getProductId());
        this.wishListService.deleteToWishList(user, product);
        List<WishList> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    /**
     * cart
     * */

    @Transactional
    public List<CartResponseDTO> getCart(String username) {
        SiteUser user = this.userService.get(username);

        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);

        return cartItems.stream()
                .map(cartItem -> {
                    List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(cartItem);
                    return DTOConverter.toCartResponseDTO(cartItem, cartItemDetails);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CartResponseDTO> addToCart(String username, CartRequestDTO cartRequestDTO, int count) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(cartRequestDTO.getProductId());
        CartItem cartItem = this.cartItemService.save(user, product, count);

        List<Options> options = this.optionsService.getOptionsList(cartRequestDTO.getOptionIdList());

        for (Options option : options) {
            this.cartItemDetailService.save(cartItem, option);
        }

        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
        return cartItems.stream()
                .map(item -> {
                    List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
                    return DTOConverter.toCartResponseDTO(item, cartItemDetails);
                })
                .collect(Collectors.toList());
    }

}
