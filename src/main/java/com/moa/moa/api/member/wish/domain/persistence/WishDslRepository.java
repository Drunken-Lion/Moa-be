package com.moa.moa.api.member.wish.domain.persistence;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.shop.shop.domain.entity.Shop;

import java.util.Optional;

public interface WishDslRepository {
    Optional<Wish> findWishByShopAndMember(Shop shop, Member member);
}
