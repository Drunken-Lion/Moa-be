package com.moa.moa.api.member.custom.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum PackageType implements CommonEnum {
    /**
    * @see "리프트+장비+의류"
    */
    LIFT_EQUIPMENT_CLOTHES("리프트+장비+의류", 1),

    /**
    * @see "리프트+장비"
    */
    LIFT_EQUIPMENT("리프트+장비", 2),

    /**
    * @see "리프트+의류"
    */
    LIFT_CLOTHES("리프트+의류", 3),
    ;

    private final String desc;
    private final int code;

    PackageType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
