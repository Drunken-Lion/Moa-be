package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.member.custom.domain.dto.FindLowPriceCustomDto;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.domain.dto.FindLowPriceShopDto;
import com.moa.moa.api.shop.shop.domain.dto.FindShopBusinessIdDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

import static com.moa.moa.api.address.address.domain.entity.QAddress.address1;
import static com.moa.moa.api.category.category.domain.entity.QCategory.category;
import static com.moa.moa.api.member.member.domain.entity.QMember.member;
import static com.moa.moa.api.shop.item.domain.entity.QItem.item;
import static com.moa.moa.api.shop.itemoption.domain.entity.QItemOption.itemOption;
import static com.moa.moa.api.shop.naverreview.domain.entity.QNaverReview.naverReview;
import static com.moa.moa.api.shop.placeshop.domain.entity.QPlaceShop.placeShop;
import static com.moa.moa.api.shop.review.domain.entity.QReview.review;
import static com.moa.moa.api.shop.shop.domain.entity.QShop.shop;
import static com.moa.moa.api.time.businesstime.domain.entity.QBusinessTime.businessTime;
import static com.moa.moa.api.time.operatingtime.domain.entity.QOperatingTime.operatingTime;
import static com.moa.moa.api.time.specificday.domain.entity.QSpecificDay.specificDay;

@RequiredArgsConstructor
public class ShopDslRepositoryImpl implements ShopDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Shop> findAllShopWithinRange(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY) {
        String polygon = String.format("POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                leftTopX, leftTopY,   // 왼쪽 위
                rightBottomX, leftTopY, // 오른쪽 위
                rightBottomX, rightBottomY, // 오른쪽 아래
                leftTopX, rightBottomY, // 왼쪽 아래
                leftTopX, leftTopY); // 다시 왼쪽 위로 돌아오기

        BooleanExpression inPolygon = Expressions.booleanTemplate("ST_Contains(ST_PolygonFromText({0}), {1})", polygon, address1.location);

        List<Shop> shops = queryFactory.selectFrom(shop)
                .leftJoin(shop.member, member).fetchJoin()
                .leftJoin(shop.category, category).fetchJoin()
                .leftJoin(shop.address, address1).fetchJoin()
                .leftJoin(shop.businessTime, businessTime).fetchJoin()
                .where(inPolygon
                        .and(shop.deletedAt.isNull())
                        .and(member.deletedAt.isNull())
                        .and(category.deletedAt.isNull())
                        .and(address1.deletedAt.isNull())
                        .and(businessTime.deletedAt.isNull())
                )
                .orderBy(shop.id.asc())
                .fetch();

        for (Shop shop : shops) {
            List<OperatingTime> operatingTimes = queryFactory.selectFrom(operatingTime)
                    .where(operatingTime.businessTime.eq(shop.getBusinessTime())
                            .and(operatingTime.deletedAt.isNull()))
                    .fetch();
            shop.getBusinessTime().addOperatingTimes(operatingTimes);

            List<SpecificDay> specificDays = queryFactory.selectFrom(specificDay)
                    .where(specificDay.businessTime.eq(shop.getBusinessTime())
                            .and(specificDay.deletedAt.isNull()))
                    .fetch();
            shop.getBusinessTime().addSpecificDays(specificDays);

            List<PlaceShop> placeShops = queryFactory.selectFrom(placeShop)
                    .where(placeShop.shop.eq(shop)
                            .and(placeShop.deletedAt.isNull())
                            .and(placeShop.place.deletedAt.isNull()))
                    .fetch();
            shop.addPlaceShops(placeShops);

            List<Review> reviews = queryFactory.selectFrom(review)
                    .where(review.shop.eq(shop)
                            .and(review.deletedAt.isNull()))
                    .fetch();
            shop.addReviews(reviews);

            NaverReview naverReviewOne = queryFactory.selectFrom(naverReview)
                    .where(naverReview.shop.eq(shop)
                            .and(naverReview.deletedAt.isNull()))
                    .fetchFirst();
            shop.addNaverReview(naverReviewOne);

            List<Item> items = queryFactory.selectFrom(item)
                    .where(item.shop.eq(shop)
                            .and(item.deletedAt.isNull()))
                    .fetch();
            shop.addItems(items);

            List<ItemOption> itemOptions = queryFactory.selectFrom(itemOption)
                    .where(itemOption.shop.eq(shop)
                            .and(itemOption.deletedAt.isNull()))
                    .fetch();
            shop.addItemOptions(itemOptions);
        }

