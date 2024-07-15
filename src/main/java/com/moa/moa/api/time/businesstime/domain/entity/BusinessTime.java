package com.moa.moa.api.time.businesstime.domain.entity;

import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "business_time")
public class BusinessTime extends BaseEntity {
    @Comment("운영 시간")
    @OneToMany(mappedBy = "businessTime")
    @Builder.Default
    private List<OperatingTime> operatingTimes = new ArrayList<>();

    @Comment("특정일")
    @OneToMany(mappedBy = "businessTime")
    @Builder.Default
    private List<SpecificDay> specificDays = new ArrayList<>();
}
