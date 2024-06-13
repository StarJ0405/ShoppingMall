package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FileSystem {

    @Id
    @Column(length = 50)
    private String k;

    @Column(length = 200)
    private String v;
}
