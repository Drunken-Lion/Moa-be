package com.moa.moa.api.place.liftticket.domain.entity;

import com.moa.moa.api.place.place.domain.entity.Place;

import java.time.LocalTime;

public class LiftTicket {
    private Place place;
    private String status;
    private String name;
    private String ticketType;
    private Long hours;
    private LocalTime open;
    private LocalTime close;
}
