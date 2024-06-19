package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.EventProduct;
import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.EventProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProductService {

    private final EventProductRepository eventProductRepository;

    public EventProduct saveEventProduct (Event event, Product product) {
        return this.eventProductRepository.save(EventProduct.builder()
                .product(product)
                .event(event)
                .build());
    }
}
