package com.moa.moa.api.place.amenity.util.convert;

import com.moa.moa.api.place.amenity.util.enumerated.AmenityType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class AmenityTypeConverter extends CommonAttributeConverter<AmenityType> {
    public AmenityTypeConverter() {
        super(AmenityType.class);
    }
}
