package com.moa.moa.api.time.operatingtime.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum OperatingType implements CommonEnum {
    /**
    * @see "영업"
    */
    OPEN("영업", 1),

    /**
    * @see "브레이크타임"
    */
    BREAK_TIME("브레이크타임", 2),

    /**
    * @see "휴무"
    */
    CLOSED("휴무", 3),
    ;

    private final String desc;
    private final int code;

    OperatingType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
