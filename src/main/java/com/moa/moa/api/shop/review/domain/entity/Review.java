package com.moa.moa.api.shop.review.domain.entity;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.shop.shop.domain.entity.Shop;

public class Review {
    private Shop shop;
    private Member member;
    private Double score;
    private String content;
}
