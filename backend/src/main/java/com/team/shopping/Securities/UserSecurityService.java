package com.team.shopping.Securities;


import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = this.userRepository.findById(username);
        if (_siteUser.isEmpty())
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        UserDetails userDetails = new CustomUserDetails(_siteUser.get());
        return userDetails;
    }
}
