package com.moa.moa.api.place.place.domain.persistence;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.domain.persistence.CategoryRepository;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.amenity.domain.persistence.AmenityRepository;
import com.moa.moa.api.place.amenity.util.enumerated.AmenityType;
import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.place.liftticket.domain.persistence.LiftTicketRepository;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.place.placeamenity.domain.entity.PlaceAmenity;
import com.moa.moa.api.place.placeamenity.domain.persistence.PlaceAmenityRepository;
import com.moa.moa.api.place.slope.domain.entity.Slope;
import com.moa.moa.api.place.slope.domain.persistence.SlopeRepository;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class PlaceRepositoryTest {
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
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private LiftTicketRepository liftTicketRepository;
    @Autowired
    private PlaceAmenityRepository placeAmenityRepository;
    @Autowired
    private SlopeRepository slopeRepository;

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

        // 슬로프 생성
        createSlope(places);

        // 편의시설 생성
        List<Amenity> amenities = createAmenity();

        // 스키장 편의시설 중간테이블 생성
        createPlaceAmenity(places, amenities);

        // 스키장 리프트권 생성
        createLiftTicket(places);
    }

    @AfterEach
    void afterEach() {
        categoryRepository.deleteAll();
        addressRepository.deleteAll();
        businessTimeRepository.deleteAll();
        operatingTimeRepository.deleteAll();
        specificDayRepository.deleteAll();
        placeRepository.deleteAll();
        amenityRepository.deleteAll();
        liftTicketRepository.deleteAll();
        placeAmenityRepository.deleteAll();
        slopeRepository.deleteAll();
    }

    @Test
    @DisplayName("스키장 목록 조회 성공 - deleteAt 조건 적용 확인")
    public void t1() {
        //when
        List<Place> places = placeRepository.findAllPlaceInMap(0D, 40D, 130D, 0D);

        //then
        assertThat(places.size()).isEqualTo(1);
        assertThat(places.get(0).getName()).isEqualTo("비발디파크");
        assertThat(places.get(0).getOpenDate()).isEqualTo(LocalDate.of(2024, 10, 15));
        assertThat(places.get(0).getCloseDate()).isEqualTo(LocalDate.of(2025, 3, 12));
        assertThat(places.get(0).getRecLevel()).isEqualTo(PlaceLevel.LEVEL_1);

        assertThat(places.get(0).getCategory().getCategoryType()).isEqualTo(CategoryType.SKI_RESORT);

        assertThat(places.get(0).getAddress().getAddress()).isEqualTo("강원도 홍천군 서면 한치골길 262");
        assertThat(places.get(0).getAddress().getAddressDetail()).isEqualTo(null);
        assertThat(places.get(0).getAddress().getLocation()).isEqualTo(geometryFactory.createPoint(new Coordinate(127.687106349987, 37.6521031526954)));
        assertThat(places.get(0).getAddress().getUrl()).isEqualTo("https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh");

        assertThat(places.get(0).getBusinessTime().getOperatingTimes().size()).isEqualTo(6);
        assertThat(places.get(0).getBusinessTime().getOperatingTimes().get(0).getStatus()).isEqualTo(OperatingType.OPEN);
        assertThat(places.get(0).getBusinessTime().getOperatingTimes().get(0).getDay()).isEqualTo(DayType.MON);
        assertThat(places.get(0).getBusinessTime().getOperatingTimes().get(0).getOpenTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(places.get(0).getBusinessTime().getOperatingTimes().get(0).getCloseTime()).isEqualTo(LocalTime.of(2, 0));

        assertThat(places.get(0).getBusinessTime().getSpecificDays().size()).isEqualTo(2);
        assertThat(places.get(0).getBusinessTime().getSpecificDays().get(0).getStatus()).isEqualTo(SpecificDayType.CLOSED);
        assertThat(places.get(0).getBusinessTime().getSpecificDays().get(0).getReason()).isEqualTo("설연휴");
        assertThat(places.get(0).getBusinessTime().getSpecificDays().get(0).getDate()).isEqualTo(LocalDate.of(2025, 1, 28));
        assertThat(places.get(0).getBusinessTime().getSpecificDays().get(0).getOpenTime()).isNull();
        assertThat(places.get(0).getBusinessTime().getSpecificDays().get(0).getCloseTime()).isNull();

        assertThat(places.get(0).getLiftTickets().size()).isEqualTo(10);
        assertThat(places.get(0).getLiftTickets().get(0).getStatus()).isEqualTo(LiftTicketStatus.WEEK_DAY);
        assertThat(places.get(0).getLiftTickets().get(0).getName()).isEqualTo("스마트4시간권");
        assertThat(places.get(0).getLiftTickets().get(0).getTicketType()).isEqualTo(LiftTicketType.SMART);
        assertThat(places.get(0).getLiftTickets().get(0).getHours()).isEqualTo(4L);
        assertThat(places.get(0).getLiftTickets().get(0).getStartTime()).isNull();
        assertThat(places.get(0).getLiftTickets().get(0).getEndTime()).isNull();

        assertThat(places.get(0).getSlopes().size()).isEqualTo(8);
        assertThat(places.get(0).getSlopes().get(0).getName()).isEqualTo("발라드");
        assertThat(places.get(0).getSlopes().get(0).getLevel()).isEqualTo(SlopeLevel.LEVEL_1);

        assertThat(places.get(0).getAmenities().size()).isEqualTo(4);
        assertThat(places.get(0).getAmenities().get(0).getUsed()).isEqualTo(true);
        assertThat(places.get(0).getAmenities().get(0).getAmenity().getType()).isEqualTo(AmenityType.HOTEL);
    }

    @Test
    @DisplayName("스키장 목록 조회 성공 - 범위 내 스키장 없음")
    public void t2() {
        //when
        List<Place> places = placeRepository.findAllPlaceInMap(0D, 30D, 130D, 0D);

        //then
        assertThat(places.size()).isEqualTo(0);
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
                LocalDate.of(2025, 3, 15), PlaceLevel.LEVEL_4, true); // 삭제

        list.add(place_1);
        list.add(place_2);
        list.add(place_3);
        list.add(place_4);

        placeRepository.saveAll(list);

        return list;
    }

    @Test
    @DisplayName("스키장 상세 조회 성공")
    public void t3() {
        // given
        List<Place> places = this.placeRepository.findAll();

        //when
        Place place = placeRepository.findPlaceById(places.get(0).getId()).get();

        //then
        assertThat(place.getName()).isEqualTo("비발디파크");
        assertThat(place.getOpenDate()).isEqualTo(LocalDate.of(2024, 10, 15));
        assertThat(place.getCloseDate()).isEqualTo(LocalDate.of(2025, 3, 12));
        assertThat(place.getRecLevel()).isEqualTo(PlaceLevel.LEVEL_1);

        assertThat(place.getCategory().getCategoryType()).isEqualTo(CategoryType.SKI_RESORT);

        assertThat(place.getAddress().getAddress()).isEqualTo("강원도 홍천군 서면 한치골길 262");
        assertThat(place.getAddress().getAddressDetail()).isEqualTo(null);
        assertThat(place.getAddress().getLocation()).isEqualTo(geometryFactory.createPoint(new Coordinate(127.687106349987, 37.6521031526954)));
        assertThat(place.getAddress().getUrl()).isEqualTo("https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh");

        assertThat(place.getBusinessTime().getOperatingTimes().size()).isEqualTo(6);
        assertThat(place.getBusinessTime().getOperatingTimes().get(0).getStatus()).isEqualTo(OperatingType.OPEN);
        assertThat(place.getBusinessTime().getOperatingTimes().get(0).getDay()).isEqualTo(DayType.MON);
        assertThat(place.getBusinessTime().getOperatingTimes().get(0).getOpenTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(place.getBusinessTime().getOperatingTimes().get(0).getCloseTime()).isEqualTo(LocalTime.of(2, 0));

        assertThat(place.getBusinessTime().getSpecificDays().size()).isEqualTo(2);
        assertThat(place.getBusinessTime().getSpecificDays().get(0).getStatus()).isEqualTo(SpecificDayType.CLOSED);
        assertThat(place.getBusinessTime().getSpecificDays().get(0).getReason()).isEqualTo("설연휴");
        assertThat(place.getBusinessTime().getSpecificDays().get(0).getDate()).isEqualTo(LocalDate.of(2025, 1, 28));
        assertThat(place.getBusinessTime().getSpecificDays().get(0).getOpenTime()).isNull();
        assertThat(place.getBusinessTime().getSpecificDays().get(0).getCloseTime()).isNull();

        assertThat(place.getLiftTickets().size()).isEqualTo(10);
        assertThat(place.getLiftTickets().get(0).getStatus()).isEqualTo(LiftTicketStatus.WEEK_DAY);
        assertThat(place.getLiftTickets().get(0).getName()).isEqualTo("스마트4시간권");
        assertThat(place.getLiftTickets().get(0).getTicketType()).isEqualTo(LiftTicketType.SMART);
        assertThat(place.getLiftTickets().get(0).getHours()).isEqualTo(4L);
        assertThat(place.getLiftTickets().get(0).getStartTime()).isNull();
        assertThat(place.getLiftTickets().get(0).getEndTime()).isNull();

        assertThat(place.getSlopes().size()).isEqualTo(8);
        assertThat(place.getSlopes().get(0).getName()).isEqualTo("발라드");
        assertThat(place.getSlopes().get(0).getLevel()).isEqualTo(SlopeLevel.LEVEL_1);

        assertThat(place.getAmenities().size()).isEqualTo(4);
        assertThat(place.getAmenities().get(0).getUsed()).isEqualTo(true);
        assertThat(place.getAmenities().get(0).getAmenity().getType()).isEqualTo(AmenityType.HOTEL);
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
                "https://map.naver.com/p/entry/place/92136142?lng=128.8388599&lat=37.204042&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh", true); // 삭제
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

        BusinessTime businessTime_5 = BusinessTime.builder().deletedAt(LocalDateTime.now()).build(); // 삭제
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
                LocalTime.of(2, 0), true)); // 삭제

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
        list.add(createSpecific(businessTime, SpecificDayType.CLOSED, "설연휴", LocalDate.of(2025, 1, 30), true)); // 삭제

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
        list_1.add(Slope.builder().place(places.get(0)).name("블루스").level(SlopeLevel.LEVEL_1).deletedAt(LocalDateTime.now()).build()); // 삭제
        list_1.add(Slope.builder().place(places.get(0)).name("레게").level(SlopeLevel.LEVEL_2).build());
        list_1.add(Slope.builder().place(places.get(0)).name("째즈").level(SlopeLevel.LEVEL_2).build());
        list_1.add(Slope.builder().place(places.get(0)).name("클래식").level(SlopeLevel.LEVEL_3).build());
        list_1.add(Slope.builder().place(places.get(0)).name("힙합").level(SlopeLevel.LEVEL_3).build());
        list_1.add(Slope.builder().place(places.get(0)).name("펑키").level(SlopeLevel.LEVEL_4).build());
        list_1.add(Slope.builder().place(places.get(0)).name("테크노").level(SlopeLevel.LEVEL_4).build());
        list_1.add(Slope.builder().place(places.get(0)).name("락").level(SlopeLevel.LEVEL_5).build());

        List<Slope> list_2 = new ArrayList<>();

        list_2.add(Slope.builder().place(places.get(1)).name("제우스1").level(SlopeLevel.LEVEL_1).build());
        list_2.add(Slope.builder().place(places.get(1)).name("제우스2").level(SlopeLevel.LEVEL_1).build());
        list_2.add(Slope.builder().place(places.get(1)).name("제우스3").level(SlopeLevel.LEVEL_1).build());
        list_2.add(Slope.builder().place(places.get(1)).name("빅토리아1").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("빅토리아2").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("헤라1").level(SlopeLevel.LEVEL_2).build());
        list_2.add(Slope.builder().place(places.get(1)).name("헤라2").level(SlopeLevel.LEVEL_3).build());
        list_2.add(Slope.builder().place(places.get(1)).name("헤라3").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴리1").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴로3").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴로4").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아폴로6").level(SlopeLevel.LEVEL_4).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아테나2").level(SlopeLevel.LEVEL_2).build());
        list_2.add(Slope.builder().place(places.get(1)).name("아테나3").level(SlopeLevel.LEVEL_1).build());

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

        slopeRepository.saveAll(list_1);
        slopeRepository.saveAll(list_2);
        slopeRepository.saveAll(list_3);
    }

    private List<Amenity> createAmenity() {
        List<Amenity> list = new ArrayList<>();

        list.add(Amenity.builder().type(AmenityType.HOTEL).build());
        list.add(Amenity.builder().type(AmenityType.INNER_RENTAL_SHOP).deletedAt(LocalDateTime.now()).build()); // 삭제
        list.add(Amenity.builder().type(AmenityType.SHUTTLE_BUS).build());
        list.add(Amenity.builder().type(AmenityType.CONVENIENCE_STORE).build());
        list.add(Amenity.builder().type(AmenityType.FOOD_COURT).build());
        list.add(Amenity.builder().type(AmenityType.GIFT_SHOP).build());
        list.add(Amenity.builder().type(AmenityType.INFIRMARY).build());

        amenityRepository.saveAll(list);

        return list;
    }

    private void createPlaceAmenity(List<Place> places, List<Amenity> amenities) {
        List<PlaceAmenity> list_1 = new ArrayList<>();
        for (Amenity amenity : amenities) {
            list_1.add(PlaceAmenity.builder().place(places.get(0)).amenity(amenity).used(true).build());
        }
        list_1.get(2).modUse(false);
        list_1.get(3).modDeletedAt(); // 삭제

        List<PlaceAmenity> list_2 = new ArrayList<>();
        for (Amenity amenity : amenities) {
            list_2.add(PlaceAmenity.builder().place(places.get(1)).amenity(amenity).used(true).build());
        }
        list_2.get(2).modUse(false);

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

        placeAmenityRepository.saveAll(list_1);
        placeAmenityRepository.saveAll(list_2);
        placeAmenityRepository.saveAll(list_3);
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

        liftTicketRepository.saveAll(list_1);
        liftTicketRepository.saveAll(list_2);
        liftTicketRepository.saveAll(list_3);
    }
}