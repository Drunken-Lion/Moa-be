package com.moa.moa.api.place.place.domain.entity;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;

import java.time.LocalDate;

public class Place {
    private Category category;
    private Address address;
    private String name;
    private LocalDate open;
    private LocalDate close;
    private String recLevel;
}
