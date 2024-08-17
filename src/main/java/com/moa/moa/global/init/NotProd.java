package com.moa.moa.global.init;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.category.category.domain.persistence.CategoryRepository;
import com.moa.moa.api.category.category.util.enumerated.CategoryType;
import com.moa.moa.api.cs.answer.domain.entity.Answer;
import com.moa.moa.api.cs.answer.domain.persistence.AnswerRepository;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.domain.persistence.QuestionRepository;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.domain.persistence.CustomRepository;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.member.wish.domain.persistence.WishRepository;
import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.amenity.domain.persistence.AmenityRepository;
import com.moa.moa.api.place.amenity.util.enumerated.AmenityType;
import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.place.liftticket.domain.persistence.LiftTicketRepository;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.domain.persistence.PlaceRepository;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.place.placeamenity.domain.entity.PlaceAmenity;
import com.moa.moa.api.place.placeamenity.domain.persistence.PlaceAmenityRepository;
import com.moa.moa.api.place.slope.domain.entity.Slope;
import com.moa.moa.api.place.slope.domain.persistence.SlopeRepository;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Slf4j
@Profile("!prod & !test")
@RequiredArgsConstructor
@Configuration
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;

    // address.*
    private final AddressRepository addressRepository;

    // category.*
    private final CategoryRepository categoryRepository;

    // cs.*
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // member.*
    private final MemberRepository memberRepository;
    private final CustomRepository customRepository;
    private final WishRepository wishRepository;

    // place.*
    private final AmenityRepository amenityRepository;
    private final LiftTicketRepository liftTicketRepository;
    private final PlaceRepository placeRepository;
    private final PlaceAmenityRepository placeAmenityRepository;
    private final SlopeRepository slopeRepository;

    // shop.*
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final NaverReviewRepository naverReviewRepository;
    private final PlaceShopRepository placeShopRepository;
    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    // time.*
    private final BusinessTimeRepository businessTimeRepository;
    private final OperatingTimeRepository operatingTimeRepository;
    private final SpecificDayRepository specificDayRepository;

    // DataBase Point
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Bean
    @Order(3)
    public ApplicationRunner init() {
        return args -> {
            if (!memberRepository.findAll().isEmpty()) {
                return;
            }
            data();
        };
    }


    public void data() {
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

        // 슬로프 생성
        List<Slope> slopes = createSlope(places);

        // 편의시설 생성
        List<Amenity> amenities = createAmenity();

        // 스키장 편의시설 중간테이블 생성
        createPlaceAmenity(places, amenities);

        // 스키장 리프트권 생성
        List<LiftTicket> liftTickets = createLiftTicket(places);

        // 회원 생성
        List<Member> members = createMember();

        // 문의 & 답변 생성
        createQuestionAndAnswer(members);
        // 렌탈샵 생성
        List<Shop> shops = createShop(category_1, addresses, businessTimes);

        // 찜목록 생성
        createWish(shops, members);

        // 렌탈샵 리뷰 생성
        createReview(shops, members);

        // 스키장 렌탈샵 중간테이블 생성
        createPlaceShop(places, shops);

        // 스키어 생성
        List<Custom> customs = createCustom(members);

        // 렌탈샵 패키지 생성 (Item)
        createItem(shops, liftTickets);

        // 렌탈샵 상세 옵션 생성
        createItemOption(shops);
    }

    // Save DB
    private void createItemOption(List<Shop> shops) {
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(24L).addPrice(BigDecimal.valueOf(10000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(24L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(10000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(7L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(0)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(7L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.LUXURY).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(25000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.PREMIUM).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(35000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(5000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(10000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(10000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(1)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(8L).addPrice(BigDecimal.valueOf(15000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(25000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(35000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.LUXURY).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.PREMIUM).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(40000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(10000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(15000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.SHORT_SKI).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(2)).name(ItemOptionName.INLINE_SKI).used(false).startTime(6L).endTime(9L).addPrice(BigDecimal.valueOf(25000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.LUXURY).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.PREMIUM).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(4L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.SHORT_SKI).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(3)).name(ItemOptionName.INLINE_SKI).used(true).startTime(5L).endTime(13L).addPrice(BigDecimal.valueOf(40000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(5000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(18000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.LUXURY).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.PREMIUM).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(5000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(6L).addPrice(BigDecimal.valueOf(18000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.SHORT_SKI).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(4)).name(ItemOptionName.INLINE_SKI).used(true).startTime(7L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.LUXURY).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(12000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.PREMIUM).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.LUXURY).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(20000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.PREMIUM).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(30000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.SHORT_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(5000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.INLINE_SKI).used(true).startTime(0L).endTime(5L).addPrice(BigDecimal.valueOf(10000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.SHORT_SKI).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(7000L)).build());
        itemOptionRepository.save(ItemOption.builder().shop(shops.get(5)).name(ItemOptionName.INLINE_SKI).used(true).startTime(6L).endTime(13L).addPrice(BigDecimal.valueOf(12000L)).build());
    }
    private void createItem(List<Shop> shops, List<LiftTicket> liftTickets) {
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(52000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(52000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(81000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(61000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(61000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(56000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(56000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(45000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(45000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(57000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(57000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(87000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(67000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(51000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(0)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(51000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(67000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(52000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(61000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(57000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(67000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(1)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(55000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(0)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(55000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트6시간권+장비+의류").price(BigDecimal.valueOf(83000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트6시간권+장비").price(BigDecimal.valueOf(65000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(1)).type(PackageType.LIFT_CLOTHES).name("주중 스마트6시간권+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오전권+장비+의류").price(BigDecimal.valueOf(73000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_EQUIPMENT).name("주중 오전권+장비").price(BigDecimal.valueOf(53000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(2)).type(PackageType.LIFT_CLOTHES).name("주중 오전권+의류").price(BigDecimal.valueOf(53000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 오후권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_EQUIPMENT).name("주중 오후권+장비").price(BigDecimal.valueOf(58000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(3)).type(PackageType.LIFT_CLOTHES).name("주중 오후권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_EQUIPMENT).name("주중 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(4)).type(PackageType.LIFT_CLOTHES).name("주중 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(75000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(59000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(5)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(59000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트6시간권+장비+의류").price(BigDecimal.valueOf(85000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트6시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(6)).type(PackageType.LIFT_CLOTHES).name("주말 스마트6시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오전권+장비+의류").price(BigDecimal.valueOf(83000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_EQUIPMENT).name("주말 오전권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(7)).type(PackageType.LIFT_CLOTHES).name("주말 오전권+의류").price(BigDecimal.valueOf(65000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 오후권+장비+의류").price(BigDecimal.valueOf(89000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_EQUIPMENT).name("주말 오후권+장비").price(BigDecimal.valueOf(69000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(8)).type(PackageType.LIFT_CLOTHES).name("주말 오후권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 야간권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_EQUIPMENT).name("주말 야간권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(2)).liftTicket(liftTickets.get(9)).type(PackageType.LIFT_CLOTHES).name("주말 야간권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(62000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(74000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(106000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(104000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(100000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(62000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(58000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(74000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(70000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(84000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(106000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(104000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(3)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(100000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(60000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(74000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(82000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(60000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(56000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(62000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(74000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(68000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(82000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(80000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(4)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(0L)).used(false).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트3시간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트3시간권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(10)).type(PackageType.LIFT_CLOTHES).name("주중 스마트3시간권+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트4시간권+장비").price(BigDecimal.valueOf(70000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(11)).type(PackageType.LIFT_CLOTHES).name("주중 스마트4시간권+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트5시간권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트5시간권+장비").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(12)).type(PackageType.LIFT_CLOTHES).name("주중 스마트5시간권+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 스마트7시간권+장비+의류").price(BigDecimal.valueOf(86000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_EQUIPMENT).name("주중 스마트7시간권+장비").price(BigDecimal.valueOf(84000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(13)).type(PackageType.LIFT_CLOTHES).name("주중 스마트7시간권+의류").price(BigDecimal.valueOf(80000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주중 종일권+장비+의류").price(BigDecimal.valueOf(108000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_EQUIPMENT).name("주중 종일권+장비").price(BigDecimal.valueOf(106000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(14)).type(PackageType.LIFT_CLOTHES).name("주중 종일권+의류").price(BigDecimal.valueOf(102000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트3시간권+장비+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트3시간권+장비").price(BigDecimal.valueOf(64000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(15)).type(PackageType.LIFT_CLOTHES).name("주말 스마트3시간권+의류").price(BigDecimal.valueOf(60000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트4시간권+장비+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트4시간권+장비").price(BigDecimal.valueOf(70000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(16)).type(PackageType.LIFT_CLOTHES).name("주말 스마트4시간권+의류").price(BigDecimal.valueOf(66000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트5시간권+장비+의류").price(BigDecimal.valueOf(78000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트5시간권+장비").price(BigDecimal.valueOf(76000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(17)).type(PackageType.LIFT_CLOTHES).name("주말 스마트5시간권+의류").price(BigDecimal.valueOf(72000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 스마트7시간권+장비+의류").price(BigDecimal.valueOf(86000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_EQUIPMENT).name("주말 스마트7시간권+장비").price(BigDecimal.valueOf(84000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(18)).type(PackageType.LIFT_CLOTHES).name("주말 스마트7시간권+의류").price(BigDecimal.valueOf(80000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT_CLOTHES).name("주말 종일권+장비+의류").price(BigDecimal.valueOf(108000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_EQUIPMENT).name("주말 종일권+장비").price(BigDecimal.valueOf(102000L)).used(true).build());
        itemRepository.save(Item.builder().shop(shops.get(5)).liftTicket(liftTickets.get(19)).type(PackageType.LIFT_CLOTHES).name("주말 종일권+의류").price(BigDecimal.valueOf(102000L)).used(true).build());
    }
    private void createQuestionAndAnswer(List<Member> members) {
        Question question_1 = questionRepository.save(Question.builder().member(members.get(2)).type(QuestionType.COMMON).title("주중, 주말 구분 어떻게 해야 하나요?").content("주중, 주말 구분 : 주중(월~금), 주말(토~일, 공휴일)\n" + "위와 같은 형식인가요?").status(QuestionStatus.INCOMPLETE).build());
        Question question_2 = questionRepository.save(Question.builder().member(members.get(2)).type(QuestionType.BUSINESS).title("온라인 구매는 네이버에서만 가능한가요?").content("네이버말고 다른 사이트를 이용할 수 있나요?").status(QuestionStatus.COMPLETE).build());
        Question question_3 = questionRepository.save(Question.builder().member(members.get(3)).type(QuestionType.COMMON).title("스키/보드 고급, 프리미엄, 스페셜 차이가 궁금합니다.").content("스키/보드 고급, 프리미엄, 스페셜 차이가 궁금합니다.\n" + "초보자의 경우 어떤 걸 선택해야 할까요?").status(QuestionStatus.COMPLETE).build());
        Question question_4 = questionRepository.save(Question.builder().member(members.get(3)).type(QuestionType.COMMON).title("의류에서 고급과 프리미엄의 차이가 궁금합니다.").content("프리미엄보다 스페셜이 더 좋을 걸까요? 최근에 구매한 의류를 입고 싶을 때 어떤 걸 선택해야 하나요?").status(QuestionStatus.INCOMPLETE).build());
        Question question_5 = questionRepository.save(Question.builder().member(members.get(3)).type(QuestionType.BUSINESS).title("픽업 가능한가요?").content("교통이 불편한 관계로 픽업 해주시나요?").status(QuestionStatus.COMPLETE).build());

        answerRepository.save(Answer.builder().question(question_2).member(members.get(0)).content("청춘스키 온라인 상품은 네이버 스마트 스토어에서만 현재 구매 가능합니다.\n" + "온라인 구매를 원하시면 아래 링크를 클릭해주세요.").build());
        answerRepository.save(Answer.builder().question(question_3).member(members.get(0)).content("고급 장비 : 장비 연식이 3년 이상된 장비로 약간의 사용감이 있습니다. (사용 및 안전상에는 전혀 문제 없습니다.)\n" + "프리미엄 장비 : 장비 연식이 1년된 장비로서 최상 A급 장비입니다. (스노우보드 100% 신상데크입니다.)\n" + "스페셜 장비 : 최신 장비는 초중급, 중상급장용으로 구분되어 있습니다. (고급 유명 브랜드)").build());
        answerRepository.save(Answer.builder().question(question_5).member(members.get(0)).content("렌탈샵에서 스키장까지 차로 2분 거리입니다.\n" + "장비 픽업 시 지산리조트 렌탈샵은 5주차장에서 장비를 수령하시기 때문에 장비수령하는 곳까지 거리가 있으므로 차에 싣고 가시는게 편하며 경차 경우 조수석에 4대까지 실립니다.\n" + "부득이하게 픽업을 요청하실 경우 미리 유선 예약해 주셔야 하며(왕복 픽업비 인원수 상관없이 10,000원입니다.) 대기시간이 발생하실 수 있습니다.\n" + "주말은 픽업이 불가한 점 죄송합니다.").build());
    }
    private void createWish(List<Shop> shops, List<Member> members) {
        wishRepository.save(Wish.builder().shop(shops.get(1)).member(members.get(0)).build());
        wishRepository.save(Wish.builder().shop(shops.get(1)).member(members.get(1)).build());
        wishRepository.save(Wish.builder().shop(shops.get(2)).member(members.get(2)).build());
        wishRepository.save(Wish.builder().shop(shops.get(2)).member(members.get(3)).build());
        wishRepository.save(Wish.builder().shop(shops.get(4)).member(members.get(4)).build());
        wishRepository.save(Wish.builder().shop(shops.get(1)).member(members.get(5)).build());
    }
    private List<Custom> createCustom(List<Member> members) {
        List<Custom> list = new ArrayList<>();
        list.add(customRepository.save(Custom.builder().member(members.get(1)).gender(Gender.MALE).nickname("일남이").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).equipmentType(EquipmentType.SKI).clothesType(ClothesType.STANDARD).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(1)).gender(Gender.MALE).nickname("일친구").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.PREMIUM).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(2)).gender(Gender.MALE).nickname("남친이").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.INLINE_SKI).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(2)).gender(Gender.FEMALE).nickname("여친이").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.LUXURY).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(3)).gender(Gender.FEMALE).nickname("삼남매 첫째").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SNOW_BOARD).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(3)).gender(Gender.MALE).nickname("삼남매 둘째").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).equipmentType(EquipmentType.INLINE_SKI).clothesType(ClothesType.STANDARD).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(3)).gender(Gender.FEMALE).nickname("삼남매 셋째").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.PREMIUM).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(4)).gender(Gender.FEMALE).nickname("시링").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SKI).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(4)).gender(Gender.FEMALE).nickname("사월").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).clothesType(ClothesType.STANDARD).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(5)).gender(Gender.MALE).nickname("오호츠크해").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.LUXURY).build()));
        list.add(customRepository.save(Custom.builder().member(members.get(5)).gender(Gender.FEMALE).nickname("오스트리아").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SHORT_SKI).build()));
        return list;
    }
    private void createReview(List<Shop> shops, List<Member> members) {
        // naver review
        naverReviewRepository.save(NaverReview.builder().shop(shops.get(0)).avgScore(4.5d).totalReview(186L).build());
        naverReviewRepository.save(NaverReview.builder().shop(shops.get(1)).avgScore(4d).totalReview(299L).build());
        naverReviewRepository.save(NaverReview.builder().shop(shops.get(2)).avgScore(3.5d).totalReview(100L).build());
        naverReviewRepository.save(NaverReview.builder().shop(shops.get(3)).avgScore(5d).totalReview(50L).build());
        naverReviewRepository.save(NaverReview.builder().shop(shops.get(4)).avgScore(4.8d).totalReview(177L).build());
        naverReviewRepository.save(NaverReview.builder().shop(shops.get(5)).avgScore(4.3d).totalReview(80L).build());

        // review
        reviewRepository.save(Review.builder().shop(shops.get(0)).member(members.get(1)).score(4d).content("좋아요").build());
        reviewRepository.save(Review.builder().shop(shops.get(0)).member(members.get(2)).score(3d).content("아쉬워요").build());
        reviewRepository.save(Review.builder().shop(shops.get(0)).member(members.get(3)).score(2d).content("별로에요").build());
        reviewRepository.save(Review.builder().shop(shops.get(0)).member(members.get(4)).score(1d).content("개쓰레기 ㅋㅋㅋ").build());
        reviewRepository.save(Review.builder().shop(shops.get(1)).member(members.get(1)).score(3d).content("어중간히 괜찮음 ㅋ").build());
        reviewRepository.save(Review.builder().shop(shops.get(1)).member(members.get(2)).score(3d).content("괜찮음").build());
        reviewRepository.save(Review.builder().shop(shops.get(1)).member(members.get(3)).score(3d).content("나쁘지 않음").build());
        reviewRepository.save(Review.builder().shop(shops.get(1)).member(members.get(4)).score(3d).content("직원이 불친절").build());
        reviewRepository.save(Review.builder().shop(shops.get(1)).member(members.get(5)).score(3d).content("ㅋㅋㅋㅋㅋ").build());
        reviewRepository.save(Review.builder().shop(shops.get(2)).member(members.get(1)).score(3d).content("그저 그럼").build());
        reviewRepository.save(Review.builder().shop(shops.get(2)).member(members.get(2)).score(4d).content("좋음").build());
        reviewRepository.save(Review.builder().shop(shops.get(2)).member(members.get(3)).score(5d).content("아주 좋음").build());
        reviewRepository.save(Review.builder().shop(shops.get(2)).member(members.get(4)).score(1d).content("별로").build());
        reviewRepository.save(Review.builder().shop(shops.get(2)).member(members.get(5)).score(2d).content("ㅠㅠ").build());
        reviewRepository.save(Review.builder().shop(shops.get(3)).member(members.get(1)).score(1d).content("진짜 별로").build());
        reviewRepository.save(Review.builder().shop(shops.get(3)).member(members.get(2)).score(2d).content("가지마세요").build());
        reviewRepository.save(Review.builder().shop(shops.get(3)).member(members.get(3)).score(3d).content("좋습니다").build());
        reviewRepository.save(Review.builder().shop(shops.get(3)).member(members.get(4)).score(4d).content("추천합니다").build());
        reviewRepository.save(Review.builder().shop(shops.get(4)).member(members.get(2)).score(3d).content("그저 그럼").build());
        reviewRepository.save(Review.builder().shop(shops.get(4)).member(members.get(3)).score(3d).content("아쉬워요").build());
        reviewRepository.save(Review.builder().shop(shops.get(4)).member(members.get(4)).score(5d).content("진짜 좋음").build());
        reviewRepository.save(Review.builder().shop(shops.get(4)).member(members.get(5)).score(5d).content("꼭 가세요").build());
        reviewRepository.save(Review.builder().shop(shops.get(5)).member(members.get(1)).score(3d).content("보통").build());
        reviewRepository.save(Review.builder().shop(shops.get(5)).member(members.get(2)).score(4d).content("좋아용").build());
        reviewRepository.save(Review.builder().shop(shops.get(5)).member(members.get(3)).score(2d).content("불친절함").build());
        reviewRepository.save(Review.builder().shop(shops.get(5)).member(members.get(4)).score(1d).content("장비가 별로임").build());
        reviewRepository.save(Review.builder().shop(shops.get(5)).member(members.get(5)).score(5d).content("짱!!").build());
    }
    private List<Member> createMember() {
        List<Member> list = new ArrayList<>();
        list.add(memberRepository.save(Member.builder().email("admin@moa.com").nickname("admin").role(MemberRole.ADMIN).build()));
        list.add(memberRepository.save(Member.builder().email("one@moa.com").nickname("one").role(MemberRole.MEMBER).build()));
        list.add(memberRepository.save(Member.builder().email("two@moa.com").nickname("two").role(MemberRole.MEMBER).build()));
        list.add(memberRepository.save(Member.builder().email("three@moa.com").nickname("three").role(MemberRole.MEMBER).build()));
        list.add(memberRepository.save(Member.builder().email("four@moa.com").nickname("four").role(MemberRole.MEMBER).build()));
        list.add(memberRepository.save(Member.builder().email("five@moa.com").nickname("five").role(MemberRole.MEMBER).build()));
        return list;
    }
    private void createPlaceShop(List<Place> places, List<Shop> shops) {
        placeShopRepository.save(PlaceShop.builder().place(places.get(0)).shop(shops.get(0)).build());
        placeShopRepository.save(PlaceShop.builder().place(places.get(0)).shop(shops.get(1)).build());
        placeShopRepository.save(PlaceShop.builder().place(places.get(0)).shop(shops.get(2)).build());
        placeShopRepository.save(PlaceShop.builder().place(places.get(1)).shop(shops.get(3)).build());
        placeShopRepository.save(PlaceShop.builder().place(places.get(1)).shop(shops.get(4)).build());
        placeShopRepository.save(PlaceShop.builder().place(places.get(1)).shop(shops.get(5)).build());
    }
    private List<Shop> createShop(Category category, List<Address> addresses, List<BusinessTime> times) {
        List<Shop> list = new ArrayList<>();
        // shop
        list.add(shopRepository.save(Shop.builder().category(category).address(addresses.get(1)).businessTime(times.get(1)).name("찐렌탈샵").pickUp(true).url("https://smartstore.naver.com/jjinrental/products/6052896905?nl-au=675e2f12d95a4dc9a11c0aafb7bc6cba&NaPm=ct%3Dlzikkp60%7Cci%3D67a24e6eb4e2ddb3b7a4acb882fa1ffd44935b00%7Ctr%3Dslsl%7Csn%3D4902315%7Chk%3Deae6b25f20daa67df1450ce45b9134cf59eb2bb9").build()));
        list.add(shopRepository.save(Shop.builder().category(category).address(addresses.get(2)).businessTime(times.get(2)).name("아지트").pickUp(true).url("https://smartstore.naver.com/rentalshop1/products/6117544378?nl-au=9e070d7e195341e699c36096c861ab13&NaPm=ct%3Dlziltmg8%7Cci%3D0d4c31cf63842f0accf616231028952db4b1b241%7Ctr%3Dslsl%7Csn%3D5126374%7Chk%3D73c670109b1457dee566270729957b85127e0128").build()));
        list.add(shopRepository.save(Shop.builder().category(category).address(addresses.get(5)).businessTime(times.get(5)).name("인생렌탈샵").pickUp(false).url("https://smartstore.naver.com/dgshop/products/9614236927").build()));
        list.add(shopRepository.save(Shop.builder().category(category).address(addresses.get(6)).businessTime(times.get(6)).name("월남스키").pickUp(true).url("https://smartstore.naver.com/wnskishop/products/5314182831").build()));
        list.add(shopRepository.save(Shop.builder().category(category).address(addresses.get(7)).businessTime(times.get(7)).name("눈의나라").pickUp(false).url("https://smartstore.naver.com/noonnara/products/5323804307").build()));
        list.add(shopRepository.save(Shop.builder().category(category).address(addresses.get(8)).businessTime(times.get(8)).name("스노우블루").pickUp(true).url("https://smartstore.naver.com/snowblue1/products/9727997372").build()));
        return list;
    }
    private List<LiftTicket> createLiftTicket(List<Place> places) {
        List<LiftTicket> list = new ArrayList<>();
        // place 1
        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("오전권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(12,0)).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("오후권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(14,0)).endTime(LocalTime.of(18,0)).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_DAY).name("야간권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(22,0)).endTime(LocalTime.of(2,0)).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("오전권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(12,0)).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("오후권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(14,0)).endTime(LocalTime.of(18,0)).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(0)).status(LiftTicketStatus.WEEK_END).name("야간권").ticketType(
                LiftTicketType.TIME).hours(4L).startTime(LocalTime.of(22,0)).endTime(LocalTime.of(2,0)).build()));

        // place 2
        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트3시간권").ticketType(
                LiftTicketType.SMART).hours(3L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트5시간권").ticketType(
                LiftTicketType.SMART).hours(5L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("스마트7시간권").ticketType(
                LiftTicketType.SMART).hours(7L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_DAY).name("종일권").ticketType(
                LiftTicketType.TIME).hours(13L).startTime(LocalTime.of(9,0)).endTime(LocalTime.of(22,0)).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트3시간권").ticketType(
                LiftTicketType.SMART).hours(3L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트4시간권").ticketType(
                LiftTicketType.SMART).hours(4L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트5시간권").ticketType(
                LiftTicketType.SMART).hours(5L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("스마트7시간권").ticketType(
                LiftTicketType.SMART).hours(7L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(1)).status(LiftTicketStatus.WEEK_END).name("종일권").ticketType(
                LiftTicketType.TIME).hours(13L).startTime(LocalTime.of(9,0)).endTime(LocalTime.of(22,0)).build()));

        // place 3
        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(2)).status(LiftTicketStatus.WEEK_DAY).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build()));

        list.add(liftTicketRepository.save(LiftTicket.builder().place(places.get(2)).status(LiftTicketStatus.WEEK_END).name("스마트6시간권").ticketType(
                LiftTicketType.SMART).hours(6L).build()));
        return list;
    }
    private void createPlaceAmenity(List<Place> places, List<Amenity> amenities) {
        List<PlaceAmenity> list = new ArrayList<>();
        for (Place place : places) {
            for (Amenity amenity : amenities) {
                list.add(PlaceAmenity.builder().place(place).amenity(amenity).used(true).build());
            }
        }

        list.get(9).modUse(false);
        list.get(14).modUse(false);
        list.get(15).modUse(false);
        list.get(16).modUse(false);
        list.get(17).modUse(false);
        list.get(18).modUse(false);
        list.get(19).modUse(false);

        placeAmenityRepository.saveAll(list);
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
    private List<Slope> createSlope(List<Place> places) {
        List<Slope> list = new ArrayList<>();
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("발라드").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("블루스").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("레게").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("째즈").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("클래식").level(SlopeLevel.LEVEL_3).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("힙합").level(SlopeLevel.LEVEL_3).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("펑키").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("테크노").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(0)).name("락").level(SlopeLevel.LEVEL_5).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("제우스1").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("제우스2").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("제우스3").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("빅토리아1").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("빅토리아2").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("헤라1").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("헤라2").level(SlopeLevel.LEVEL_3).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("헤라3").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("아폴리1").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("아폴로3").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("아폴로4").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("아폴로6").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("아테나2").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(1)).name("아테나3").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("래빗").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("팬더").level(SlopeLevel.LEVEL_1).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("드래곤").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("호스").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("퓨마").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("디어").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("제브라").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("페가수스").level(SlopeLevel.LEVEL_2).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("래퍼드").level(SlopeLevel.LEVEL_4).build()));
        list.add(slopeRepository.save(Slope.builder().place(places.get(2)).name("재규어").level(SlopeLevel.LEVEL_4).build()));
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
    private List<Amenity> createAmenity() {
        List<Amenity> list = new ArrayList<>();
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.HOTEL).build()));
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.INNER_RENTAL_SHOP).build()));
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.SHUTTLE_BUS).build()));
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.CONVENIENCE_STORE).build()));
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.FOOD_COURT).build()));
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.GIFT_SHOP).build()));
        list.add(amenityRepository.save(Amenity.builder().type(AmenityType.INFIRMARY).build()));
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

    private SpecificDay createSpecific(BusinessTime businessTime, SpecificDayType type, String reason, LocalDate date, LocalTime open, LocalTime close) {
        return SpecificDay.builder()
                .businessTime(businessTime)
                .status(type)
                .reason(reason)
                .date(date)
                .openTime(open)
                .closeTime(close)
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