        return shops;
    }

    @Override
    public Optional<Shop> findShopById(Long id) {
        Shop shopOne = queryFactory.selectFrom(shop)
                .leftJoin(shop.member, member).fetchJoin()
                .leftJoin(shop.category, category).fetchJoin()
                .leftJoin(shop.address, address1).fetchJoin()
                .leftJoin(shop.businessTime, businessTime).fetchJoin()
                .where(shop.id.eq(id)
                        .and(shop.deletedAt.isNull())
                        .and(member.deletedAt.isNull())
                        .and(category.deletedAt.isNull())
                        .and(address1.deletedAt.isNull())
                        .and(businessTime.deletedAt.isNull())
                )
                .orderBy(shop.id.asc())
                .fetchOne();

        if (shopOne != null) {
            List<OperatingTime> operatingTimes = queryFactory.selectFrom(operatingTime)
                    .where(operatingTime.businessTime.eq(shopOne.getBusinessTime())
                            .and(operatingTime.deletedAt.isNull()))
                    .fetch();
            shopOne.getBusinessTime().addOperatingTimes(operatingTimes);

            List<SpecificDay> specificDays = queryFactory.selectFrom(specificDay)
                    .where(specificDay.businessTime.eq(shopOne.getBusinessTime())
                            .and(specificDay.deletedAt.isNull()))
                    .fetch();
            shopOne.getBusinessTime().addSpecificDays(specificDays);

            List<PlaceShop> placeShops = queryFactory.selectFrom(placeShop)
                    .where(placeShop.shop.eq(shopOne)
                            .and(placeShop.deletedAt.isNull())
                            .and(placeShop.place.deletedAt.isNull()))
                    .fetch();
            shopOne.addPlaceShops(placeShops);

            List<Review> reviews = queryFactory.selectFrom(review)
                    .where(review.shop.eq(shopOne)
                            .and(review.deletedAt.isNull()))
                    .fetch();
            shopOne.addReviews(reviews);

            NaverReview naverReviewOne = queryFactory.selectFrom(naverReview)
                    .where(naverReview.shop.eq(shopOne)
                            .and(naverReview.deletedAt.isNull()))
                    .fetchFirst();
            shopOne.addNaverReview(naverReviewOne);

            List<Item> items = queryFactory.selectFrom(item)
                    .where(item.shop.eq(shopOne)
                            .and(item.deletedAt.isNull()))
                    .fetch();
            shopOne.addItems(items);

            List<ItemOption> itemOptions = queryFactory.selectFrom(itemOption)
                    .where(itemOption.shop.eq(shopOne)
                            .and(itemOption.deletedAt.isNull()))
                    .fetch();
            shopOne.addItemOptions(itemOptions);
        }

        return Optional.ofNullable(shopOne);
    }

    @Override
    public Optional<FindLowPriceShopDto> findShopWithCustomForSearch(Long shopId,
                                                           List<FindLowPriceCustomDto> customs,
                                                           Boolean pickUp) {
        BooleanBuilder deletedAtIsNullBuilder = new BooleanBuilder();
        deletedAtIsNullBuilder.and(shop.deletedAt.isNull());
        deletedAtIsNullBuilder.and(naverReview.deletedAt.isNull());

        // select 동적으로 생성
        List<Expression<?>> selectFields = new ArrayList<>();

        // 기본 필드 추가
        selectFields.add(shop.id.as("shopId"));
        selectFields.add(shop.name);
        selectFields.add(shop.pickUp);
        selectFields.add(shop.url.as("storeUrl"));

        // 리뷰 관련 필드 추가
        selectFields.add(getReviewAvgScore());
        selectFields.add(getReviewTotalCount());
        selectFields.add(naverReview.avgScore.as("nrAvgScore"));
        selectFields.add(naverReview.totalReview.as("nrTotalCount"));

        // customs 별 item, itemOption 조회 서브쿼리
        for (FindLowPriceCustomDto customDto : customs) {
            JPAQuery<BigDecimal> customPriceQuery = getCustomPrice(shopId, customDto.getItemName(),
                                                                    customDto.getItemOptionNames(), customDto.getLiftTime());

            Expression<BigDecimal> customPrice = ExpressionUtils.as(customPriceQuery, customDto.getNickname());

            selectFields.add(customPrice);
        }

        // 메인 쿼리
        Optional<Tuple> tuple = Optional.ofNullable(queryFactory.select(selectFields.toArray(new Expression<?>[0]))
                .from(shop)
                .leftJoin(naverReview).on(shop.id.eq(naverReview.shop.id))
                .where(shop.id.eq(shopId)
                        .and(shop.pickUp.eq(pickUp))
                        .and(deletedAtIsNullBuilder))
                .fetchOne());

        // tuple에 값이 없는 경우
        if (tuple.isEmpty()) {
            return Optional.empty();
        }

        // tuple에 있는 값을 매칭 시키기 위해 배열로 변경
        Object[] array = tuple.get().toArray();

        // Object 배열을 String 배열로 변환
        String[] customArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                customArray[i] = array[i].toString(); // Object를 String으로 변환
            } else {
                customArray[i] = null;
            }
        }

        // customs 닉네임과 price 매칭
        List<BigDecimal> customPrices = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO; // customs 들의 price 총합
        for (int i = 8; i < customArray.length; i++) { // 동적인 값인 customs 은 배열 8번째 부터 시작
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(customArray[i]));
            customPrices.add(price);
            totalPrice = totalPrice.add(price);
        }

        FindLowPriceShopDto findLowPriceShopDto = FindLowPriceShopDto.builder()
                .shopId(Long.parseLong(customArray[0]))
                .name(customArray[1])
                .pickUp(Boolean.valueOf(customArray[2]))
                .storeUrl(customArray[3])
                .customPrices(customPrices)
                .totalPrice(totalPrice)
                .avgScore(Double.valueOf(customArray[4]))
                .totalCount(Long.parseLong(customArray[5]))
                .nrAvgScore(Double.valueOf(customArray[6]))
                .nrTotalCount(Long.parseLong(customArray[7]))
                .build();

        return Optional.ofNullable(findLowPriceShopDto);
    }

    // 샵의 모아 리뷰 평점
    private JPAQuery<Double> getReviewAvgScore() {
        BooleanBuilder deletedAtIsNullBuilder = new BooleanBuilder();
        deletedAtIsNullBuilder.and(review.deletedAt.isNull());

        return queryFactory
                .select(review.score.avg())
                .from(review)
                .where(review.shop.id.eq(shop.id)
                        .and(deletedAtIsNullBuilder))
                .groupBy(review.shop.id);
    }

    // 샵의 모아 리뷰 개수
    private JPAQuery<Long> getReviewTotalCount() {
        BooleanBuilder deletedAtIsNullBuilder = new BooleanBuilder();
        deletedAtIsNullBuilder.and(review.deletedAt.isNull());

        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.shop.id.eq(shop.id)
                        .and(deletedAtIsNullBuilder))
                .groupBy(review.shop.id);
    }

    private JPAQuery<BigDecimal> getCustomPrice(Long shopId, String itemName, List<ItemOptionName> itemOptionNames, String liftTime) {
        BooleanBuilder deletedAtIsNullBuilder = new BooleanBuilder();
        deletedAtIsNullBuilder.and(item.deletedAt.isNull());
        deletedAtIsNullBuilder.and(itemOption.deletedAt.isNull());

        // itemOption 없이 기본 조건일 경우
        BooleanBuilder includeItemOptionNames = new BooleanBuilder();
        if (itemOptionNames != null && !itemOptionNames.isEmpty()) {
            includeItemOptionNames.and(itemOption.name.in(itemOptionNames));
            includeItemOptionNames.and(itemOption.startTime.loe(Integer.parseInt(liftTime)));
            includeItemOptionNames.and(itemOption.endTime.goe(Integer.parseInt(liftTime)));
        }

        return queryFactory
                .select(selectItemPriceOrItemOption(itemOptionNames))
                .from(item)
                .leftJoin(itemOption).on(item.shop.id.eq(itemOption.shop.id))
                .where(item.shop.id.eq(shopId)
                        .and(item.name.eq(itemName))
                        .and(includeItemOptionNames)
                        .and(deletedAtIsNullBuilder))
                .groupBy(item.shop.id, item.price);
    }

    private NumberExpression<BigDecimal> selectItemPriceOrItemOption(List<ItemOptionName> itemOptionNames) {
        if (itemOptionNames == null || itemOptionNames.isEmpty()) {
            return item.price; // itemOption 없이 기본 조건일 경우
        }

        return item.price.add(itemOption.addPrice.sum()); // 조건 추가
    }

    // shop에 맞는 business_time_id 가져오기
    @Override
    public Optional<Map<Long, Long>> findBusinessTimeIdOfShops(List<Long> shopIds) {
        BooleanBuilder deletedAtIsNullBuilder = new BooleanBuilder();
        deletedAtIsNullBuilder.and(shop.deletedAt.isNull());

        List<FindShopBusinessIdDto> findShopBusinessIds = queryFactory.select(Projections.constructor(FindShopBusinessIdDto.class,
                        shop.id,
                        shop.businessTime.id))
                .from(shop)
                .where(shop.id.in(shopIds)
                        .and(deletedAtIsNullBuilder))
                .fetch();

        if (findShopBusinessIds.isEmpty()) return Optional.empty();

        Map<Long, Long> shopBusinessIds = new HashMap<>();
        for (FindShopBusinessIdDto findShopBusinessIdDto : findShopBusinessIds) {
            shopBusinessIds.put(findShopBusinessIdDto.getShopId(), findShopBusinessIdDto.getBusinessId());
        }

        return Optional.of(shopBusinessIds);
    }

    // shopId로 memberId 조회
    @Override
    public Optional<Long> findMemberIdOfShopById(Long shopId) {
        BooleanBuilder deletedAtIsNullBuilder = new BooleanBuilder();
        deletedAtIsNullBuilder.and(shop.deletedAt.isNull());

        Long memberId = queryFactory.select(shop.member.id)
                .from(shop)
                .where(shop.id.eq(shopId)
                        .and(deletedAtIsNullBuilder))
                .fetchOne();

        return Optional.ofNullable(memberId);
    }
}
