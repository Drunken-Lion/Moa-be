package com.moa.moa.api.place.liftticket.domain.util.convert;

import com.moa.moa.api.place.liftticket.domain.util.enumerated.LiftTicketStatus;
import com.moa.moa.global.util.enumconvert.MoaAttributeConverter;

public class LiftTicketStatusConverter extends MoaAttributeConverter<LiftTicketStatus> {
    public LiftTicketStatusConverter() {
        super(LiftTicketStatus.class);
    }
}
