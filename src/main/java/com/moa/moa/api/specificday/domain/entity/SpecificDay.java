package com.moa.moa.api.specificday.domain.entity;

import com.moa.moa.api.place.domain.entity.Place;
import com.moa.moa.api.shop.domain.entity.Shop;

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
