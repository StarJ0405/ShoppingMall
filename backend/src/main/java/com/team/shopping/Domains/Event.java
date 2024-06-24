package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double discount;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser creator;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private Boolean active;

    @Builder
    public Event (LocalDateTime startDate, LocalDateTime endDate, Double discount, SiteUser creator) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
        this.creator = creator;
        this.createDate = LocalDateTime.now();
        this.active = true;
    }
}
