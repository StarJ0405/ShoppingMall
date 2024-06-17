package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Domains.Wish;
import com.team.shopping.Repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;

    public List<Wish> get(SiteUser user) {
        return this.wishListRepository.findByUserOrderByCreateDateDesc(user);
    }


    public Wish save (Wish wish) {
        return this.wishListRepository.save(wish);
    }

    public Optional<Wish> get(SiteUser user, Product product) {
        return Optional.ofNullable(this.wishListRepository.findByUserAndProduct(user, product));
    }


    public Wish addToWishList(SiteUser user, Product product) {
        return wishListRepository.save(Wish.builder()
                .user(user)
                .product(product)
                .build());
    }

    public void deleteToWishList(SiteUser user, Product product) {

        Wish wishList = this.wishListRepository.findByUserAndProduct(user, product);
        this.wishListRepository.delete(wishList);

    }
}
