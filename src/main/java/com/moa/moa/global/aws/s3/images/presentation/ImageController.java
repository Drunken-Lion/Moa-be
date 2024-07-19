package com.moa.moa.global.aws.s3.images.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "v1-image", description = "이미지 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/images")
public class ImageController {
    @Operation(summary = "이미지 추가")
    @PostMapping
    public ResponseEntity<?> addImage() {
        return ResponseEntity.created(null).body(null);
    }
}
