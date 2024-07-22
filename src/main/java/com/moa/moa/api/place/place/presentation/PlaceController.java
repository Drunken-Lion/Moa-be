package com.moa.moa.api.place.place.presentation;

import com.moa.moa.api.place.place.presentation.dto.FindAllPlaceExternalDto;
import com.moa.moa.api.place.place.presentation.dto.FindPlaceExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Place-API", description = "장소 API")
@RequestMapping("/v1/places")
public class PlaceController {
    @Operation(summary = "스키장 목록 조회")
    @GetMapping
    public ResponseEntity<List<FindAllPlaceExternalDto.Response>> findAllPlace(@RequestParam(name = "leftTopX") Double leftTopX,
                                                                               @RequestParam(name = "leftTopY") Double leftTopY,
                                                                               @RequestParam(name = "rightBottomX") Double rightBottomX,
                                                                               @RequestParam(name = "rightBottomY") Double rightBottomY) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "스키장 상세 조회")
    @GetMapping("{id}")
    public ResponseEntity<FindPlaceExternalDto.Response> findPlace(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }
}
