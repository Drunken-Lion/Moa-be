package com.moa.moa.api.shop.item.util.convert;

import com.moa.moa.api.shop.item.util.enumerated.LiftTicketType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class LiftTicketTypeConverter extends CommonAttributeConverter<LiftTicketType> {
    public LiftTicketTypeConverter() {
        super(LiftTicketType.class);
    }
}
