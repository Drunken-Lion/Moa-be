package com.moa.moa.api.member.custom.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum EquipmentType implements CommonEnum {
    /**
    * @see "스키"
    */
    SKI("스키",1),

    /**
    * @see "보드"
    */
    SNOW_BOARD("보드",2),

    /**
    * @see "숏스키"
    */
    SHORT_SKI("숏스키",3),

    /**
    * @see "인라인스키"
    */
    INLINE_SKI("인라인스키",4),
    ;

    private final String desc;
    private final int code;

    EquipmentType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
