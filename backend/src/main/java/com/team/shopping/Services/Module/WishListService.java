package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Domains.WishList;
import com.team.shopping.Repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;

    public WishList get(SiteUser user) {
        return this.wishListRepository.findByUser(user);
    }

    public WishList save (WishList wishList) {
        return this.wishListRepository.save(wishList);
    }

    public WishList addToWishList(SiteUser user, Product product) {

        WishList wishList = this.get(user);

        List<Product> productList = wishList.getProductList();

        productList.add(product);

        this.wishListRepository.save(wishList);

        return this.wishListRepository.findById(wishList.getId()).orElse(wishList);
    }

    public WishList deleteToWishList(SiteUser user, Product product) {

        WishList wishList = this.get(user);

        if (wishList == null || wishList.getProductList() == null) {
            return null;
        }

        List<Product> productList = wishList.getProductList();

        if (!productList.contains(product)) {
            return null;
        }

        productList.remove(product);

        wishList.setProductList(productList);

        return this.wishListRepository.save(wishList);
    }
}
