package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ArticleRequestDTO;
import com.team.shopping.DTOs.ArticleResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor // 서비스(객체 = class)를 불러와서 쓰려면 만들어줘야함
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final MultiService multiService;


    @PostMapping
    public ResponseEntity<?> createArticle (@RequestHeader("Authorization") String accessToken,
                                           @RequestBody ArticleRequestDTO articleRequestDTO) {  // 성재가 보내주는걸 받는거(이안에 있는 값을 보내줌) ,형식이라고 생각하면됌

        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ArticleResponseDTO articleResponseDTO = this.multiService.saveArticle(username, articleRequestDTO); // 내가 ArticleResponseDTO articleResponseDTO 만들어서
            return tokenRecord.getResponseEntity(articleResponseDTO); // 성재한테 쏴줌(반환)
        }
        return tokenRecord.getResponseEntity(); //(로그인안하면 기능이안되니 없을리가 없으니 ok 보낸다.)

    }


    @PutMapping
    public ResponseEntity<?> updateArticle(@RequestHeader("Authorization") String accessToken, // "Authorization" 이이름으로 String accessToken 값을 받겠다는뜻
                                           @RequestBody ArticleRequestDTO articleRequestDTO){

        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if(tokenRecord.isOK()){
            String username = tokenRecord.username();
            ArticleResponseDTO articleResponseDTO = this.multiService.updateArticle(username,articleRequestDTO);
            return tokenRecord.getResponseEntity(articleResponseDTO); // 니가 업데이트 한걸 봐라
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteArticle(@RequestHeader("Authorization") String accessToken,
                                           @RequestHeader("ArticleId") Long articleId)
                                          {

        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if(tokenRecord.isOK()){
            String username = tokenRecord.username();
           this.multiService.deleteArticle(username, articleId);
            return tokenRecord.getResponseEntity("article delete complete"); // 삭제한거를 볼수가 없으니깐
        }
        return tokenRecord.getResponseEntity();
    }


    @GetMapping
    public ResponseEntity<?> GetArticleList(@RequestHeader("Authorization") String accessToken,
                                            @RequestHeader("Type") int type)  // 컨트롤러에서는 캑체로 못받음.
    {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if(tokenRecord.isOK()){
            String username = tokenRecord.username();
            List<ArticleResponseDTO> articleResponseDTOList = this.multiService.getArticleList(type);
            return tokenRecord.getResponseEntity(articleResponseDTOList);  // 엔티티가 아닌 DTO 로 반환하는 이유는 필요한 정보 가있고 필요없는 정보가있으니깐 , 보안상위험
        }
        return tokenRecord.getResponseEntity();
    }

    // 성재가 type 1 을 쏴줌 -> 우리는 TYPE 값이 1인 ARTICLE들을 보내야함
    // 그럼 ARTICLE 의 TYPE 필드 값이 1인 것들만 찾아서 DTO 로 변환 후 LIST<ATICLERESPONSEDTO> 로 보내야함.


}



