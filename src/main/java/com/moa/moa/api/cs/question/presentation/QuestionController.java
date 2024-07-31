package com.moa.moa.api.cs.question.presentation;

import static com.moa.moa.global.common.response.ApiResponseCode.DELETE;
import static com.moa.moa.global.common.response.ApiResponseCode.GET;
import static com.moa.moa.global.common.response.ApiResponseCode.POST;
import static com.moa.moa.global.common.response.ApiResponseCode.PUT;

import com.moa.moa.api.cs.question.presentation.dto.AddQuestionExternalDto;
import com.moa.moa.api.cs.question.presentation.dto.FindQuestionExternalDto;
import com.moa.moa.api.cs.question.presentation.dto.ModQuestionExternalDto;
import com.moa.moa.api.shop.review.presentation.dto.FindAllReviewExternalDto;
import com.moa.moa.global.common.response.PageExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/questions")
@Tag(name = "Question-API", description = "문의 API")
public class QuestionController {
    @Operation(summary = "나의 문의 내역 전체 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<List<FindAllReviewExternalDto.Response>>> findAllQuestion(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 작성", responses = {@ApiResponse(responseCode = POST)})
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<AddQuestionExternalDto.Response> addQuestion(@AuthenticationPrincipal UserPrincipal user,
                                                                       @Valid @RequestBody final AddQuestionExternalDto.Request request) {
        return ResponseEntity.ok().body(null);
    }

    // FIXME
    //  : REST API로 전체 목록에서 전달받은 값을 통해 화면 구성이 가능한데, 이 기능이 필요할까요?
    @Operation(summary = "문의 내역 상세 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping("{id}")
    public ResponseEntity<FindQuestionExternalDto.Response> findQuestion(@PathVariable("id") Long id,
                                                                         @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 문의 내역 수정", responses = {@ApiResponse(responseCode = PUT)})
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("{id}")
    public ResponseEntity<ModQuestionExternalDto.Response> modQuestion(@PathVariable("id") Long id,
                                                                       @AuthenticationPrincipal UserPrincipal user,
                                                                       @Valid @RequestBody final ModQuestionExternalDto.Request request) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 문의 내역 삭제", responses = {@ApiResponse(responseCode = DELETE)})
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delQuestion(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.noContent().build();
    }

}
