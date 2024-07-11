package com.moa.moa.api.packages.domain.entity;

import com.moa.moa.api.liftticket.domain.entity.LiftTicket;
import com.moa.moa.api.shop.domain.entity.Shop;

import java.math.BigDecimal;

public class Package {
    private Shop shop;
    private LiftTicket liftTicketId;
    private String type;
    private String name;
    private BigDecimal price;
    private Boolean use;
}
