package com.moa.moa.api.member.custom.util.convert;

import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class PackageTypeConverter extends MoaAttributeConverter<PackageType> {
    public PackageTypeConverter() {
        super(PackageType.class);
    }
}
