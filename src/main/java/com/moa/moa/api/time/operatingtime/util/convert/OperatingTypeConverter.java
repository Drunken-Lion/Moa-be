package com.moa.moa.api.time.operatingtime.util.convert;

import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class OperatingTypeConverter extends CommonAttributeConverter<OperatingType> {
    public OperatingTypeConverter() {
        super(OperatingType.class);
    }
}
