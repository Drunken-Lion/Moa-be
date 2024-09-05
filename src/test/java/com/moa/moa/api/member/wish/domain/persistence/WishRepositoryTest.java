package com.moa.moa.api.member.wish.domain.persistence;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.domain.persistence.CategoryRepository;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.shop.shop.domain.persistence.ShopRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class WishRepositoryTest {
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
    private WishRepository wishRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void beforeEach() {
        // 카테고리 생성
        Category category = createCategory();

        // 주소 생성
        List<Address> addresses = createAddress();

        // 비즈니스 타임 생성
        List<BusinessTime> businessTimes = createBusinessTime();

        // 회원 생성
        List<Member> members = createMember();

        // 렌탈샵 생성
        List<Shop> shops = createShop(members, category, addresses, businessTimes);

        // 찜목록 생성
        createWish(shops, members);
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
        wishRepository.deleteAll();
    }

    @Test
    @DisplayName("내가 선택한 렌탈샵의 찜 조회 성공")
    void t1() {
        //given
        Shop shop = shopRepository.findAll().get(2);
        Member member = memberRepository.findByEmail("three@moa.com").get();

        //when
        Wish wish = wishRepository.findWishByShopAndMember(shop, member).get();

        //then
        assertThat(wish.getMember().getEmail()).isEqualTo("three@moa.com");
        assertThat(wish.getMember().getNickname()).isEqualTo("three");
        assertThat(wish.getMember().getRole()).isEqualTo(MemberRole.MEMBER);

        assertThat(wish.getShop().getName()).isEqualTo("인생렌탈샵");
        assertThat(wish.getShop().getPickUp()).isEqualTo(false);
        assertThat(wish.getShop().getUrl()).isEqualTo("https://smartstore.naver.com/dgshop/products/9614236927");
    }

    @Test
    @DisplayName("내가 선택한 렌탈샵의 찜 조회 성공 - 찜하지 않아 값 없음")
    void t2() {
        //given
        Shop shop = shopRepository.findAll().get(0);
        Member member = memberRepository.findByEmail("three@moa.com").get();

        //when
        Optional<Wish> wish = wishRepository.findWishByShopAndMember(shop, member);

        //then
        assertThat(wish).isEqualTo(Optional.empty());
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
}