package com.moa.moa.api.member.custom.application.mapstruct;

import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomMapstructMapper {
    @Mapping(source = "member.id", target = "memberId")
    FindAllCustomDto.Response of(Custom custom);
}
