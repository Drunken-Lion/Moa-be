package com.moa.moa.api.cs.question.application.mapstruct;

import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.global.common.response.PageExternalDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface QuestionMapstructMapper {
    FindAllQuestionDto.Response of(Question question);

    @Mapping(target = "data", expression = "java(responses.getContent())")
    @Mapping(target = "pageInfo", expression = "java(new PageExternalDto.PageInfo(pageable.getPageNumber(), pageable.getPageSize(), responses.hasNext(), totalSize))")
    PageExternalDto.Response<List<FindAllQuestionDto.Response>> of(Slice<FindAllQuestionDto.Response> responses, Pageable pageable, Integer totalSize);
}
