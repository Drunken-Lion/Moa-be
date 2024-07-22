package com.moa.moa.api.place.place.presentation.dto;

import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindPlaceExternalDto() {
    @Builder
    public record Response(
            Long id,
            String name,
            LocalDate open,
            LocalDate close,
            PlaceLevel recLevel,
            LocalDateTime createdAt,

            FindPlaceExternalDto.ImageResponse image,
            FindPlaceExternalDto.AddressResponse address,
            List<FindPlaceExternalDto.OperatingTimeResponse> operatingTimes,
            List<FindPlaceExternalDto.SpecificDayResponse> specificDays,
            List<FindPlaceExternalDto.AmenityResponse> amenities,
            List<FindPlaceExternalDto.SlopeResponse> slopes
    ) {
    }

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt) {
    }

    @Builder
    public record AddressResponse(
            Long id,
            String address,
            String addressDetail,
            Double locationX,
            Double locationY,
            String mapUrl
    ) {
    }

    @Builder
    public record OperatingTimeResponse(
            Long id,
            OperatingType status,
            String day, // TODO : Type 변경 필요
            LocalDate open,
            LocalDate close
    ) {
    }

    @Builder
    public record SpecificDayResponse(
            Long id,
            SpecificDayType status,
            String reason,
            LocalDate date,
            LocalDateTime open,
            LocalDateTime close
    ) {
    }

    @Builder
    public record AmenityResponse(
            Long id,
            String name
    ) {
    }

    @Builder
    public record SlopeResponse(
            Long id,
            String name,
            SlopeLevel level
    ) {
    }
}
