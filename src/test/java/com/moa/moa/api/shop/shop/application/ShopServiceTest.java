package com.moa.moa.api.shop.shop.application;

import com.moa.moa.api.address.address.AddressProcessor;
import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.member.custom.domain.dto.FindLowPriceCustomDto;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.MemberProcessor;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.api.member.wish.domain.WishProcessor;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.api.place.place.domain.PlaceProcessor;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.PlaceShopProcessor;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.application.mapstruct.ShopMapstructMapper;
import com.moa.moa.api.shop.shop.application.mapstruct.ShopMapstructMapperImpl;
import com.moa.moa.api.shop.shop.domain.ShopProcessor;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopDto;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopLowPriceDto;
import com.moa.moa.api.shop.shop.domain.dto.FindLowPriceShopDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.shop.shop.util.ShopUtil;
import com.moa.moa.api.time.businesstime.domain.BusinessTimeProcessor;
import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {
    @Mock
    private ShopProcessor shopProcessor;
    @Mock
    private WishProcessor wishProcessor;
    @Mock
    private PlaceProcessor placeProcessor;
    @Mock
    private AddressProcessor addressProcessor;
    @Mock
    private PlaceShopProcessor placeShopProcessor;
    @Mock
    private BusinessTimeProcessor businessTimeProcessor;
    @Mock
    private MemberProcessor memberProcessor;

    @Mock
    private ShopMapstructMapper shopMapstructMapper;

    private ShopMapstructMapperImpl shopMapstructMapperImpl;

    @InjectMocks
    private ShopService shopService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private List<Shop> mockShops;
    private List<Wish> mockWishes;
    private List<FindAllShopDto.Response> mockAllShopResponses;
    private Member mockMember;

    private final LocalDate visitDate = LocalDate.of(2024, 7, 30);
    private final DayType dayType = ShopUtil.date.getDayType(visitDate);
    private Place mockPlace; // "비발디 파크"
    private Address mockAddress; // "비발디 파크" 주소
    private List<PlaceShop> mockPlaceShops;
    private final List<FindLowPriceShopDto> mockShopLowPriceDtos = new ArrayList<>();
    private final List<FindLowPriceCustomDto> mockCustoms = new ArrayList<>();
    private final List<Image> mockShopImages = new ArrayList<>(); // TODO image 기능 완성 시 구현 추가
    private final Map<Long, Address> mockShopsAddress = new HashMap<>();
    private final Map<Long, Wish> mockShop1MemberWish = new HashMap<>();
    private final Map<Long, Member> mockShop1Owner = new HashMap<>();

    @BeforeEach
    void beforeEach() {
        shopMapstructMapperImpl = new ShopMapstructMapperImpl();

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
        List<Wish> wishes = createWish(shops, members);

        // 렌탈샵 리뷰 생성
        createReview(shops, members);

        // 렌탈샵 네이버 리뷰 생성
        createNaverReview(shops);

        // 스키장 렌탈샵 중간테이블 생성
        List<PlaceShop> placeShops = createPlaceShop(places, shops);

        // 스키장 리프트권 생성
        List<LiftTicket> liftTickets = createLiftTicket(places);

        // 렌탈샵 패키지 생성 (Item)
        createItem(shops, liftTickets);

        // 렌탈샵 상세 옵션 생성
        createItemOption(shops);

        // TODO: Image 기능 완성 시 수정
        List<Image> images = createImage();

        mockShops = shops;
        mockWishes = createMockWishes();
        mockAllShopResponses = createAllShopResponse(shops, mockWishes, images);
        mockMember = members.get(3);

        mockPlace = places.getFirst();
        mockAddress = addresses.getFirst();
        mockPlaceShops = placeShops;
        mockShopLowPriceDtos.add(mockLowPriceShop1());
        mockShopsAddress.put(mockShop1().getId(), mockShop1Address());
        mockShop1MemberWish.put(mockShop1().getId(), mockShop1WishOfMemberThree());
        mockShop1Owner.put(mockShop1().getId(), mockOwnerMember());
        mockCustoms.add(mockCustom1ForSearch());
        mockCustoms.add(mockCustom2ForSearch());
    }

    @Test
    @DisplayName("렌탈샵 목록 조회 성공")
    public void t1() {
        // given
        when(shopProcessor.findAllShopWithinRange(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(mockShops);

        for (int i = 0; i < mockShops.size(); i++) {
            when(wishProcessor.findWishByShopAndMember(
                    eq(mockShops.get(i)),
                    any()
            )).thenReturn(Optional.ofNullable(mockWishes.get(i)));

            when(shopMapstructMapper.ofFindAllShop(
                    eq(mockShops.get(i)),
                    any(),
                    any(),
                    anyList(),
                    anyDouble(),
                    anyLong(),
                    any(),
                    anyList()
            )).thenReturn(mockAllShopResponses.get(i));
        }

        // when
        List<FindAllShopDto.Response> shops = shopService.findAllShopWithinRange(anyDouble(), anyDouble(), anyDouble(), anyDouble(), mockMember);

        // then
        assertThat(shops.size()).isEqualTo(6);

        FindAllShopDto.Response shopResponse = shops.get(0);
        assertThat(shopResponse.id()).isEqualTo(1L);
        assertThat(shopResponse.name()).isEqualTo("찐렌탈샵");
        assertThat(shopResponse.wishId()).isEqualTo(1L);

        // TODO: image 기능 완성 시 구현 추가
        assertThat(shopResponse.images()).isNotNull();

        assertThat(shopResponse.packages().size()).isEqualTo(30);
        assertThat(shopResponse.packages().get(0).id()).isEqualTo(1L);
        assertThat(shopResponse.packages().get(0).type()).isEqualTo(PackageType.LIFT_EQUIPMENT_CLOTHES);
        assertThat(shopResponse.packages().get(0).name()).isEqualTo("주중 스마트4시간권+장비+의류");
        assertThat(shopResponse.packages().get(0).price()).isEqualTo(BigDecimal.valueOf(67000L));

        assertThat(shopResponse.moaReview().avgScore()).isEqualTo(2.5D);
        assertThat(shopResponse.moaReview().totalCount()).isEqualTo(4L);

        assertThat(shopResponse.naverReview().avgScore()).isEqualTo(4.5D);
        assertThat(shopResponse.naverReview().totalCount()).isEqualTo(186L);

        assertThat(shopResponse.places().size()).isEqualTo(1);
        assertThat(shopResponse.places().get(0).id()).isEqualTo(1L);
        assertThat(shopResponse.places().get(0).name()).isEqualTo("비발디파크");
    }

    @Test
    @DisplayName("[성공] 최저가 렌탈샵 검색")
    public void findAllShopSearchForTheLowestPrice_success() {
        // given
        // place 관련
        when(placeProcessor.findPlaceByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockPlace));
        when(addressProcessor.findAddressByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockAddress));

        List<PlaceShop> mockPlaceShopsByPlace = new ArrayList<>();
        mockPlaceShopsByPlace.add(mockPlaceShops.get(0));
        mockPlaceShopsByPlace.add(mockPlaceShops.get(1));
        mockPlaceShopsByPlace.add(mockPlaceShops.get(2));
        when(placeShopProcessor.findAllShopRelatedToPlace(mockPlace)).thenReturn(mockPlaceShopsByPlace);

        List<Long> mockShopIds = new ArrayList<>(); // [1, 2, 3]
        mockShopIds.add(mockPlaceShops.get(0).getId());
        mockShopIds.add(mockPlaceShops.get(1).getId());
        mockShopIds.add(mockPlaceShops.get(2).getId());

        Map<Long, Long> shopBusinessTimeIds = new HashMap<>(); // {1=2, 2=3, 3=6}
        shopBusinessTimeIds.put(mockShops.get(0).getId(), mockPlaceShops.get(1).getId());
        shopBusinessTimeIds.put(mockShops.get(1).getId(), mockPlaceShops.get(2).getId());
        shopBusinessTimeIds.put(mockShops.get(2).getId(), mockPlaceShops.get(5).getId());

        when(shopProcessor.findBusinessTimeIdOfShops(mockShopIds)).thenReturn(Optional.of(shopBusinessTimeIds));

        // shopBusinessTimeIds.keySet() for문
        when(businessTimeProcessor.isShopInOperation(2L, visitDate, dayType)).thenReturn(true);
        when(businessTimeProcessor.isShopInOperation(3L, visitDate, dayType)).thenReturn(true);
        when(businessTimeProcessor.isShopInOperation(6L, visitDate, dayType)).thenReturn(true);

        // shop 관련
        // shopsInOperation for문
        when(shopProcessor.findShopWithCustomForSearch(
                eq(1L),
                anyList(),
                any(Boolean.class))).thenReturn(Optional.ofNullable(mockLowPriceShop1()));
        when(shopProcessor.findShopWithCustomForSearch(
                eq(2L),
                anyList(),
                any(Boolean.class))).thenReturn(Optional.ofNullable(mockLowPriceShop2()));
        when(shopProcessor.findShopWithCustomForSearch(
                eq(3L),
                anyList(),
                any(Boolean.class))).thenReturn(Optional.empty());

        when(shopProcessor.findShopByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockShop1()));

        when(addressProcessor.findAddressByIdAndDeletedAtIsNull(2L)).thenReturn(Optional.ofNullable(mockShop1Address()));

        if (mockMember != null) {
            when(wishProcessor.findWishByShopAndMember(any(Shop.class), any(Member.class))).thenReturn(Optional.ofNullable(mockShop1WishOfMemberThree()));
        }

        Long memberId = mockShop1().getMember() == null ? null : mockOwnerMember().getId();
        when(shopProcessor.findMemberIdOfShopById(1L)).thenReturn(Optional.ofNullable(memberId));

        when(memberProcessor.findMemberByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockOwnerMember()));

        when(shopMapstructMapper.ofFindAllLowestShops(
                visitDate,
                mockPlace,
                mockAddress,
                null,
                mockShopLowPriceDtos,
                null,
                mockLowestPriceRequests().customs(),
                mockShopsAddress,
                mockShop1MemberWish,
                mockShop1Owner)).thenReturn(createLowestPriceShopsResponse(visitDate));

        // when
        FindAllShopLowPriceDto.Response allShopSearchForTheLowestPrice = shopService.findAllShopSearchForTheLowestPrice(mockLowestPriceRequests(), mockMember);

        // then
        assertThat(allShopSearchForTheLowestPrice.visitDate()).isEqualTo(LocalDate.of(2024, 7, 30));

        FindAllShopLowPriceDto.PlaceResponse place = allShopSearchForTheLowestPrice.place();
        assertThat(place.id()).isEqualTo(1L);
        assertThat(place.name()).isEqualTo("비발디파크");
        assertThat(place.open()).isEqualTo(LocalDate.of(2024, 10, 15));
        assertThat(place.close()).isEqualTo(LocalDate.of(2025, 3, 12));
        assertThat(place.recLevel()).isEqualTo(PlaceLevel.LEVEL_1);

        FindAllShopLowPriceDto.AddressResponse placeAddress = place.address();
        assertThat(placeAddress.id()).isEqualTo(1L);
        assertThat(placeAddress.address()).isEqualTo("강원도 홍천군 서면 한치골길 262");
        assertThat(placeAddress.addressDetail()).isNull();
        assertThat(placeAddress.locationX()).isEqualTo(127.687106349987);
        assertThat(placeAddress.locationY()).isEqualTo(37.6521031526954);
        assertThat(placeAddress.mapUrl()).isEqualTo("https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh");

        // TODO: image 기능 완성 시 구현 추가
        assertThat(place.images()).isNotNull();

        List<FindAllShopLowPriceDto.ShopResponse> shops = allShopSearchForTheLowestPrice.shops();
        assertThat(shops.size()).isEqualTo(1);

        FindAllShopLowPriceDto.ShopResponse shop1Response = shops.get(0);
        assertThat(shop1Response.id()).isEqualTo(1L);
        assertThat(shop1Response.wishId()).isNull();
        assertThat(shop1Response.totalPrice()).isEqualTo(BigDecimal.valueOf(163000.0));
        assertThat(shop1Response.memberName()).isEqualTo("admin");
        assertThat(shop1Response.name()).isEqualTo("찐렌탈샵");
        assertThat(shop1Response.storeUrl()).isEqualTo("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9");

        assertThat(shop1Response.moaReview().avgScore()).isEqualTo(2.5D);
        assertThat(shop1Response.moaReview().totalCount()).isEqualTo(4L);

        assertThat(shop1Response.naverReview().avgScore()).isEqualTo(4.5D);
        assertThat(shop1Response.naverReview().totalCount()).isEqualTo(186L);

        assertThat(shop1Response.address().id()).isEqualTo(2L);
        assertThat(shop1Response.address().address()).isEqualTo("강원 홍천군 서면 한치골길 39");
        assertThat(shop1Response.address().addressDetail()).isEqualTo("1, 2층");
        assertThat(shop1Response.address().locationX()).isEqualTo(37.625378749786);
        assertThat(shop1Response.address().locationY()).isEqualTo(127.666621133276);
        assertThat(shop1Response.address().mapUrl()).isEqualTo("https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%B0%90%EB%A0%8C%ED%83%88%EC%83%B5/place/1680503531?c=15.00,0,0,0,dh&isCorrectAnswer=true");

        // TODO: image 기능 완성 시 구현 추가
        assertThat(shop1Response.images()).isNotNull();

        List<FindAllShopLowPriceDto.CustomResponse> customsResponse = shop1Response.customs();
        assertThat(customsResponse.size()).isEqualTo(2);
        assertThat(customsResponse.get(0).gender()).isEqualTo(Gender.MALE.getDesc());
        assertThat(customsResponse.get(0).nickname()).isEqualTo("커스텀1");
        assertThat(customsResponse.get(0).packageType()).isEqualTo(PackageType.LIFT_EQUIPMENT_CLOTHES.getDesc());
        assertThat(customsResponse.get(0).clothesType()).isEqualTo(ClothesType.LUXURY.getDesc());
        assertThat(customsResponse.get(0).equipmentType()).isEqualTo(EquipmentType.SHORT_SKI.getDesc());
        assertThat(customsResponse.get(0).price()).isEqualTo(BigDecimal.valueOf(87000.0));

        assertThat(customsResponse.get(1).gender()).isEqualTo(Gender.FEMALE.getDesc());
        assertThat(customsResponse.get(1).nickname()).isEqualTo("커스텀2");
        assertThat(customsResponse.get(1).packageType()).isEqualTo(PackageType.LIFT_EQUIPMENT_CLOTHES.getDesc());
        assertThat(customsResponse.get(1).clothesType()).isEqualTo(ClothesType.STANDARD.getDesc());
        assertThat(customsResponse.get(1).equipmentType()).isEqualTo(EquipmentType.SKI.getDesc());
        assertThat(customsResponse.get(1).price()).isEqualTo(BigDecimal.valueOf(76000.0));
    }

    @Test
    @DisplayName("[실패] 최저가 렌탈샵 검색 - place가 없을 경우")
    void findAllShopSearchForTheLowestPrice_placeNotFound() {
        // given
        when(placeProcessor.findPlaceByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            shopService.findAllShopSearchForTheLowestPrice(mockLowestPriceRequests(), mockMember);
        });

        // then
        assertEquals(FailHttpMessage.Place.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("[실패] 최저가 렌탈샵 검색 - place를 찾았지만, 관련된 shop이 없을 경우")
    void findAllShopSearchForTheLowestPrice_shopsNotFound() {
        // given
        when(placeProcessor.findPlaceByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockPlace));
        when(placeShopProcessor.findAllShopRelatedToPlace(any(Place.class))).thenReturn(Collections.emptyList());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            shopService.findAllShopSearchForTheLowestPrice(mockLowestPriceRequests(), mockMember);
        });

        // then
        assertEquals(FailHttpMessage.Shop.NOT_FOUND_SHOP_RELATED_TO_PLACE.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("[실패] 최저가 렌탈샵 검색 - 운영 중인 shop이 없을 경우")
    void findAllShopSearchForTheLowestPrice_shopsNotInOperation() {
        // given
        when(placeProcessor.findPlaceByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockPlace));
        when(addressProcessor.findAddressByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockAddress));

        List<PlaceShop> mockPlaceShopsByPlace = new ArrayList<>();
        mockPlaceShopsByPlace.add(mockPlaceShops.get(0));
        mockPlaceShopsByPlace.add(mockPlaceShops.get(1));
        mockPlaceShopsByPlace.add(mockPlaceShops.get(2));
        when(placeShopProcessor.findAllShopRelatedToPlace(mockPlace)).thenReturn(mockPlaceShopsByPlace);

        List<Long> mockShopIds = new ArrayList<>(); // [1, 2, 3]
        mockShopIds.add(mockPlaceShops.get(0).getId());
        mockShopIds.add(mockPlaceShops.get(1).getId());
        mockShopIds.add(mockPlaceShops.get(2).getId());

        Map<Long, Long> shopBusinessTimeIds = new HashMap<>(); // {1=2, 2=3, 3=6}
        shopBusinessTimeIds.put(mockShops.get(0).getId(), mockPlaceShops.get(1).getId());
        shopBusinessTimeIds.put(mockShops.get(1).getId(), mockPlaceShops.get(2).getId());
        shopBusinessTimeIds.put(mockShops.get(2).getId(), mockPlaceShops.get(5).getId());

        when(shopProcessor.findBusinessTimeIdOfShops(mockShopIds)).thenReturn(Optional.of(shopBusinessTimeIds));

        // shopBusinessTimeIds.keySet() for문
        when(businessTimeProcessor.isShopInOperation(2L, visitDate, dayType)).thenReturn(false);
        when(businessTimeProcessor.isShopInOperation(3L, visitDate, dayType)).thenReturn(false);
        when(businessTimeProcessor.isShopInOperation(6L, visitDate, dayType)).thenReturn(false);

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            shopService.findAllShopSearchForTheLowestPrice(mockLowestPriceRequests(), mockMember);
        });

        // then
        assertEquals(FailHttpMessage.Shop.NOT_FOUND_SHOP_IN_OPERATION.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("[실패] 최저가 렌탈샵 검색 - 매칭되는 커스텀이 없을 경우")
    void findAllShopSearchForTheLowestPrice_noMatchingCustom() {
        // given
        when(placeProcessor.findPlaceByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockPlace));
        when(addressProcessor.findAddressByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.ofNullable(mockAddress));

        List<PlaceShop> mockPlaceShopsByPlace = new ArrayList<>();
        mockPlaceShopsByPlace.add(mockPlaceShops.get(0));
        mockPlaceShopsByPlace.add(mockPlaceShops.get(1));
        mockPlaceShopsByPlace.add(mockPlaceShops.get(2));
        when(placeShopProcessor.findAllShopRelatedToPlace(mockPlace)).thenReturn(mockPlaceShopsByPlace);

        List<Long> mockShopIds = new ArrayList<>(); // [1, 2, 3]
        mockShopIds.add(mockPlaceShops.get(0).getId());
        mockShopIds.add(mockPlaceShops.get(1).getId());
        mockShopIds.add(mockPlaceShops.get(2).getId());

        Map<Long, Long> shopBusinessTimeIds = new HashMap<>(); // {1=2, 2=3, 3=6}
        shopBusinessTimeIds.put(mockShops.get(0).getId(), mockPlaceShops.get(1).getId());
        shopBusinessTimeIds.put(mockShops.get(1).getId(), mockPlaceShops.get(2).getId());
        shopBusinessTimeIds.put(mockShops.get(2).getId(), mockPlaceShops.get(5).getId());

        when(shopProcessor.findBusinessTimeIdOfShops(mockShopIds)).thenReturn(Optional.of(shopBusinessTimeIds));

        when(businessTimeProcessor.isShopInOperation(2L, visitDate, dayType)).thenReturn(true);

        when(shopProcessor.findShopWithCustomForSearch(eq(1L), anyList(), any(Boolean.class))).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            shopService.findAllShopSearchForTheLowestPrice(mockLowestPriceRequests(), mockMember);
        });

        // then
        assertEquals(FailHttpMessage.Shop.NOT_FOUND_MATCHING_SHOP.getMessage(), exception.getMessage());
    }

    private Category createCategory() {
        Category category = Category.builder()
                .categoryType(CategoryType.SKI_RESORT)
                .build();

        return category;
    }

    private List<Address> createAddress() {
        List<Address> list = new ArrayList<>();
        Address address_1 = createAddress(1L, "강원도 홍천군 서면 한치골길 262", 37.6521031526954, 127.687106349987,
                "https://map.naver.com/p/entry/place/13139708?c=15.00,0,0,0,dh");
        Address address_2 = createAddress(2L, "강원 홍천군 서면 한치골길 39", "1, 2층", 37.625378749786, 127.666621133276,
                "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%B0%90%EB%A0%8C%ED%83%88%EC%83%B5/place/1680503531?c=15.00,0,0,0,dh&isCorrectAnswer=true");
        Address address_3 = createAddress(3L, "강원 홍천군 서면 한치골길 952", 37.6935333700434, 127.701873788335,
                "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%95%84%EC%A7%80%ED%8A%B8/place/11590090?c=18.01,0,0,0,dh&isCorrectAnswer=true");
        Address address_4 = createAddress(4L, "강원 정선군 고한읍 하이원길 424", 37.2072213760495, 128.836835354268,
                "https://map.naver.com/p/entry/place/92136142?lng=128.8388599&lat=37.204042&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh");
        Address address_5 = createAddress(5L, "강원 춘천시 남산면 북한강변길 688", 37.8300557977982, 127.57878172946,
                "https://map.naver.com/p/search/%EC%8A%A4%ED%82%A4%EC%9E%A5/place/15648643?placePath=?entry=pll&from=nx&fromNxList=true&searchType=place&c=15.00,0,0,0,dh");
        Address address_6 = createAddress(6L, "강원 홍천군 서면 한서로 2137", "비발디파크인생렌탈샵", 37.6167793731889, 127.671714070978,
                "https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%20%ED%8C%8C%ED%81%AC%20%EB%A0%8C%ED%83%88/place/1034233118?c=12.00,0,0,0,dh&placePath=%3Fentry%253Dpll");
        Address address_7 = createAddress(7L, "강원 정선군 고한읍 고한로 40", "하이원 스키샵 월남스키 렌탈샵", 37.2076563451798, 128.843415629048,
                "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/12447242?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dpll");
        Address address_8 = createAddress(8L, "강원 정선군 사북읍 하이원길 36", "눈의나라", 37.2234130104246, 128.814883350542,
                "https://map.naver.com/p/search/%ED%95%98%EC%9D%B4%EC%9B%90%EB%A6%AC%EC%A1%B0%ED%8A%B8%20%EB%A0%8C%ED%83%88%EC%83%B5/place/12995662?c=17.00,0,0,0,dh&placePath=%3Fentry%253Dpll");
        Address address_9 = createAddress(9L, "강원 정선군 고한읍 고한로 12", "스노우블루 스키샵", 37.2095420989986, 128.841584050646,
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
        businessTime_1.addSpecificDays(specificDays_1);

        BusinessTime businessTime_2 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_2 = operating2(businessTime_2);
        List<SpecificDay> specificDays_2 = specific2(businessTime_2);
        list.add(businessTime_2);

        businessTime_2.addOperatingTimes(operatingTimes_2);
        businessTime_2.addSpecificDays(specificDays_2);

        BusinessTime businessTime_3 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_3 = operating3(businessTime_3);
        List<SpecificDay> specificDays_3 = specific3(businessTime_3);
        list.add(businessTime_3);

        businessTime_3.addOperatingTimes(operatingTimes_3);
        businessTime_3.addSpecificDays(specificDays_3);

        BusinessTime businessTime_4 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_4 = operating4(businessTime_4);
        List<SpecificDay> specificDays_4 = specific4(businessTime_4);
        list.add(businessTime_4);

        businessTime_4.addOperatingTimes(operatingTimes_4);
        businessTime_4.addSpecificDays(specificDays_4);

        BusinessTime businessTime_5 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_5 = operating5(businessTime_5);
        List<SpecificDay> specificDays_5 = specific5(businessTime_5);
        list.add(businessTime_5);

        businessTime_5.addOperatingTimes(operatingTimes_5);
        businessTime_5.addSpecificDays(specificDays_5);

        BusinessTime businessTime_6 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_6 = operating6(businessTime_6);
        List<SpecificDay> specificDays_6 = specific6(businessTime_6);
        list.add(businessTime_6);

        businessTime_6.addOperatingTimes(operatingTimes_6);
        businessTime_6.addSpecificDays(specificDays_6);

        BusinessTime businessTime_7 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_7 = operating7(businessTime_7);
        List<SpecificDay> specificDays_7 = specific7(businessTime_7);
        list.add(businessTime_7);

        businessTime_7.addOperatingTimes(operatingTimes_7);
        businessTime_7.addSpecificDays(specificDays_7);

        BusinessTime businessTime_8 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_8 = operating8(businessTime_8);
        List<SpecificDay> specificDays_8 = specific8(businessTime_8);
        list.add(businessTime_8);

        businessTime_8.addOperatingTimes(operatingTimes_8);
        businessTime_8.addSpecificDays(specificDays_8);

        BusinessTime businessTime_9 = BusinessTime.builder().build();
        List<OperatingTime> operatingTimes_9 = operating9(businessTime_9);
        List<SpecificDay> specificDays_9 = specific9(businessTime_9);
        list.add(businessTime_9);

        businessTime_9.addOperatingTimes(operatingTimes_9);
        businessTime_9.addSpecificDays(specificDays_9);

        return list;
    }

    private List<Place> createPlace(Category category, List<Address> addresses, List<BusinessTime> businessTimes) {
        List<Place> list = new ArrayList<>();
        Place place_1 = createPlace(1L, category, addresses.get(0), businessTimes.get(0), "비발디파크", LocalDate.of(2024, 10, 15),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_1);

        Place place_2 = createPlace(2L, category, addresses.get(3), businessTimes.get(3), "하이원리조트", LocalDate.of(2024, 10, 16),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_2);

        Place place_3 = createPlace(3L, category, addresses.get(4), businessTimes.get(4), "엘리시안", LocalDate.of(2024, 10, 17),
                LocalDate.of(2025, 3, 12), PlaceLevel.LEVEL_4);

        Place place_4 = createPlace(4L, category, addresses.get(8), businessTimes.get(8), "지산리조트", LocalDate.of(2024, 10, 18),
                LocalDate.of(2025, 3, 15), PlaceLevel.LEVEL_4);

        list.add(place_1);
        list.add(place_2);
        list.add(place_3);
        list.add(place_4);

        return list;
    }

    // Create Object
    private Place createPlace(Long id, Category category, Address address, BusinessTime time, String name, LocalDate open, LocalDate close, PlaceLevel level, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return Place.builder()
                .id(id)
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

    private Address createAddress(Long addressId, String address, double y, double x, String url, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return Address.builder()
                .id(addressId)
                .address(address)
                .location(geometryFactory.createPoint(new Coordinate(x, y)))
                .url(url)
                .deletedAt(deleted ? LocalDateTime.now() : null)
                .build();
    }

    private Address createAddress(Long addressId, String address, String detail, double y, double x, String url, boolean... isDeleted) {
        boolean deleted = isDeleted.length > 0 && isDeleted[0];

        return Address.builder()
                .id(addressId)
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
        list.add(Member.builder().id(1L).email("admin@moa.com").nickname("admin").role(MemberRole.ADMIN).build());
        list.add(Member.builder().id(2L).email("one@moa.com").nickname("one").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(3L).email("two@moa.com").nickname("two").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(4L).email("three@moa.com").nickname("three").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(5L).email("four@moa.com").nickname("four").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(6L).email("five@moa.com").nickname("five").role(MemberRole.MEMBER).build());
        return list;
    }

    private List<PlaceShop> createPlaceShop(List<Place> places, List<Shop> shops) {
        List<PlaceShop> list_0 = new ArrayList<>();
        list_0.add(PlaceShop.builder().id(1L).place(places.get(0)).shop(shops.get(0)).build());
        shops.get(0).addPlaceShops(list_0);

        List<PlaceShop> list_1 = new ArrayList<>();
        list_1.add(PlaceShop.builder().id(2L).place(places.get(0)).shop(shops.get(1)).build());
        shops.get(1).addPlaceShops(list_1);

        List<PlaceShop> list_2 = new ArrayList<>();
        list_2.add(PlaceShop.builder().id(3L).place(places.get(0)).shop(shops.get(2)).build());
        shops.get(2).addPlaceShops(list_2);

        List<PlaceShop> list_3 = new ArrayList<>();
        list_3.add(PlaceShop.builder().id(4L).place(places.get(1)).shop(shops.get(3)).build());
        shops.get(3).addPlaceShops(list_3);

        List<PlaceShop> list_4 = new ArrayList<>();
        list_4.add(PlaceShop.builder().id(5L).place(places.get(1)).shop(shops.get(4)).build());
        shops.get(4).addPlaceShops(list_4);

        List<PlaceShop> list_5 = new ArrayList<>();
        list_5.add(PlaceShop.builder().id(6L).place(places.get(1)).shop(shops.get(5)).build());
        shops.get(5).addPlaceShops(list_5);

        List<PlaceShop> list = new ArrayList<>();
        list.addAll(list_0);
        list.addAll(list_1);
        list.addAll(list_2);
        list.addAll(list_3);
        list.addAll(list_4);
        list.addAll(list_5);
        return list;
    }

    private List<Shop> createShop(List<Member> members, Category category, List<Address> addresses, List<BusinessTime> times) {
        List<Shop> list = new ArrayList<>();
        list.add(Shop.builder().id(1L).category(category).address(addresses.get(1)).businessTime(times.get(1)).name("찐렌탈샵").pickUp(true).url("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9").build());
        list.add(Shop.builder().id(2L).category(category).address(addresses.get(2)).businessTime(times.get(2)).name("아지트").pickUp(true).url("https://smartstore.naver.com/rentalshop1/products/6117544378?nl-au=9e070d7e195341e699c36096c861ab13&NaPm=ct%3Dlziltmg8%7Cci%3D0d4c31cf63842f0accf616231028952db4b1b241%7Ctr%3Dslsl%7Csn%3D5126374%7Chk%3D73c670109b1457dee566270729957b85127e0128").build());
        list.add(Shop.builder().id(3L).category(category).address(addresses.get(5)).businessTime(times.get(5)).name("인생렌탈샵").pickUp(false).url("https://smartstore.naver.com/dgshop/products/9614236927").build());
        list.add(Shop.builder().id(4L).category(category).address(addresses.get(6)).businessTime(times.get(6)).name("월남스키").pickUp(true).url("https://smartstore.naver.com/wnskishop/products/5314182831").build());
        list.add(Shop.builder().id(5L).category(category).address(addresses.get(7)).businessTime(times.get(7)).name("눈의나라").pickUp(false).url("https://smartstore.naver.com/noonnara/products/5323804307").build());
        list.add(Shop.builder().id(6L).category(category).address(addresses.get(8)).businessTime(times.get(8)).name("스노우블루").pickUp(true).url("https://smartstore.naver.com/snowblue1/products/9727997372").build());
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
        return list;
    }

    private List<Review> createReview(List<Shop> shops, List<Member> members) {
        List<Review> list_0 = new ArrayList<>();
        list_0.add(Review.builder().shop(shops.get(0)).member(members.get(1)).score(4d).content("좋아요").build());
        list_0.add(Review.builder().shop(shops.get(0)).member(members.get(2)).score(3d).content("아쉬워요").build());
        list_0.add(Review.builder().shop(shops.get(0)).member(members.get(3)).score(2d).content("별로에요").build());
        list_0.add(Review.builder().shop(shops.get(0)).member(members.get(4)).score(1d).content("개쓰레기 ㅋㅋㅋ").build());
        shops.get(0).addReviews(list_0);

        List<Review> list_1 = new ArrayList<>();
        list_1.add(Review.builder().shop(shops.get(1)).member(members.get(1)).score(3d).content("어중간히 괜찮음 ㅋ").build());
        list_1.add(Review.builder().shop(shops.get(1)).member(members.get(2)).score(3d).content("괜찮음").build());
        list_1.add(Review.builder().shop(shops.get(1)).member(members.get(3)).score(3d).content("나쁘지 않음").build());
        list_1.add(Review.builder().shop(shops.get(1)).member(members.get(4)).score(3d).content("직원이 불친절").build());
        list_1.add(Review.builder().shop(shops.get(1)).member(members.get(5)).score(3d).content("ㅋㅋㅋㅋㅋ").build());
        shops.get(1).addReviews(list_1);

        List<Review> list_2 = new ArrayList<>();
        list_2.add(Review.builder().shop(shops.get(2)).member(members.get(1)).score(3d).content("그저 그럼").build());
        list_2.add(Review.builder().shop(shops.get(2)).member(members.get(2)).score(4d).content("좋음").build());
        list_2.add(Review.builder().shop(shops.get(2)).member(members.get(3)).score(5d).content("아주 좋음").build());
        list_2.add(Review.builder().shop(shops.get(2)).member(members.get(4)).score(1d).content("별로").build());
        list_2.add(Review.builder().shop(shops.get(2)).member(members.get(5)).score(2d).content("ㅠㅠ").build());
        shops.get(2).addReviews(list_2);

        List<Review> list_3 = new ArrayList<>();
        list_3.add(Review.builder().shop(shops.get(3)).member(members.get(1)).score(1d).content("진짜 별로").build());
        list_3.add(Review.builder().shop(shops.get(3)).member(members.get(2)).score(2d).content("가지마세요").build());
        list_3.add(Review.builder().shop(shops.get(3)).member(members.get(3)).score(3d).content("좋습니다").build());
        list_3.add(Review.builder().shop(shops.get(3)).member(members.get(4)).score(4d).content("추천합니다").build());
        shops.get(3).addReviews(list_3);

        List<Review> list_4 = new ArrayList<>();
        list_4.add(Review.builder().shop(shops.get(4)).member(members.get(2)).score(3d).content("그저 그럼").build());
        list_4.add(Review.builder().shop(shops.get(4)).member(members.get(3)).score(3d).content("아쉬워요").build());
        list_4.add(Review.builder().shop(shops.get(4)).member(members.get(4)).score(5d).content("진짜 좋음").build());
        list_4.add(Review.builder().shop(shops.get(4)).member(members.get(5)).score(5d).content("꼭 가세요").build());
        shops.get(4).addReviews(list_4);

        List<Review> list_5 = new ArrayList<>();
        list_5.add(Review.builder().shop(shops.get(5)).member(members.get(1)).score(3d).content("보통").build());
        list_5.add(Review.builder().shop(shops.get(5)).member(members.get(2)).score(4d).content("좋아용").build());
        list_5.add(Review.builder().shop(shops.get(5)).member(members.get(3)).score(2d).content("불친절함").build());
        list_5.add(Review.builder().shop(shops.get(5)).member(members.get(4)).score(1d).content("장비가 별로임").build());
        list_5.add(Review.builder().shop(shops.get(5)).member(members.get(5)).score(5d).content("짱!!").build());
        shops.get(5).addReviews(list_5);

        List<Review> list = new ArrayList<>();
        list.addAll(list_0);
        list.addAll(list_1);
        list.addAll(list_2);
        list.addAll(list_3);
        list.addAll(list_4);
        list.addAll(list_5);
        return list;
    }

    private void createNaverReview(List<Shop> shops) {
        shops.get(0).addNaverReview(NaverReview.builder().shop(shops.get(0)).avgScore(4.5d).totalReview(186L).build());
        shops.get(1).addNaverReview(NaverReview.builder().shop(shops.get(1)).avgScore(4d).totalReview(299L).build());
        shops.get(2).addNaverReview(NaverReview.builder().shop(shops.get(2)).avgScore(3.5d).totalReview(100L).build());
        shops.get(3).addNaverReview(NaverReview.builder().shop(shops.get(3)).avgScore(5d).totalReview(50L).build());
        shops.get(4).addNaverReview(NaverReview.builder().shop(shops.get(4)).avgScore(4.8d).totalReview(177L).build());
        shops.get(5).addNaverReview(NaverReview.builder().shop(shops.get(5)).avgScore(4.3d).totalReview(80L).build());
    }

    private List<LiftTicket> createLiftTicket(List<Place> places) {
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

        List<LiftTicket> list = new ArrayList<>();
        list.addAll(list_1);
        list.addAll(list_2);
        list.addAll(list_3);
        return list;
    }

    private List<Item> createItem(List<Shop> shops, List<LiftTicket> liftTickets) {
        List<Item> list_0 = new ArrayList<>();
        list_0.add(Item.builder().id(1L).shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(52000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(52000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(81000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(61000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(61000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(56000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(56000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(45000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(45000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(57000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(57000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(87000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(67000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(51000L)).used(true).build());
        list_0.add(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(51000L)).used(true).build());
        shops.get(0).addItems(list_0);

        List<Item> list_1 = new ArrayList<>();
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(52000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(61000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(57000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(67000L)).used(true).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_1.add(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        shops.get(1).addItems(list_1);

        List<Item> list_2 = new ArrayList<>();
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(55000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(55000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(83000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(65000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(73000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(53000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(53000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(58000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(75000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(59000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(59000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(85000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(83000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(89000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(69000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_2.add(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        shops.get(2).addItems(list_2);

        List<Item> list_3 = new ArrayList<>();
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(62000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(74000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(106000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(104000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(100000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(62000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(74000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(106000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(104000L)).used(true).build());
        list_3.add(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(100000L)).used(true).build());
        shops.get(3).addItems(list_3);

        List<Item> list_4 = new ArrayList<>();
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(60000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(74000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(60000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(74000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(80000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        list_4.add(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        shops.get(4).addItems(list_4);

        List<Item> list_5 = new ArrayList<>();
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(70000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(86000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(84000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(80000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(108000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(106000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(102000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(70000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(76000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(86000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(84000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(80000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(108000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(102000L)).used(true).build());
        list_5.add(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(102000L)).used(true).build());
        shops.get(5).addItems(list_5);

        List<Item> list = new ArrayList<>();
        list.addAll(list_0);
        list.addAll(list_1);
        list.addAll(list_2);
        list.addAll(list_3);
        list.addAll(list_4);
        list.addAll(list_5);
        return list;
    }

    private List<ItemOption> createItemOption(List<Shop> shops) {
        List<ItemOption> list_0 = new ArrayList<>();
        list_0.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(24L).addPrice(BigDecimal.valueOf(10000L)).build());
        list_0.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(24L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_0.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(10000L)).build());
        list_0.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_0.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(7L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_0.add(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(7L).addPrice(BigDecimal.valueOf(30000L)).build());
        shops.get(0).addItemOptions(list_0);

        List<ItemOption> list_1 = new ArrayList<>();
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.LUXURY).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(25000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.PREMIUM).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(35000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(5000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(10000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(10000L)).build());
        list_1.add(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(15000L)).build());
        shops.get(1).addItemOptions(list_1);

        List<ItemOption> list_2 = new ArrayList<>();
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(25000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(35000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.LUXURY).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.PREMIUM).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(40000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(10000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(15000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.SHORT_SKI).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_2.add(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.INLINE_SKI).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(25000L)).build());
        shops.get(2).addItemOptions(list_1);

        List<ItemOption> list_3 = new ArrayList<>();
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.LUXURY).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.PREMIUM).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        list_3.add(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        shops.get(3).addItemOptions(list_3);

        List<ItemOption> list_4 = new ArrayList<>();
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(5000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(18000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.LUXURY).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.PREMIUM).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(5000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(18000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.SHORT_SKI).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        list_4.add(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.INLINE_SKI).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        shops.get(4).addItemOptions(list_4);

        List<ItemOption> list_5 = new ArrayList<>();
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(12000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.LUXURY).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.PREMIUM).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(30000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(5000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(10000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.SHORT_SKI).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        list_5.add(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.INLINE_SKI).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(12000L)).build());
        shops.get(5).addItemOptions(list_5);

        List<ItemOption> list = new ArrayList<>();
        list.addAll(list_0);
        list.addAll(list_1);
        list.addAll(list_1);
        list.addAll(list_3);
        list.addAll(list_4);
        list.addAll(list_5);
        return list;
    }

    private List<Image> createImage() {
        List<Image> list = new ArrayList<>();

        list.add(Image.builder().build());
        list.add(Image.builder().build());
        list.add(Image.builder().build());
        list.add(Image.builder().build());
        list.add(Image.builder().build());
        list.add(Image.builder().build());

        return list;
    }

    private List<Wish> createMockWishes() {
        List<Wish> list = new ArrayList<>();
        list.add(Wish.builder().id(1L).build());
        list.add(Wish.builder().id(2L).build());
        list.add(Wish.builder().id(3L).build());
        list.add(Wish.builder().id(4L).build());
        list.add(Wish.builder().id(5L).build());
        list.add(Wish.builder().id(6L).build());
        return list;
    }

    private List<FindAllShopDto.Response> createAllShopResponse(List<Shop> shops, List<Wish> wishes, List<Image> images) {
        List<FindAllShopDto.Response> responseList = new ArrayList<>();

        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            Wish wish = wishes.get(i);
            Image image = images.get(i);

            responseList.add(createAllResponseFromShop(shop, wish, image));
        }

        return responseList;
    }

    private FindAllShopDto.Response createAllResponseFromShop(Shop shop, Wish wish, Image image) {
        List<Review> reviews = shop.getReviews();
        Long moaTotalCount = (long) reviews.size();
        Double moaAvgScore = reviews.stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(0.0);

        NaverReview naverReview = shop.getNaverReview();
        if (naverReview == null) {
            naverReview = NaverReview.builder()
                    .avgScore(0.0)
                    .totalReview(0L)
                    .build();
        }

        List<Place> places = shop.getPlaceShops().stream()
                .map(PlaceShop::getPlace)
                .collect(Collectors.toList());

        return shopMapstructMapperImpl.ofFindAllShop(
                shop,
                wish,
                image,
                shop.getItems(),
                moaAvgScore,
                moaTotalCount,
                naverReview,
                places
        );
    }

    // id 1인 "찐렌탈샵"
    private Shop mockShop1() {
        Shop shop = Shop.builder()
                .id(1L)
                .category(null)
                .address(null)
                .businessTime(null)
                .name("찐렌탈샵")
                .pickUp(true)
                .url("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9")
                .build();
        shop.addMember(mockOwnerMember());
        shop.addAddress(mockShop1Address());

        return shop;
    }

    // id 1인 "찐렌탈샵"의 주소
    private Address mockShop1Address() {
        return Address.builder()
                .id(2L)
                .address("강원 홍천군 서면 한치골길 39")
                .addressDetail("1, 2층")
                .location(geometryFactory.createPoint(new Coordinate(37.625378749786, 127.666621133276)))
                .url("https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%B0%90%EB%A0%8C%ED%83%88%EC%83%B5/place/1680503531?c=15.00,0,0,0,dh&isCorrectAnswer=true")
                .build();
    }

    // id 2인 "아지트"의 주소
    private Address mockShop2Address() {
        return Address.builder()
                .id(3L)
                .address("강원 홍천군 서면 한치골길 952")
                .addressDetail(null)
                .location(geometryFactory.createPoint(new Coordinate(37.6935333700434, 127.701873788335)))
                .url("https://map.naver.com/p/search/%EB%B9%84%EB%B0%9C%EB%94%94%ED%8C%8C%ED%81%AC%20%EC%95%84%EC%A7%80%ED%8A%B8/place/11590090?c=18.01,0,0,0,dh&isCorrectAnswer=true")
                .build();
    }

    // mockMember("three@moa.com")은 id 1인 "찐렌탈샵"을 좋아하지 않는다.
    private Wish mockShop1WishOfMemberThree() {
        return Wish.builder()
                .shop(mockShop1())
                .member(null)
                .build();
    }

    // 최저가 렌탈샵 검색을 위해 커스텀 가공
    private FindLowPriceCustomDto mockCustom1ForSearch() {
        return FindLowPriceCustomDto.builder()
                .gender(Gender.MALE)
                .nickname("커스텀1")
                .liftType("스마트권")
                .liftTime("4")
                .itemName("주중 스마트4시간권+장비+의류")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.LUXURY)
                .equipmentType(EquipmentType.SHORT_SKI)
                .itemOptionNames(ShopUtil.match.getItemOptionNames(ClothesType.LUXURY, EquipmentType.SHORT_SKI))
                .build();
    }

    private FindLowPriceCustomDto mockCustom2ForSearch() {
        return FindLowPriceCustomDto.builder()
                .gender(Gender.MALE)
                .nickname("커스텀2")
                .liftType("시간지정권-오후권")
                .liftTime("4")
                .itemName("주중 오후권+장비+의류")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SKI)
                .itemOptionNames(null)
                .build();
    }

    // id 1인 "찐렌탈샵"에 대한 2명의 커스텀 가격 결과
    private FindLowPriceShopDto mockLowPriceShop1() {
        return FindLowPriceShopDto.builder()
                .shopId(1L)
                .name("찐렌탈샵")
                .pickUp(true)
                .storeUrl("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9")
                .customPrices(List.of(BigDecimal.valueOf(87000.0), BigDecimal.valueOf(76000.0)))
                .totalPrice(BigDecimal.valueOf(163000.0))
                .avgScore(2.5)
                .totalCount(4L)
                .nrAvgScore(4.5)
                .nrTotalCount(186L)
                .build();
    }

    // id 2인 "아지트"에 대한 2명의 커스텀 가격 결과
    private FindLowPriceShopDto mockLowPriceShop2() {
        return FindLowPriceShopDto.builder()
                .shopId(2L)
                .name("아지트")
                .pickUp(true)
                .storeUrl("https://smartstore.naver.com/rentalshop1/products/6117544378?nl-au=9e070d7e195341e699c36096c861ab13&NaPm=ct%3Dlziltmg8%7Cci%3D0d4c31cf63842f0accf616231028952db4b1b241%7Ctr%3Dslsl%7Csn%3D5126374%7Chk%3D73c670109b1457dee566270729957b85127e0128")
                .customPrices(List.of(BigDecimal.valueOf(92000.0), BigDecimal.valueOf(0.0)))
                .totalPrice(BigDecimal.valueOf(92000.0))
                .avgScore(3.0)
                .totalCount(5L)
                .nrAvgScore(4.0)
                .nrTotalCount(299L)
                .build();
    }

    // id 1인 "찐렌탈샵" 사장
    private Member mockOwnerMember() {
        // 임시로 테스트를 위해서 추가한 id 1인 "찐렌탈샵"의 owner
        return Member.builder()
                .id(1L)
                .email("admin@moa.com")
                .nickname("admin")
                .role(MemberRole.ADMIN)
                .build();
    }

    // 최저가 렌탈샵 검색 요청
    private FindAllShopLowPriceDto.Request mockLowestPriceRequests() {
        FindAllShopLowPriceDto.CustomRequest customRequest1 = FindAllShopLowPriceDto.CustomRequest.builder()
                .gender(Gender.MALE)
                .nickname("커스텀1")
                .liftType("스마트권")
                .liftTime("4")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.LUXURY)
                .equipmentType(EquipmentType.SHORT_SKI)
                .build();

        FindAllShopLowPriceDto.CustomRequest customRequest2 = FindAllShopLowPriceDto.CustomRequest.builder()
                .gender(Gender.FEMALE)
                .nickname("커스텀2")
                .liftType("시간지정권-오후권")
                .liftTime("4")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SKI)
                .build();

        return FindAllShopLowPriceDto.Request.builder()
                .place(FindAllShopLowPriceDto.PlaceRequest.builder().id(1L).visitDate(LocalDate.of(2024,7,30)).build())
                .shop(FindAllShopLowPriceDto.ShopRequest.builder().pickUp(true).build())
                .customs(List.of(customRequest1, customRequest2))
                .build();
    }

    // 최저가 렌탈샵 검색 응답
    private FindAllShopLowPriceDto.Response createLowestPriceShopsResponse(LocalDate visitDate) {
        return shopMapstructMapperImpl.ofFindAllLowestShops(
                visitDate,
                mockPlace,
                mockAddress,
                null, // placeImage
                mockShopLowPriceDtos,
                mockShopImages,
                mockLowestPriceRequests().customs(), // customs
                mockShopsAddress,
                mockShop1MemberWish,
                mockShop1Owner);
    }
}