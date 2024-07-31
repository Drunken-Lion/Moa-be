package com.moa.moa.api.time.operatingtime.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
}
