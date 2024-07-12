package com.moa.moa.api.shop.itemoption.domain.entity;

import com.moa.moa.api.shop.item.domain.entity.Item;

import java.math.BigDecimal;

public class ItemOption {
    private Item item;
    private String name;
    private Boolean use;
    private String extraTime;
    private BigDecimal extraTimePrice;
    private BigDecimal addPrice;
}
