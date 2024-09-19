package com.moa.moa.api.member.wish.domain.persistence;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.moa.moa.global.util.pagination.CursorPaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

import static com.moa.moa.api.address.address.domain.entity.QAddress.address1;
import static com.moa.moa.api.category.category.domain.entity.QCategory.category;
import static com.moa.moa.api.member.member.domain.entity.QMember.member;
import static com.moa.moa.api.member.wish.domain.entity.QWish.wish;
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
public class WishDslRepositoryImpl implements WishDslRepository {
    private final JPAQueryFactory queryFactory;
    private final CursorPaginationUtil<Wish> cursorPaginationUtil;

    @Override
    public Optional<Wish> findWishByShopAndMember(Shop shop, Member member) {
        Wish wishOne = queryFactory.selectFrom(wish)
                .where(wish.shop.eq(shop)
                        .and(wish.member.eq(member))
                        .and(wish.deletedAt.isNull()))
                .fetchFirst();

        return Optional.ofNullable(wishOne);
    }

    @Override
    public Slice<Wish> findAllWishByMember(Member userMember, Pageable pageable) {
        List<Wish> wishes = queryFactory
                .selectFrom(wish)
                .leftJoin(wish.shop, shop).fetchJoin()
                .leftJoin(wish.shop.member, member).fetchJoin()
                .leftJoin(wish.shop.category, category).fetchJoin()
                .leftJoin(wish.shop.address, address1).fetchJoin()
                .leftJoin(wish.shop.businessTime, businessTime).fetchJoin()
                .where(wish.member.eq(userMember)
                        .and(cursorPaginationUtil.gtCursorId(wish.id, pageable.getPageNumber()))
                        .and(wish.deletedAt.isNull())
                        .and(shop.deletedAt.isNull())
                        .and(shop.member.deletedAt.isNull())
                        .and(shop.category.deletedAt.isNull())
                        .and(shop.address.deletedAt.isNull())
                        .and(shop.businessTime.deletedAt.isNull())
                )
                .orderBy(wish.id.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        for (Wish wish : wishes) {
            List<OperatingTime> operatingTimes = queryFactory.selectFrom(operatingTime)
                    .where(operatingTime.businessTime.eq(wish.getShop().getBusinessTime())
                            .and(operatingTime.deletedAt.isNull()))
                    .fetch();
            wish.getShop().getBusinessTime().addOperatingTimes(operatingTimes);

            List<SpecificDay> specificDays = queryFactory.selectFrom(specificDay)
                    .where(specificDay.businessTime.eq(wish.getShop().getBusinessTime())
                            .and(specificDay.deletedAt.isNull()))
                    .fetch();
            wish.getShop().getBusinessTime().addSpecificDays(specificDays);

            List<PlaceShop> placeShops = queryFactory.selectFrom(placeShop)
                    .where(placeShop.shop.eq(wish.getShop())
                            .and(placeShop.deletedAt.isNull())
                            .and(placeShop.place.deletedAt.isNull()))
                    .fetch();
            wish.getShop().addPlaceShops(placeShops);

            List<Review> reviews = queryFactory.selectFrom(review)
                    .where(review.shop.eq(wish.getShop())
                            .and(review.deletedAt.isNull()))
                    .fetch();
            wish.getShop().addReviews(reviews);

            NaverReview naverReviewOne = queryFactory.selectFrom(naverReview)
                    .where(naverReview.shop.eq(wish.getShop())
                            .and(naverReview.deletedAt.isNull()))
                    .fetchFirst();
            wish.getShop().addNaverReview(naverReviewOne);

            List<Item> items = queryFactory.selectFrom(item)
                    .where(item.shop.eq(wish.getShop())
                            .and(item.deletedAt.isNull()))
                    .fetch();
            wish.getShop().addItems(items);

            List<ItemOption> itemOptions = queryFactory.selectFrom(itemOption)
                    .where(itemOption.shop.eq(wish.getShop())
                            .and(itemOption.deletedAt.isNull()))
                    .fetch();
            wish.getShop().addItemOptions(itemOptions);
        }

        return cursorPaginationUtil.checkLastPage(wishes, pageable);
    }

    @Override
    public Optional<Wish> findWishById(Long id) {
        Wish wishOne = queryFactory.selectFrom(wish)
                .where(wish.id.eq(id)
                        .and(wish.deletedAt.isNull()))
                .fetchOne();

        return Optional.ofNullable(wishOne);
    }
}
