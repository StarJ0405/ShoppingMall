package com.team.shopping.Services;


import com.team.shopping.DTOs.*;
import com.team.shopping.Domains.*;
import com.team.shopping.Enums.ImageKey;
import com.team.shopping.Enums.Sorts;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final MultiKeyService multiKeyService;
    private final EventService eventService;
    private final EventProductService eventProductService;


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
        if (siteUser == null) throw new DataNotFoundException("유저가 없습니다.");
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.USER.getKey(username));
        String url = null;

        if (_fileSystem.isPresent()) url = _fileSystem.get().getV();

        return UserResponseDTO.builder().username(siteUser.getUsername()).gender(siteUser.getGender().toString()).role(siteUser.getRole().toString()).email(siteUser.getEmail()).point(siteUser.getPoint()).phoneNumber(siteUser.getPhoneNumber()).nickname(siteUser.getNickname()).birthday(this.dateTimeTransfer(siteUser.getBirthday())).createDate(this.dateTimeTransfer(siteUser.getCreateDate())).modifyDate(this.dateTimeTransfer(siteUser.getModifyDate())).name(siteUser.getName()).url(url).build();
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
            if (old.exists()) old.delete();
        }
        // 새로 생성
        if (newUserRequestDTO.getUrl() != null && !newUserRequestDTO.getUrl().isBlank()) {
            String newFile = "/api/user" + "/" + siteUser.getUsername() + "/";
            Optional<FileSystem> _ordFileSystem = fileSystemService.get(ImageKey.TEMP.getKey(username));
            if (_ordFileSystem.isPresent()) {
                String newUrl = this.fileMove(newUserRequestDTO.getUrl(), newFile, _ordFileSystem.get());
                if (newUrl != null) fileSystemService.save(ImageKey.USER.getKey(username), newUrl);
            }
        }
        return UserResponseDTO.builder().username(siteUser.getUsername()).gender(siteUser.getGender().toString()).email(siteUser.getEmail()).point(siteUser.getPoint()).phoneNumber(siteUser.getPhoneNumber()).nickname(siteUser.getNickname()).birthday(this.dateTimeTransfer(siteUser.getBirthday())).createDate(this.dateTimeTransfer(siteUser.getCreateDate())).modifyDate(this.dateTimeTransfer(siteUser.getModifyDate())).url(_fileSystem.get().getV()).name(siteUser.getName()).build();
    }

    @Transactional
    public UserResponseDTO updatePassword(String username, UserRequestDTO userRequestDTO) {
        SiteUser user = userService.get(username);
        if (!this.userService.isMatch(userRequestDTO.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("not match");
        SiteUser siteUser = userService.updatePassword(user, userRequestDTO.getNewPassword());

        return UserResponseDTO.builder().username(siteUser.getUsername()).gender(siteUser.getGender().toString()).email(siteUser.getEmail()).point(siteUser.getPoint()).phoneNumber(siteUser.getPhoneNumber()).nickname(siteUser.getNickname()).birthday(this.dateTimeTransfer(siteUser.getBirthday())).createDate(this.dateTimeTransfer(siteUser.getCreateDate())).modifyDate(this.dateTimeTransfer(siteUser.getModifyDate())).name(siteUser.getName()).build();
    }

    /**
     * address
     */

    private List<AddressResponseDTO> getAddressDTOList(SiteUser user) {
        List<AddressResponseDTO> addressResponseDTOList = new ArrayList<>();
        List<Address> addressList = this.addressService.getList(user);

//        if (addressList.isEmpty()) {
//            throw new NoSuchElementException("not found addresses");
//        }

        for (Address address : addressList) {
            addressResponseDTOList.add(AddressResponseDTO.builder().address(address).build());
        }
        return addressResponseDTOList;
    }

    @Transactional
    public List<AddressResponseDTO> getAddressList(String username) {
        SiteUser user = this.userService.get(username);
        return this.getAddressDTOList(user);
    }

    @Transactional
    public List<AddressResponseDTO> createAddress(String username, AddressRequestDTO addressRequestDTO) {
        SiteUser user = this.userService.get(username);
        this.addressService.saveAddress(user, addressRequestDTO);

        return this.getAddressDTOList(user);
    }

    @Transactional
    public List<AddressResponseDTO> updateAddress(String username, AddressRequestDTO addressRequestDTO) {
        SiteUser user = this.userService.get(username);
        Address _address = this.addressService.get(addressRequestDTO.getAddressId());

        if (!username.equals(_address.getUser().getUsername()) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("not role");
        }
        this.addressService.updateAddress(addressRequestDTO);

        return this.getAddressDTOList(user);
    }

    @Transactional
    public List<AddressResponseDTO> deleteAddress(String username, List<Long> addressIdList) {
        SiteUser user = this.userService.get(username);
        for (Long addressId : addressIdList) {
            Address address = this.addressService.get(addressId);
            if (!username.equals(address.getUser().getUsername()) && !user.getRole().equals(UserRole.ADMIN)) {
                throw new IllegalArgumentException("not role");
            } else {
                this.addressService.delete(address);
            }
        }
        return this.getAddressDTOList(user);
    }


    /**
     * Wish
     */

    @Transactional
    public void deleteWish(Product product) {
        Optional<Wish> _wish = wishListService.findByProduct(product);
        _wish.ifPresent(wishListService::delete);
    }

    public boolean checkWishList(String username, Long product_id) {
        SiteUser user = userService.get(username);
        Product product = productService.getProduct(product_id);
        if (user == null || product == null) return false;
        else return this.wishListService.get(user, product).isPresent();
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

    private CartResponseDTO createCartResponseDTO(CartItem cartItem) {
        List<CartItemDetail> cartItemDetails = this.cartItemDetailService.getList(cartItem);
        Double discount = this.getProductDiscount(cartItem.getProduct());
        int discountPrice = this.getProductDiscountPrice(cartItem.getProduct(), discount);
        String url = this.getImageUrl(cartItem.getProduct());
        return DTOConverter.toCartResponseDTO(cartItem, cartItemDetails, discount, discountPrice, url);
    }

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
            responseDTOList.add(this.createCartResponseDTO(item));
        }
        return responseDTOList;
    }

    @Transactional
    public void deleteCart(Product product) {
        Optional<CartItem> _cartItem = cartItemService.findByProduct(product);
        // 카트아이템 찾아서 카트 디테일도 먼저 삭제시키고 진행해야함
        if (_cartItem.isPresent()) {
            List<CartItemDetail> cartItemDetailList = cartItemDetailService.getList(_cartItem.get());
            if (cartItemDetailList != null) {
                for (CartItemDetail cartItemDetail : cartItemDetailList)
                    cartItemDetailService.delete(cartItemDetail);

                cartItemService.delete(_cartItem.get());
            }
        }
    }

    @Transactional
    public List<CartResponseDTO> selectCart(String username, List<Long> cartItemIdList) {
        SiteUser user = this.userService.get(username);
        List<CartItem> cartItemList = this.cartItemService.getCartItemList(user);
        List<CartResponseDTO> cartResponseDTOList = new ArrayList<>();

        Set<Long> cartItemIdSet = new HashSet<>(cartItemIdList);

        for (CartItem cartItem : cartItemList) {
            if (cartItemIdSet.contains(cartItem.getId())) {
                cartResponseDTOList.add(this.createCartResponseDTO(cartItem));
            }
        }
        return cartResponseDTOList;
    }

    @Transactional
    public List<CartResponseDTO> addToCart(String username, CartRequestDTO cartRequestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(cartRequestDTO.getProductId());
        List<CartItem> cartItemList = this.cartItemService.getCartItem(user, product);

        List<CartResponseDTO> responseDTOList = new ArrayList<>();

        boolean updated = false;

        for (CartItem cartItem : cartItemList) {
            List<CartItemDetail> cartItemDetailList = this.cartItemDetailService.getList(cartItem);
            List<Long> cartItemOptionIds = new ArrayList<>();
            for (CartItemDetail detail : cartItemDetailList) {
                cartItemOptionIds.add(detail.getOptions().getId());
            }
            Collections.sort(cartItemOptionIds);

            if (isOptionListEqual(cartItemOptionIds, cartRequestDTO.getOptionIdList())) {
                cartItem.updateCount(cartItem.getCount() + cartRequestDTO.getCount());
                this.cartItemService.save(cartItem);
                updated = true;
                break;
            }
        }

        if (!updated) {
            CartItem cartItem = this.cartItemService.addToCart(user, product, cartRequestDTO.getCount());
            List<Options> options = this.optionsService.getOptionsList(cartRequestDTO.getOptionIdList());
            for (Options option : options) {
                if (option.getCount() <= 0) {
                    throw new NoSuchElementException("option count 0");
                } else {
                    this.cartItemDetailService.saveCartItemDetail(cartItem, option);
                }
            }
        }

        List<CartItem> updatedCartItems = this.cartItemService.getCartItemList(user);
        for (CartItem item : updatedCartItems) {
            responseDTOList.add(this.createCartResponseDTO(item));
        }

        return responseDTOList;
    }

    private boolean isOptionListEqual(List<Long> list1, List<Long> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        Collections.sort(list1);
        Collections.sort(list2);
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public List<CartResponseDTO> updateToCart(String username, CartRequestDTO cartRequestDTO) {

        SiteUser user = this.userService.get(username);
        CartItem cartItem = this.cartItemService.get(cartRequestDTO.getCartItemId());

        cartItem.updateCount(cartRequestDTO.getCount());
        if (cartItem.getCount() > cartItem.getProduct().getRemain()) {
            throw new IllegalArgumentException("a lot your item count more than product remain");
        } else {
            this.cartItemService.save(cartItem);

            List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
            List<CartResponseDTO> responseDTOList = new ArrayList<>();

            for (CartItem item : cartItems) {
                responseDTOList.add(this.createCartResponseDTO(item));
            }
            return responseDTOList;
        }
    }


    @Transactional
    public List<CartResponseDTO> deleteToCart(String username, Long cartItemId) {
        SiteUser user = this.userService.get(username);
        CartItem cartItem = this.cartItemService.get(cartItemId);
        if (cartItem != null) {
            this.cartItemDetailService.deleteByCartItem(cartItem);
            this.cartItemService.delete(cartItem);
        } else {
            throw new IllegalArgumentException("CartItem not found for user: " + username);
        }

        List<CartResponseDTO> responseDTOList = new ArrayList<>();
        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
        for (CartItem item : cartItems) {
            responseDTOList.add(this.createCartResponseDTO(item));
        }
        return responseDTOList;
    }

    @Transactional
    public List<CartResponseDTO> deleteMultipleToCart(String username, List<Long> cartItemIdList) {
        SiteUser user = this.userService.get(username);
        for (Long cartItemId : cartItemIdList) {
            CartItem cartItem = this.cartItemService.get(cartItemId);
            if (cartItem != null) {
                this.cartItemDetailService.deleteByCartItem(cartItem);
                this.cartItemService.delete(cartItem);
            } else {
                throw new IllegalArgumentException("CartItem not found for user: " + username);
            }
        }

        List<CartResponseDTO> responseDTOList = new ArrayList<>();
        List<CartItem> cartItems = this.cartItemService.getCartItemList(user);
        for (CartItem item : cartItems) {
            responseDTOList.add(this.createCartResponseDTO(item));
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
        return this.getPaymentLogList(paymentLogList);
    }




    @Transactional
    public PaymentLogResponseDTO addPaymentLog(String username, PaymentLogRequestDTO paymentLogRequestDTO) {
        SiteUser user = this.userService.get(username);
        List<CartItem> cartItemList = this.cartItemService.getList(paymentLogRequestDTO.getCartItemIdList());

        // 장바구니 항목이 없는 경우 예외 처리
        if (cartItemList.isEmpty()) {
            throw new NoSuchElementException("Cart is empty, cannot proceed with payment.");
        }
        long point = paymentLogRequestDTO.getPoint();
        long maxPoint = 0L;
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
            Double discount = this.getProductDiscount(cartItem.getProduct());
            Integer discountPrice = this.getProductDiscountPrice(cartItem.getProduct(), discount);
            maxPoint += (long) discountPrice * cartItem.getCount();
        }
        point = Math.max(0, Math.min(maxPoint, Math.min(user.getPoint(), point)));

        // 결제 로그 생성
        PaymentLog paymentLog = this.paymentLogService.save(user, paymentLogRequestDTO);

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            // 결제 상품 생성 및 저장
            Double discount = this.getProductDiscount(product);
            PaymentProduct paymentProduct = this.paymentProductService.save(paymentLog, product, cartItem, discount);

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
            Product product = this.productService.getProduct(paymentProduct.getProductId());
            String url = this.getImageUrl(product);
            Optional<Review> review = this.reviewService.findByPaymentProductId(paymentProduct.getId());
            ReviewResponseDTO reviewResponseDTO = null;
            if (review.isPresent())
                reviewResponseDTO = this.getReview(review.get());
            PaymentProductResponseDTO paymentProductResponseDTO = DTOConverter.toPaymentProductResponseDTO(paymentProduct, paymentProductDetailList, url, reviewResponseDTO);
            paymentProductResponseDTOList.add(paymentProductResponseDTO);
        }


        this.userService.useToPoint(user, point);
        PaymentLog _paymentLog = this.paymentLogService.usedPoint(paymentLog, point);


        PaymentLogResponseDTO paymentLogResponseDTO = DTOConverter.toPaymentLogResponseDTO(_paymentLog, paymentProductResponseDTOList);
        this.userService.addToPoint(user, paymentLogResponseDTO);
        return paymentLogResponseDTO;
    }

    private Long pointCal (SiteUser user, PaymentLogRequestDTO paymentLogRequestDTO) {

        Long point = paymentLogRequestDTO.getPoint();
        List<CartItem> cartItemList = new ArrayList<>();

        for (Long cartItemId : paymentLogRequestDTO.getCartItemIdList()) {
            cartItemList.add(this.cartItemService.get(cartItemId));
        }
        long maxPoint = 0L;
        for (CartItem cartItem : cartItemList) {
            Double discount = this.getProductDiscount(cartItem.getProduct());
            Integer discountPrice = this.getProductDiscountPrice(cartItem.getProduct(), discount);
            maxPoint += (long) discountPrice * cartItem.getCount();
        }


        if (user.getPoint() < point) {
            point = user.getPoint();
        }
        if (point <= 0) {
            point = 0L;
        }
        if (point > maxPoint) {
            point = maxPoint;
        }
        return point;
    }

    private List<PaymentLogResponseDTO> getPaymentLogList(List<PaymentLog> paymentLogList) {

        List<PaymentLogResponseDTO> paymentLogResponseDTOList = new ArrayList<>();

        for (PaymentLog paymentLog : paymentLogList) {
            List<PaymentProduct> paymentProductList = this.paymentProductService.getList(paymentLog);
            List<PaymentProductResponseDTO> paymentProductResponseDTOList = new ArrayList<>();

            for (PaymentProduct paymentProduct : paymentProductList) {
                List<PaymentProductDetail> paymentProductDetailList = this.paymentProductDetailService.getList(paymentProduct);
                Product product = this.productService.getProduct(paymentProduct.getProductId());
                String url = this.getImageUrl(product);
                Optional<Review> review = this.reviewService.findByPaymentProductId(paymentProduct.getId());
                ReviewResponseDTO reviewResponseDTO = null;
                if (review.isPresent())
                    reviewResponseDTO = this.getReview(review.get());
                PaymentProductResponseDTO paymentProductResponseDTO = DTOConverter.toPaymentProductResponseDTO(paymentProduct, paymentProductDetailList, url, reviewResponseDTO);
                paymentProductResponseDTOList.add(paymentProductResponseDTO);
            }

            PaymentLogResponseDTO paymentLogResponseDTO = DTOConverter.toPaymentLogResponseDTO(paymentLog, paymentProductResponseDTOList);
            paymentLogResponseDTOList.add(paymentLogResponseDTO);
        }
        return paymentLogResponseDTOList;
    }

    /**
     * option
     */

    public List<OptionListResponseDTO> getOptionListResponseDTOList(Product product) {
        List<OptionListResponseDTO> optionListResponseDTOList = new ArrayList<>();
        List<OptionList> optionLists = this.optionListService.getList(product);

        for (OptionList optionList : optionLists) {
            List<OptionResponseDTO> optionResponseDTOList = this.getOptionResponseDTOList(optionList);
            optionListResponseDTOList.add(OptionListResponseDTO.builder().optionResponseDTOList(optionResponseDTOList).optionList(optionList).build());
        }
        return optionListResponseDTOList;
    }

    private List<OptionResponseDTO> getOptionResponseDTOList(OptionList optionList) {
        List<OptionResponseDTO> optionResponseDTOList = new ArrayList<>();
        List<Options> optionsList = this.optionsService.getList(optionList);

        for (Options options : optionsList) {
            optionResponseDTOList.add(OptionResponseDTO.builder().options(options).build());
        }

        return optionResponseDTOList;
    }

    @Transactional
    public void deleteOptions(OptionList optionList) {
        Optional<Options> _options = optionsService.getOptionByOptionList(optionList);
        _options.ifPresent(optionsService::delete);
        optionListService.delete(optionList);
    }

    @Transactional
    public void saveOptions(OptionRequestDTO optionRequestDTO, OptionList optionList) {
        optionsService.saveOption(optionRequestDTO.getCount(), optionRequestDTO.getName(), optionRequestDTO.getPrice(), optionList);
    }

    /**
     * OptionList
     */

    @Transactional
    public void deleteOptionList(Product product) {
        Optional<OptionList> _optionList = optionListService.getOptionListByProduct(product);
        _optionList.ifPresent(this::deleteOptions);
    }

    @Transactional
    public void saveOptionList(OptionListRequestDTO optionListRequestDTO, Product product) {
        OptionList optionList = optionListService.save(optionListRequestDTO.getName(), product);
        for (OptionRequestDTO optionRequestDTO : optionListRequestDTO.getChild()) {
            this.saveOptions(optionRequestDTO, optionList);
        }
    }

    /**
     * Tag
     */
    @Transactional
    public void saveTag(List<String> tagList, Product product) {
        for (String tagName : tagList) {
            tagService.save(tagName, product);
        }
    }

    @Transactional
    public void deleteTag(Product product) {
        List<Tag> tagList = tagService.getTagByProduct(product);
        if (!tagList.isEmpty()) for (Tag tag : tagList) {
            tagService.delete(tag);
        }
    }

    /**
     * Product
     */
    @Transactional
    public void deleteProduct(Long ProductId, String username) {
        SiteUser siteUser = userService.get(username);
        Product product = productService.getProduct(ProductId);
        Optional<MultiKey> _multiKey = multiKeyService.get(ImageKey.PRODUCT.getKey(product.getId().toString()));
        if (_multiKey.isPresent() && siteUser == product.getSeller()) {
            String path = ShoppingApplication.getOsType().getLoc();
            for (String keyName : _multiKey.get().getVs()) {
                Optional<FileSystem> _fileSystem = fileSystemService.get(keyName);
                _fileSystem.ifPresent(fileSystemService::delete);
            }
            Optional<FileSystem> fileSystem = fileSystemService.get(ImageKey.PRODUCT.getKey(product.getId().toString()));
            if (fileSystem.isPresent()) {
                Path tempPath = Paths.get(path + fileSystem.get().getV());
                File file = tempPath.toFile();
                this.deleteFolder(file.getParentFile());
                fileSystemService.delete(fileSystem.get());
            }
            multiKeyService.delete(_multiKey.get());
        }
        if (product != null) {
            this.deleteTag(product);
            this.deleteOptionList(product);
            Optional<Review> _review = reviewService.findByProduct(product);
            _review.ifPresent(reviewService::delete);
            this.deleteQAByProductAll(product);
            this.deleteWish(product);
            this.deleteCart(product);
            this.deleteByEventProduct(product);
            Optional<Recent> _recent = recentService.findByProduct(product);
            _recent.ifPresent(recentService::delete);

            productService.deleteProduct(product);
        }
    }


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
            this.saveTag(requestDTO.getTagList(), product);
        }
        if (requestDTO.getOptionLists() != null) {
            for (OptionListRequestDTO optionListRequestDTO : requestDTO.getOptionLists()) {
                this.saveOptionList(optionListRequestDTO, product);
            }
        }
        if (requestDTO.getUrl() != null && !requestDTO.getUrl().isBlank()) {
            Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.TEMP.getKey(username));
            String newFile = "/api/product" + "/" + product.getId() + "/";
            if (_fileSystem.isPresent()) {
                String newUrl = this.fileMove(requestDTO.getUrl(), newFile, _fileSystem.get());
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
        Optional<MultiKey> _multiKey = multiKeyService.get(ImageKey.TEMP.getKey(username));
        this.updateProductContent(username, product, _multiKey.get());
    }

    @Transactional
    public ProductResponseDTO updateProduct(String username, ProductCreateRequestDTO requestDTO) {
        Product product = productService.getProduct(requestDTO.getProductId());

        if (product != null) {
            Optional<MultiKey> _multiKey = multiKeyService.get(ImageKey.TEMP.getKey(username));
            if (_multiKey.isPresent()) {
                Category category = this.categoryService.get(requestDTO.getCategoryId());
                Product newProduct = productService.updateProduct(product, requestDTO, category);
                return getProduct(this.updateProductContent(username, newProduct, _multiKey.get()));
            }
        }
        return null;
    }

    private Product updateProductContent(String username, Product product, MultiKey multiKey) {
        String detail = product.getDetail();
        for (String keyName : multiKey.getVs()) {
            Optional<MultiKey> _productMulti = multiKeyService.get(ImageKey.PRODUCT.getKey(product.getId().toString()));
            Optional<FileSystem> _fileSystem = fileSystemService.get(keyName);
            if (_fileSystem.isPresent()) {
                String newFile = "/api/product" + "/" + product.getId() + "/content/";
                String newUrl = this.fileMove(_fileSystem.get().getV(), newFile, _fileSystem.get());
                if (_productMulti.isEmpty()) {
                    MultiKey multiKey1 = multiKeyService.save(ImageKey.PRODUCT.getKey(product.getId().toString()), ImageKey.PRODUCT.getKey(product.getId().toString()) + ".0");
                    fileSystemService.save(multiKey1.getVs().getLast(), newUrl);
                } else {
                    multiKeyService.add(_productMulti.get(), ImageKey.PRODUCT.getKey(product.getId().toString()) + "." + _productMulti.get().getVs().size());
                    fileSystemService.save(_productMulti.get().getVs().getLast(), newUrl);
                }
                detail = detail.replace(_fileSystem.get().getV(), newUrl);
            }
        }
        multiKeyService.delete(multiKey);
        productService.Update(product, detail);

        return product;
    }


    private String getImageUrl(Product product) {
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.PRODUCT.getKey(product.getId().toString()));
        return _fileSystem.map(FileSystem::getV).orElse(null);
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
        List<Review> reviewList = this.reviewService.getList(product);
        String url = this.getImageUrl(product);
        Map<String, Object> gradeCalculate = this.gradeCalculate(reviewList);
        List<OptionListResponseDTO> optionListResponseDTOList = this.getOptionListResponseDTOList(product);

        Map<String, Integer> numOfGrade = (Map<String, Integer>) gradeCalculate.get("numOfGrade");
        Double averageGrade = (Double) gradeCalculate.get("averageGrade");


        Double discount = this.getProductDiscount(product);
        int discountPrice = this.getProductDiscountPrice(product, discount);

        return ProductResponseDTO.builder().optionListResponseDTOList(optionListResponseDTOList).product(product).tagList(tagList).url(url).discount(discount).discountPrice(discountPrice).dateLimit(this.dateTimeTransfer(product.getDateLimit())).createDate(this.dateTimeTransfer(product.getCreateDate())).modifyDate(this.dateTimeTransfer(product.getModifyDate())).reviewList(reviewList).averageGrade(averageGrade).numOfGrade(numOfGrade).build();
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

    @Transactional
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
        Pageable pageable = PageRequest.of(page, 15);
        Page<Product> productPage = productService.getLatestList(pageable);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (Product product : productPage) {
            ProductResponseDTO productResponseDTO = this.getProduct(product);
            productResponseDTOList.add(productResponseDTO);
        }
        return new PageImpl<>(productResponseDTOList, pageable, productPage.getTotalElements());
    }

    /**
     * ProductQA
     */

    @Transactional
    public List<ProductResponseDTO> getMyProductList(String username) {
        SiteUser user = this.userService.get(username);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();

        if (user.getRole().equals(UserRole.USER)) {
            throw new IllegalArgumentException("only Seller and ADMIN SERVICE");
        }
        List<Product> productList = this.productService.getMyList(user);
        if (productList.isEmpty()) {
            throw new NoSuchElementException("not found your list");
        }
        for (Product product : productList) {
            productResponseDTOList.add(this.getProduct(product));
        }
        return productResponseDTOList;
    }

    public List<ProductQAResponseDTO> getQA(long productId) {
        List<ProductQAResponseDTO> list = new ArrayList<>();
        Product product = productService.getProduct(productId);
        List<ProductQA> productQAList = productQAService.findByProduct(product);
        for (ProductQA qa : productQAList)
            list.add(ProductQAResponseDTO.builder().productId(qa.getProduct().getId()).productQAId(qa.getId()).title(qa.getTitle()).content(qa.getContent()).author(qa.getAuthor().getNickname()).answer(qa.getAnswer()).createDate(qa.getCreateDate()).build());
        return list;
    }

    @Transactional
    public void productQASave(String username, ProductQARequestDTO requestDTO) {
        SiteUser user = this.userService.get(username);
        Product product = productService.getProduct(requestDTO.getProductId());
        if (user == null) throw new NoSuchElementException("not user");
        if (product != null) this.productQAService.save(requestDTO.getTitle(), requestDTO.getContent(), user, product);
    }

    @Transactional
    public void productQAUpdate(String username, ProductQARequestDTO requestDTO) {
        SiteUser user = this.userService.get(username);
        Optional<ProductQA> _productQA = productQAService.getProductQA(requestDTO.getProductQAId());
        Product product = productService.getProduct(requestDTO.getProductId());
        if (user == null) throw new NoSuchElementException("not user");
        if (product == null)
            throw new NoSuchElementException("not product");
        if (!product.getSeller().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("not owner");
        _productQA.ifPresent(productQA -> this.productQAService.update(requestDTO.getAnswer(), user, productQA));
    }

    @Transactional
    public void deleteQAByProductAll(Product product) {
        List<ProductQA> productQAList = productQAService.findByProduct(product);
        for (ProductQA qa : productQAList)
            productQAService.delete(qa);
    }

    /**
     * Image
     */

    @Transactional
    public String fileMove(String url, String newUrl, FileSystem fileSystem) {
        try {
            String path = ShoppingApplication.getOsType().getLoc();
            Path tempPath = Paths.get(path + url);
            Path newPath = Paths.get(path + newUrl + tempPath.getFileName());

            Files.createDirectories(newPath.getParent());
            Files.move(tempPath, newPath);
            File file = tempPath.toFile();
            if (file.exists()) file.delete();
            fileSystemService.delete(fileSystem);
            return newUrl + tempPath.getFileName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteTempImage(String username) {
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.TEMP.getKey(username));
        if (_fileSystem.isPresent()) {
            String path = ShoppingApplication.getOsType().getLoc();
            FileSystem fileSystem = _fileSystem.get();
            File file = new File(path + fileSystem.getV());
            if (file.exists()) file.delete();
            fileSystemService.delete(_fileSystem.get());
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
                if (file.exists()) file.delete();
            }
            UUID uuid = UUID.randomUUID();
            String fileLoc = "/api/user" + "/" + username + "/temp/" + uuid + "." + requestDTO.getFile().getContentType().split("/")[1];
            File file = new File(path + fileLoc);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            requestDTO.getFile().transferTo(file);
            if (fileLoc != null) {
                fileSystemService.save(ImageKey.TEMP.getKey(username), fileLoc);
            }
            return ImageResponseDTO.builder().url(fileLoc).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public void deleteTempImageList(String username) {
        String path = ShoppingApplication.getOsType().getLoc();
        Optional<MultiKey> _multiKey = multiKeyService.get(ImageKey.TEMP.getKey(username));
        if (_multiKey.isPresent()) {
            for (String key : _multiKey.get().getVs()) {
                Optional<FileSystem> _fileSystem = fileSystemService.get(key);
                if (_fileSystem.isPresent()) {
                    FileSystem fileSystem = _fileSystem.get();
                    File file = new File(path + fileSystem.getV());
                    if (file.exists()) file.delete();
                    fileSystemService.delete(_fileSystem.get());
                }
            }
            multiKeyService.delete(_multiKey.get());
        }
    }

    @Transactional
    public ImageResponseDTO tempImageList(ImageRequestDTO requestDTO, String username) {
        if (!requestDTO.getFile().isEmpty()) try {
            String path = ShoppingApplication.getOsType().getLoc();

            UUID uuid = UUID.randomUUID();
            String fileLoc = "/api/user" + "/" + username + "/temp_list/" + uuid + "." + requestDTO.getFile().getContentType().split("/")[1];
            File file = new File(path + fileLoc);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            requestDTO.getFile().transferTo(file);

            Optional<MultiKey> _multiKey = multiKeyService.get(ImageKey.TEMP.getKey(username));
            if (_multiKey.isEmpty()) {
                MultiKey multiKey = multiKeyService.save(ImageKey.TEMP.getKey(username), ImageKey.TEMP.getKey(username) + ".0");
                fileSystemService.save(multiKey.getVs().getLast(), fileLoc);
            } else {
                multiKeyService.add(_multiKey.get(), ImageKey.TEMP.getKey(username) + "." + _multiKey.get().getVs().size());
                fileSystemService.save(_multiKey.get().getVs().getLast(), fileLoc);
            }
            Optional<MultiKey> _newMultiKey = multiKeyService.get(ImageKey.TEMP.getKey(username));
            List<String> urlList = new ArrayList<>();
            for (String name1 : _newMultiKey.get().getVs()) {
                Optional<FileSystem> fileSystem = fileSystemService.get(name1);
                fileSystem.ifPresent(system -> urlList.add(system.getV()));
            }
            return ImageResponseDTO.builder().url(fileLoc).build();
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
            if (category != null) categoryService.deleteCategory(category);
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
        return CategoryResponseDTO.builder().id(parentCategory.getId()).parentName(parentCategory.getParent() != null ? parentCategory.getParent().getName() : null).name(parentCategory.getName()).categoryResponseDTOList(childrenDTOList).build();
    }

    /**
     * Review
     */

    private ReviewResponseDTO getReview(Review review) {
        String _username = review.getAuthor().getUsername();
        Optional<FileSystem> _fileSystem = fileSystemService.get(ImageKey.USER.getKey(_username));
        String profileUrl = null;
        if (_fileSystem.isPresent()) profileUrl = _fileSystem.get().getV();

        return ReviewResponseDTO.builder().profileUrl(profileUrl).createDate(this.dateTimeTransfer(review.getCreateDate())).modifyDate(this.dateTimeTransfer(review.getModifyDate())).review(review).user(review.getAuthor()).build();
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
    public void addToReview(String username, ReviewRequestDTO reviewRequestDTO) {
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();
        SiteUser user = this.userService.get(username);
        Product product = this.productService.getProduct(reviewRequestDTO.getProductId());
        List<Review> _reviewList = this.reviewService.getMyReviewByProduct(user, product);

        // 사용자의 구매 기록을 가져옴
        List<PaymentLog> paymentLogList = this.paymentLogService.get(user);
        List<PaymentProduct> _paymentProductList = new ArrayList<>();
        boolean hasPurchased = false;

        for (PaymentLog paymentLog : paymentLogList) {
            List<PaymentProduct> paymentProductList = this.paymentProductService.getList(paymentLog);
            for (PaymentProduct paymentProduct : paymentProductList) {
                if (Objects.equals(paymentProduct.getProductId(), product.getId())) {
                    _paymentProductList.add(paymentProduct);
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

        // 구매기록에 해당 제품이 포함된 만큼만 리뷰 작성 가능
        if (_reviewList.size() >= _paymentProductList.size()) {
            throw new DataDuplicateException("1 paymentProduct by 1 review");
        }
        // 구매 기록이 있는 경우에만 리뷰를 저장

        Review reviewKey = this.reviewService.save(user, reviewRequestDTO, product);
        this.paymentProductService.updateStatus(reviewRequestDTO.getPaymentProductId());
        this.updateReviewContent(reviewKey, username);

        // 리뷰 리스트를 가져와서 DTO로 변환하여 반환
//        List<Review> reviewList = this.reviewService.getList(product);
//        for (Review review : reviewList) {
//            ReviewResponseDTO reviewResponseDTO = this.getReview(review);
//            reviewResponseDTOList.add(reviewResponseDTO);
//        }
//
//
//        return reviewResponseDTOList;

    }

    private void updateReviewContent(Review reviewKey, String username) {
        String detail = reviewKey.getContent();
        // 이미지 저장
        Optional<MultiKey> _multiKey = multiKeyService.get(ImageKey.TEMP.getKey(username));
        if (_multiKey.isPresent()) {
            for (String keyName : _multiKey.get().getVs()) {
                Optional<MultiKey> _reviewMulti = multiKeyService.get(ImageKey.REVIEW.getKey(reviewKey.getId().toString()));
                Optional<FileSystem> _fileSystem = fileSystemService.get(keyName);
                String newFile = "/api/review" + "/" + reviewKey.getId() + "/";
                String newPath = this.fileMove(_fileSystem.get().getV(), newFile, _fileSystem.get());
                if (_reviewMulti.isEmpty()) {
                    MultiKey multiKey = multiKeyService.save(ImageKey.REVIEW.getKey(reviewKey.getId().toString()), ImageKey.REVIEW.getKey(reviewKey.getId().toString()) + ".0");
                    fileSystemService.save(multiKey.getVs().getLast(), newPath);
                } else {
                    multiKeyService.add(_reviewMulti.get(), ImageKey.REVIEW.getKey(reviewKey.getId().toString()) + "." + _reviewMulti.get().getVs().size());
                    fileSystemService.save(_reviewMulti.get().getVs().getLast(), newPath);
                }
                detail = detail.replace(_fileSystem.get().getV(), newPath);
            }
            reviewService.updateContent(reviewKey, detail);
            multiKeyService.delete(_multiKey.get());
        }
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
            Review newReview = this.reviewService.update(review, reviewRequestDTO);
            List<Review> reviewList = this.reviewService.getList(review.getProduct());
            this.updateReviewContent(newReview, username);
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
            ArticleResponseDTO articleResponseDTO = this.getArticleResponseDTO(article);
            articleResponseDTOList.add(articleResponseDTO);
        }

        return articleResponseDTOList;
    }

    @Transactional
    public ArticleResponseDTO saveArticle(String username, ArticleRequestDTO articleRequestDTO) {
        SiteUser siteUser = this.userService.get(username);
        Article article = this.articleService.save(articleRequestDTO, siteUser);

        return this.getArticleResponseDTO(article);
    }

    @Transactional
    public ArticleResponseDTO updateArticle(String username, ArticleRequestDTO articleRequestDTO) {
        SiteUser user = userService.get(username);
        Article _article = this.articleService.get(articleRequestDTO.getArticleId());
        if (!user.getUsername().equals(_article.getAuthor().getUsername()) || !user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("not role");
        } else {
            Article article = this.articleService.update(_article, articleRequestDTO);
            return this.getArticleResponseDTO(article);
        }
    }

    private ArticleResponseDTO getArticleResponseDTO(Article article) {
        return ArticleResponseDTO.builder().article(article).siteUser(article.getAuthor()).createDate(this.dateTimeTransfer(article.getCreateDate())).modifyDate(this.dateTimeTransfer(article.getModifyDate())).build();
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
            if (_recent.isPresent()) {
                this.recentService.update(_recent.get());
            } else {
                List<Recent> recentList = recentService.getRecent(user);
                if (recentList.size() >= 10) {
                    Recent recent = recentList.getLast();
                    this.recentService.delete(recent);
                }
                recentService.save(product, user);
            }
        }
    }

    @Transactional
    public List<RecentResponseDTO> getRecentList(String username) {
        SiteUser user = userService.get(username);
        List<Recent> recentList = recentService.getRecent(user);
        List<RecentResponseDTO> responseDTOList = new ArrayList<>();
        for (Recent recent : recentList) {
            if (recent.getProduct().getRemain() <= 0) {
                this.recentService.delete(recent);
            }
            ProductResponseDTO responseDTO = this.getProduct(recent.getProduct().getId());

            responseDTOList.add(RecentResponseDTO.builder().price(responseDTO.getPrice()).title(responseDTO.getTitle()).grade(responseDTO.getGrade()).recentId(recent.getId()).productId(responseDTO.getId()).url(responseDTO.getUrl()).createDate(responseDTO.getCreateDate()).build());
        }
        return responseDTOList;
    }


    @Transactional
    public void deleteRecent(Long recentId, String username) {
        Optional<Recent> _recent = recentService.getRecentId(recentId);
        SiteUser user = userService.get(username);
        if (_recent.isPresent() && _recent.get().getUser().equals(user)) this.recentService.delete(_recent.get());
    }

    /**
     * search
     */

    @Transactional
    public Page<ProductResponseDTO> searchByKeyword(int page, String encodedKeyword, int sort) {
        String keyword = URLDecoder.decode(encodedKeyword, StandardCharsets.UTF_8);
        Sorts sorts = Sorts.values()[sort];

        Pageable pageable = PageRequest.of(page, 15);
        Page<Product> productPage = this.productService.searchByKeyword(pageable, keyword, sorts);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (Product product : productPage) {
            ProductResponseDTO productResponseDTO = this.getProduct(product);
            productResponseDTOList.add(productResponseDTO);
        }
        return new PageImpl<>(productResponseDTOList, pageable, productPage.getTotalElements());
    }

    @Transactional
    public Page<ProductResponseDTO> categorySearchByKeyword(int page, String encodedKeyword, int sort, Long
            categoryId) {
        String keyword = URLDecoder.decode(encodedKeyword, StandardCharsets.UTF_8);
        Sorts sorts = Sorts.values()[sort];

        Pageable pageable = PageRequest.of(page, 15);
        Page<Product> productPage = this.productService.categorySearchByTitle(pageable, keyword, sorts, categoryId);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (Product product : productPage) {
            ProductResponseDTO productResponseDTO = this.getProduct(product);
            productResponseDTOList.add(productResponseDTO);
        }
        return new PageImpl<>(productResponseDTOList, pageable, productPage.getTotalElements());
    }

    /**
     * event
     */

    @Transactional
    public List<EventResponseDTO> getEventList(String username) {
        SiteUser user = this.userService.get(username);
        List<EventResponseDTO> eventResponseDTOList = new ArrayList<>();
        List<Event> eventList = this.eventService.getMyList(user);
        for (Event event : eventList) {
            if (!user.equals(event.getCreator())) {
                throw new IllegalArgumentException("not role");
            }
            eventResponseDTOList.add(this.getEventDTO(event));
        }
        return eventResponseDTOList;
    }

    @Transactional
    public EventResponseDTO getEvent(String username, Long eventId) {
        SiteUser user = this.userService.get(username);
        Event event = this.eventService.get(eventId);
        if (!user.equals(event.getCreator())) {
            throw new IllegalArgumentException("not role");
        }
        return this.getEventDTO(event);
    }


    @Transactional
    public EventResponseDTO createEvent(String username, EventRequestDTO eventRequestDTO) {
        SiteUser user = this.userService.get(username);
        if (user.getRole().equals(UserRole.USER)) {
            throw new IllegalArgumentException("not role");
        }

        Event event = this.eventService.saveEvent(user, eventRequestDTO);
        List<Long> productIdList = eventRequestDTO.getProductIdList();

        for (Long productId : productIdList) {
            Product product = this.productService.getProduct(productId);
            this.eventProductService.saveEventProduct(event, product);
        }
        return this.getEventDTO(event);
    }

    @Transactional
    public EventResponseDTO updateEvent(String username, EventRequestDTO eventRequestDTO) {
        SiteUser user = this.userService.get(username);
        Event _event = this.eventService.get(eventRequestDTO.getEventId());

        if (user.getRole().equals(UserRole.USER) || !_event.getCreator().equals(user)) {
            throw new IllegalArgumentException("not role");
        }
        List<Long> productIdList = eventRequestDTO.getProductIdList();


        List<EventProduct> eventProductList = this.eventProductService.getList(_event);
        Iterator<EventProduct> iterator = eventProductList.iterator();
        while (iterator.hasNext()) {
            EventProduct eventProduct = iterator.next();
            Long productId = eventProduct.getProduct().getId();

            if (!productIdList.contains(productId)) {
                this.eventProductService.delete(eventProduct);
                iterator.remove();
            }
        }

        for (Long productId : productIdList) {
            boolean exists = eventProductList.stream().anyMatch(ep -> ep.getProduct().getId().equals(productId));
            if (!exists) {
                Product product = this.productService.getProduct(productId);
                this.eventProductService.saveEventProduct(_event, product);
            }
        }

        Event updatedEvent = this.eventService.updateEvent(_event, eventRequestDTO);
        return this.getEventDTO(updatedEvent);
    }

    @Transactional
    public void deleteByEventProduct(Product product) {
        List<EventProduct> eventProductList = eventProductService.findByProduct(product);
        if (eventProductList != null) {
            for (EventProduct eventProduct : eventProductList) {
                eventProductService.delete(eventProduct);
            }
        }

    }

    // 초 분 시 일 월 주

    @Scheduled(cron = "0 * * * * *")
    public void disableEvent() {

        LocalDateTime now = LocalDateTime.now();
        List<Event> eventList = this.eventService.findByEndDateAfter(now);

        for (Event event : eventList) {
            this.eventService.disableActive(event);
        }
    }

    private EventResponseDTO getEventDTO(Event event) {

        List<EventProduct> eventProductList = this.eventProductService.getList(event);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (EventProduct eventProduct : eventProductList) {
            productResponseDTOList.add(this.getProduct(eventProduct.getProduct()));
        }
        return EventResponseDTO.builder().startDate(this.dateTimeTransfer(event.getStartDate())).endDate(this.dateTimeTransfer(event.getEndDate())).productResponseDTOList(productResponseDTOList).user(event.getCreator()).event(event).createDate(this.dateTimeTransfer(event.getCreateDate())).modifyDate(this.dateTimeTransfer(event.getModifyDate())).build();
    }

    /**
     * function
     */

    private Long dateTimeTransfer(LocalDateTime dateTime) {

        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private Long dateTimeTransfer(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = date.atStartOfDay();
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private Integer getProductDiscountPrice(Product product, Double discount) {
        // 유효성 검사: 할인율이 음수인 경우 0으로 처리
        if (discount < 0.0) {
            discount = 0.0;
        }

        // 할인된 가격 계산 (부동소수점 정밀도 보장을 위해 BigDecimal 사용)
        BigDecimal originalPrice = BigDecimal.valueOf(product.getPrice());
        BigDecimal discountPercent = BigDecimal.valueOf(discount);
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(discountPercent.divide(BigDecimal.valueOf(100)));
        BigDecimal discountedPrice = originalPrice.multiply(discountMultiplier);

        // 소수점 첫 번째 자리에서 반올림하여 정수로 변환

        return discountedPrice.setScale(0, RoundingMode.HALF_UP).intValue();
    }


    private Double getProductDiscount(Product product) {
        List<Event> eventList = this.eventService.findByProduct(product);
        double maxDiscount = 0.0;
        LocalDateTime now = LocalDateTime.now();

        for (Event event : eventList) {
            if (isActiveEvent(event, now)) {
                double discount = event.getDiscount();
                if (discount > maxDiscount) {
                    maxDiscount = discount;
                }
            }
        }

        return maxDiscount;
    }

    private boolean isActiveEvent(Event event, LocalDateTime now) {
        // 이벤트가 활성화되어 있고, 현재 시간이 이벤트의 유효 기간에 속하는지 여부를 반환
        return event.getActive() && now.isEqual(event.getStartDate()) || now.isAfter(event.getStartDate()) && now.isBefore(event.getEndDate());
    }

    private Map<String, Object> gradeCalculate(List<Review> reviewList) {
        double totalGrade = 0.0;
        Map<String, Integer> numOfGrade = new HashMap<>();

        numOfGrade.put("0", 0);
        numOfGrade.put("0.5~1", 0);
        numOfGrade.put("1.5~2", 0);
        numOfGrade.put("2.5~3", 0);
        numOfGrade.put("3.5~4", 0);
        numOfGrade.put("4.5~5", 0);

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

        double averageGrade = totalGrade / reviewList.size();
        if (averageGrade <= 0) {
            averageGrade = 0.0;
        } else {
            averageGrade = Math.round(averageGrade * 10) / 10.0;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("numOfGrade", numOfGrade);
        result.put("averageGrade", averageGrade);

        return result;
    }


    /**
     * File
     */
    public void deleteFolder(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File list : file.listFiles())
                    deleteFolder(list);
            }
            file.delete();
        }
    }

    public List<ReviewResponseDTO> getMyReview(String username) {
        SiteUser user = userService.get(username);
        if (user == null) {
            throw new DataNotFoundException("not found user");
        }
        List<Review> reviewList = reviewService.getMyReview(user);
        List<ReviewResponseDTO> responseDTOList = new ArrayList<>();
        for (Review review : reviewList)
            responseDTOList.add(this.getReview(review));
        return responseDTOList;
    }
}


