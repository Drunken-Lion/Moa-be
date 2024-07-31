package com.moa.moa.api.place.liftticket.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum LiftTicketType implements CommonEnum {
    /**
    * @see "스마트 티겟 : 입장시간으로부터 n시간 이용"
    */
    SMART("스마트권", 1),

    /**
    * @see "시간지정권 : 특정 시간에 입장 및 특정 시간에 퇴장"
    */
    TIME("시간지정권", 2),
    ;

    private final String desc;
    private final int code;

    LiftTicketType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
