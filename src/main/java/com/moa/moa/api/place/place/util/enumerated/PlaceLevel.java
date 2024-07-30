package com.moa.moa.api.place.place.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceLevel implements CommonEnum {
    /**
     * @see "초급자"
     */
    LEVEL_1("초급자", 1),

    /**
     * @see "중급자"
     */
    LEVEL_2("중급자", 2),

    /**
     * @see "상급자"
     */
    LEVEL_3("상급자", 3),

    /**
     * @see "최상급자"
     */
    LEVEL_4("최상급자", 4),
    ;

    private final String desc;
    private final int code;
}
