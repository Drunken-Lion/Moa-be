package com.moa.moa.api.place.liftticket.domain.util.convert;

import com.moa.moa.api.place.liftticket.domain.util.enumerated.LiftTicketType;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class LiftTicketTypeConverter extends MoaAttributeConverter<LiftTicketType> {
    public LiftTicketTypeConverter() {
        super(LiftTicketType.class);
    }
}
