package com.moa.moa.api.member.wish.presentation;

import static com.moa.moa.global.common.response.ApiResponseCode.DELETE;
import static com.moa.moa.global.common.response.ApiResponseCode.GET;
import static com.moa.moa.global.common.response.ApiResponseCode.POST;

import com.moa.moa.api.member.wish.presentation.dto.AddWishExternalDto;
import com.moa.moa.api.member.wish.presentation.dto.FindAllWishExternalDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Wish-API", description = "렌탈샵 찜 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/wishes")
public class WishController {
    @Operation(summary = "렌탈샵 찜 추가", responses = {@ApiResponse(responseCode = POST)})
    @PostMapping
    public ResponseEntity<AddWishExternalDto.Response> addWish(@Valid @RequestBody final AddWishExternalDto.Request request,
                                                               @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "나의 찜 목록 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<List<FindAllWishExternalDto.Response>>> findAllWish(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                      @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 찜 항목 삭제", responses = {@ApiResponse(responseCode = DELETE)})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delWish(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.noContent().build();
    }
}
