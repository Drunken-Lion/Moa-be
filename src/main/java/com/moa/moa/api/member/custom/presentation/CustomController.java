package com.moa.moa.api.member.custom.presentation;

import com.moa.moa.api.member.custom.application.CustomService;
import com.moa.moa.api.member.custom.domain.dto.AddCustomDto;
import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.dto.ModCustomDto;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Custom-API", description = "렌탈정보 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/customs")
public class CustomController {
    private final CustomService customService;
    private final MemberRepository memberRepository;

    @Operation(summary = "내 스키어 추가", responses = {@ApiResponse(responseCode = POST)})
    @PostMapping
    public ResponseEntity<AddCustomDto.Response> addCustom(@Valid @RequestBody final AddCustomDto.Request request,
                                                           @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        AddCustomDto.Response response = customService.addCustom(request, member);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "내 스키어 리스트 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<List<FindAllCustomDto.Response>> findAllCustom(@AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        List<FindAllCustomDto.Response> responses = customService.findAllCustom(member);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "내 스키어 수정", responses = {@ApiResponse(responseCode = PUT)})
    @PutMapping("{id}")
    public ResponseEntity<ModCustomDto.Response> modCustom(@PathVariable("id") Long id,
                                                           @Valid @RequestBody final ModCustomDto.Request request,
                                                           @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        ModCustomDto.Response response = customService.modCustom(id, request, member);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 스키어 삭제", responses = {@ApiResponse(responseCode = DELETE)})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delCustom(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.noContent().build();
    }
}
