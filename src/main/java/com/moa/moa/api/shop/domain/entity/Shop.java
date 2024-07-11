package com.moa.moa.api.shop.domain.entity;

import com.moa.moa.api.address.domain.entity.Address;
import com.moa.moa.api.category.domain.entity.Category;
import com.moa.moa.api.member.domain.entity.Member;

public class Shop {
    private Member member;
    private Category category;
    private Address address;
    private String name;
    private Boolean pickUp;
    private String url;
}
