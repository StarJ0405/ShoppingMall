package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.EventProduct;
import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.EventProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public void delete (EventProduct eventProduct) {
        this.eventProductRepository.delete(eventProduct);
    }

    public List<EventProduct> getList (Event event) {
        return this.eventProductRepository.findByEvent(event);
    }

    public void deleteEventProduct (List<Long> eventProductId) {
        for (Long id : eventProductId) {
            EventProduct eventProduct = this.get(id);
            if (eventProduct != null) {
                this.delete(eventProduct);
            }
        }
    }

    public EventProduct get (Long id) {
        return this.eventProductRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("not found eventProduct"));
    }

    public List<EventProduct> findByProduct(Product product) {
        return eventProductRepository.findByProduct(product);
    }
}
