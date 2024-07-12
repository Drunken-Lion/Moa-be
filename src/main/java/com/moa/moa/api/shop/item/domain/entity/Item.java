package com.moa.moa.api.shop.item.domain.entity;

import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.shop.shop.domain.entity.Shop;

import java.math.BigDecimal;

public class Item {
    private Shop shop;
    private LiftTicket liftTicketId;
    private String type;
    private String name;
    private BigDecimal price;
    private Boolean use;
}
