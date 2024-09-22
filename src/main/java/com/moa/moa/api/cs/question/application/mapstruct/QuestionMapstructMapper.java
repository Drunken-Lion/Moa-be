package com.moa.moa.api.cs.question.application.mapstruct;

import com.moa.moa.api.cs.answer.domain.entity.Answer;
import com.moa.moa.api.cs.question.domain.dto.AddQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.ModQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
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
    PageExternalDto.Response<List<FindAllQuestionDto.Response>> of(Slice<FindAllQuestionDto.Response> responses,
                                                                   Pageable pageable,
                                                                   Integer totalSize);

    @Mapping(target = "id", expression = "java(question.getId())")
    FindQuestionDto.Response of(Question question,
                                FindQuestionDto.MemberResponse member,
                                List<FindQuestionDto.ImageResponse> images,
                                List<FindQuestionDto.AnswerResponse> answers);

    FindQuestionDto.MemberResponse of(Member member);

    FindQuestionDto.ImageResponse of(Image image);

    FindQuestionDto.AnswerResponse of(Answer answer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "member", source = "member")
    @Mapping(target = "status", source = "status")
    Question addOf(AddQuestionDto.Request request, Member member, QuestionStatus status);

    AddQuestionDto.Response addOf(Question question);

    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "title", source = "request.title")
    @Mapping(target = "content", source = "request.content")
    Question modOf(Question question, ModQuestionDto.Request request);

    ModQuestionDto.Response modOf(Question question);
}
