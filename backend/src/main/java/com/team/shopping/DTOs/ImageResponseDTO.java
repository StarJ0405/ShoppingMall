package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ImageResponseDTO {

    private String url;


    @Builder
    public ImageResponseDTO(String url) {
        this.url = url;
    }
}
