package com.moa.moa.api.cs.question.util.convert;

import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class QuestionTypeConverter extends MoaAttributeConverter<QuestionType> {
    public QuestionTypeConverter() {
        super(QuestionType.class);
    }
}
