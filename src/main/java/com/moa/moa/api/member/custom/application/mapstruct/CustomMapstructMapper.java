package com.moa.moa.api.member.custom.application.mapstruct;

import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.dto.ModCustomDto;
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

    @Mapping(target = "gender", source = "request.gender")
    @Mapping(target = "nickname", source = "request.nickname")
    @Mapping(target = "packageType", source = "request.packageType")
    @Mapping(target = "clothesType", source = "request.clothesType")
    @Mapping(target = "equipmentType", source = "request.equipmentType")
    Custom modOf(Custom custom, ModCustomDto.Request request);

    ModCustomDto.Response modOf(Custom custom);
}
