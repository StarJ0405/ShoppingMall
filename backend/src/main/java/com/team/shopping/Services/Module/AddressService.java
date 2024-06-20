package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.AddressRequestDTO;
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

    public Address saveAddress (SiteUser user, AddressRequestDTO addressRequestDTO) {
        return this.addressRepository.save(Address.builder()
                .title(addressRequestDTO.getTitle())
                .recipient(addressRequestDTO.getRecipient())
                .phoneNumber(addressRequestDTO.getPhoneNumber())
                .mainAddress(addressRequestDTO.getMainAddress())
                .addressDetail(addressRequestDTO.getAddressDetail())
                .postNumber(addressRequestDTO.getPostNumber())
                .user(user)
                .deliveryMessage(addressRequestDTO.getDeliveryMessage())
                .build());
    }

    public void delete (Address address) {
        this.addressRepository.delete(address);
    }

    public Address updateAddress(AddressRequestDTO addressRequestDTO) {
        Address address = this.addressRepository.findById(addressRequestDTO.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("not found address"));

        String title = addressRequestDTO.getTitle();
        String recipient = addressRequestDTO.getRecipient();
        String phoneNumber = addressRequestDTO.getPhoneNumber();
        String mainAddress = addressRequestDTO.getMainAddress();
        String addressDetail = addressRequestDTO.getAddressDetail();
        int postNumber = addressRequestDTO.getPostNumber();
        String deliveryMessage = addressRequestDTO.getDeliveryMessage();

        if (addressRequestDTO.getTitle() == null || addressRequestDTO.getTitle().trim().isEmpty()) {
            title = address.getTitle();
        }
        if (addressRequestDTO.getRecipient() == null || addressRequestDTO.getRecipient().trim().isEmpty()) {
            recipient = address.getRecipient();
        }
        if (addressRequestDTO.getPhoneNumber() == null || addressRequestDTO.getPhoneNumber().trim().isEmpty()) {
            phoneNumber = address.getPhoneNumber();
        }
        if (addressRequestDTO.getMainAddress() == null || addressRequestDTO.getMainAddress().trim().isEmpty()) {
            mainAddress = address.getMainAddress();
        }
        if (addressRequestDTO.getAddressDetail() == null || addressRequestDTO.getAddressDetail().trim().isEmpty()) {
            addressDetail = address.getAddressDetail();
        }
        if (addressRequestDTO.getPostNumber() == 0) {
            postNumber = address.getPostNumber();
        }
        if (addressRequestDTO.getDeliveryMessage() == null || addressRequestDTO.getDeliveryMessage().trim().isEmpty()) {
            deliveryMessage = address.getDeliveryMessage();
        }

        address.setAddressDetail(addressDetail);
        address.setMainAddress(mainAddress);
        address.setTitle(title);
        address.setRecipient(recipient);
        address.setPhoneNumber(phoneNumber);
        address.setPostNumber(postNumber);
        address.setDeliveryMessage(deliveryMessage);

        return this.addressRepository.save(address);
    }

}
