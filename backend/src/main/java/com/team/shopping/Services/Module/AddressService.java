package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Address;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address get (Long id) {
        return this.addressRepository.findById(id).orElseThrow();
    }

    public List<Address> getList (SiteUser user) {
        return this.addressRepository.findByUser(user);
    }
}
