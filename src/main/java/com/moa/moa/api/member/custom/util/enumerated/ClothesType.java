package com.moa.moa.api.member.custom.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClothesType implements CommonEnum {
    /**
    * @see "일반 의류"
    */
    STANDARD("일반",1),

    /**
    * @see "고급 의류"
    */
    LUXURY("고급",2),

    /**
    * @see "프리미엄 의류"
    */
    PREMIUM("프리미엄",3),
    ;

    private final String desc;
    private final int code;
}
