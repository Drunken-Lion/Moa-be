package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
}
