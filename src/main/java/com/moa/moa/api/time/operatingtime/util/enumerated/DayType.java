package com.moa.moa.api.time.operatingtime.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum DayType implements CommonEnum {
    /**
    * @see "월"
    */
    MON("월", 1),

    /**
    * @see "화"
    */
    TUE("화", 2),

    /**
    * @see "수"
    */
    WED("수", 3),

    /**
     * @see "목"
     */
    THU("목", 4),

    /**
     * @see "금"
     */
    FRI("금", 5),

    /**
     * @see "토"
     */
    SAT("토", 6),

    /**
     * @see "알"
     */
    SUN("알", 7),

    ;

    private final String desc;
    private final int code;

    DayType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
