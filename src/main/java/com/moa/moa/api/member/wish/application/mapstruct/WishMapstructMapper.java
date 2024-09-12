package com.moa.moa.api.member.wish.application.mapstruct;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.member.wish.domain.dto.FindAllWishDto;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.common.response.PageExternalDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface WishMapstructMapper {
    default FindAllWishDto.Response ofFindAllWish(Wish wish,
                                                  Shop shop,
                                                  Image image,
                                                  Address address,
                                                  Double moaAvgScore,
                                                  Long moaTotalCount,
                                                  List<Place> places) {
        FindAllWishDto.ShopResponse shopResponse = FindAllWishDto.ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();

        // TODO: image 기능 완성 시 구현 추가
        FindAllWishDto.ImageResponse imageResponse = FindAllWishDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        FindAllWishDto.AddressResponse addressResponse = FindAllWishDto.AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .locationX(address.getLocation().getX())
                .locationY(address.getLocation().getY())
                .mapUrl(address.getUrl())
                .build();

        FindAllWishDto.MoaReviewResponse moaReviewResponse = FindAllWishDto.MoaReviewResponse.builder()
                .avgScore(moaAvgScore)
                .totalCount(moaTotalCount)
                .build();

        List<FindAllWishDto.PlaceResponse> placeResponses = places.stream()
                .map(place -> FindAllWishDto.PlaceResponse.builder()
                        .id(place.getId())
                        .name(place.getName())
                        .build())
                .collect(Collectors.toList());

        return FindAllWishDto.Response.builder()
                .id(wish.getId())
                .createdAt(wish.getCreatedAt())
                .shop(shopResponse)
                .images(imageResponse)
                .address(addressResponse)
                .moaReview(moaReviewResponse)
                .places(placeResponses)
                .build();
    }

    @Mapping(target = "data", source = "responses")
    @Mapping(target = "pageInfo", expression = "java(new PageExternalDto.PageInfo(pageable.getPageNumber(), pageable.getPageSize(), responses.size() == pageable.getPageSize(), totalSize))")
    PageExternalDto.Response<List<FindAllWishDto.Response>> of(List<FindAllWishDto.Response> responses,
                                                               Pageable pageable,
                                                               Integer totalSize);
}
