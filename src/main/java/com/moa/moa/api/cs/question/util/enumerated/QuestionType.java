package com.moa.moa.api.cs.question.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum QuestionType implements CommonEnum {
    /**
     * @see "일반 사용자 문의"
     */
    COMMON("사용자 문의", 1),

    /**
     * @see "렌탈샵 사업자 문의"
     */
    BUSINESS("렌탈 업체 문의",2),
    ;

    private final String desc;
    private final Integer code;

    QuestionType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
