package com.moa.moa.api.place.place.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "v1-place", description = "장소 API")
@RequestMapping("/v1/places")
public class PlaceController {
    @Operation(summary = "스키장 목록 조회")
    @GetMapping
    public ResponseEntity<?> findAllPlace() {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "스키장 상세 조회")
    @GetMapping("{id}")
    public ResponseEntity<?> findPlace(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }
}
