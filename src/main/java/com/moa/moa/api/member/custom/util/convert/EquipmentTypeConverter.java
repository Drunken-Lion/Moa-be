package com.moa.moa.api.member.custom.util.convert;

import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class EquipmentTypeConverter extends CommonAttributeConverter<EquipmentType> {
    public EquipmentTypeConverter() {
        super(EquipmentType.class);
    }
}
