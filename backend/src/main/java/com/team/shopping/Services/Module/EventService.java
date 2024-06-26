package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.EventRequestDTO;
import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getMyList (SiteUser user) {
        return this.eventRepository.findMyList(user);
    }

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

    public void changeActive (Event event) {
        event.setActive(!event.getActive());
        this.eventRepository.save(event);
    }

    public List<Event> findByProduct (Product product) {
        return this.eventRepository.findByProduct(product);
    }

    public List<Event> findByEndDateAfter (LocalDateTime now) {
        return this.eventRepository.findByEndDateGreaterThanEqual(now);
    }

    public List<Event> findByStartDateAfter (LocalDateTime now) {
        return this.eventRepository.findByStartDateGreaterThanEqual(now);
    }

    public void delete (Event event) {
        this.eventRepository.delete(event);
    }

    public Event get (Long eventId) {
        return this.eventRepository.findById(eventId)
                .orElseThrow(()-> new NoSuchElementException("not found Event"));
    }

    public Event updateEvent(Event event, EventRequestDTO eventRequestDTO) {
        event.setDiscount(eventRequestDTO.getDiscount() != null ? eventRequestDTO.getDiscount() : event.getDiscount());
        event.setStartDate(eventRequestDTO.getStartDate() != null ? eventRequestDTO.getStartDate() : event.getStartDate());
        event.setEndDate(eventRequestDTO.getEndDate() != null ? eventRequestDTO.getEndDate() : event.getEndDate());
        event.setModifyDate(LocalDateTime.now());
        return this.eventRepository.save(event);
    }
}
