package com.team.shopping.DTOs;

import com.team.shopping.Domains.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDTO {

    private Long cartItemId;

    private String authorName;

    private Long productId;

    private String productTitle;

    private int count;

    private List<OptionResponseDTO> optionResponseDTOList;

    private int productPrice;

    private LocalDateTime createDate;

    @Builder
    public CartResponseDTO (CartItem cartItem, List<OptionResponseDTO> optionResponseDTOList) {
        this.cartItemId = cartItem.getId();
        this.authorName = cartItem.getProduct().getSeller().getName();
        this.productId = cartItem.getProduct().getId();
        this.productTitle = cartItem.getProduct().getTitle();
        this.count = cartItem.getCount();
        this.optionResponseDTOList = optionResponseDTOList;
        this.productPrice = cartItem.getProduct().getPrice();
        this.createDate = cartItem.getCreateDate();
    }

}
