package com.moa.moa.api.shop.shop.domain.entity;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.member.member.domain.entity.Member;

public class Shop {
    private Member member;
    private Category category;
    private Address address;
    private String name;
    private Boolean pickUp;
    private String url;
}
