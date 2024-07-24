package com.moa.moa.api.cs.question.util.enumerated;

import com.moa.moa.global.util.enumconvert.CommonEnum;
import lombok.Getter;

@Getter
public enum QuestionStatus implements CommonEnum {
    /**
     * @see "문의중"
     */
    INCOMPLETE("문의중", 1),

    /**
     * @see "답변완료"
     */
    COMPLETE("답변완료",2),
    ;

    private final String desc;
    private final int code;

    QuestionStatus(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
