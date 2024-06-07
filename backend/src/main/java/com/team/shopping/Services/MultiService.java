package com.team.shopping.Services;


import com.team.shopping.DTOs.AuthRequestDTO;
import com.team.shopping.DTOs.AuthResponseDTO;
import com.team.shopping.DTOs.CategoryRequestDTO;
import com.team.shopping.DTOs.SignupRequestDTO;
import com.team.shopping.Domains.Auth;
import com.team.shopping.Domains.Category;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.UserRole;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Securities.CustomUserDetails;
import com.team.shopping.Securities.JWT.JwtTokenProvider;
import com.team.shopping.Services.Module.AuthService;
import com.team.shopping.Services.Module.CategoryService;
import com.team.shopping.Services.Module.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.xml.catalog.Catalog;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiService {
    private final AuthService authService;
    private final UserService userService;
    private final CategoryService categoryService;
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
    public SiteUser signup(SignupRequestDTO signupRequestDTO) throws DataDuplicateException {
        userService.check(signupRequestDTO);
        return userService.save(signupRequestDTO);
    }
    @Transactional
    public void deleteUser(String username, String name) {
        SiteUser targetUser = userService.get(username);
        SiteUser siteUser = userService.get(name);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            userService.delete(targetUser);
        } else {
            throw new IllegalArgumentException("ADMIN 권한이 아닙니다.");
        }
    }

    /**
     * Category
     */
    @Transactional
    public void saveCategory(String username, CategoryRequestDTO requestDto) throws DataDuplicateException{
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)){
            this.categoryService.check(requestDto);
            this.categoryService.save(requestDto);
        }else {
            throw new IllegalArgumentException("ADMIN 권한이 아닙니다.");
        }

    }

    /**
     * 테스트용
     */

    @Transactional
    public void updateCategory(CategoryRequestDTO requestDto) {
        Optional<Category> _category = categoryService.get(requestDto.getId());
        if (_category.isPresent()) {
            categoryService.updateCheck(requestDto);
            categoryService.update(_category.get(), requestDto.getNewName());
        } else throw new IllegalArgumentException("해당 ID를 가진 카테고리가 존재하지 않습니다.");
    }

    @Transactional
    public void deleteCategory(Long id) {
        Optional<Category> _category = categoryService.get(id);
        if (_category.isPresent())
            categoryService.deleteCategory(_category.get());
        else throw new IllegalArgumentException("해당 ID를 가진 카테고리가 존재하지 않습니다.");
    }


//    @Transactional
//    public void updateCategory(String username, CategoryRequestDTO requestDto) throws DataDuplicateException{
//        SiteUser siteUser = userService.get(username);
//        if (siteUser.getRole().equals(UserRole.ADMIN)){
//            categoryService.updateCheck(requestDto);
//            categoryService.update(requestDto);
//        }else {
//            throw new IllegalArgumentException("ADMIN 권한이 아닙니다.");
//        }
//    }


}
