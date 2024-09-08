package com.moa.moa.api.shop.shop.application.mapstruct;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.item.domain.entity.Item;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopDto;
import com.moa.moa.api.shop.shop.domain.dto.FindShopDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
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

    default FindShopDto.Response ofFindShop(Shop shop,
                                            Boolean isWish,
                                            List<Place> places,
                                            Image image,
                                            Address address,
                                            Double moaAvgScore,
                                            Long moaTotalCount,
                                            NaverReview naverReview) {
        List<FindShopDto.PlaceResponse> placeResponses = places.stream()
                .map(place -> FindShopDto.PlaceResponse.builder()
                        .id(place.getId())
                        .name(place.getName())
                        .open(place.getOpenDate())
                        .close(place.getCloseDate())
                        .build())
                .collect(Collectors.toList());

        // TODO: image 기능 완성 시 구현 추가
        FindShopDto.ImageResponse imageResponse = FindShopDto.ImageResponse.builder()
                .id(null)
                .keyName(null)
                .createdAt(null)
                .build();

        FindShopDto.AddressResponse addressResponse = FindShopDto.AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .locationX(address.getLocation().getX())
                .locationY(address.getLocation().getY())
                .mapUrl(address.getUrl())
                .build();

        FindShopDto.MoaReviewResponse moaReviewResponse = FindShopDto.MoaReviewResponse.builder()
                .avgScore(moaAvgScore)
                .totalCount(moaTotalCount)
                .build();

        FindShopDto.NaverReviewResponse naverReviewResponse = FindShopDto.NaverReviewResponse.builder()
                .avgScore(naverReview.getAvgScore())
                .totalCount(naverReview.getTotalReview())
                .build();

        return FindShopDto.Response.builder()
                .id(shop.getId())
                .categoryId(shop.getCategory().getId())
                .name(shop.getName())
                .pickUp(shop.getPickUp())
                .storeUrl(shop.getUrl())
                .createdAt(shop.getCreatedAt())
                .isWish(isWish)
                .places(placeResponses)
                .images(imageResponse)
                .address(addressResponse)
                .moaReview(moaReviewResponse)
                .naverReview(naverReviewResponse)
                .build();
    }
}
