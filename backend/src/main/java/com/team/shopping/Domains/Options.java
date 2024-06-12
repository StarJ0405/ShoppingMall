package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int count;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionList optionList;

    @Builder
    public Options(String name, int count, int price, OptionList optionList) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.optionList = optionList;
    }
}
