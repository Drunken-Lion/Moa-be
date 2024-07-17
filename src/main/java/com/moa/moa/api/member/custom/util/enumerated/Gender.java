package com.moa.moa.api.member.custom.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum Gender implements CommonEnum {
    /**
     * @see "남자"
     */
    MALE("남자", 1),

    /**
     * @see "여자"
     */
    FEMALE("여자",2),
    ;

    private final String desc;
    private final int code;

    Gender(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
