package com.moa.moa.api.member.member.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole implements CommonEnum {
    /**
     * @see "일반 사용자 권한"
     */
    MEMBER("일반", 1),

    /**
     * @see "렌탈샵 사업자 권한"
     */
    OWNER("사업자", 2),

    /**
     * @see "모아 관리자 권한"
     * @
     */
    ADMIN("관리자", 3),
    ;

    private final String desc;
    private final int code;
}