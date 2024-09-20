package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.domain.persistence.CategoryRepository;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.member.custom.domain.dto.FindLowPriceCustomDto;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.member.wish.domain.persistence.WishRepository;
import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.place.liftticket.domain.persistence.LiftTicketRepository;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.domain.persistence.PlaceRepository;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.item.domain.persistence.ItemRepository;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.itemoption.domain.persistence.ItemOptionRepository;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.naverreview.domain.persistence.NaverReviewRepository;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.placeshop.domain.persistence.PlaceShopRepository;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.review.domain.persistence.ReviewRepository;
import com.moa.moa.api.shop.shop.domain.dto.FindLowPriceShopDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.shop.shop.util.ShopUtil;
import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.api.time.businesstime.domain.persistence.BusinessTimeRepository;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.operatingtime.domain.persistence.OperatingTimeRepository;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.moa.moa.api.time.specificday.domain.persistence.SpecificDayRepository;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ShopRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private BusinessTimeRepository businessTimeRepository;
    @Autowired
    private OperatingTimeRepository operatingTimeRepository;
    @Autowired
    private SpecificDayRepository specificDayRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemOptionRepository itemOptionRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private NaverReviewRepository naverReviewRepository;
    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private PlaceShopRepository placeShopRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private LiftTicketRepository liftTicketRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void beforeEach() {
        // 카테고리 생성
        Category category = createCategory();

        // 주소 생성
        List<Address> addresses = createAddress();

        // 비즈니스 타임 생성
        List<BusinessTime> businessTimes = createBusinessTime();

        // 스키장 생성
        List<Place> places = createPlace(category, addresses, businessTimes);

        // 회원 생성
        List<Member> members = createMember();

        // 렌탈샵 생성
        List<Shop> shops = createShop(members, category, addresses, businessTimes);

        // 찜목록 생성
        createWish(shops, members);

        // 렌탈샵 리뷰 생성
        createReview(shops, members);

        // 렌탈샵 네이버 리뷰 생성
        createNaverReview(shops, members);

        // 스키장 렌탈샵 중간테이블 생성
        createPlaceShop(places, shops);

        // 스키장 리프트권 생성
        List<LiftTicket> liftTickets = createLiftTicket(places);

        // 렌탈샵 패키지 생성 (Item)
        createItem(shops, liftTickets);

        // 렌탈샵 상세 옵션 생성
        createItemOption(shops);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        categoryRepository.deleteAll();
        addressRepository.deleteAll();
        businessTimeRepository.deleteAll();
        operatingTimeRepository.deleteAll();
        specificDayRepository.deleteAll();
        shopRepository.deleteAll();
        itemRepository.deleteAll();
        itemOptionRepository.deleteAll();
        reviewRepository.deleteAll();
        naverReviewRepository.deleteAll();
        wishRepository.deleteAll();
        placeShopRepository.deleteAll();
        placeRepository.deleteAll();
        liftTicketRepository.deleteAll();
    }

    @Test
    @DisplayName("렌탈샵 목록 조회 성공")
    void t1() {
        //when
        List<Shop> shops = shopRepository.findAllShopWithinRange(0D, 40D, 130D, 0D);

        //then
        assertThat(shops.size()).isEqualTo(6);
        assertThat(shops.get(0).getName()).isEqualTo("찐렌탈샵");
        assertThat(shops.get(0).getPickUp()).isEqualTo(true);
        assertThat(shops.get(0).getUrl()).isEqualTo("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9");

        assertThat(shops.get(0).getMember().getEmail()).isEqualTo("admin@moa.com");
        assertThat(shops.get(0).getMember().getNickname()).isEqualTo("admin");
        assertThat(shops.get(0).getMember().getRole()).isEqualTo(MemberRole.ADMIN);

        assertThat(shops.get(0).getCategory().getCategoryType()).isEqualTo(CategoryType.SKI_RESORT);

        assertThat(shops.get(0).getAddress().getAddress()).isEqualTo("강원 홍천군 서면 한치골길 39");
        assertThat(shops.get(0).getAddress().getAddressDetail()).isEqualTo("1, 2층");
        assertThat(shops.get(0).getAddress().getLocation()).isEqualTo(geometryFactory.createPoint(new Coordinate(127.666621133276, 37.625378749786)));
        assertThat(shops.get(0).getAddress().getUrl()).isEqualTo("https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%B0%90%EB%A0%8C%ED%83%88%EC%83%B5/place/1680503531?c=15.00,0,0,0,dh&isCorrectAnswer=true");

        assertThat(shops.get(0).getBusinessTime().getOperatingTimes().size()).isEqualTo(13);
        assertThat(shops.get(0).getBusinessTime().getOperatingTimes().get(0).getStatus()).isEqualTo(OperatingType.CLOSED);
        assertThat(shops.get(0).getBusinessTime().getOperatingTimes().get(0).getDay()).isEqualTo(DayType.MON);
        assertThat(shops.get(0).getBusinessTime().getOperatingTimes().get(0).getOpenTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(shops.get(0).getBusinessTime().getOperatingTimes().get(0).getCloseTime()).isEqualTo(LocalTime.of(2, 0));

        assertThat(shops.get(0).getBusinessTime().getSpecificDays().size()).isEqualTo(4);
        assertThat(shops.get(0).getBusinessTime().getSpecificDays().get(0).getStatus()).isEqualTo(SpecificDayType.CLOSED);
        assertThat(shops.get(0).getBusinessTime().getSpecificDays().get(0).getReason()).isEqualTo("신정");
        assertThat(shops.get(0).getBusinessTime().getSpecificDays().get(0).getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(shops.get(0).getBusinessTime().getSpecificDays().get(0).getOpenTime()).isNull();
        assertThat(shops.get(0).getBusinessTime().getSpecificDays().get(0).getCloseTime()).isNull();

        assertThat(shops.get(0).getPlaceShops().size()).isEqualTo(1);
        assertThat(shops.get(0).getPlaceShops().get(0).getPlace().getName()).isEqualTo("비발디파크");
        assertThat(shops.get(0).getPlaceShops().get(0).getPlace().getOpenDate()).isEqualTo(LocalDate.of(2024, 10, 15));
        assertThat(shops.get(0).getPlaceShops().get(0).getPlace().getCloseDate()).isEqualTo(LocalDate.of(2025, 3, 12));
        assertThat(shops.get(0).getPlaceShops().get(0).getPlace().getRecLevel()).isEqualTo(PlaceLevel.LEVEL_1);

        assertThat(shops.get(0).getNaverReview().getAvgScore()).isEqualTo(4.5D);
        assertThat(shops.get(0).getNaverReview().getTotalReview()).isEqualTo(186L);

        assertThat(shops.get(0).getReviews().size()).isEqualTo(4);
        assertThat(shops.get(0).getReviews().get(0).getScore()).isEqualTo(4D);
        assertThat(shops.get(0).getReviews().get(0).getContent()).isEqualTo("좋아요");

        assertThat(shops.get(0).getItems().size()).isEqualTo(30);
        assertThat(shops.get(0).getItems().get(0).getLiftTicket().getStatus()).isEqualTo(LiftTicketStatus.WEEK_DAY);
        assertThat(shops.get(0).getItems().get(0).getLiftTicket().getName()).isEqualTo("스마트4시간권");
        assertThat(shops.get(0).getItems().get(0).getLiftTicket().getTicketType()).isEqualTo(LiftTicketType.SMART);
        assertThat(shops.get(0).getItems().get(0).getLiftTicket().getHours()).isEqualTo(4L);
        assertThat(shops.get(0).getItems().get(0).getLiftTicket().getStartTime()).isNull();
        assertThat(shops.get(0).getItems().get(0).getLiftTicket().getEndTime()).isNull();
        assertThat(shops.get(0).getItems().get(0).getType()).isEqualTo(PackageType.LIFT_EQUIPMENT_CLOTHES);
        assertThat(shops.get(0).getItems().get(0).getName()).isEqualTo("주중 스마트4시간권+장비+의류");
        assertThat(shops.get(0).getItems().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(67000L));
        assertThat(shops.get(0).getItems().get(0).getUsed()).isEqualTo(true);

        assertThat(shops.get(0).getItemOptions().size()).isEqualTo(6);
        assertThat(shops.get(0).getItemOptions().get(0).getName()).isEqualTo(ItemOptionName.LUXURY);
        assertThat(shops.get(0).getItemOptions().get(0).getUsed()).isEqualTo(true);
        assertThat(shops.get(0).getItemOptions().get(0).getStartTime()).isEqualTo(0L);
        assertThat(shops.get(0).getItemOptions().get(0).getEndTime()).isEqualTo(24L);
        assertThat(shops.get(0).getItemOptions().get(0).getAddPrice()).isEqualTo(BigDecimal.valueOf(10000L));
    }

    @Test
    @DisplayName("렌탈샵 목록 조회 성공 - 범위 내 스키장 없음")
    public void t2() {
        //when
        List<Shop> shops = shopRepository.findAllShopWithinRange(0D, 30D, 130D, 0D);

        //then
        assertThat(shops.size()).isEqualTo(0);
    }

    // 최저가 렌탈샵 검색 고정값
    private final LocalDate visitDate = LocalDate.of(2024, 7, 30);
    private final LocalDate visitDateNotInOperation = LocalDate.of(2025, 1, 28);

    // 커스텀 1의 정보
    private final Gender custom1Gender = Gender.MALE;
    private final String custom1Nickname = "커스텀1";
    private final String custom1LiftType= "스마트권";
    private final String custom1LiftTime= "4";
    private final PackageType custom1PackageType = PackageType.LIFT_EQUIPMENT_CLOTHES;
    private final ClothesType custom1ClothesType = ClothesType.LUXURY;
    private final EquipmentType custom1equipmentType = EquipmentType.SHORT_SKI;
    private final String custom1ItemName = ShopUtil.mix.getItemName(visitDate, custom1LiftType, custom1LiftTime, custom1PackageType);
    private final List<ItemOptionName> custom1ItemOptionNames = ShopUtil.match.getItemOptionNames(custom1ClothesType, custom1equipmentType);

    // 커스텀 1의 정보
    private final Gender custom2Gender = Gender.FEMALE;
    private final String custom2Nickname = "커스텀2";
    private final String custom2LiftType= "시간지정권-오후권";
    private final String custom2LiftTime= "4";
    private final PackageType custom2PackageType = PackageType.LIFT_EQUIPMENT_CLOTHES;
    private final ClothesType custom2ClothesType = ClothesType.STANDARD;
    private final EquipmentType custom2equipmentType = EquipmentType.SKI;
    private final String custom2ItemName = ShopUtil.mix.getItemName(visitDate, custom2LiftType, custom2LiftTime, custom2PackageType);
    private final List<ItemOptionName> custom2ItemOptionNames = ShopUtil.match.getItemOptionNames(custom2ClothesType, custom2equipmentType);

    @Test
    @DisplayName("[성공] 샵 정보에 일치하는 커스텀 가격 조회 (최저가 렌탈샵 검색)")
    public void findShopWithCustomForSearch_success() {
        // given
        List<FindLowPriceCustomDto> customs = new ArrayList<>();
        FindLowPriceCustomDto custom1 = FindLowPriceCustomDto.builder()
                .gender(custom1Gender)
                .nickname(custom1Nickname)
                .liftType(custom1LiftType)
                .liftTime(custom1LiftTime)
                .itemName(custom1ItemName)
                .packageType(custom1PackageType)
                .clothesType(custom1ClothesType)
                .equipmentType(custom1equipmentType)
                .itemOptionNames(custom1ItemOptionNames)
                .build();

        FindLowPriceCustomDto custom2 = FindLowPriceCustomDto.builder()
                .gender(custom2Gender)
                .nickname(custom2Nickname)
                .liftType(custom2LiftType)
                .liftTime(custom2LiftTime)
                .itemName(custom2ItemName)
                .packageType(custom2PackageType)
                .clothesType(custom2ClothesType)
                .equipmentType(custom2equipmentType)
                .itemOptionNames(custom2ItemOptionNames)
                .build();

        customs.add(custom1);
        customs.add(custom2);

        // when
        FindLowPriceShopDto shopWithCustom = shopRepository.findShopWithCustomForSearch(1L, customs, true).get();

        //then
        assertThat(shopWithCustom.getShopId()).isEqualTo(1L);
        assertThat(shopWithCustom.getName()).isEqualTo("찐렌탈샵");
        assertThat(shopWithCustom.getStoreUrl()).isEqualTo("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9");
        assertThat(shopWithCustom.getCustomPrices().size()).isEqualTo(2);
        assertThat(shopWithCustom.getCustomPrices().get(0)).isEqualTo(BigDecimal.valueOf(87000.0));
        assertThat(shopWithCustom.getCustomPrices().get(1)).isEqualTo(BigDecimal.valueOf(76000.0));
        assertThat(shopWithCustom.getTotalPrice()).isEqualTo(BigDecimal.valueOf(163000.0));
        assertThat(shopWithCustom.getAvgScore()).isEqualTo(2.5);
        assertThat(shopWithCustom.getTotalCount()).isEqualTo(4);
        assertThat(shopWithCustom.getNrAvgScore()).isEqualTo(4.5);
        assertThat(shopWithCustom.getNrTotalCount()).isEqualTo(186);
    }

    @Test
    @DisplayName("[실패] 샵 정보에 일치하는 커스텀 가격 조회 (최저가 렌탈샵 검색) - 쿼리 조건 itemName에 null이 들어갈 경우")
    public void findShopWithCustomForSearch_fail() {
        // given
        List<FindLowPriceCustomDto> customs = new ArrayList<>();
        FindLowPriceCustomDto custom1 = FindLowPriceCustomDto.builder()
                .gender(custom1Gender)
                .nickname(custom1Nickname)
                .liftType(custom1LiftType)
                .liftTime(custom1LiftTime)
                .itemName(null)
                .packageType(custom1PackageType)
                .clothesType(custom1ClothesType)
                .equipmentType(custom1equipmentType)
                .itemOptionNames(custom1ItemOptionNames)
                .build();

        FindLowPriceCustomDto custom2 = FindLowPriceCustomDto.builder()
                .gender(custom2Gender)
                .nickname(custom2Nickname)
                .liftType(custom2LiftType)
                .liftTime(custom2LiftTime)
                .itemName(null)
                .packageType(custom2PackageType)
                .clothesType(custom2ClothesType)
                .equipmentType(custom2equipmentType)
                .itemOptionNames(custom2ItemOptionNames)
                .build();

        customs.add(custom1);
        customs.add(custom2);

        // when
        InvalidDataAccessApiUsageException exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            shopRepository.findShopWithCustomForSearch(1L, customs, true).get();
        });

        // then
        assertTrue(exception.getMessage().contains("eq(null) is not allowed. Use isNull() instead"));
    }

    private Category createCategory() {
        Category category = Category.builder()
                .categoryType(CategoryType.SKI_RESORT)
                .build();

        categoryRepository.save(category);

        return category;
    }

    private List<Address> createAddress() {
        List<Address> list = new ArrayList<>();
        Address address_1 = createAddress("강원도 홍천군 서면 한치골길 262", 37.6521031526954, 127.687106349987,
                "https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh");
        Address address_2 = createAddress("강원 홍천군 서면 한치골길 39", "1, 2층", 37.625378749786, 127.666621133276,
                "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%B0%90%EB%A0%8C%ED%83%88%EC%83%B5/place/1680503531?c=15.00,0,0,0,dh&isCorrectAnswer=true");
        Address address_3 = createAddress("강원 홍천군 서면 한치골길 952", 37.6935333700434, 127.701873788335,
                "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%95%84%EC%A7%80%ED%8A%B8/place/11590090?c=18.01,0,0,0,dh&isCorrectAnswer=true");
        Address address_4 = createAddress("강원 정선군 고한읍 하이원길 424", 37.2072213760495, 128.836835354268,
                "https://map.naver.com/p/entry/place/92136142?lng=128.8388599&lat=37.204042&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh");
        Address address_5 = createAddress("강원 춘천시 남산면 북한강변길 688", 37.8300557977982, 127.57878172946,
                "https://map.naver.com/p/search/%EC%8A%A4%ED%82%A4%EC%9E%A5/place/15648643?placePath=?entry=pll&from=nx&fromNxList=true&searchType=place&c=15.00,0,0,0,dh");
        Address address_6 = createAddress("강원 홍천군 서면 한서로 2137", "비발디파크인생렌탈샵", 37.6167793731889, 127.671714070978,
                "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%20%ED%8C%8C%ED%81%AC%20%EB%A0%8C%ED%83%88/place/1034233118?c=12.00,0,0,0,dh&placePath=%3Fentry%253Dpll");
        Address address_7 = createAddress("강원 정선군 고한읍 고한로 40", "하이원 스키샵 월남스키 렌탈샵", 37.2076563451798, 128.843415629048,
                "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/12447242?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dpll");
        Address address_8 = createAddress("강원 정선군 사북읍 하이원길 36", "눈의나라", 37.2234130104246, 128.814883350542,
                "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/12995662?c=17.00,0,0,0,dh&placePath=%3Fentry%253Dpll");
        Address address_9 = createAddress("강원 정선군 고한읍 고한로 12", "스노우블루 스키샵", 37.2095420989986, 128.841584050646,
                "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/1053516546?c=17.00,0,0,0,dh&placePath=%3Fentry%253Dpll");

        list.add(address_1);
        list.add(address_2);
        list.add(address_3);
        list.add(address_4);
        list.add(address_5);
        list.add(address_6);
        list.add(address_7);
        list.add(address_8);
        list.add(address_9);

        addressRepository.saveAll(list);

        return list;
    }

    private List<BusinessTime> createBusinessTime() {
        List<BusinessTime> list = new ArrayList<>();

        BusinessTime businessTime_1 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_1 = operating1(businessTime_1);
        List<SpecificDay> specificDays_1 = specific1(businessTime_1);
        list.add(businessTime_1);

        operatingTimeRepository.saveAll(operatingTimes_1);
        specificDayRepository.saveAll(specificDays_1);

        BusinessTime businessTime_2 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_2 = operating2(businessTime_2);
        List<SpecificDay> specificDays_2 = specific2(businessTime_2);
        list.add(businessTime_2);

        operatingTimeRepository.saveAll(operatingTimes_2);
        specificDayRepository.saveAll(specificDays_2);

        BusinessTime businessTime_3 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_3 = operating3(businessTime_3);
        List<SpecificDay> specificDays_3 = specific3(businessTime_3);
        list.add(businessTime_3);

        operatingTimeRepository.saveAll(operatingTimes_3);
        specificDayRepository.saveAll(specificDays_3);

        BusinessTime businessTime_4 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_4 = operating4(businessTime_4);
        List<SpecificDay> specificDays_4 = specific4(businessTime_4);
        list.add(businessTime_4);

        operatingTimeRepository.saveAll(operatingTimes_4);
        specificDayRepository.saveAll(specificDays_4);

        BusinessTime businessTime_5 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_5 = operating5(businessTime_5);
        List<SpecificDay> specificDays_5 = specific5(businessTime_5);
        list.add(businessTime_5);

        operatingTimeRepository.saveAll(operatingTimes_5);
        specificDayRepository.saveAll(specificDays_5);

        BusinessTime businessTime_6 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_6 = operating6(businessTime_6);
        List<SpecificDay> specificDays_6 = specific6(businessTime_6);
        list.add(businessTime_6);

        operatingTimeRepository.saveAll(operatingTimes_6);
        specificDayRepository.saveAll(specificDays_6);

        BusinessTime businessTime_7 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_7 = operating7(businessTime_7);
        List<SpecificDay> specificDays_7 = specific7(businessTime_7);
        list.add(businessTime_7);

        operatingTimeRepository.saveAll(operatingTimes_7);
        specificDayRepository.saveAll(specificDays_7);

        BusinessTime businessTime_8 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_8 = operating8(businessTime_8);
        List<SpecificDay> specificDays_8 = specific8(businessTime_8);
        list.add(businessTime_8);

        operatingTimeRepository.saveAll(operatingTimes_8);
        specificDayRepository.saveAll(specificDays_8);

        BusinessTime businessTime_9 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_9 = operating9(businessTime_9);
        List<SpecificDay> specificDays_9 = specific9(businessTime_9);
        list.add(businessTime_9);

        operatingTimeRepository.saveAll(operatingTimes_9);
        specificDayRepository.saveAll(specificDays_9);

        businessTimeRepository.saveAll(list);

        return list;
    }

    private List<Place> createPlace(Category category, List<Address> addresses, List<BusinessTime> businessTimes) {
        List<Place> list = new ArrayList<>();
        Place place_1 = createPlace(category, addresses.get(0), businessTimes.get(0), "비발디파크", LocalDate.of(2024, 10, 15),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_1);

        Place place_2 = createPlace(category, addresses.get(3), businessTimes.get(3), "하이원리조트", LocalDate.of(2024, 10, 16),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_2);

        Place place_3 = createPlace(category, addresses.get(4), businessTimes.get(4), "엘리시안", LocalDate.of(2024, 10, 17),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_4);

        Place place_4 = createPlace(category, addresses.get(8), businessTimes.get(8), "지산리조트", LocalDate.of(2024, 10, 18),
                LocalDate.of(2025, 3, 15), PlaceLevel.LEVEL_4);

        list.add(place_1);
        list.add(place_2);
        list.add(place_3);
        list.add(place_4);

        placeRepository.saveAll(list);

        return list;
    }

    // Create Object
    private Place createPlace(Category category, Address address, BusinessTime time, String name, LocalDate open, LocalDate close, PlaceLevel level, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return Place.builder()
                .category(category)
                .address(address)
                .businessTime(time)
                .name(name)
                .openDate(open)
                .closeDate(close)
                .recLevel(level)
                .deletedAt(deleted ? LocalDateTime.now() : null)
                .build();
    }

    private Address createAddress(String address, double y, double x, String url, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return Address.builder()
                .address(address)
                .location(geometryFactory.createPoint(new Coordinate(x, y)))
                .url(url)
                .deletedAt(deleted ? LocalDateTime.now() : null)
                .build();
    }

    private Address createAddress(String address, String detail, double y, double x, String url, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return Address.builder()
                .address(address)
                .addressDetail(detail)
                .location(geometryFactory.createPoint(new Coordinate(x, y)))
                .url(url)
                .deletedAt(deleted ? LocalDateTime.now() : null)
                .build();
    }

    private OperatingTime createOperatingTime(BusinessTime businessTime, OperatingType type, DayType day, LocalTime open, LocalTime close, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return OperatingTime.builder()
                .businessTime(businessTime)
                .status(type)
                .day(day)
                .openTime(open)
                .closeTime(close)
                .deletedAt(deleted ? LocalDateTime.now() : null)
                .build();
    }

    private List<OperatingTime> operating1(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));

        return list;
    }

    private List<OperatingTime> operating2(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.CLOSED, DayType.MON, LocalTime.of(8, 0),
                LocalTime.of(2, 0)));

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.TUE, LocalTime.of(12, 0),
                LocalTime.of(13, 0)));

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.WED, LocalTime.of(12, 0),
                LocalTime.of(13, 0)));

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.THU, LocalTime.of(12, 0),
                LocalTime.of(13, 0)));

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.FRI, LocalTime.of(12, 0),
                LocalTime.of(13, 0)));

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.SAT, LocalTime.of(12, 0),
                LocalTime.of(13, 0)));

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.SUN, LocalTime.of(12, 0),
                LocalTime.of(13, 0)));

        return list;
    }

    private List<OperatingTime> operating3(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(7, 0),
                LocalTime.of(2, 30)));

        return list;
    }

    private List<OperatingTime> operating4(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.addAll(operating3(businessTime));

        return list;
    }

    private List<OperatingTime> operating5(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.addAll(operating3(businessTime));

        return list;
    }

    private List<OperatingTime> operating6(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));

        return list;
    }

    private List<OperatingTime> operating7(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(5, 0),
                LocalTime.of(23, 30)));

        return list;
    }

    private List<OperatingTime> operating8(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(11, 0),
                LocalTime.of(3, 30)));

        return list;
    }

    private List<OperatingTime> operating9(BusinessTime businessTime) {
        List<OperatingTime> list = new ArrayList<>();

        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));
        list.add(createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(8, 0),
                LocalTime.of(1, 30)));

        return list;
    }

    private SpecificDay createSpecific(BusinessTime businessTime, SpecificDayType type, String reason, LocalDate date, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return SpecificDay.builder()
                .businessTime(businessTime)
                .status(type)
                .reason(reason)
                .date(date)
                .deletedAt(deleted ? LocalDateTime.now() : null)
                .build();
    }

    private List<SpecificDay> specific1(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 28)));
        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 29)));
        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 30)));

        return list;
    }

    private List<SpecificDay> specific2(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "신정", LocalDate.of(2025, 1, 1)));
        list.addAll(specific1(businessTime));

        return list;
    }

    private List<SpecificDay> specific3(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.addAll(specific1(businessTime));

        return list;
    }

    private List<SpecificDay> specific4(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 29)));

        return list;
    }

    private List<SpecificDay> specific5(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 29)));

        return list;
    }

    private List<SpecificDay> specific6(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.addAll(specific1(businessTime));

        return list;
    }

    private List<SpecificDay> specific7(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.addAll(specific1(businessTime));

        return list;
    }

    private List<SpecificDay> specific8(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.addAll(specific1(businessTime));

        return list;
    }

    private List<SpecificDay> specific9(BusinessTime businessTime) {
        List<SpecificDay> list = new ArrayList<>();

        list.addAll(specific1(businessTime));

        return list;
    }

    private List<Member> createMember() {
        List<Member> list = new ArrayList<>();
        list.add(Member.builder().email("admin@moa.com").nickname("admin").role(MemberRole.ADMIN).build());
        list.add(Member.builder().email("one@moa.com").nickname("one").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("two@moa.com").nickname("two").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("three@moa.com").nickname("three").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("four@moa.com").nickname("four").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("five@moa.com").nickname("five").role(MemberRole.MEMBER).build());
        memberRepository.saveAll(list);
        return list;
    }

    private List<PlaceShop> createPlaceShop(List<Place> places, List<Shop> shops) {
        List<PlaceShop> list = new ArrayList<>();
        list.add(PlaceShop.builder().place(places.get(0)).shop(shops.get(0)).build());
        list.add(PlaceShop.builder().place(places.get(0)).shop(shops.get(1)).build());
        list.add(PlaceShop.builder().place(places.get(0)).shop(shops.get(2)).build());
        list.add(PlaceShop.builder().place(places.get(1)).shop(shops.get(3)).build());
        list.add(PlaceShop.builder().place(places.get(1)).shop(shops.get(4)).build());
        list.add(PlaceShop.builder().place(places.get(1)).shop(shops.get(5)).build());
        placeShopRepository.saveAll(list);
        return list;
    }

    private List<Shop> createShop(List<Member> members, Category category, List<Address> addresses, List<BusinessTime> times) {
        List<Shop> list = new ArrayList<>();
        list.add(Shop.builder().member(members.get(0)).category(category).address(addresses.get(1)).businessTime(times.get(1)).name("찐렌탈샵").pickUp(true).url("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9").build());
        list.add(Shop.builder().member(members.get(0)).category(category).address(addresses.get(2)).businessTime(times.get(2)).name("아지트").pickUp(true).url("https://smartstore.naver.com/rentalshop1/products/6117544378?nl-au=9e070d7e195341e699c36096c861ab13&NaPm=ct%3Dlziltmg8%7Cci%3D0d4c31cf63842f0accf616231028952db4b1b241%7Ctr%3Dslsl%7Csn%3D5126374%7Chk%3D73c670109b1457dee566270729957b85127e0128").build());
        list.add(Shop.builder().member(members.get(0)).category(category).address(addresses.get(5)).businessTime(times.get(5)).name("인생렌탈샵").pickUp(false).url("https://smartstore.naver.com/dgshop/products/9614236927").build());
        list.add(Shop.builder().member(members.get(0)).category(category).address(addresses.get(6)).businessTime(times.get(6)).name("월남스키").pickUp(true).url("https://smartstore.naver.com/wnskishop/products/5314182831").build());
        list.add(Shop.builder().member(members.get(0)).category(category).address(addresses.get(7)).businessTime(times.get(7)).name("눈의나라").pickUp(false).url("https://smartstore.naver.com/noonnara/products/5323804307").build());
        list.add(Shop.builder().member(members.get(0)).category(category).address(addresses.get(8)).businessTime(times.get(8)).name("스노우블루").pickUp(true).url("https://smartstore.naver.com/snowblue1/products/9727997372").build());
        shopRepository.saveAll(list);
        return list;
    }

    private List<Wish> createWish(List<Shop> shops, List<Member> members) {
        List<Wish> list = new ArrayList<>();
        list.add(Wish.builder().shop(shops.get(1)).member(members.get(0)).build());
        list.add(Wish.builder().shop(shops.get(1)).member(members.get(1)).build());
        list.add(Wish.builder().shop(shops.get(2)).member(members.get(2)).build());
        list.add(Wish.builder().shop(shops.get(2)).member(members.get(3)).build());
        list.add(Wish.builder().shop(shops.get(4)).member(members.get(4)).build());
        list.add(Wish.builder().shop(shops.get(1)).member(members.get(5)).build());
        wishRepository.saveAll(list);
        return list;
    }

    private List<Review> createReview(List<Shop> shops, List<Member> members) {
        List<Review> list = new ArrayList<>();
        list.add(Review.builder().shop(shops.get(0)).member(members.get(1)).score(4d).content("좋아요").build());
        list.add(Review.builder().shop(shops.get(0)).member(members.get(2)).score(3d).content("아쉬워요").build());
        list.add(Review.builder().shop(shops.get(0)).member(members.get(3)).score(2d).content("별로에요").build());
        list.add(Review.builder().shop(shops.get(0)).member(members.get(4)).score(1d).content("개쓰레기 ㅋㅋㅋ").build());
        list.add(Review.builder().shop(shops.get(1)).member(members.get(1)).score(3d).content("어중간히 괜찮음 ㅋ").build());
        list.add(Review.builder().shop(shops.get(1)).member(members.get(2)).score(3d).content("괜찮음").build());
        list.add(Review.builder().shop(shops.get(1)).member(members.get(3)).score(3d).content("나쁘지 않음").build());
        list.add(Review.builder().shop(shops.get(1)).member(members.get(4)).score(3d).content("직원이 불친절").build());
        list.add(Review.builder().shop(shops.get(1)).member(members.get(5)).score(3d).content("ㅋㅋㅋㅋㅋ").build());
        list.add(Review.builder().shop(shops.get(2)).member(members.get(1)).score(3d).content("그저 그럼").build());
        list.add(Review.builder().shop(shops.get(2)).member(members.get(2)).score(4d).content("좋음").build());
        list.add(Review.builder().shop(shops.get(2)).member(members.get(3)).score(5d).content("아주 좋음").build());
        list.add(Review.builder().shop(shops.get(2)).member(members.get(4)).score(1d).content("별로").build());
        list.add(Review.builder().shop(shops.get(2)).member(members.get(5)).score(2d).content("ㅠㅠ").build());
        list.add(Review.builder().shop(shops.get(3)).member(members.get(1)).score(1d).content("진짜 별로").build());
        list.add(Review.builder().shop(shops.get(3)).member(members.get(2)).score(2d).content("가지마세요").build());
        list.add(Review.builder().shop(shops.get(3)).member(members.get(3)).score(3d).content("좋습니다").build());
        list.add(Review.builder().shop(shops.get(3)).member(members.get(4)).score(4d).content("추천합니다").build());
        list.add(Review.builder().shop(shops.get(4)).member(members.get(2)).score(3d).content("그저 그럼").build());
        list.add(Review.builder().shop(shops.get(4)).member(members.get(3)).score(3d).content("아쉬워요").build());
        list.add(Review.builder().shop(shops.get(4)).member(members.get(4)).score(5d).content("진짜 좋음").build());
        list.add(Review.builder().shop(shops.get(4)).member(members.get(5)).score(5d).content("꼭 가세요").build());
        list.add(Review.builder().shop(shops.get(5)).member(members.get(1)).score(3d).content("보통").build());
        list.add(Review.builder().shop(shops.get(5)).member(members.get(2)).score(4d).content("좋아용").build());
        list.add(Review.builder().shop(shops.get(5)).member(members.get(3)).score(2d).content("불친절함").build());
        list.add(Review.builder().shop(shops.get(5)).member(members.get(4)).score(1d).content("장비가 별로임").build());
        list.add(Review.builder().shop(shops.get(5)).member(members.get(5)).score(5d).content("짱!!").build());
        reviewRepository.saveAll(list);
        return list;
    }

    private List<NaverReview> createNaverReview(List<Shop> shops, List<Member> members) {
        List<NaverReview> list = new ArrayList<>();
        list.add(NaverReview.builder().shop(shops.get(0)).avgScore(4.5d).totalReview(186L).build());
        list.add(NaverReview.builder().shop(shops.get(1)).avgScore(4d).totalReview(299L).build());
        list.add(NaverReview.builder().shop(shops.get(2)).avgScore(3.5d).totalReview(100L).build());
        list.add(NaverReview.builder().shop(shops.get(3)).avgScore(5d).totalReview(50L).build());
        list.add(NaverReview.builder().shop(shops.get(4)).avgScore(4.8d).totalReview(177L).build());
        list.add(NaverReview.builder().shop(shops.get(5)).avgScore(4.3d).totalReview(80L).build());
        naverReviewRepository.saveAll(list);
        return list;
    }

    private List<LiftTicket> createLiftTicket(List<Place> places) {
        List<LiftTicket> list = new ArrayList<>();

        // place 1
        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("오전권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(12, 0)).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("오후권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(18, 0)).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("야간권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(22, 0)).endTime(LocalTime.of(2, 0)).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("오전권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(12, 0)).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("오후권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(18, 0)).build());

        list.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("야간권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(22, 0)).endTime(LocalTime.of(2, 0)).build());

        places.get(0).addLiftTickets(list);

        // place 2
        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트3시간권").ticketType(
                LiftTicketType.SMART).hours(3L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트5시간권").ticketType(
                LiftTicketType.SMART).hours(5L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트7시간권").ticketType(
                LiftTicketType.SMART).hours(7L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("종일권").ticketType(
                LiftTicketType.TIME).hours(13L).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(22, 0)).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트3시간권").ticketType(
                LiftTicketType.SMART).hours(3L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트5시간권").ticketType(
                LiftTicketType.SMART).hours(5L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트7시간권").ticketType(
                LiftTicketType.SMART).hours(7L).build());

        list.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("종일권").ticketType(
                LiftTicketType.TIME).hours(13L).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(22, 0)).build());

        places.get(1).addLiftTickets(list);

        // place 3
        list.add(LiftTicket.builder().place(places.get(2)).status(LiftTicketStatus.WEEK_DAY).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        list.add(LiftTicket.builder().place(places.get(2)).status(LiftTicketStatus.WEEK_END).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        places.get(2).addLiftTickets(list);

        liftTicketRepository.saveAll(list);

        return list;
    }

    private List<Item> createItem(List<Shop> shops, List<LiftTicket> liftTickets) {
        List<Item> list = new ArrayList<>();
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(52000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(52000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(81000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(61000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(61000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(56000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(56000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(45000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(45000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(57000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(57000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(87000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(67000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(51000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(51000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(52000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(61000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(57000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(67000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(55000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(55000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(83000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(65000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(73000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(53000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(53000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(58000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(75000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(59000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(59000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(85000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(83000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(89000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(69000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(62000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(74000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(106000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(104000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(100000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(62000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(74000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(106000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(104000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(100000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(60000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(74000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(60000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(74000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(80000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(70000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(86000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(84000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(80000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(108000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(106000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(102000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(70000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(76000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(86000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(84000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(80000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(108000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(102000L)).used(true).build());
        list.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(102000L)).used(true).build());
        itemRepository.saveAll(list);
        return list;
    }

    private List<ItemOption> createItemOption(List<Shop> shops) {
        List<ItemOption> list = new ArrayList<>();
        list.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(24L).addPrice(BigDecimal.valueOf(10000L)).build());
        list.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(24L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(10000L)).build());
        list.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(7L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(7L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.LUXURY).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(25000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.PREMIUM).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(35000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(5000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(10000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(10000L)).build());
        list.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(15000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(25000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(35000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.LUXURY).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.PREMIUM).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(40000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(10000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(15000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.SHORT_SKI).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.INLINE_SKI).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(25000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.LUXURY).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.PREMIUM).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(5000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(18000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.LUXURY).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.PREMIUM).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(5000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(18000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.SHORT_SKI).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        list.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.INLINE_SKI).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(12000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.LUXURY).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.PREMIUM).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(30000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(5000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(10000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.SHORT_SKI).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        list.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.INLINE_SKI).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(12000L)).build());
        itemOptionRepository.saveAll(list);
        return list;
    }
}