package com.moa.moa.api.place.liftticket.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum LiftTicketStatus implements CommonEnum {
    /**
    * @see "티켓 구분 : 주중"
    */
    WEEK_DAY("주중", 1),

    /**
    * @see "티켓 구분 : 주말"
    */
    WEEK_END("주말", 2),
    ;

    private final String desc;
    private final int code;

    LiftTicketStatus(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
