package com.team.shopping.Services.Module;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.CartItemDetail;
import com.team.shopping.Domains.Options;
import com.team.shopping.Repositories.CartItemDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemDetailService {

    private final CartItemDetailRepository cartItemDetailRepository;

    public List<CartItemDetail> getList(CartItem cartItem) {
        return this.cartItemDetailRepository.findAllByCartItem(cartItem);
    }

    public CartItemDetail save (CartItem cartItem, Options options) {
        return this.cartItemDetailRepository.save(CartItemDetail.builder()
                        .cartItem(cartItem)
                        .options(options)
                .build());
    }

    public void delete (CartItem cartItem) {
        this.cartItemDetailRepository.deleteByCartItem(cartItem);
    }

    public void deleteByCartItem(CartItem cartItem) {

        this.cartItemDetailRepository.deleteByCartItem(cartItem);
    }
}
