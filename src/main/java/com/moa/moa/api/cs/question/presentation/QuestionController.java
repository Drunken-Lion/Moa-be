package com.moa.moa.api.cs.question.presentation;

import com.moa.moa.api.cs.question.application.QuestionService;
import com.moa.moa.api.cs.question.domain.dto.AddQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.ModQuestionDto;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.global.common.response.PageExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;

import static com.moa.moa.global.common.response.ApiResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/questions")
@Tag(name = "Question-API", description = "문의 API")
public class QuestionController {
    private final QuestionService questionService;
    // TODO: 회원 관련 기능이 완성되면 삭제할 것
    private final MemberRepository memberRepository;

    @Operation(summary = "나의 문의 내역 전체 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<List<FindAllQuestionDto.Response>>> findAllQuestion(@AuthenticationPrincipal UserPrincipal user,
                                                                                                       Pageable pageable) {

        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        return ResponseEntity.ok().body(questionService.findAllQuestion(member, pageable));
    }

    @Operation(summary = "문의 작성", responses = {@ApiResponse(responseCode = POST)})
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<AddQuestionDto.Response> addQuestion(@AuthenticationPrincipal UserPrincipal user,
                                                               @Valid @RequestBody final AddQuestionDto.Request request) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        AddQuestionDto.Response response = questionService.addQuestion(request, member);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "문의 내역 상세 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping("{id}")
    public ResponseEntity<FindQuestionDto.Response> findQuestion(@PathVariable("id") Long id,
                                                                 @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        return ResponseEntity.ok().body(questionService.findQuestion(id, member));
    }

    @Operation(summary = "내 문의 내역 수정", responses = {@ApiResponse(responseCode = PUT)})
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("{id}")
    public ResponseEntity<ModQuestionDto.Response> modQuestion(@PathVariable("id") Long id,
                                                               @AuthenticationPrincipal UserPrincipal user,
                                                               @Valid @RequestBody final ModQuestionDto.Request request) {

        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        return ResponseEntity.ok().body(questionService.modQuestion(id, request, member));
    }

    @Operation(summary = "내 문의 내역 삭제", responses = {@ApiResponse(responseCode = DELETE)})
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delQuestion(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        questionService.delQuestion(id, member);

        return ResponseEntity.noContent().build();
    }

}
