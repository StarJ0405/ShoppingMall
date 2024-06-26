package com.team.shopping.DTOs;

import com.team.shopping.Domains.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDTO {

    private Long cartItemId;

    private String authorName;

    private Long productId;

    private String productTitle;

    private String imageUrl;

    private int count;

    private List<CartItemDetailResponseDTO> cartItemDetailResponseDTOList;

    private int productPrice;

    private Double discount;

    private int discountPrice;

    private Long totalPrice;

    private Long createDate;
    private int remain;

    @Builder
    public CartResponseDTO(CartItem cartItem, List<CartItemDetailResponseDTO> cartItemDetailResponseDTOList,
                           Long totalPrice, Long createDate, Double discount, int discountPrice, String imageUrl) {
        this.cartItemId = cartItem.getId();
        this.authorName = cartItem.getProduct().getSeller().getName();
        this.productId = cartItem.getProduct().getId();
        this.productTitle = cartItem.getProduct().getTitle();
        this.count = cartItem.getCount();
        this.cartItemDetailResponseDTOList = cartItemDetailResponseDTOList;
        this.createDate = createDate;
        this.productPrice = cartItem.getProduct().getPrice();
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
        this.remain = cartItem.getProduct().getRemain();
    }

}
