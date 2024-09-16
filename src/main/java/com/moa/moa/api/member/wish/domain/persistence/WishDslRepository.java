package com.moa.moa.api.member.wish.domain.persistence;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface WishDslRepository {
    Optional<Wish> findWishByShopAndMember(Shop shop, Member member);

    Slice<Wish> findAllWishByMember(Member member, Pageable pageable);
}
