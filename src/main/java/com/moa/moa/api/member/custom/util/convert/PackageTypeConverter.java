package com.moa.moa.api.member.custom.util.convert;

import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class PackageTypeConverter extends CommonAttributeConverter<PackageType> {
    public PackageTypeConverter() {
        super(PackageType.class);
    }
}
