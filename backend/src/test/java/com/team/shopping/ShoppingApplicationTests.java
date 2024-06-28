package com.team.shopping;

import com.team.shopping.Domains.*;
import com.team.shopping.Enums.Gender;
import com.team.shopping.Enums.UserRole;
import com.team.shopping.Repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootTest
class ShoppingApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OptionListRepository optionListRepository;
    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    void createAdmin() {
        userRepository.save(SiteUser.builder().username("admin").password(passwordEncoder.encode("1")).name("관리").birthday(LocalDate.now()).email("admin@naver.com").nickname("관리자").phoneNumber("00000000000").role(UserRole.ADMIN).gender(Gender.MAN).build());
    }

    @Test
    void createCategory() {
        {
            Category firstCategory = categoryRepository.save(Category.builder().name("식품").build());
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("신선식품").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("과일").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("채소").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("수산").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("축산").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("곡물").parent(secondCategory).build());
            }
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("가공식품").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("밀키트").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("음료").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("과자").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("냉동식품").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("유제품").parent(secondCategory).build());
            }
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("건강식품").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("영양제").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("건강즙").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("영양식").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("꿀/프로폴리스").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("홍삼").parent(secondCategory).build());
            }
        }
        {
            Category firstCategory = categoryRepository.save(Category.builder().name("패션").build());
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("여성").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("원피스").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("블라우스").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("티셔츠").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("치마").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("바지").parent(secondCategory).build());
            }
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("남성").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("셔츠").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("정장").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("니트").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("코트").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("속옷").parent(secondCategory).build());
            }
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("공용").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("여행가방").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("주얼리").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("시계").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("잡화/소품").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("지갑").parent(secondCategory).build());
            }
        }
        {
            Category firstCategory = categoryRepository.save(Category.builder().name("가전/디지털").build());
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("가전").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("TV").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("냉장고").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("세탁기").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("전자레인지").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("청소기").parent(secondCategory).build());
            }
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("컴퓨터").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("데스크탑").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("모니터").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("키보드").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("마우스").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("스피커").parent(secondCategory).build());
            }
            {
                Category secondCategory = categoryRepository.save(Category.builder().name("휴대용").parent(firstCategory).build());
                categoryRepository.save(Category.builder().name("휴대폰").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("노트북").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("태블릿").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("헤드셋/이어폰").parent(secondCategory).build());
                categoryRepository.save(Category.builder().name("카메라").parent(secondCategory).build());
            }
        }
    }

    @Test
    void insertData() {
        Random r = new Random();
        List<SiteUser> sellers = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            userRepository.save(SiteUser.builder().username("user" + i).email("user" + i + "@naver.com").birthday(LocalDate.now().minusDays(r.nextInt(31)).minusMonths(r.nextInt(12)).minusYears(r.nextInt(70))).role(UserRole.USER).name("사용자" + i).gender(Gender.values()[r.nextInt(Gender.values().length)]).nickname("구매자" + i).password(passwordEncoder.encode("1")).phoneNumber("010000000" + (i < 10 ? "0" + i : i)).build());
            sellers.add(userRepository.save(SiteUser.builder().username("seller" + i).email("seller" + i + "@naver.com").birthday(LocalDate.now().minusDays(r.nextInt(31)).minusMonths(r.nextInt(12)).minusYears(r.nextInt(70))).role(UserRole.SELLER).name("이용자" + i).gender(Gender.values()[r.nextInt(Gender.values().length)]).nickname("판매자" + i).password(passwordEncoder.encode("1")).phoneNumber("010000001" + (i < 10 ? "0" + i : i)).build()));
        }
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category firstCategory = categoryRepository.save(Category.builder().name("top" + i).build());
            for (int j = 0; j < 5; j++) {
                Category secondCategory = categoryRepository.save(Category.builder().name("top" + i + "-" + "middle" + j).parent(firstCategory).build());
                for (int k = 0; k < 10; k++)
                    categories.add(categoryRepository.save(Category.builder().name("top" + i + "-" + "middle" + j + "-" + "bottom" + k).parent(secondCategory).build()));
            }
        }
        Collections.shuffle(categories);
        for (int i = 0; i < 2500; i++) {
            SiteUser seller = sellers.get(r.nextInt(sellers.size()));
            Product product = productRepository.save(Product.builder().seller(seller).category(categories.get(i % categories.size())).price(r.nextInt(1000, 1000000000)).description("이것은 설명").detail("이것은 자세한 내용").dateLimit(LocalDateTime.now().plusDays(r.nextInt(5000))).remain(2500 + r.nextInt(5001)).title(i + "번째 물건 팝니다.").delivery("배달은 어떻게 할까요~").address("전국 처리는 요렇게").receipt("영수증 발행은 알아서").a_s(seller.getPhoneNumber()).brand(seller.getNickname()).build());
            for (int j = 0; j < r.nextInt(5); j++) {
                OptionList optionList = optionListRepository.save(OptionList.builder().name("옵션 목록" + j).product(product).build());
                for (int k = 0; k < (3 + r.nextInt(8)); k++)
                    optionsRepository.save(Options.builder().name("옵션" + k).count(product.getRemain() + r.nextInt(product.getRemain() / 2 + 1)).price(r.nextInt(product.getPrice() / 10, product.getPrice() / 5)).optionList(optionList).build());
            }
            for (int j = 0; j < (5 + r.nextInt(11)); j++)
                tagRepository.save(Tag.builder().name("태그" + (j * (1 + r.nextInt(15)) * (1 + r.nextInt(15)))).product(product).build());
        }
    }

}
