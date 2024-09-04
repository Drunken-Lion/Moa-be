package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.moa.moa.api.address.address.domain.entity.QAddress.address1;
import static com.moa.moa.api.shop.item.domain.entity.QItem.item;
import static com.moa.moa.api.shop.naverreview.domain.entity.QNaverReview.naverReview;
import static com.moa.moa.api.shop.placeshop.domain.entity.QPlaceShop.placeShop;
import static com.moa.moa.api.shop.review.domain.entity.QReview.review;
import static com.moa.moa.api.shop.shop.domain.entity.QShop.shop;

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
                .leftJoin(shop.address, address1).fetchJoin()
                .where(inPolygon
                        .and(shop.deletedAt.isNull())
                        .and(address1.deletedAt.isNull())
                )
                .orderBy(shop.id.asc())
                .fetch();

        for (Shop shop : shops) {
            List<Item> items = queryFactory.selectFrom(item)
                    .where(item.shop.eq(shop)
                            .and(item.deletedAt.isNull()))
                    .fetch();
            shop.addItems(items);

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
        }

        return shops;
    }
}
