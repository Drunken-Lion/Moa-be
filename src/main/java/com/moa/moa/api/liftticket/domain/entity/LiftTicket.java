package com.moa.moa.api.liftticket.domain.entity;

import java.time.LocalTime;

public class LiftTicket {
    private String status;
    private String name;
    private String ticketType;
    private Long hours;
    private LocalTime open;
    private LocalTime close;
    private Place place;
}
