package com.moa.moa.api.shop.shop.application.mapstruct;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopDto;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopLowPriceDto;
import com.moa.moa.api.shop.shop.domain.dto.FindLowPriceShopDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ShopMapstructMapper {
    default FindAllShopDto.Response ofFindAllShop(Shop shop,
                                                  Wish wish,
                                                  Image image,
                                                  List<Item> packages,
                                                  Double moaAvgScore,
                                                  Long moaTotalCount,
                                                  NaverReview naverReview,
                                                  List<Place> places) {
        // TODO: image 기능 완성 시 구현 추가
        FindAllShopDto.ImageResponse imageResponse = FindAllShopDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        List<FindAllShopDto.PackageResponse> packageResponses = packages.stream()
                .map(packageItem -> FindAllShopDto.PackageResponse.builder()
                        .id(packageItem.getId())
                        .type(packageItem.getType())
                        .name(packageItem.getName())
                        .price(packageItem.getPrice())
                        .build())
                .collect(Collectors.toList());

        FindAllShopDto.MoaReviewResponse moaReviewResponse = FindAllShopDto.MoaReviewResponse.builder()
                .avgScore(moaAvgScore)
                .totalCount(moaTotalCount)
                .build();

        FindAllShopDto.NaverReviewResponse naverReviewResponse = FindAllShopDto.NaverReviewResponse.builder()
                .avgScore(naverReview.getAvgScore())
                .totalCount(naverReview.getTotalReview())
                .build();

        List<FindAllShopDto.PlaceResponse> placeResponses = places.stream()
                .map(place -> FindAllShopDto.PlaceResponse.builder()
                        .id(place.getId())
                        .name(place.getName())
                        .build())
                .collect(Collectors.toList());

        return FindAllShopDto.Response.builder()
                .id(shop.getId())
                .name(shop.getName())
                .wishId(wish != null ? wish.getId() : null)
                .images(imageResponse)
                .packages(packageResponses)
                .moaReview(moaReviewResponse)
                .naverReview(naverReviewResponse)
                .places(placeResponses)
                .build();
    }

    default FindAllShopLowPriceDto.Response ofFindAllLowestShops(LocalDate visitDate,
                                                                 Place place,
                                                                 Address placeAddress,
                                                                 Image placeImage,
                                                                 List<FindLowPriceShopDto> shopsData,
                                                                 List<Image> shopImage,
                                                                 List<FindAllShopLowPriceDto.CustomRequest> customs,
                                                                 List<Address> shopAddress,
                                                                 List<Wish> memberWish) {
        // place 반환값 만들기
        // TODO: image 기능 완성 시 구현 추가
        FindAllShopLowPriceDto.ImageResponse placeImageResponse = FindAllShopLowPriceDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        FindAllShopLowPriceDto.AddressResponse placeAddressResponse = null;
        if (placeAddress != null) {
            placeAddressResponse = FindAllShopLowPriceDto.AddressResponse.builder()
                    .address(placeAddress.getAddress())
                    .addressDetail(placeAddress.getAddressDetail())
                    .locationX(placeAddress.getLocation().getX())
                    .locationY(placeAddress.getLocation().getY())
                    .mapUrl(placeAddress.getUrl())
                    .build();
        }

        FindAllShopLowPriceDto.PlaceResponse placeResponse = FindAllShopLowPriceDto.PlaceResponse.builder()
                .id(place.getId())
                .name(place.getName())
                .open(place.getOpenDate())
                .close(place.getCloseDate())
                .address(placeAddressResponse)
                .images(placeImageResponse)
                .recLevel(place.getRecLevel())
                .build();

        // shop 반환값 만들기


        return null;
    }
}
