package com.moa.moa.api.place.domain.entity;

import com.moa.moa.api.address.domain.entity.Address;
import com.moa.moa.api.category.domain.entity.Category;

import java.time.LocalDate;

public class Place {
    private Category category;
    private Address address;
    private String name;
    private LocalDate open;
    private LocalDate close;
    private String recLevel;
}
