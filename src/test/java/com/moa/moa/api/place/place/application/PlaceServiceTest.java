package com.moa.moa.api.place.place.application;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.amenity.util.enumerated.AmenityType;
import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.api.place.place.application.mapstruct.PlaceMapstructMapper;
import com.moa.moa.api.place.place.domain.PlaceProcessor;
import com.moa.moa.api.place.place.domain.dto.FindAllPlaceDto;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.place.placeamenity.domain.entity.PlaceAmenity;
import com.moa.moa.api.place.slope.domain.entity.Slope;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {
    @Mock
    private PlaceProcessor placeProcessor;

    @Mock
    private PlaceMapstructMapper placeMapstructMapper;

    @InjectMocks
    private PlaceService placeService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final Long id = 1L;
    private final LocalDateTime createdAt = LocalDateTime.now();

    private List<Place> mockPlaces;
    private List<FindAllPlaceDto.Response> mockPlaceResponses;

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

        // 슬로프 생성
        createSlope(places);

        // 편의시설 생성
        List<Amenity> amenities = createAmenity();

        // 스키장 편의시설 중간테이블 생성
        createPlaceAmenity(places, amenities);

        // 스키장 리프트권 생성
        createLiftTicket(places);

        // TODO: Image 기능 완성 시 수정
        List<Image> images = createImage();

        mockPlaces = places;
        mockPlaceResponses = createPlaceResponse(places, images);
    }

    @Test
    @DisplayName("스키장 목록 조회 성공")
    public void t1() {
        // given
        when(placeProcessor.findAllPlaceInMap(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(mockPlaces);

        for (int i = 0; i < mockPlaces.size(); i++) {
            when(placeMapstructMapper.of(
                    eq(mockPlaces.get(i)),
                    any(),
                    any(),
                    anyList(),
                    anyList(),
                    anyList(),
                    anyList()
            )).thenReturn(mockPlaceResponses.get(i));
        }

        // when
        List<FindAllPlaceDto.Response> places = placeService.findAllPlace(anyDouble(), anyDouble(), anyDouble(), anyDouble());

        // then
        assertThat(places.size()).isEqualTo(3);

        FindAllPlaceDto.Response placeResponse = places.get(0);
        assertThat(placeResponse.id()).isEqualTo(1L);
        assertThat(placeResponse.name()).isEqualTo("비발디파크");
        assertThat(placeResponse.open()).isEqualTo(LocalDate.of(2024, 10, 15));
        assertThat(placeResponse.close()).isEqualTo(LocalDate.of(2025, 3, 12));
        assertThat(placeResponse.recLevel()).isEqualTo(PlaceLevel.LEVEL_1);

        // TODO: image 기능 완성 시 구현 추가
        assertThat(placeResponse.images()).isNotNull();

        assertThat(placeResponse.address().address()).isEqualTo("강원도 홍천군 서면 한치골길 262");
        assertThat(placeResponse.address().addressDetail()).isEqualTo(null);
        assertThat(placeResponse.address().locationX()).isEqualTo(127.687106349987);
        assertThat(placeResponse.address().locationY()).isEqualTo(37.6521031526954);
        assertThat(placeResponse.address().mapUrl()).isEqualTo("https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh");

        assertThat(placeResponse.operatingTimes().size()).isEqualTo(7);
        assertThat(placeResponse.operatingTimes().get(0).status()).isEqualTo(OperatingType.OPEN);
        assertThat(placeResponse.operatingTimes().get(0).day()).isEqualTo(DayType.MON);
        assertThat(placeResponse.operatingTimes().get(0).open()).isEqualTo(LocalTime.of(8, 0));
        assertThat(placeResponse.operatingTimes().get(0).close()).isEqualTo(LocalTime.of(2, 0));

        assertThat(placeResponse.specificDays().size()).isEqualTo(3);
        assertThat(placeResponse.specificDays().get(0).status()).isEqualTo(SpecificDayType.CLOSED);
        assertThat(placeResponse.specificDays().get(0).reason()).isEqualTo("설연휴");
        assertThat(placeResponse.specificDays().get(0).date()).isEqualTo(LocalDate.of(2025, 1, 28));
        assertThat(placeResponse.specificDays().get(0).open()).isNull();
        assertThat(placeResponse.specificDays().get(0).close()).isNull();

        assertThat(placeResponse.amenities().size()).isEqualTo(7);
        assertThat(placeResponse.amenities().get(0).name()).isEqualTo(AmenityType.HOTEL.toString());

        assertThat(placeResponse.slopes().size()).isEqualTo(9);
        assertThat(placeResponse.slopes().get(0).name()).isEqualTo("발라드");
        assertThat(placeResponse.slopes().get(0).level()).isEqualTo(SlopeLevel.LEVEL_1);
    }

    private List<Place> createPlace(Category category, List<Address> addresses, List<BusinessTime> businessTimes) {
        List<Place> list = new ArrayList<>();
        Place place_1 = createPlace(1L, category, addresses.get(0), businessTimes.get(0), "비발디파크", LocalDate.of(2024, 10, 15),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_1);

        Place place_2 = createPlace(2L, category, addresses.get(3), businessTimes.get(3), "하이원리조트", LocalDate.of(2024, 10, 16),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_2);

        Place place_3 = createPlace(3L, category, addresses.get(4), businessTimes.get(4), "엘리시안", LocalDate.of(2024, 10, 17),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_4);

        list.add(place_1);
        list.add(place_2);
        list.add(place_3);

        return list;
    }

    private Category createCategory() {
        Category category = Category.builder()
                .categoryType(CategoryType.SKI_RESORT)
                .build();

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

        return list;
    }

    private List<BusinessTime> createBusinessTime() {
        List<BusinessTime> list = new ArrayList<>();

        BusinessTime businessTime_1 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_1 = operating1(businessTime_1);
        List<SpecificDay> specificDays_1 = specific1(businessTime_1);
        list.add(businessTime_1);

        businessTime_1.addOperatingTimes(operatingTimes_1);
        businessTime_1.addOSpecificDays(specificDays_1);

        BusinessTime businessTime_2 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_2 = operating2(businessTime_2);
        List<SpecificDay> specificDays_2 = specific2(businessTime_2);
        list.add(businessTime_2);

        businessTime_2.addOperatingTimes(operatingTimes_2);
        businessTime_2.addOSpecificDays(specificDays_2);

        BusinessTime businessTime_3 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_3 = operating3(businessTime_3);
        List<SpecificDay> specificDays_3 = specific3(businessTime_3);
        list.add(businessTime_3);

        businessTime_3.addOperatingTimes(operatingTimes_3);
        businessTime_3.addOSpecificDays(specificDays_3);

        BusinessTime businessTime_4 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_4 = operating4(businessTime_4);
        List<SpecificDay> specificDays_4 = specific4(businessTime_4);
        list.add(businessTime_4);

        businessTime_4.addOperatingTimes(operatingTimes_4);
        businessTime_4.addOSpecificDays(specificDays_4);

        BusinessTime businessTime_5 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_5 = operating5(businessTime_5);
        List<SpecificDay> specificDays_5 = specific5(businessTime_5);
        list.add(businessTime_5);

        businessTime_5.addOperatingTimes(operatingTimes_5);
        businessTime_5.addOSpecificDays(specificDays_5);

        BusinessTime businessTime_6 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_6 = operating6(businessTime_6);
        List<SpecificDay> specificDays_6 = specific6(businessTime_6);
        list.add(businessTime_6);

        businessTime_6.addOperatingTimes(operatingTimes_6);
        businessTime_6.addOSpecificDays(specificDays_6);

        BusinessTime businessTime_7 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_7 = operating7(businessTime_7);
        List<SpecificDay> specificDays_7 = specific7(businessTime_7);
        list.add(businessTime_7);

        businessTime_7.addOperatingTimes(operatingTimes_7);
        businessTime_7.addOSpecificDays(specificDays_7);

        BusinessTime businessTime_8 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_8 = operating8(businessTime_8);
        List<SpecificDay> specificDays_8 = specific8(businessTime_8);
        list.add(businessTime_8);

        businessTime_8.addOperatingTimes(operatingTimes_8);
        businessTime_8.addOSpecificDays(specificDays_8);

        BusinessTime businessTime_9 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_9 = operating9(businessTime_9);
        List<SpecificDay> specificDays_9 = specific9(businessTime_9);
        list.add(businessTime_9);

        businessTime_9.addOperatingTimes(operatingTimes_9);
        businessTime_9.addOSpecificDays(specificDays_9);

        return list;
    }

    // Create Object
    private Place createPlace(Long id, Category category, Address address, BusinessTime time, String name, LocalDate open, LocalDate close, PlaceLevel level) {
        return Place.builder()
                .id(id)
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

    private SpecificDay createSpecific(BusinessTime businessTime, SpecificDayType type, String reason, LocalDate date) {
        return SpecificDay.builder()
                .businessTime(businessTime)
                .status(type)
                .reason(reason)
                .date(date)
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

    private void createSlope(List<Place> places) {
        List<Slope> list_1 = new ArrayList<>();

        list_1.add(Slope.builder().place(places.get(0)).name("발라드").level(SlopeLevel.LEVEL_1).build());
        list_1.add(Slope.builder().place(places.get(0)).name("블루스").level(SlopeLevel.LEVEL_1).build());
        list_1.add(Slope.builder().place(places.get(0)).name("레게").level(SlopeLevel.LEVEL_2).build());
        list_1.add(Slope.builder().place(places.get(0)).name("째즈").level(SlopeLevel.LEVEL_2).build());
        list_1.add(Slope.builder().place(places.get(0)).name("클래식").level(SlopeLevel.LEVEL_3).build());
        list_1.add(Slope.builder().place(places.get(0)).name("힙합").level(SlopeLevel.LEVEL_3).build());
        list_1.add(Slope.builder().place(places.get(0)).name("펑키").level(SlopeLevel.LEVEL_4).build());
        list_1.add(Slope.builder().place(places.get(0)).name("테크노").level(SlopeLevel.LEVEL_4).build());
        list_1.add(Slope.builder().place(places.get(0)).name("락").level(SlopeLevel.LEVEL_5).build());

        places.get(0).addSlopes(list_1);

        List<Slope> list_2 = new ArrayList<>();

        list_2.add(Slope.builder().place(places.get(1)).name("제우스1").level(SlopeLevel.LEVEL_1).build());
        list_2.add(Slope.builder().place(places.get(1)).name("제우스2").level(SlopeLevel.LEVEL_1).build());
        list_2.add(Slope.builder().place(places.get(1)).name("제우스3").level(SlopeLevel.LEVEL_1).build());
        list_2.add(Slope.builder().place(places.get(1)).name("빅토리아1").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("빅토리아2").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("헤라1").level(SlopeLevel.LEVEL_2).build());
        list_2.add(Slope.builder().place(places.get(1)).name("헤라2").level(SlopeLevel.LEVEL_3).build());
        list_2.add(Slope.builder().place(places.get(1)).name("헤라3").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴리1").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴로3").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴로4").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴로6").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아테나2").level(SlopeLevel.LEVEL_2).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아테나3").level(SlopeLevel.LEVEL_1).build());

        places.get(1).addSlopes(list_2);

        List<Slope> list_3 = new ArrayList<>();

        list_3.add(Slope.builder().place(places.get(2)).name("래빗").level(SlopeLevel.LEVEL_1).build());
        list_3.add(Slope.builder().place(places.get(2)).name("팬더").level(SlopeLevel.LEVEL_1).build());
        list_3.add(Slope.builder().place(places.get(2)).name("드래곤").level(SlopeLevel.LEVEL_2).build());
        list_3.add(Slope.builder().place(places.get(2)).name("호스").level(SlopeLevel.LEVEL_2).build());
        list_3.add(Slope.builder().place(places.get(2)).name("퓨마").level(SlopeLevel.LEVEL_2).build());
        list_3.add(Slope.builder().place(places.get(2)).name("디어").level(SlopeLevel.LEVEL_2).build());
        list_3.add(Slope.builder().place(places.get(2)).name("제브라").level(SlopeLevel.LEVEL_2).build());
        list_3.add(Slope.builder().place(places.get(2)).name("페가수스").level(SlopeLevel.LEVEL_2).build());
        list_3.add(Slope.builder().place(places.get(2)).name("래퍼드").level(SlopeLevel.LEVEL_4).build());
        list_3.add(Slope.builder().place(places.get(2)).name("재규어").level(SlopeLevel.LEVEL_4).build());

        places.get(2).addSlopes(list_3);
    }

    private List<Amenity> createAmenity() {
        List<Amenity> list = new ArrayList<>();

        list.add(Amenity.builder().type(AmenityType.HOTEL).build());
        list.add(Amenity.builder().type(AmenityType.INNER_RENTAL_SHOP).build());
        list.add(Amenity.builder().type(AmenityType.SHUTTLE_BUS).build());
        list.add(Amenity.builder().type(AmenityType.CONVENIENCE_STORE).build());
        list.add(Amenity.builder().type(AmenityType.FOOD_COURT).build());
        list.add(Amenity.builder().type(AmenityType.GIFT_SHOP).build());
        list.add(Amenity.builder().type(AmenityType.INFIRMARY).build());

        return list;
    }

    private void createPlaceAmenity(List<Place> places, List<Amenity> amenities) {
        List<PlaceAmenity> list_1 = new ArrayList<>();
        for (Amenity amenity : amenities) {
            list_1.add(PlaceAmenity.builder().place(places.get(0)).amenity(amenity).used(true).build());
        }
        places.get(0).addPlaceAmenities(list_1);

        List<PlaceAmenity> list_2 = new ArrayList<>();
        for (Amenity amenity : amenities) {
            list_2.add(PlaceAmenity.builder().place(places.get(1)).amenity(amenity).used(true).build());
        }
        list_2.get(2).modUse(false);
        places.get(1).addPlaceAmenities(list_2);

        List<PlaceAmenity> list_3 = new ArrayList<>();
        for (Amenity amenity : amenities) {
            list_3.add(PlaceAmenity.builder().place(places.get(2)).amenity(amenity).used(true).build());
        }
        list_3.get(0).modUse(false);
        list_3.get(1).modUse(false);
        list_3.get(2).modUse(false);
        list_3.get(3).modUse(false);
        list_3.get(4).modUse(false);
        list_3.get(5).modUse(false);
        places.get(2).addPlaceAmenities(list_3);
    }

    private void createLiftTicket(List<Place> places) {
        // place 1
        List<LiftTicket> list_1 = new ArrayList<>();

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("오전권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(12, 0)).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("오후권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(18, 0)).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("야간권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(22, 0)).endTime(LocalTime.of(2, 0)).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("오전권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(12, 0)).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("오후권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(18, 0)).build());

        list_1.add(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("야간권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(22, 0)).endTime(LocalTime.of(2, 0)).build());

        places.get(0).addLiftTickets(list_1);

        // place 2
        List<LiftTicket> list_2 = new ArrayList<>();

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트3시간권").ticketType(
                LiftTicketType.SMART).hours(3L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트5시간권").ticketType(
                LiftTicketType.SMART).hours(5L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트7시간권").ticketType(
                LiftTicketType.SMART).hours(7L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("종일권").ticketType(
                LiftTicketType.TIME).hours(13L).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(22, 0)).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트3시간권").ticketType(
                LiftTicketType.SMART).hours(3L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트5시간권").ticketType(
                LiftTicketType.SMART).hours(5L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트7시간권").ticketType(
                LiftTicketType.SMART).hours(7L).build());

        list_2.add(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("종일권").ticketType(
                LiftTicketType.TIME).hours(13L).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(22, 0)).build());

        places.get(1).addLiftTickets(list_2);

        // place 3
        List<LiftTicket> list_3 = new ArrayList<>();

        list_3.add(LiftTicket.builder().place(places.get(2)).status(LiftTicketStatus.WEEK_DAY).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        list_3.add(LiftTicket.builder().place(places.get(2)).status(LiftTicketStatus.WEEK_END).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build());

        places.get(2).addLiftTickets(list_3);
    }

    private List<Image> createImage() {
        List<Image> list = new ArrayList<>();

        list.add(Image.builder().build());
        list.add(Image.builder().build());
        list.add(Image.builder().build());

        return list;
    }

    private List<FindAllPlaceDto.Response> createPlaceResponse(List<Place> places, List<Image> images) {
        List<FindAllPlaceDto.Response> responseList = new ArrayList<>();

        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            Image image = images.get(i);

            responseList.add(createResponseFromPlace(place, image));
        }

        return responseList;
    }

    private FindAllPlaceDto.Response createResponseFromPlace(Place place, Image image) {
        List<Amenity> amenities = place.getAmenities().stream()
                .map(PlaceAmenity::getAmenity)
                .collect(Collectors.toList());

        return mapper(
                place,
                image,
                place.getAddress(),
                place.getBusinessTime().getOperatingTimes(),
                place.getBusinessTime().getSpecificDays(),
                amenities,
                place.getSlopes()
        );
    }

    private FindAllPlaceDto.Response mapper(Place place,
                                            Image image,
                                            Address address,
                                            List<OperatingTime> operatingTimes,
                                            List<SpecificDay> specificDays,
                                            List<Amenity> amenities,
                                            List<Slope> slopes) {
        // TODO: image 기능 완성 시 구현 추가
        FindAllPlaceDto.ImageResponse imageResponse = FindAllPlaceDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        FindAllPlaceDto.AddressResponse addressResponse = FindAllPlaceDto.AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .locationX(address.getLocation().getX())
                .locationY(address.getLocation().getY())
                .mapUrl(address.getUrl())
                .build();

        List<FindAllPlaceDto.OperatingTimeResponse> operatingTimeResponses = operatingTimes.stream()
                .map(operatingTime -> FindAllPlaceDto.OperatingTimeResponse.builder()
                        .id(operatingTime.getId())
                        .status(operatingTime.getStatus())
                        .day(operatingTime.getDay())
                        .open(operatingTime.getOpenTime())
                        .close(operatingTime.getCloseTime())
                        .build())
                .collect(Collectors.toList());

        List<FindAllPlaceDto.SpecificDayResponse> specificDayResponses = specificDays.stream()
                .map(specificDay -> FindAllPlaceDto.SpecificDayResponse.builder()
                        .id(specificDay.getId())
                        .status(specificDay.getStatus())
                        .reason(specificDay.getReason())
                        .date(specificDay.getDate())
                        .open(specificDay.getOpenTime())
                        .close(specificDay.getCloseTime())
                        .build())
                .collect(Collectors.toList());

        List<FindAllPlaceDto.AmenityResponse> amenityResponses = amenities.stream()
                .map(amenity -> FindAllPlaceDto.AmenityResponse.builder()
                        .id(amenity.getId())
                        .name(amenity.getType().name())
                        .build())
                .collect(Collectors.toList());

        List<FindAllPlaceDto.SlopeResponse> slopeResponses = slopes.stream()
                .map(slope -> FindAllPlaceDto.SlopeResponse.builder()
                        .id(slope.getId())
                        .name(slope.getName())
                        .level(slope.getLevel())
                        .build())
                .collect(Collectors.toList());

        return FindAllPlaceDto.Response.builder()
                .id(place.getId())
                .name(place.getName())
                .open(place.getOpenDate())
                .close(place.getCloseDate())
                .recLevel(place.getRecLevel())
                .createdAt(place.getCreatedAt())
                .images(imageResponse)
                .address(addressResponse)
                .operatingTimes(operatingTimeResponses)
                .specificDays(specificDayResponses)
                .amenities(amenityResponses)
                .slopes(slopeResponses)
                .build();
    }
}