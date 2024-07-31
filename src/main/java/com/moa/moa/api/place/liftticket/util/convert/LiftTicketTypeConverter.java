package com.moa.moa.api.place.liftticket.util.convert;

import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class LiftTicketTypeConverter extends CommonAttributeConverter<LiftTicketType> {
    public LiftTicketTypeConverter() {
        super(LiftTicketType.class);
    }
}
