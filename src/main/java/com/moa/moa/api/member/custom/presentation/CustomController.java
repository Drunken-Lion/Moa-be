package com.moa.moa.api.member.custom.presentation;

import com.moa.moa.api.member.custom.presentation.dto.AddCustomExternalDto;
import com.moa.moa.api.member.custom.presentation.dto.FindAllCustomExternalDto;
import com.moa.moa.api.member.custom.presentation.dto.ModCustomExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Custom-API", description = "렌탈정보 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/customs")
public class CustomController {
    @Operation(summary = "내 스키어 추가")
    @PostMapping
    public ResponseEntity<AddCustomExternalDto.Response> addCustom(@Valid @RequestBody final AddCustomExternalDto.Request request,
                                                                   @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "내 스키어 리스트 조회")
    @GetMapping
    public ResponseEntity<List<FindAllCustomExternalDto.Response>> findAllCustom(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 스키어 수정")
    @PutMapping("{id}")
    public ResponseEntity<ModCustomExternalDto.Response> modCustom(@PathVariable("id") Long id,
                                                                   @Valid @RequestBody final ModCustomExternalDto.Request request,
                                                                   @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 스키어 삭제")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delCustom(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.noContent().build();
    }
}
