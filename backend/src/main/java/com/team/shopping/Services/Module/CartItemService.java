package com.team.shopping.Services.Module;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem get (Long id) {
        return this.cartItemRepository.findById(id).orElseThrow();
    }

    public List<CartItem> getCartItemList (SiteUser user) {
        return this.cartItemRepository.findAllByUser(user);
    }

    public CartItem addToCart(SiteUser user, Product product, int count) {
        return this.cartItemRepository.save(CartItem.builder()
                        .count(count)
                        .createDate(LocalDateTime.now())
                        .product(product)
                        .user(user)
                .build());
    }

    public CartItem save (CartItem cartItem) {
        return this.cartItemRepository.save(cartItem);
    }

    public CartItem getCartItem(SiteUser user, Product product) {
        return this.cartItemRepository.findByUserAndProduct(user, product);
    }

    public void delete(CartItem cartItem) {
        this.cartItemRepository.delete(cartItem);
    }

    public List<CartItem> getList (List<Long> cartItemIdList) {
        return this.cartItemRepository.findAllById(cartItemIdList);
    }
}
