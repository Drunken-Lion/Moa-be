package com.moa.moa.api.place.liftticket.util.convert;

import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.global.util.enumconvert.CommonAttributeConverter;

public class LiftTicketStatusConverter extends CommonAttributeConverter<LiftTicketStatus> {
    public LiftTicketStatusConverter() {
        super(LiftTicketStatus.class);
    }
}
