package com.moa.moa.api.category.category.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType implements CommonEnum {
    /**
    * @see "스키장"
    */
    SKI_RESORT("스키장", 1),
    ;

    private final String desc;
    private final int code;
}
