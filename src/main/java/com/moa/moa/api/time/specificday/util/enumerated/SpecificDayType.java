package com.moa.moa.api.time.specificday.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpecificDayType implements CommonEnum {
    /**
     * @see "주중영업"
     */
    OPEN_WEEK_DAYS("주중영업", 1),

    /**
     * @see "주말영업"
     */
    WEEKEND_OPEN("주말영업", 2),

    /**
     * @see "휴무"
     */
    CLOSED("휴무", 3),

    /**
     * @see "공휴일"
     */
    HOLIDAY("공휴일", 4),
    ;

    private final String desc;
    private final int code;
}
