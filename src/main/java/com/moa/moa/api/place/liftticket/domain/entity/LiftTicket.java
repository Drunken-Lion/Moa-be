package com.moa.moa.api.place.liftticket.domain.entity;

import com.moa.moa.api.place.liftticket.util.convert.LiftTicketStatusConverter;
import com.moa.moa.api.place.liftticket.util.convert.LiftTicketTypeConverter;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketStatus;
import com.moa.moa.api.place.liftticket.util.enumerated.LiftTicketType;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lift_ticket")
public class LiftTicket extends BaseEntity {
    @Comment("장소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Place place;

    @Comment("리프트권 상태")
    @Column(name = "status", columnDefinition = "BIGINT")
    @Convert(converter = LiftTicketStatusConverter.class)
    private LiftTicketStatus status;

    @Comment("리프트권 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @Comment("이용권 종류")
    @Column(name = "ticket_type", columnDefinition = "BIGINT")
    @Convert(converter = LiftTicketTypeConverter.class)
    private LiftTicketType ticketType;

    @Comment("시간 종류")
    @Column(name = "hours", columnDefinition = "BIGINT")
    private Long hours;

    @Comment("시작 시간")
    @Column(name = "open", columnDefinition = "TIME")
    private LocalTime startTime;

    @Comment("종료 시간")
    @Column(name = "close", columnDefinition = "TIME")
    private LocalTime endTime;
}
