package com.team.shopping.Services;


import com.team.shopping.DTOs.*;
import com.team.shopping.Domains.*;
import com.team.shopping.Enums.ImageKey;
import com.team.shopping.Enums.Type;
import com.team.shopping.Enums.UserRole;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Exceptions.DataNotFoundException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Securities.CustomUserDetails;
import com.team.shopping.Securities.JWT.JwtTokenProvider;
import com.team.shopping.Services.Module.*;
import com.team.shopping.ShoppingApplication;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private final OptionListService optionListService;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileSystemService fileSystemService;
    private final ProductQAService productQAService;
    private final PaymentLogService paymentLogService;
    private final PaymentProductService paymentProductService;
    private final PaymentProductDetailService paymentProductDetailService;
    private final AddressService addressService;
    private final TagService tagService;
    private final ReviewService reviewService;
    private final ArticleService articleService;
    private final RecentService recentService;


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
            throw new IllegalArgumentException("username");
        }
        if (!this.userService.isMatch(requestDto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("password");
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
        userService.usernameCheck(signupRequestDTO.getUsername());
        userService.otherCheck(signupRequestDTO.getEmail(), signupRequestDTO.getNickname(), signupRequestDTO.getPhoneNumber());
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
        if (siteUser == null)
            throw new DataNotFoundException("유저가 없습니다.");
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.USER.getKey(username));
        String url = null;

        if (_fileSystem.isPresent())
            url = _fileSystem.get().getV();

        return UserResponseDTO.builder()
                .username(siteUser.getUsername())
                .gender(siteUser.getGender().toString())
                .role(siteUser.getRole().toString())
                .email(siteUser.getEmail())
                .point(siteUser.getPoint())
                .phoneNumber(siteUser.getPhoneNumber())
                .nickname(siteUser.getNickname())
                .birthday(siteUser.getBirthday())
                .createDate(siteUser.getCreateDate())
                .modifyDate(siteUser.getModifyDate())
                .name(siteUser.getName())
                .url(url)
                .build();
    }

    @Transactional
    public UserResponseDTO updateProfile(String username, UserRequestDTO newUserRequestDTO) {
        userService.otherCheck(newUserRequestDTO.getEmail(), newUserRequestDTO.getNickname(), newUserRequestDTO.getPhoneNumber());

        SiteUser siteUser = userService.updateProfile(username, newUserRequestDTO);
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.USER.getKey(username));
        String path = ShoppingApplication.getOsType().getLoc();
        // 삭제 or 동일하지 않는 경우만 진행
        if (_fileSystem.isPresent() && (newUserRequestDTO.getUrl() == null || !_fileSystem.get().getV().equals(newUserRequestDTO.getUrl()))) {
            File old = new File(path + _fileSystem.get().getV());
            if (old.exists())
                old.delete();
        }
        String newUrl = null;
        // 새로 생성
        if (newUserRequestDTO.getUrl() != null && !newUserRequestDTO.getUrl().isBlank()) {
            String newFile = "/api/user" + "_" + siteUser.getUsername() + "/";
            newUrl = this.fileMove(newUserRequestDTO.getUrl(), newFile, ImageKey.TEMP.getKey(username));
            if (newUrl != null)
                fileSystemService.save(ImageKey.USER.getKey(username), newUrl);
        }
        return UserResponseDTO.builder().username(siteUser.getUsername()).gender(siteUser.getGender().toString()).email(siteUser.getEmail()).point(siteUser.getPoint()).phoneNumber(siteUser.getPhoneNumber()).nickname(siteUser.getNickname()).birthday(siteUser.getBirthday()).createDate(siteUser.getCreateDate()).modifyDate(siteUser.getModifyDate())
                .url(newUrl).name(siteUser.getName()).build();
    }


    @Transactional
    public UserResponseDTO updatePassword(String username, UserRequestDTO userRequestDTO) {
        SiteUser user = userService.get(username);
        if (!this.userService.isMatch(userRequestDTO.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("not match");
        SiteUser siteUser = userService.updatePassword(user, userRequestDTO.getNewPassword());

        return UserResponseDTO.builder().username(siteUser.getUsername()).gender(siteUser.getGender().toString()).email(siteUser.getEmail()).point(siteUser.getPoint()).phoneNumber(siteUser.getPhoneNumber()).nickname(siteUser.getNickname()).birthday(siteUser.getBirthday()).createDate(siteUser.getCreateDate()).modifyDate(siteUser.getModifyDate()).name(siteUser.getName()).build();
    }


    /**
     * List
     */

    public boolean checkWishList(String username, Long product_id) {
        SiteUser user = userService.get(username);
        Product product = productService.getProduct(product_id);
        if (user == null || product == null)
            return false;
        else
            return this.wishListService.get(user, product).isPresent();
    }

    @Transactional
    public List<ProductResponseDTO> getWishList(String username) throws NoSuchElementException {
        SiteUser user = this.userService.get(username);
        List<Wish> wishList = this.wishListService.get(user);
        return wishList.stream().map(wish -> getProduct(wish.getProduct())).toList();
    }

    @Transactional
    public List<ProductResponseDTO> addToWishList(String username, ProductRequestDTO productRequestDTO) {
        SiteUser user = this.userService.get(username);
        List<Wish> _wishList = this.wishListService.get(user);
        Product product = this.productService.getProduct(productRequestDTO.getProductId());
        for (Wish wish : _wishList) {
            if (wish.getProduct().equals(product)) {
                throw new IllegalArgumentException("already have to wishList");
            }
        }
        this.wishListService.addToWishList(user, product);
        List<Wish> wishList = this.wishListService.get(user);

        return wishList.stream().map(wish -> getProduct(wish.getProduct())).toList();
    }

    @Transactional
    public List<ProductResponseDTO> deleteToWishList(String username, Long productId) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productId);
        if (product == null) {
            throw new IllegalArgumentException("already deleted or not found product");
        }
        this.wishListService.deleteToWishList(user, product);
        List<Wish> wishList = this.wishListService.get(user);
        return wishList.stream().map(wish -> getProduct(wish.getProduct())).toList();
    }


    @Transactional
    public List<ProductResponseDTO> deleteMultipleToWishList(String username, List<Long> productIdList) {
        SiteUser user = this.userService.get(username);
        for (Long productId : productIdList) {
            Product product = this.productService.getProduct(productId);
            if (product == null) {
                throw new IllegalArgumentException("already deleted or not found product");
            } else {
                this.wishListService.deleteToWishList(user, product);
            }
        }
        List<Wish> wishList = this.wishListService.get(user);
        return wishList.stream().map(wish -> getProduct(wish.getProduct())).toList();
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
            if (item.getCount() == 0) {
                this.cartItemDetailService.deleteByCartItem(item);
                this.cartItemService.delete(item);
            }
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
            responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
        }
        return responseDTOList;
    }

    @Transactional
    public List<CartResponseDTO> selectCart(String username, List<Long> cartItemIdList) {
        SiteUser user = this.userService.get(username);
        List<CartItem> cartItemList = this.cartItemService.getCartItemList(user);
        List<CartResponseDTO> cartResponseDTOList = new ArrayList<>();

        // cartItemIdList에 포함된 카트 아이템 ID와 일치하는 카트 아이템들만 선택하여 처리
        for (CartItem cartItem : cartItemList) {
            if (cartItemIdList.contains(cartItem.getId())) {
                List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(cartItem);
                cartResponseDTOList.add(DTOConverter.toCartResponseDTO(cartItem, cartItemDetails));
            }
        }
        return cartResponseDTOList;
    }

    @Transactional
    public List<CartResponseDTO> addToCart(String username, CartRequestDTO cartRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(cartRequestDTO.getProductId());
        CartItem cartItem = this.cartItemService.getCartItem(user, product);

        if (cartItem != null) {
            cartItem.updateCount(cartItem.getCount() + cartRequestDTO.getCount());
            this.cartItemService.save(cartItem);
        } else {
            cartItem = this.cartItemService.addToCart(user, product, cartRequestDTO.getCount());
        }

        List<Options> options = this.optionsService.getOptionsList(cartRequestDTO.getOptionIdList());
        List<CartItemDetail> cartItemDetailList = this.cartItemDetailService.getList(cartItem);

        for (Options option : options) {
            if (option.getCount() <= 0) {
                throw new NoSuchElementException("option count 0");
            }

            boolean found = false;
            for (CartItemDetail cartItemDetail : cartItemDetailList) {
                if (cartItemDetail.getOptions().getId().equals(option.getId())) {
                    cartItemDetail.getOptions().setCount(cartItemDetail.getOptions().getCount());
                    this.cartItemDetailService.save(cartItemDetail);
                    found = true;
                    break;
                }
            }

            if (!found) {
                this.cartItemDetailService.saveCartItemDetail(cartItem, option);
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
        if (cartItem.getCount() > product.getRemain()) {
            throw new IllegalArgumentException("a lot your item count more than product remain");
        } else {
            this.cartItemService.save(cartItem);

            List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
            List<CartResponseDTO> responseDTOList = new ArrayList<>();

            for (CartItem item : cartItems) {
                List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(item);
                responseDTOList.add(DTOConverter.toCartResponseDTO(item, cartItemDetails));
            }

            return responseDTOList;
        }
    }


    @Transactional
    public List<CartResponseDTO> deleteToCart(String username, Long productId) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(productId);
        CartItem cartItem = this.cartItemService.getCartItem(user, product);
        if (cartItem != null) {
            this.cartItemDetailService.deleteByCartItem(cartItem);
            this.cartItemService.delete(cartItem);
        } else {
            throw new IllegalArgumentException("CartItem not found for user: " + username + " and product: " + productId);
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
                this.cartItemDetailService.deleteByCartItem(cartItem);
                this.cartItemService.delete(cartItem);
            } else {
                throw new IllegalArgumentException("CartItem not found for user: " + username + " and product: " + productId);
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
     */

    @Transactional
    public List<PaymentLogResponseDTO> getPaymentLogList(String username) {
        SiteUser user = this.userService.get(username);
        List<PaymentLog> paymentLogList = this.paymentLogService.get(user);

        if (paymentLogList == null) {
            return null;
        }

        List<PaymentLogResponseDTO> paymentLogResponseDTOList = new ArrayList<>();

        for (PaymentLog paymentLog : paymentLogList) {
            List<PaymentProduct> paymentProductList = this.paymentProductService.getList(paymentLog);
            List<PaymentProductResponseDTO> paymentProductResponseDTOList = new ArrayList<>();

            for (PaymentProduct paymentProduct : paymentProductList) {
                List<PaymentProductDetail> paymentProductDetailList = this.paymentProductDetailService.getList(paymentProduct);

                PaymentProductResponseDTO paymentProductResponseDTO = DTOConverter.toPaymentProductResponseDTO(paymentProduct, paymentProductDetailList);
                paymentProductResponseDTOList.add(paymentProductResponseDTO);
            }

            PaymentLogResponseDTO paymentLogResponseDTO = DTOConverter.toPaymentLogResponseDTO(paymentLog, paymentProductResponseDTOList);
            paymentLogResponseDTOList.add(paymentLogResponseDTO);
        }

        return paymentLogResponseDTOList;
    }


    @Transactional
    public PaymentLogResponseDTO addPaymentLog(String username,
                                               PaymentLogRequestDTO paymentLogRequestDTO) {
        SiteUser user = this.userService.get(username);
        Address address = this.addressService.get(paymentLogRequestDTO.getAddressId());
        List<CartItem> cartItemList = this.cartItemService.getList(paymentLogRequestDTO.getCartItemIdList());

        // 장바구니 항목이 없는 경우 예외 처리
        if (cartItemList.isEmpty()) {
            throw new NoSuchElementException("Cart is empty, cannot proceed with payment.");
        }

        // 상품 재고 및 장바구니 항목 수량 검증
        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            if (product.getRemain() <= 0 || product.getRemain() - cartItem.getCount() < 0) {
                cartItem.setCount(product.getRemain());
                this.cartItemService.save(cartItem);
                throw new IllegalArgumentException("This product remain is empty");
            }

            if (cartItem.getCount() == 0) {
                throw new NoSuchElementException("Please set this product count");
            }
        }

        // 결제 로그 생성
        PaymentLog paymentLog = this.paymentLogService.save(user, address);

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            // 결제 상품 생성 및 저장
            PaymentProduct paymentProduct = this.paymentProductService.save(paymentLog, product, cartItem);

            // 상품 재고 감소
            product.setRemain(product.getRemain() - paymentProduct.getCount());
            this.productService.save(product);

            // 장바구니 상세 항목 처리
            List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(cartItem);
            for (CartItemDetail cartItemDetail : cartItemDetails) {
                Options option = cartItemDetail.getOptions();

                // 결제 상품 상세 항목 생성 및 저장
                PaymentProductDetail paymentProductDetail = this.paymentProductDetailService.save(paymentProduct, option);

                // 옵션 재고 감소
                option.setCount(option.getCount() - paymentProductDetail.getOptionCount());
                this.optionsService.save(option);

                // 장바구니 상세 항목 삭제
                this.cartItemDetailService.delete(cartItemDetail);
            }

            // 장바구니 항목 삭제
            this.cartItemService.delete(cartItem);
        }

        // 결제 상품 리스트 가져오기
        List<PaymentProduct> paymentProductList = this.paymentProductService.getList(paymentLog);
        List<PaymentProductResponseDTO> paymentProductResponseDTOList = new ArrayList<>();
        for (PaymentProduct paymentProduct : paymentProductList) {
            List<PaymentProductDetail> paymentProductDetailList = this.paymentProductDetailService.getList(paymentProduct);
            PaymentProductResponseDTO paymentProductResponseDTO = DTOConverter.toPaymentProductResponseDTO(paymentProduct, paymentProductDetailList);
            paymentProductResponseDTOList.add(paymentProductResponseDTO);
        }

        return DTOConverter.toPaymentLogResponseDTO(paymentLog, paymentProductResponseDTOList);
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
        Product product = this.productService.saveProduct(requestDTO, user, category);
        if (requestDTO.getTagList() != null) {
            for (String tagName : requestDTO.getTagList()) {
                tagService.save(tagName, product);
            }
        }
        if (requestDTO.getOptionLists() != null) {
            for (OptionListRequestDTO optionListRequestDTO : requestDTO.getOptionLists()) {
                OptionList optionList = optionListService.save(optionListRequestDTO.getName(), product);
                for (OptionRequestDTO optionRequestDTO : optionListRequestDTO.getChild()) {
                    optionsService.saveOption(optionRequestDTO.getCount(), optionRequestDTO.getName(), optionRequestDTO.getPrice(), optionList);
                }
            }
        }
        if (requestDTO.getUrl() != null && !requestDTO.getUrl().isBlank()) {
            String newFile = "/api/product" + "_" + product.getId() + "/";
            String newUrl = this.fileMove(requestDTO.getUrl(), newFile, ImageKey.TEMP.getKey(username));
            if (newUrl != null) {
                String path = ShoppingApplication.getOsType().getLoc();
                File file = new File(path + requestDTO.getUrl());
                if (file.exists()) {
                    file.delete();
                }
                fileSystemService.save(ImageKey.PRODUCT.getKey(product.getId().toString()), newUrl);
            }
        }
    }

    @Transactional
    public ProductResponseDTO getProduct(Long productID) {
        Product product = productService.getProduct(productID);
        if (product == null) {
            throw new NoSuchElementException("not product");
        }
        return getProduct(product);
    }

    private ProductResponseDTO getProduct(Product product) {
        List<String> tagList = tagService.findByProduct(product);
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.PRODUCT.getKey(product.getId().toString()));
        List<Review> reviewList = this.reviewService.getList(product);
        String url = _fileSystem.map(FileSystem::getV).orElse(null);
        Map<String, Integer> numOfGrade = new HashMap<>();

        numOfGrade.put("0", 0);
        numOfGrade.put("0.5~1", 0);
        numOfGrade.put("1.5~2", 0);
        numOfGrade.put("2.5~3", 0);
        numOfGrade.put("3.5~4", 0);
        numOfGrade.put("4.5~5", 0);

        Double totalGrade = 0.0;
        for (Review review : reviewList) {
            double grade = review.getGrade();
            totalGrade += grade;

            if (grade == 0) {
                numOfGrade.put("0", numOfGrade.get("0") + 1);
            } else if (grade <= 1) {
                numOfGrade.put("0.5~1", numOfGrade.get("0.5~1") + 1);
            } else if (grade <= 2) {
                numOfGrade.put("1.5~2", numOfGrade.get("1.5~2") + 1);
            } else if (grade <= 3) {
                numOfGrade.put("2.5~3", numOfGrade.get("2.5~3") + 1);
            } else if (grade <= 4) {
                numOfGrade.put("3.5~4", numOfGrade.get("3.5~4") + 1);
            } else if (grade <= 5) {
                numOfGrade.put("4.5~5", numOfGrade.get("4.5~5") + 1);
            }
        }
        Double averageGrade = totalGrade / reviewList.size();

        if (averageGrade <= 0) {
            averageGrade = 0.0;
        } else {
            averageGrade = Math.round(averageGrade * 10) / 10.0;
        }

        return ProductResponseDTO
                .builder()
                .product(product)
                .tagList(tagList)
                .url(url)
                .reviewList(reviewList)
                .averageGrade(averageGrade)
                .numOfGrade(numOfGrade)
                .build();
    }

    @Transactional
    public List<ProductResponseDTO> getProductList() {
        List<Product> productList = productService.getProductList();
        List<ProductResponseDTO> responseDTOList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponseDTO productResponseDTO = this.getProduct(product);
            responseDTOList.add(productResponseDTO);
        }
        return responseDTOList;
    }
    public List<ProductResponseDTO> getBestList() {
        List<Product> bestList = new ArrayList<>();
        List<Product> productList = this.productService.getProductList();
        Map<Product, Double> productAverageGrades = new HashMap<>();

        // 제품별 평균 평점 계산하여 맵에 담기
        for (Product product : productList) {
            List<Review> reviewList = this.reviewService.getList(product);
            double averageGrade = 0.0;
            if (!reviewList.isEmpty()) {
                double totalGrade = 0.0;
                for (Review review : reviewList) {
                    totalGrade += review.getGrade();
                }
                averageGrade = totalGrade / reviewList.size();
            }
            productAverageGrades.put(product, averageGrade);
        }

        // 평균 평점으로 정렬하여 상위 20개의 제품을 선택
        List<Map.Entry<Product, Double>> sortedEntries = new ArrayList<>(productAverageGrades.entrySet());
        sortedEntries.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue())); // 내림차순 정렬

        int count = 0;
        for (Map.Entry<Product, Double> entry : sortedEntries) {
            if (count >= 15) {
                break;
            }
            bestList.add(entry.getKey());
            count++;
        }

        // ProductResponseDTO로 변환하여 반환
        List<ProductResponseDTO> responseDTOList = new ArrayList<>();
        for (Product product : bestList) {

            ProductResponseDTO productResponseDTO = this.getProduct(product);
            responseDTOList.add(productResponseDTO);
        }
        return responseDTOList;
    }

    @Transactional
    public Page<ProductResponseDTO> getLatestList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> productPage = productService.getLatestList(pageable);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (Product product : productPage) {
            ProductResponseDTO productResponseDTO = this.getProduct(product);
            productResponseDTOList.add(productResponseDTO);
        }
        return new PageImpl<>(productResponseDTOList, pageable, productPage.getTotalElements());
    }

    @Transactional
    public void productQASave(String username, ProductQARequestDTO requestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = productService.getProduct(requestDTO.getProductId());
        if (user != null && product != null && user.getRole().equals(UserRole.USER))
            this.productQAService.save(requestDTO.getTitle(), user, product);
    }

    @Transactional
    public void productQAUpdate(String username, ProductQARequestDTO requestDTO) {
        SiteUser user = this.userService.get(username);
        Optional<ProductQA> _productQA = productQAService.getProductQA(requestDTO.getProductQAId());
        Product product = productService.getProduct(requestDTO.getProductId());
        if (user != null && product != null && user.getRole().equals(UserRole.SELLER))
            this.productQAService.update(requestDTO.getContent(), user, _productQA.get());
    }

    /**
     * Image
     */

    @Transactional
    public String fileMove(String url, String newUrl, String k) {
        try {
            String path = ShoppingApplication.getOsType().getLoc();
            Path tempPath = Paths.get(path + url);
            Path newPath = Paths.get(path + newUrl + tempPath.getFileName());

            Files.createDirectories(newPath.getParent());
            Files.move(tempPath, newPath);
            File file = tempPath.toFile();
            if (file.exists())
                file.delete();
            return newUrl + tempPath.getFileName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ImageResponseDTO tempUpload(ImageRequestDTO requestDTO, String username) {
        if (!requestDTO.getFile().isEmpty()) try {
            String path = ShoppingApplication.getOsType().getLoc();
            Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.TEMP.getKey(username));
            if (_fileSystem.isPresent()) {
                FileSystem fileSystem = _fileSystem.get();
                File file = new File(path + fileSystem.getV());
                if (file.exists())
                    file.delete();
            }
            UUID uuid = UUID.randomUUID();
            String fileLoc = "/api/user" + "_" + username + "/temp/" + uuid + "." + requestDTO.getFile().getContentType().split("/")[1];
            File file = new File(path + fileLoc);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            requestDTO.getFile().transferTo(file);
            if (fileLoc != null) {
                fileSystemService.save(ImageKey.TEMP.getKey(username), fileLoc);
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
    public void saveCategory(String username, CategoryRequestDTO requestDto) {
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            this.categoryService.check(requestDto);
            this.categoryService.save(requestDto);
        }
    }


    @Transactional
    public void deleteCategory(String username, Long id) {
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            Category category = categoryService.get(id);
            if (category != null)
                categoryService.deleteCategory(category);
        }
    }


    @Transactional
    public void updateCategory(String username, CategoryRequestDTO requestDto) {
        SiteUser siteUser = userService.get(username);
        if (siteUser.getRole().equals(UserRole.ADMIN)) {
            Category category = categoryService.get(requestDto.getId());
            if (category != null) {
                categoryService.updateCheck(requestDto);
                categoryService.update(category, requestDto.getNewName());
            }
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
        return CategoryResponseDTO.builder().id(parentCategory.getId()).parent_name(parentCategory.getParent() != null ? parentCategory.getParent().getName() : null).name(parentCategory.getName()).categoryResponseDTOList(childrenDTOList).build();
    }

    /**
     * Review
     */

    private ReviewResponseDTO getReview (Review review) {
        String _username = review.getAuthor().getUsername();
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.USER.getKey(_username));
        String url = null;

        if (_fileSystem.isPresent())
            url = _fileSystem.get().getV();

        return ReviewResponseDTO.builder()
                .url(url)
                .review(review)
                .user(review.getAuthor())
                .build();
    }

    @Transactional
    public List<ReviewResponseDTO> getReviewList(Long productId) {
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        Product product = this.productService.getProduct(productId);
        List<Review> reviewList = this.reviewService.getList(product);

        for (Review review : reviewList) {
            ReviewResponseDTO reviewResponseDTO = this.getReview(review);
            reviewResponseDTOList.add(reviewResponseDTO);
        }
        return reviewResponseDTOList;
    }

    @Transactional
    public List<ReviewResponseDTO> addToReview(String username, ReviewRequestDTO reviewRequestDTO) {
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(reviewRequestDTO.getProductId());

        // 사용자의 구매 기록을 가져옴
        List<PaymentLog> paymentLogList = this.paymentLogService.get(user);
        boolean hasPurchased = false;

        for (PaymentLog paymentLog : paymentLogList) {
            List<PaymentProduct> paymentProductList = this.paymentProductService.getList(paymentLog);
            for (PaymentProduct paymentProduct : paymentProductList) {
                if (Objects.equals(paymentProduct.getProductId(), product.getId())) {
                    hasPurchased = true;
                    break;
                }
            }
            if (hasPurchased) {
                break;
            }
        }

        // 구매 기록이 없는 경우 IllegalArgumentException 발생
        if (!hasPurchased) {
            throw new NoSuchElementException("your paymentLogs have not this product");
        }

        // 구매 기록이 있는 경우에만 리뷰를 저장
        this.reviewService.save(user, reviewRequestDTO, product);

        // 리뷰 리스트를 가져와서 DTO로 변환하여 반환
        List<Review> reviewList = this.reviewService.getList(product);
        for (Review review : reviewList) {
            ReviewResponseDTO reviewResponseDTO = this.getReview(review);
            reviewResponseDTOList.add(reviewResponseDTO);
        }
        return reviewResponseDTOList;
    }



    @Transactional
    public void deleteReview(String username, Long reviewId) {
        Review review = this.reviewService.get(reviewId);
        SiteUser user = this.userService.get(username);
        if (review == null) {
            throw new NoSuchElementException("not found review");
        }
        if (!review.getAuthor().equals(user) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("you have not auth");
        } else {
            this.reviewService.delete(review);
        }
    }

    @Transactional
    public List<ReviewResponseDTO> updateReview(String username, ReviewRequestDTO reviewRequestDTO) {
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        SiteUser user = this.userService.get(username);
        Review review = this.reviewService.get(reviewRequestDTO.getReviewId());

        if (review.getAuthor() != user && !user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("not yours");
        } else {
            this.reviewService.update(review, reviewRequestDTO);
            List<Review> reviewList = this.reviewService.getList(review.getProduct());
            for (Review _review : reviewList) {
                ReviewResponseDTO reviewResponseDTO = this.getReview(_review);
                reviewResponseDTOList.add(reviewResponseDTO);
            }
        }
        return reviewResponseDTOList;
    }

    /**
     * article
     */

    @Transactional
    public void deleteArticle(String username, Long articleId) {
        SiteUser user = this.userService.get(username);
        Article article = this.articleService.get(articleId);
        if (!user.getUsername().equals(article.getAuthor().getUsername()) || !user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("not role");
        } else {
            this.articleService.delete(articleId);
        }

    }

    @Transactional
    public List<ArticleResponseDTO> getArticleList(int type) { // Long 을 Type 형식으로 바꿔야함
        List<ArticleResponseDTO> articleResponseDTOList = new ArrayList<>();

        List<Article> articleList = this.articleService.getArticleList(Type.values()[type]); // 예를들어 Type 인데 값이 1인거 ->결국 int 가아닌 Type이다
        for (Article article : articleList) {
            ArticleResponseDTO articleResponseDTO = ArticleResponseDTO.builder()
                    .article(article)
                    .siteUser(article.getAuthor())
                    .build();
            articleResponseDTOList.add(articleResponseDTO);
        }

        return articleResponseDTOList;
    }

    @Transactional
    public ArticleResponseDTO saveArticle(String username, ArticleRequestDTO articleRequestDTO) {
        SiteUser siteUser = this.userService.get(username);
        Article article = this.articleService.save(articleRequestDTO, siteUser);

        return ArticleResponseDTO.builder()
                .article(article)
                .siteUser(siteUser)
                .build();
    }

    @Transactional
    public ArticleResponseDTO updateArticle(String username, ArticleRequestDTO articleRequestDTO) {
        SiteUser user = userService.get(username);
        Article _article = this.articleService.get(articleRequestDTO.getArticleId());
        if (!user.getUsername().equals(_article.getAuthor().getUsername()) || !user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("not role");
        } else {
            Article article = this.articleService.update(_article, articleRequestDTO);
            return ArticleResponseDTO.builder()
                    .article(article)
                    .siteUser(article.getAuthor())
                    .build();
        }
    }

    /**
     * Recent
     */
    @Transactional
    public void saveRecent(Long productId, String username) {
        SiteUser user = userService.get(username);
        Product product = productService.getProduct(productId);
        if (user != null && product != null) {
            Optional<Recent> _recent = recentService.checkRecent(product, user);
            if (_recent.isPresent())
                this.recentService.delete(_recent.get());
            List<Recent> recentList = recentService.getRecent(user);
            if(recentList.size() >= 10){
                Recent recent = recentList.get(9);
                this.recentService.delete(recent);
            }
            recentService.save(product, user);
        }
    }

    @Transactional
    public List<ProductResponseDTO> getRecentList(String username) {
        SiteUser user = userService.get(username);
        List<Recent> recentList = recentService.getRecent(user);
        List<ProductResponseDTO> responseDTOList = new ArrayList<>();
        for (Recent recent : recentList) {
            ProductResponseDTO productResponseDTO = getProduct(recent.getProduct().getId());
            responseDTOList.add(productResponseDTO);
        }
        return responseDTOList;
    }

}


