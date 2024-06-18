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

    private String username;

    private String nickname;

    private String title;

    private String content;

    private Double grade;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String url;

    @Builder
    public ReviewResponseDTO (SiteUser user, Review review, String url) {
        this.id = review.getId();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.grade = review.getGrade();
        this.createDate = review.getCreateDate();
        this.modifyDate = review.getModifyDate();
        this.url = url;
    }
}
