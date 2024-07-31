package com.moa.moa.api.place.place.presentation.dto;

import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record FindAllPlaceExternalDto() {
    @Builder
    public record Response(
            Long id,
            String name,
            LocalDate open,
            LocalDate close,
            PlaceLevel recLevel,
            LocalDateTime createdAt,

            FindAllPlaceExternalDto.ImageResponse image,
            FindAllPlaceExternalDto.AddressResponse address,
            List<FindAllPlaceExternalDto.OperatingTimeResponse> operatingTimes,
            List<FindAllPlaceExternalDto.SpecificDayResponse> specificDays,
            List<FindAllPlaceExternalDto.AmenityResponse> amenities,
            List<FindAllPlaceExternalDto.SlopeResponse> slopes
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
            DayType day,
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
