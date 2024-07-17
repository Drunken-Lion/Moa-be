package com.moa.moa.api.cs.question.util.convert;

import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class QuestionStatusConverter extends MoaAttributeConverter<QuestionStatus> {
    public QuestionStatusConverter() {
        super(QuestionStatus.class);
    }
}
