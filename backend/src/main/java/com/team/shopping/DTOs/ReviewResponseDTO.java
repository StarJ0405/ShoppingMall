package com.team.shopping.DTOs;

import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long id;

    private String authorName;

    private String title;

    private String content;

    private Double grade;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Builder
    public ReviewResponseDTO (SiteUser user, Review review) {
        this.id = review.getId();
        this.authorName = user.getNickname();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.grade = review.getGrade();
        this.createDate = review.getCreateDate();
        this.modifyDate = review.getModifyDate();
    }
}
