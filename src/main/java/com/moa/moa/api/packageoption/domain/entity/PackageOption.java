package com.moa.moa.api.packageoption.domain.entity;

import com.moa.moa.api.packages.domain.entity.Package;

import java.math.BigDecimal;

public class PackageOption {
    private Package packages;
    private String name;
    private Boolean use;
    private String extraTime;
    private BigDecimal extraTimePrice;
    private BigDecimal addPrice;
}
