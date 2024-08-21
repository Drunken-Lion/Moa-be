package com.moa.moa.api.place.place.domain.dto;

import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record FindAllPlaceDto() {
    @Builder
    public record Response(
            Long id,
            String name,
            LocalDate open,
            LocalDate close,
            PlaceLevel recLevel,
            LocalDateTime createdAt,
            FindAllPlaceDto.ImageResponse images,
            FindAllPlaceDto.AddressResponse address,
            List<FindAllPlaceDto.OperatingTimeResponse> operatingTimes,
            List<FindAllPlaceDto.SpecificDayResponse> specificDays,
            List<FindAllPlaceDto.AmenityResponse> amenities,
            List<FindAllPlaceDto.SlopeResponse> slopes
    ) {
    }

    @Builder
    public record ImageResponse(
            Long id,
            String keyName,
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
            DayType day,
            LocalTime open,
            LocalTime close
    ) {
    }

    @Builder
    public record SpecificDayResponse(
            Long id,
            SpecificDayType status,
            String reason,
            LocalDate date,
            LocalTime open,
            LocalTime close
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
