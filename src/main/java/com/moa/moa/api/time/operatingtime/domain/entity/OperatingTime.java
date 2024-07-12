package com.moa.moa.api.time.operatingtime.domain.entity;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.shop.domain.entity.Shop;

import java.time.LocalTime;

public class OperatingTime {
    private Place place;
    private Shop shop;
    private String status;
    private String day;
    private LocalTime open;
    private LocalTime close;
}
