package com.moa.moa.api.member.wish.presentation;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.wish.application.WishService;
import com.moa.moa.api.member.wish.domain.dto.AddWishDto;
import com.moa.moa.api.member.wish.domain.dto.FindAllWishDto;
import com.moa.moa.global.common.response.PageExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
@Tag(name = "Wish-API", description = "렌탈샵 찜 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/wishes")
public class WishController {
    private final WishService wishService;
    private final MemberRepository memberRepository;

    @Operation(summary = "렌탈샵 찜 추가", responses = {@ApiResponse(responseCode = POST)})
    @PostMapping
    public ResponseEntity<AddWishDto.Response> addWish(@Valid @RequestBody final AddWishDto.Request request,
                                                       @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        AddWishDto.Response response = wishService.addWish(request, member);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "나의 찜 목록 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<List<FindAllWishDto.Response>>> findAllWish(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                               @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        PageExternalDto.Response<List<FindAllWishDto.Response>> pageResponse = wishService.findAllWish(member, PageRequest.of(page, size));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "내 찜 항목 삭제", responses = {@ApiResponse(responseCode = DELETE)})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delWish(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.noContent().build();
    }
}
