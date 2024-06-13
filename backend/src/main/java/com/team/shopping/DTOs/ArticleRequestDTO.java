package com.team.shopping.DTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor //생성자가 있어야 기능을한다.
public class ArticleRequestDTO { //요청받는거 , 성재가 보내는거

    private Long articleId;

    private String title;

    private String content;

    private int type; // 타입을 받아와야 공지사항 , 신고 , 자주묻는 질문중 응답해 보낼수있으니


    // 컨트롤러에서 해더로 토큰통해서 이미 이름을 찾았으니 유저정보 자체를 바디에서 굳이 받을 필요없다.



}
