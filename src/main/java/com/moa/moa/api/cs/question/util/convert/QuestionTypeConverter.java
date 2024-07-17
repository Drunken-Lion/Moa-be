package com.moa.moa.api.cs.question.util.convert;

import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class QuestionTypeConverter extends CommonAttributeConverter<QuestionType> {
    public QuestionTypeConverter() {
        super(QuestionType.class);
    }
}
