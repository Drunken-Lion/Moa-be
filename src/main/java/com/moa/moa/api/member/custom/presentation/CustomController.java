package com.moa.moa.api.member.custom.presentation;

import static com.moa.moa.global.common.response.ApiResponseCode.DELETE;
import static com.moa.moa.global.common.response.ApiResponseCode.GET;
import static com.moa.moa.global.common.response.ApiResponseCode.POST;
import static com.moa.moa.global.common.response.ApiResponseCode.PUT;

import com.moa.moa.api.member.custom.presentation.dto.AddCustomExternalDto;
import com.moa.moa.api.member.custom.presentation.dto.FindAllCustomExternalDto;
import com.moa.moa.api.member.custom.presentation.dto.ModCustomExternalDto;
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
@Tag(name = "Custom-API", description = "렌탈정보 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/customs")
public class CustomController {
    @Operation(summary = "내 스키어 추가", responses = {@ApiResponse(responseCode = POST)})
    @PostMapping
    public ResponseEntity<AddCustomExternalDto.Response> addCustom(@Valid @RequestBody final AddCustomExternalDto.Request request,
                                                                   @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "내 스키어 리스트 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<List<FindAllCustomExternalDto.Response>> findAllCustom(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 스키어 수정", responses = {@ApiResponse(responseCode = PUT)})
    @PutMapping("{id}")
    public ResponseEntity<ModCustomExternalDto.Response> modCustom(@PathVariable("id") Long id,
                                                                   @Valid @RequestBody final ModCustomExternalDto.Request request,
                                                                   @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 스키어 삭제", responses = {@ApiResponse(responseCode = DELETE)})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delCustom(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.noContent().build();
    }
}
