package com.moa.moa.api.time.specificday.domain.entity;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.shop.domain.entity.Shop;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpecificDay {
    private Place place;
    private Shop shop;
    private String status;
    private String reason;
    private LocalDate date;
    private LocalTime open;
    private LocalTime close;
}
