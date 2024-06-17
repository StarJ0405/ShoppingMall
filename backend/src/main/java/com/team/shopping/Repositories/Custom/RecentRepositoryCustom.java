package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Recent;
import com.team.shopping.Domains.SiteUser;

import java.util.List;
import java.util.Optional;

public interface RecentRepositoryCustom {

    Optional<Recent> findProductByUser(Product product, SiteUser user);

    List<Recent> findUsernameList(SiteUser user);
}
