package com.moa.moa.api.shop.itemoption.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemOptionName implements CommonEnum {
    /**
    * @see "장비 : 숏스키"
    */
    SHORT_SKI("숏스키", 1),

    /**
    * @see "장비 : 인라인 스키"
    */
    INLINE_SKI("인라인스키", 2),

    /**
     * @see "의류 : 고급 의류"
     */
    LUXURY("고급 의류", 3),

    /**
     * @see "의류 : 프리미엄 의류"
     */
    PREMIUM("프리미엄 의류", 4),
    ;

    private final String desc;
    private final int code;
}
