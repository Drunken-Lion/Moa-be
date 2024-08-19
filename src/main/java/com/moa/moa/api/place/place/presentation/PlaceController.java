package com.moa.moa.api.place.place.presentation;

import com.moa.moa.api.place.place.application.PlaceService;
import com.moa.moa.api.place.place.domain.dto.FindAllPlaceDto;
import com.moa.moa.api.place.place.domain.dto.FindPlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moa.moa.global.common.response.ApiResponseCode.GET;

@RestController
@RequiredArgsConstructor
@Tag(name = "Place-API", description = "장소 API")
@RequestMapping("/v1/places")
public class PlaceController {
    private final PlaceService placeService;

    @Operation(summary = "스키장 목록 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<List<FindAllPlaceDto.Response>> findAllPlace(@RequestParam(name = "leftTopX") Double leftTopX,
                                                                       @RequestParam(name = "leftTopY") Double leftTopY,
                                                                       @RequestParam(name = "rightBottomX") Double rightBottomX,
                                                                       @RequestParam(name = "rightBottomY") Double rightBottomY) {

        List<FindAllPlaceDto.Response> responses = placeService.findAllPlace(leftTopX, leftTopY, rightBottomX, rightBottomY);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "스키장 상세 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping("{id}")
    public ResponseEntity<FindPlaceDto.Response> findPlace(@PathVariable("id") Long id) {

        FindPlaceDto.Response responses = placeService.findPlace(id);
        return ResponseEntity.ok().body(responses);
    }
}
