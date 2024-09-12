package com.moa.moa.api.member.wish.domain;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.member.wish.domain.persistence.WishRepository;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WishProcessor {
    private final WishRepository wishRepository;

    public Optional<Wish> findWishByShopAndMember(Shop shop, Member member) {
        return wishRepository.findWishByShopAndMember(shop, member);
    }

    public Slice<Wish> findAllWishByMember(Member member, Pageable pageable) {
        return wishRepository.findAllWishByMember(member, pageable);
    }

    public Integer countMyWish(Member member) {
        return wishRepository.countByMember(member);
    }
}
