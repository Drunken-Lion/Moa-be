package com.moa.moa.api.place.place.presentation;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.domain.persistence.CategoryRepository;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.domain.persistence.PlaceRepository;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class PlaceControllerTest {
    @Autowired
    private MockMvc mvc;
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
    private PlaceRepository placeRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    @Transactional
    void beforeEach() {
        // 카테고리 생성
        Category category_1 = categoryRepository.save(
                Category.builder()
                        .categoryType(CategoryType.SKI_RESORT)
                        .build());

        // 주소 생성
        List<Address> addresses = createAddress();

        // 비즈니스 타임 생성
        List<BusinessTime> businessTimes = createBusinessTime();

        // 스키장 생성
        List<Place> places = createPlace(category_1, addresses, businessTimes);
    }

    @AfterEach
    @Transactional
    void afterEach() {
        categoryRepository.deleteAll();
        addressRepository.deleteAll();
        businessTimeRepository.deleteAll();
        operatingTimeRepository.deleteAll();
        specificDayRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    @DisplayName("스키장 목록 조회 성공")
    void t1() throws Exception {
        ResultActions actions = mvc
                .perform(get("/v1/places")
                        .param("leftTopX", "0")
                        .param("leftTopY", "40")
                        .param("rightBottomX", "130")
                        .param("rightBottomY", "0"))
                .andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(PlaceController.class))
                .andExpect(handler().methodName("findAllPlace"))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.[0].name", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].open", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].close", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].recLevel", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].createdAt", instanceOf(String.class)))
                .andExpect(jsonPath("$.[0].images", notNullValue()))
                .andExpect(jsonPath("$.[0].address", notNullValue()))
                .andExpect(jsonPath("$.[0].operatingTimes", notNullValue()))
                .andExpect(jsonPath("$.[0].specificDays", notNullValue()))
                .andExpect(jsonPath("$.[0].amenities", notNullValue()))
                .andExpect(jsonPath("$.[0].slopes", notNullValue()));
    }

    private List<Place> createPlace(Category category, List<Address> addresses, List<BusinessTime> businessTimes) {
        List<Place> list = new ArrayList<>();
        Place place_1 = placeRepository.save(
                createPlace(category, addresses.get(0), businessTimes.get(0), "비발디파크", LocalDate.of(2024, 10, 15),
                        LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_1));

        Place place_2 = placeRepository.save(
                createPlace(category, addresses.get(3), businessTimes.get(3), "하이원리조트", LocalDate.of(2024, 10, 16),
                        LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_2));

        Place place_3 = placeRepository.save(
                createPlace(category, addresses.get(4), businessTimes.get(4), "엘리시안", LocalDate.of(2024, 10, 17),
                        LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_4));

        list.add(place_1);
        list.add(place_2);
        list.add(place_3);
        return list;
    }

    private List<Address> createAddress() {
        List<Address> list = new ArrayList<>();
        Address address_1 = addressRepository.save(
                createAddress("강원도 홍천군 서면 한치골길 262", 37.6521031526954, 127.687106349987,
                        "https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh"));
        Address address_2 = addressRepository.save(
                createAddress("강원 홍천군 서면 한치골길 39", "1, 2층", 37.625378749786, 127.666621133276,
                        "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%B0%90%EB%A0%8C%ED%83%88%EC%83%B5/place/1680503531?c=15.00,0,0,0,dh&isCorrectAnswer=true"));
        Address address_3 = addressRepository.save(
                createAddress("강원 홍천군 서면 한치골길 952", 37.6935333700434, 127.701873788335,
                        "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%95%84%EC%A7%80%ED%8A%B8/place/11590090?c=18.01,0,0,0,dh&isCorrectAnswer=true"));
        Address address_4 = addressRepository.save(
                createAddress("강원 정선군 고한읍 하이원길 424", 37.2072213760495, 128.836835354268,
                        "https://map.naver.com/p/entry/place/92136142?lng=128.8388599&lat=37.204042&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh"));
        Address address_5 = addressRepository.save(
                createAddress("강원 춘천시 남산면 북한강변길 688", 37.8300557977982, 127.57878172946,
                        "https://map.naver.com/p/search/%EC%8A%A4%ED%82%A4%EC%9E%A5/place/15648643?placePath=?entry=pll&from=nx&fromNxList=true&searchType=place&c=15.00,0,0,0,dh"));
        Address address_6 = addressRepository.save(
                createAddress("강원 홍천군 서면 한서로 2137", "비발디파크인생렌탈샵", 37.6167793731889, 127.671714070978,
                        "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%20%ED%8C%8C%ED%81%AC%20%EB%A0%8C%ED%83%88/place/1034233118?c=12.00,0,0,0,dh&placePath=%3Fentry%253Dpll"));
        Address address_7 = addressRepository.save(
                createAddress("강원 정선군 고한읍 고한로 40", "하이원 스키샵 월남스키 렌탈샵", 37.2076563451798, 128.843415629048,
                        "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/12447242?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dpll"));
        Address address_8 = addressRepository.save(
                createAddress("강원 정선군 사북읍 하이원길 36", "눈의나라", 37.2234130104246, 128.814883350542,
                        "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/12995662?c=17.00,0,0,0,dh&placePath=%3Fentry%253Dpll"));
        Address address_9 = addressRepository.save(
                createAddress("강원 정선군 고한읍 고한로 12", "스노우블루 스키샵", 37.2095420989986, 128.841584050646,
                        "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/1053516546?c=17.00,0,0,0,dh&placePath=%3Fentry%253Dpll"));

        list.add(address_1);
        list.add(address_2);
        list.add(address_3);
        list.add(address_4);
        list.add(address_5);
        list.add(address_6);
        list.add(address_7);
        list.add(address_8);
        list.add(address_9);

        return list;
    }

    private List<BusinessTime> createBusinessTime() {
        List<BusinessTime> list = new ArrayList<>();
        BusinessTime businessTime_1 = businessTimeRepository.save(BusinessTime.builder().build());
        operating1(businessTime_1);
        specific1(businessTime_1);
        list.add(businessTime_1);

        BusinessTime businessTime_2 = businessTimeRepository.save(BusinessTime.builder().build());
        operating2(businessTime_2);
        specific2(businessTime_2);
        list.add(businessTime_2);

        BusinessTime businessTime_3 = businessTimeRepository.save(BusinessTime.builder().build());
        operating3(businessTime_3);
        specific3(businessTime_3);
        list.add(businessTime_3);

        BusinessTime businessTime_4 = businessTimeRepository.save(BusinessTime.builder().build());
        operating4(businessTime_4);
        specific4(businessTime_4);
        list.add(businessTime_4);

        BusinessTime businessTime_5 = businessTimeRepository.save(BusinessTime.builder().build());
        operating5(businessTime_5);
        specific5(businessTime_5);
        list.add(businessTime_5);

        BusinessTime businessTime_6 = businessTimeRepository.save(BusinessTime.builder().build());
        operating6(businessTime_6);
        specific6(businessTime_6);
        list.add(businessTime_6);

        BusinessTime businessTime_7 = businessTimeRepository.save(BusinessTime.builder().build());
        operating7(businessTime_7);
        specific7(businessTime_7);
        list.add(businessTime_7);

        BusinessTime businessTime_8 = businessTimeRepository.save(BusinessTime.builder().build());
        operating8(businessTime_8);
        specific8(businessTime_8);
        list.add(businessTime_8);

        BusinessTime businessTime_9 = businessTimeRepository.save(BusinessTime.builder().build());
        operating9(businessTime_9);
        specific9(businessTime_9);
        list.add(businessTime_9);
        return list;
    }

    // Create Object
    private Place createPlace(Category category, Address address, BusinessTime time, String name, LocalDate open, LocalDate close, PlaceLevel level) {
        return Place.builder()
                .category(category)
                .address(address)
                .businessTime(time)
                .name(name)
                .openDate(open)
                .closeDate(close)
                .recLevel(level)
                .build();
    }

    private Address createAddress(String address, double y, double x, String url) {
        return Address.builder()
                .address(address)
                .location(geometryFactory.createPoint(new Coordinate(x, y)))
                .url(url)
                .build();
    }

    private Address createAddress(String address, String detail, double y, double x, String url) {
        return Address.builder()
                .address(address)
                .addressDetail(detail)
                .location(geometryFactory.createPoint(new Coordinate(x, y)))
                .url(url)
                .build();
    }

    private OperatingTime createOperatingTime(BusinessTime businessTime, OperatingType type, DayType day, LocalTime open, LocalTime close) {
        return OperatingTime.builder()
                .businessTime(businessTime)
                .status(type)
                .day(day)
                .openTime(open)
                .closeTime(close)
                .build();
    }

    private void operating1(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));
    }

    private void operating2(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.CLOSED, DayType.MON, LocalTime.of(8, 0),
                        LocalTime.of(2, 0)));

        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.TUE, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)));

        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.WED, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)));

        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.THU, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)));

        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.FRI, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)));

        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.SAT, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)));

        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.BREAK_TIME, DayType.SUN, LocalTime.of(12, 0),
                        LocalTime.of(13, 0)));
    }

    private void operating3(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(7, 0),
                        LocalTime.of(2, 30)));
    }

    private void operating4(BusinessTime businessTime) {
        operating3(businessTime);
    }

    private void operating5(BusinessTime businessTime) {
        operating3(businessTime);
    }

    private void operating6(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
    }

    private void operating7(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(5, 0),
                        LocalTime.of(23, 30)));
    }

    private void operating8(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(11, 0),
                        LocalTime.of(3, 30)));
    }

    private void operating9(BusinessTime businessTime) {
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.MON, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.TUE, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.WED, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.THU, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.FRI, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SAT, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
        operatingTimeRepository.save(
                createOperatingTime(businessTime, OperatingType.OPEN, DayType.SUN, LocalTime.of(8, 0),
                        LocalTime.of(1, 30)));
    }

    private SpecificDay createSpecific(BusinessTime businessTime, SpecificDayType type, String reason, LocalDate date) {
        return SpecificDay.builder()
                .businessTime(businessTime)
                .status(type)
                .reason(reason)
                .date(date)
                .build();
    }

    private void specific1(BusinessTime businessTime) {
        specificDayRepository.save(
                createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 28)));
        specificDayRepository.save(
                createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 29)));
        specificDayRepository.save(
                createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 30)));
    }

    private void specific2(BusinessTime businessTime) {
        specificDayRepository.save(
                createSpecific(businessTime, SpecificDayType.CLOSED, "신정", LocalDate.of(2025, 1, 1)));
        specific1(businessTime);
    }

    private void specific3(BusinessTime businessTime) {
        specific1(businessTime);
    }

    private void specific4(BusinessTime businessTime) {
        specificDayRepository.save(
                createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 29)));
    }

    private void specific5(BusinessTime businessTime) {
        specificDayRepository.save(
                createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 29)));
    }

    private void specific6(BusinessTime businessTime) {
        specific1(businessTime);
    }

    private void specific7(BusinessTime businessTime) {
        specific1(businessTime);
    }

    private void specific8(BusinessTime businessTime) {
        specific1(businessTime);
    }

    private void specific9(BusinessTime businessTime) {
        specific1(businessTime);
    }

}