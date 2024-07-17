package com.moa.moa.api.member.custom.util.convert;

import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class GenderConverter extends MoaAttributeConverter<Gender> {
    public GenderConverter() {
        super(Gender.class);
    }
}
