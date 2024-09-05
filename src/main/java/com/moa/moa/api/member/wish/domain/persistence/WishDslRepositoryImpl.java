package com.moa.moa.api.member.wish.domain.persistence;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.moa.moa.api.member.wish.domain.entity.QWish.wish;

@RequiredArgsConstructor
public class WishDslRepositoryImpl implements WishDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Wish> findWishByShopAndMember(Shop shop, Member member) {
        Wish wishOne = queryFactory.selectFrom(wish)
                .where(wish.shop.eq(shop)
                        .and(wish.member.eq(member))
                        .and(wish.deletedAt.isNull()))
                .fetchFirst();

        return Optional.ofNullable(wishOne);
    }

}
