package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private SiteUser user;

    @OneToMany
    @JoinTable(
            name = "wish_list_product_list",
            joinColumns = @JoinColumn(name = "wish_list_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> productList = new ArrayList<>();

    @Builder
    public WishList(SiteUser user, List<Product> productList){
        this.user = user;
        this.productList = productList;
    }
}
