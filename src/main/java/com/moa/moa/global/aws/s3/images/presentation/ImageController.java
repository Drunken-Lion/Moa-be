package com.moa.moa.global.aws.s3.images.presentation;

import static com.moa.moa.global.common.response.ApiResponseCode.POST;

import com.moa.moa.global.aws.s3.images.presentation.dto.AddImageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Image-API", description = "이미지 API")
@RequestMapping("/v1/images")
public class ImageController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 저장", description = "1개 이미지 저장 API (확장자 : jpg, jpeg, png)", responses = {@ApiResponse(responseCode = POST)})
    public ResponseEntity<AddImageDto.Response> addImage(@RequestPart MultipartFile file) {
        return ResponseEntity.created(null).body(null);
    }
}
