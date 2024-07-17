package com.moa.moa.api.member.custom.util.convert;

import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class GenderConverter extends CommonAttributeConverter<Gender> {
    public GenderConverter() {
        super(Gender.class);
    }
}
