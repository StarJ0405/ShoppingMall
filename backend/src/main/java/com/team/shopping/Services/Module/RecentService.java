package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Recent;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.RecentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecentService {
    private final RecentRepository recentRepository;

    public void save(Product product, SiteUser user) {
        recentRepository.save(Recent.builder()
                .product(product)
                .user(user)
                .build());
    }


    public Optional<Recent> checkRecent(Product product, SiteUser user) {
        return this.recentRepository.findProductByUser(product, user);
    }

    public void delete(Recent recent) {
        recentRepository.delete(recent);
    }

    public List<Recent> getRecent(SiteUser user) {
        return recentRepository.findUsernameList(user);
    }
}
