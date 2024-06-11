package com.team.shopping.Services;


import com.team.shopping.DTOs.*;
import com.team.shopping.Domains.*;
import com.team.shopping.Enums.ImageKey;
import com.team.shopping.Enums.UserRole;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Securities.CustomUserDetails;
import com.team.shopping.Securities.JWT.JwtTokenProvider;
import com.team.shopping.Services.Module.*;
import com.team.shopping.ShoppingApplication;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MultiService {
    private final AuthService authService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final WishListService wishListService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CartItemDetailService cartItemDetailService;
    private final OptionsService optionsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileSystemService fileSystemService;
    private final PaymentLogService paymentLogService;
    private final PaymentProductService paymentProductService;
    private final PaymentProductDetailService paymentProductDetailService;


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
        if (user == null) {
            throw new IllegalArgumentException("아이디가 일치하지 않습니다.");
        }
        if (!this.userService.isMatch(requestDto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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
    public void signup(SignupRequestDTO signupRequestDTO) throws DataDuplicateException {
        userService.check(signupRequestDTO);
        userService.save(signupRequestDTO);
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

    @Transactional
    public UserResponseDTO getProfile(String username) {
        SiteUser siteUser = this.userService.get(username);
        return UserResponseDTO.builder().username(siteUser.getUsername()).gender(siteUser.getGender().toString()).role(siteUser.getRole().toString()).email(siteUser.getEmail()).point(siteUser.getPoint()).phoneNumber(siteUser.getPhoneNumber()).nickname(siteUser.getNickname()).birthday(siteUser.getBirthday()).createDate(siteUser.getCreateDate()).modifyDate(siteUser.getModifyDate()).name(siteUser.getName()).build();
    }

    @Transactional
    public UserResponseDTO updateProfile(String username, UserRequestDTO newUserRequestDTO) {
        SiteUser siteUser = userService.updateProfile(username, newUserRequestDTO);

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


    @Transactional
    public UserResponseDTO updatePassword(String username, UserRequestDTO userRequestDTO) {
        SiteUser user = userService.get(username);
        if (!this.userService.isMatch(userRequestDTO.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        SiteUser siteUser = userService.updatePassword(user, userRequestDTO.getNewPassword());
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
     * List
     */

    @Transactional
    public List<ProductResponseDTO> getWishList(String username) throws NoSuchElementException {
        SiteUser user = this.userService.get(username);
        List<Wish> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    @Transactional
    public List<ProductResponseDTO> addToWishList(String username, ProductRequestDTO productRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productRequestDTO.getProductId());
        this.wishListService.addToWishList(user, product);
        List<Wish> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }

    @Transactional
    public List<ProductResponseDTO> deleteToWishList(String username, Long productId) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productId);
        this.wishListService.deleteToWishList(user, product);
        List<Wish> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }




    @Transactional
    public List<ProductResponseDTO> deleteMultipleToWishList (String username, List<Long> productIdList) {
        SiteUser user = this.userService.get(username);
        for (Long productId : productIdList) {
            Product product = this.productService.getProduct(productId);
            this.wishListService.deleteToWishList(user, product);
        }
        List<Wish> wishList = this.wishListService.get(user);
        return DTOConverter.toProductResponseDTOList(wishList);
    }


  /**
     * cart
     */

    @Transactional
    public List<CartResponseDTO> getCart(String username) {
        SiteUser user = this.userService.get(username);
        List<CartResponseDTO> responseDTOList = new ArrayList<>();
        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);

        for (CartItem item : cartItems) {
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
            responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
        }
        return responseDTOList;
    }

    @Transactional
    public List<CartResponseDTO> addToCart(String username, CartRequestDTO cartRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(cartRequestDTO.getProductId());
        CartItem cartItem = this.cartItemService.getCartItem(user, product);

        if (cartItem != null) {
            cartItem.updateCount(cartItem.getCount() + cartRequestDTO.getCount());
        } else {
            cartItem = this.cartItemService.addToCart(user, product, cartRequestDTO.getCount());
            List<Options> options = this.optionsService.getOptionsList(cartRequestDTO.getOptionIdList());

            for (Options option : options) {
                this.cartItemDetailService.save(cartItem, option);
            }
        }

        List<CartResponseDTO> responseDTOList = new ArrayList<>();
        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);

        for (CartItem item : cartItems) {
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
            responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
        }
        return responseDTOList;
    }


    @Transactional
    public List<CartResponseDTO> updateToCart(String username, CartRequestDTO cartRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(cartRequestDTO.getProductId());
        CartItem cartItem = this.cartItemService.getCartItem(user, product);
        cartItem.updateCount(cartRequestDTO.getCount());
        this.cartItemService.save(cartItem);

        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
        List<CartResponseDTO> responseDTOList = new ArrayList<>();

        for (CartItem item : cartItems) {
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
            responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
        }

        return responseDTOList;
    }


    @Transactional
    public List<CartResponseDTO> deleteToCart(String username, Long productId) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productId);
        CartItem cartItem = this.cartItemService.getCartItem(user, product);
        if (cartItem != null) {
            this.cartItemDetailService.delete(cartItem);
            this.cartItemService.delete(cartItem);
        } else {
            System.out.println("CartItem not found for user: " + username + " and product: " + productId);
        }

        List<CartResponseDTO> responseDTOList = new ArrayList<>();
        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
        for (CartItem item : cartItems) {
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
            responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
        }
        return responseDTOList;
    }

    @Transactional
    public List<CartResponseDTO> deleteMultipleToCart(String username, List<Long> productIdList) {
        SiteUser user = this.userService.get(username);
        for (Long productId : productIdList) {
            Product product = this.productService.getProduct(productId);
            CartItem cartItem = this.cartItemService.getCartItem(user, product);
            if (cartItem != null) {
                this.cartItemDetailService.delete(cartItem);
                this.cartItemService.delete(cartItem);
            } else {
                System.out.println("CartItem not found for user: " + username + " and product: " + productId);
            }
        }

        List<CartResponseDTO> responseDTOList = new ArrayList<>();
        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
        for (CartItem item : cartItems) {
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
            responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
        }
        return responseDTOList;
    }

    /**
     * Payment
     * */

    @Transactional
    public List<PaymentLogResponseDTO> addPaymentLog(String username, PaymentLogRequestDTO paymentLogRequestDTO) {
        SiteUser user = this.userService.get(username);
        List<CartItem> cartItemList = this.cartItemService.getList(paymentLogRequestDTO.getCartItemIdList());

        // PaymentLog 생성 및 저장
        PaymentLog paymentLog = this.paymentLogService.save(user);

        List<PaymentProduct> paymentProductList = new ArrayList<>();
        List<PaymentLogResponseDTO> paymentLogResponseDTOList = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();
            SiteUser seller = product.getSeller();

            // PaymentProduct 생성 및 저장
            PaymentProduct paymentProduct = paymentProductService.save(paymentLog, product, seller, cartItem);
            paymentProductList.add(paymentProduct);

            // PaymentProductDetail 생성 및 저장
            List<CartItemDetail> cartItemDetailList = this.cartItemDetailService.getList(cartItem);
            for (CartItemDetail cartItemDetail : cartItemDetailList) {
                Options option = cartItemDetail.getOptions();
                PaymentProductDetail paymentProductDetail = this.paymentProductDetailService.save(paymentProduct, option);
            }
        }

        // PaymentLogResponseDTO 생성
        PaymentLogResponseDTO paymentLogResponseDTO = DTOConverter.toPaymentLogResponseDTO(paymentLog, paymentProductList);
        paymentLogResponseDTOList.add(paymentLogResponseDTO);

        // CartItem 및 관련 CartItemDetail 삭제
        for (CartItem cartItem : cartItemList) {
            this.cartItemDetailService.deleteByCartItem(cartItem);
            this.cartItemService.delete(cartItem);
        }

        return paymentLogResponseDTOList;
    }

    /**
     * Product
     */

    @Transactional
    public void saveProduct(ProductCreateRequestDTO requestDTO, String username) {
        SiteUser user = this.userService.get(username);
        if (user == null) {
            throw new NoSuchElementException("해당 유저가 존재하지 않습니다.");
        }
        if (user.getRole() == UserRole.USER) {
            throw new IllegalArgumentException("user 권한은 상품을 저장할 수 없습니다.");
        }
        Category category = this.categoryService.get(requestDTO.getCategoryId());
        Product product = this.productService.save(requestDTO, user, category);
        String newFile = "/api/product" + "_" + product.getId() + "/";
        String newUrl = this.fileMove(requestDTO.getUrl(), newFile);
        if (newUrl != null) {
            fileSystemService.save(ImageKey.Product.getKey(product.getId().toString()), newUrl);
            fileSystemService.delete(username);
        }

    }

    /**
     * Image
     */
    @Transactional
    public String fileMove(String url, String newUrl) {
        try {
            String path = ShoppingApplication.getOsType().getLoc();
            Path tempPath = Paths.get(path + url);
            Path newPath = Paths.get(path + newUrl + tempPath.getFileName());
            Files.createDirectories(newPath.getParent());
            Files.move(tempPath, newPath);
            return newUrl + tempPath.getFileName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ImageResponseDTO tempUpload(ImageRequestDTO requestDTO, String username) {
        if (!requestDTO.getFile().isEmpty()) try {
            String path = ShoppingApplication.getOsType().getLoc();
            String tempUrl = fileSystemService.get(username);
            if (tempUrl != null) {
                File file = new File(path + tempUrl);
                if (file.exists()) {
                    file.delete();
                    fileSystemService.delete(username);
                }
            }
            UUID uuid = UUID.randomUUID();
            String fileLoc = "/api/users" + "_" + username + "/temp/" + uuid + "." + requestDTO.getFile().getContentType().split("/")[1];
            File file = new File(path + fileLoc);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            requestDTO.getFile().transferTo(file);
            if (fileLoc != null) {
                fileSystemService.save(ImageKey.Temp.getKey(username), fileLoc);
            }
            return new ImageResponseDTO(fileLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Category
     */
    @Transactional
    public void saveCategory(String username, CategoryRequestDTO requestDto) throws DataDuplicateException {
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            this.categoryService.check(requestDto);
            this.categoryService.save(requestDto);
        } else {
            throw new IllegalArgumentException("ADMIN 권한이 아닙니다.");
        }
    }

    @Transactional
    public void deleteCategory(String username, Long id) {
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            Category category = categoryService.get(id);
            categoryService.deleteCategory(category);
        } else {
            throw new IllegalArgumentException("ADMIN 권한이 아닙니다.");
        }

    }


    @Transactional
    public void updateCategory(String username, CategoryRequestDTO requestDto) throws DataDuplicateException {
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            Category category = categoryService.get(requestDto.getId());
            categoryService.updateCheck(requestDto);
            categoryService.update(category, requestDto.getNewName());
        } else {
            throw new IllegalArgumentException("ADMIN 권한이 아닙니다.");
        }
    }

    @Transactional
    public List<CategoryResponseDTO> getCategoryList() {
        List<Category> categoryList = categoryService.findByParentIsNull();
        List<CategoryResponseDTO> result = new ArrayList<>();
        for (Category parentCategory : categoryList) {
            CategoryResponseDTO responseDTO = this.getCategoryWithChildren(parentCategory);
            result.add(responseDTO);
        }
        return result;
    }
    @Transactional
    private CategoryResponseDTO getCategoryWithChildren(Category parentCategory) {
        List<CategoryResponseDTO> childrenDTOList = new ArrayList<>();
        for (Category child : parentCategory.getChildren()) {
            childrenDTOList.add(getCategoryWithChildren(child));
        }
        return new CategoryResponseDTO(parentCategory.getId(),parentCategory.getName(), childrenDTOList);
    }
}

