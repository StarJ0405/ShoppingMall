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
    private Long productId;
    private String productTitle;
    private String username;

    private String nickname;

    private String title;

    private String content;

    private Double grade;

    private Long createDate;

    private Long modifyDate;

    private String profileUrl;


    @Builder
    public ReviewResponseDTO(SiteUser user, Review review, String profileUrl,
                             Long createDate, Long modifyDate) {
        this.id = review.getId();
        this.productId = review.getProduct().getId();
        this.productTitle = review.getProduct().getTitle();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.grade = review.getGrade();
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.profileUrl = profileUrl;
    }
}
