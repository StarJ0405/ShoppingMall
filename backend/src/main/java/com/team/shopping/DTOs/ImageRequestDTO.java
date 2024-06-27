package com.team.shopping.DTOs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageRequestDTO {

    private MultipartFile file;
    private Long roomId;
}
