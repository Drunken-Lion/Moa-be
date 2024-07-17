package com.moa.moa.api.place.slope.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum SlopeLevel implements CommonEnum {
    /**
     * @see "초급"
     */
    LEVEL_1("초급", 1),

    /**
     * @see "중급"
     */
    LEVEL_2("중급",2),

    /**
     * @see "중상급"
     */
    LEVEL_3("중급",3),

    /**
     * @see "상급"
     */
    LEVEL_4("상급",4),

    /**
     * @see "최상급"
     */
    LEVEL_5("최상급",5),

    /**
    * @see "미정의 슬로프"
    */
    OTHER("기타", 0),
    ;

    private final String desc;
    private final Integer code;

    SlopeLevel(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
