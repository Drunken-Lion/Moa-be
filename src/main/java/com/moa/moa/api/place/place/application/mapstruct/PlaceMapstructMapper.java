package com.moa.moa.api.place.place.application.mapstruct;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.place.domain.dto.FindAllPlaceDto;
import com.moa.moa.api.place.place.domain.dto.FindPlaceDto;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.slope.domain.entity.Slope;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PlaceMapstructMapper {
    default FindAllPlaceDto.Response ofFindAllPlace(Place place,
                                                    Image image,
                                                    Address address,
                                                    List<OperatingTime> operatingTimes,
                                                    List<SpecificDay> specificDays,
                                                    List<Amenity> amenities,
                                                    List<Slope> slopes) {
        // TODO: image 기능 완성 시 구현 추가
        FindAllPlaceDto.ImageResponse imageResponse = FindAllPlaceDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        FindAllPlaceDto.AddressResponse addressResponse = FindAllPlaceDto.AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .locationX(address.getLocation().getX())
                .locationY(address.getLocation().getY())
                .mapUrl(address.getUrl())
                .build();

        List<FindAllPlaceDto.OperatingTimeResponse> operatingTimeResponses = operatingTimes.stream()
                .map(operatingTime -> FindAllPlaceDto.OperatingTimeResponse.builder()
                        .id(operatingTime.getId())
                        .status(operatingTime.getStatus())
                        .day(operatingTime.getDay())
                        .open(operatingTime.getOpenTime())
                        .close(operatingTime.getCloseTime())
                        .build())
                .collect(Collectors.toList());

        List<FindAllPlaceDto.SpecificDayResponse> specificDayResponses = specificDays.stream()
                .map(specificDay -> FindAllPlaceDto.SpecificDayResponse.builder()
                        .id(specificDay.getId())
                        .status(specificDay.getStatus())
                        .reason(specificDay.getReason())
                        .date(specificDay.getDate())
                        .open(specificDay.getOpenTime())
                        .close(specificDay.getCloseTime())
                        .build())
                .collect(Collectors.toList());

        List<FindAllPlaceDto.AmenityResponse> amenityResponses = amenities.stream()
                .map(amenity -> FindAllPlaceDto.AmenityResponse.builder()
                        .id(amenity.getId())
                        .name(amenity.getType().name())
                        .build())
                .collect(Collectors.toList());

        List<FindAllPlaceDto.SlopeResponse> slopeResponses = slopes.stream()
                .map(slope -> FindAllPlaceDto.SlopeResponse.builder()
                        .id(slope.getId())
                        .name(slope.getName())
                        .level(slope.getLevel())
                        .build())
                .collect(Collectors.toList());

        return FindAllPlaceDto.Response.builder()
                .id(place.getId())
                .name(place.getName())
                .open(place.getOpenDate())
                .close(place.getCloseDate())
                .recLevel(place.getRecLevel())
                .createdAt(place.getCreatedAt())
                .images(imageResponse)
                .address(addressResponse)
                .operatingTimes(operatingTimeResponses)
                .specificDays(specificDayResponses)
                .amenities(amenityResponses)
                .slopes(slopeResponses)
                .build();
    }

    default FindPlaceDto.Response ofFindPlace(Place place,
                                              Image image,
                                              Address address,
                                              List<OperatingTime> operatingTimes,
                                              List<SpecificDay> specificDays,
                                              List<Amenity> amenities,
                                              List<Slope> slopes) {
        // TODO: image 기능 완성 시 구현 추가
        FindPlaceDto.ImageResponse imageResponse = FindPlaceDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        FindPlaceDto.AddressResponse addressResponse = FindPlaceDto.AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .locationX(address.getLocation().getX())
                .locationY(address.getLocation().getY())
                .mapUrl(address.getUrl())
                .build();

        List<FindPlaceDto.OperatingTimeResponse> operatingTimeResponses = operatingTimes.stream()
                .map(operatingTime -> FindPlaceDto.OperatingTimeResponse.builder()
                        .id(operatingTime.getId())
                        .status(operatingTime.getStatus())
                        .day(operatingTime.getDay())
                        .open(operatingTime.getOpenTime())
                        .close(operatingTime.getCloseTime())
                        .build())
                .collect(Collectors.toList());

        List<FindPlaceDto.SpecificDayResponse> specificDayResponses = specificDays.stream()
                .map(specificDay -> FindPlaceDto.SpecificDayResponse.builder()
                        .id(specificDay.getId())
                        .status(specificDay.getStatus())
                        .reason(specificDay.getReason())
                        .date(specificDay.getDate())
                        .open(specificDay.getOpenTime())
                        .close(specificDay.getCloseTime())
                        .build())
                .collect(Collectors.toList());

        List<FindPlaceDto.AmenityResponse> amenityResponses = amenities.stream()
                .map(amenity -> FindPlaceDto.AmenityResponse.builder()
                        .id(amenity.getId())
                        .name(amenity.getType().name())
                        .build())
                .collect(Collectors.toList());

        List<FindPlaceDto.SlopeResponse> slopeResponses = slopes.stream()
                .map(slope -> FindPlaceDto.SlopeResponse.builder()
                        .id(slope.getId())
                        .name(slope.getName())
                        .level(slope.getLevel())
                        .build())
                .collect(Collectors.toList());

        return FindPlaceDto.Response.builder()
                .id(place.getId())
                .name(place.getName())
                .open(place.getOpenDate())
                .close(place.getCloseDate())
                .recLevel(place.getRecLevel())
                .createdAt(place.getCreatedAt())
                .images(imageResponse)
                .address(addressResponse)
                .operatingTimes(operatingTimeResponses)
                .specificDays(specificDayResponses)
                .amenities(amenityResponses)
                .slopes(slopeResponses)
                .build();
    }
}
