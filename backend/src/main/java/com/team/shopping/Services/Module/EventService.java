package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.EventRequestDTO;
import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event saveEvent (SiteUser creator, EventRequestDTO eventRequestDTO) {
        Double discount = eventRequestDTO.getDiscount();
        if (discount < 0.0) {
            discount = 0.0;
        }
        return this.eventRepository.save(Event.builder()
                .startDate(eventRequestDTO.getStartDate())
                .endDate(eventRequestDTO.getEndDate())
                .creator(creator)
                .discount(discount)
                .build());
    }

    public Event findByProduct (Product product) {
        return this.eventRepository.findByProduct(product);
    }

}
