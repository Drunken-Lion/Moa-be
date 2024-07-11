package com.moa.moa.api.review.domain.entity;

import com.moa.moa.api.shop.domain.entity.Shop;

public class Review {
    private Shop shop;
    private Member member;
    private Double score;
    private String content;
}
