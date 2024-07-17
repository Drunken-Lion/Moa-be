package com.moa.moa.api.place.amenity.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum AmenityType implements CommonEnum {
    /**
     * @see "호텔"
     */
    HOTEL("호텔", 1),

    /**
     * @see "내부 렌탈샵"
     */
    INNER_RENTAL_SHOP("내부 렌탈샵",2),

    /**
     * @see "셔틀 버스"
     */
    SHUTTLE_BUS("셔틀 버스",3),

    /**
     * @see "편의점"
     */
    CONVENIENCE_STORE("편의점",4),

    /**
     * @see "푸드코트"
     */
    FOOD_COURT("푸드코트",5),

    /**
     * @see "기프트샵"
     */
    GIFT_SHOP("기프트샵",6),

    /**
     * @see "의무실"
     */
    INFIRMARY("의무실",7),
    ;

    private final String desc;
    private final Integer code;

    AmenityType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
