package com.team.shopping.Services.Module;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem get (Long id) {
        return this.cartItemRepository.findById(id).orElseThrow();
    }

    public List<CartItem> getCartItemList (SiteUser user) {
        return this.cartItemRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public CartItem addToCart(SiteUser user, Product product, int count) {
        if (count <= 0) {
            count = 1;
        }
        if (product.getRemain() - count <= -1) {
            throw new IllegalArgumentException("this product remain is not enough");
        }
        else {
            return this.cartItemRepository.save(CartItem.builder()
                        .count(count)
                        .createDate(LocalDateTime.now())
                        .product(product)
                        .user(user)
                .build());
        }
    }

    public CartItem save (CartItem cartItem) {
        if (cartItem.getProduct().getRemain() == 0 || cartItem.getProduct().getRemain() < cartItem.getCount()) {
            throw new IllegalArgumentException("this product remain is not enough");
        }else {
            return this.cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartItem(SiteUser user, Product product) {
        return this.cartItemRepository.findByUserAndProduct(user, product);
    }

    public void delete(CartItem cartItem) {
        this.cartItemRepository.delete(cartItem);
    }

    public List<CartItem> getList (List<Long> cartItemIdList) {
        return this.cartItemRepository.findAllById(cartItemIdList);
    }

    public Optional<CartItem> findByProduct(Product product) {
        return cartItemRepository.findByProduct(product);
    }
}
