package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Recent;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.RecentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecentService {
    private final RecentRepository recentRepository;

    @Transactional
    public void save(Product product, SiteUser user) {
        recentRepository.save(Recent.builder()
                .product(product)
                .user(user)
                .build());
    }


    @Transactional
    public Optional<Recent> checkRecent(Product product, SiteUser user) {
        return this.recentRepository.findProductByUser(product, user);
    }

    @Transactional
    public void delete(Recent recent) {
        this.recentRepository.delete(recent);
    }

    @Transactional
    public List<Recent> getRecent(SiteUser user) {
        return recentRepository.findUsernameList(user);
    }

    public Optional<Recent> getRecentId(Long recentId) {
        return recentRepository.findRecentId(recentId);
    }

    @Transactional
    public void update(Recent recent) {
        recent.setCreateDate(LocalDateTime.now());
        recentRepository.save(recent);
    }

    public Optional<Recent> findByProduct(Product product) {
        return recentRepository.findByProduct(product);
    }
}
