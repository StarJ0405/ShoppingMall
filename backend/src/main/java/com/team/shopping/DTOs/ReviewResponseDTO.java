package com.team.shopping.DTOs;

import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Long createDate;

    private Long modifyDate;

    private String url;

    @Builder
    public ReviewResponseDTO (SiteUser user, Review review, String url,
                              Long createDate, Long modifyDate) {
        this.id = review.getId();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.grade = review.getGrade();
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.url = url;
    }
}
