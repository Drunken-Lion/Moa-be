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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                                                                 Map<Long, Address> shopsAddress,
                                                                 Map<Long, Wish> memberWish) {
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
                    .id(placeAddress.getId())
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
        List<FindAllShopLowPriceDto.ShopResponse> shopResponses = new ArrayList<>();
        for (int i = 0; i < shopsData.size(); i++) {
            FindLowPriceShopDto shopDto = shopsData.get(i);

            if (shopDto == null) continue; // place에 속하는 렌탈샵이지만 커스텀에 맞는 가격이 없음
            
            Long shopId = shopsData.get(i).getShopId();

            // moaReview
            FindAllShopLowPriceDto.MoaReviewResponse moaReviewResponse = FindAllShopLowPriceDto.MoaReviewResponse.builder()
                    .avgScore(shopDto.getAvgScore())
                    .totalCount(shopDto.getTotalCount())
                    .build();

            // naverReview
            FindAllShopLowPriceDto.NaverReviewResponse naverReviewResponse = FindAllShopLowPriceDto.NaverReviewResponse.builder()
                    .avgScore(shopDto.getNrAvgScore())
                    .totalCount(shopDto.getNrTotalCount())
                    .build();

            // address
            Address findShopAddress = shopsAddress.get(shopId);
            FindAllShopLowPriceDto.AddressResponse shopAddress = null;
            if (findShopAddress != null) {
                shopAddress = FindAllShopLowPriceDto.AddressResponse.builder()
                        .id(findShopAddress.getId())
                        .address(findShopAddress.getAddress())
                        .addressDetail(findShopAddress.getAddressDetail())
                        .locationX(findShopAddress.getLocation().getX())
                        .locationY(findShopAddress.getLocation().getY())
                        .mapUrl(findShopAddress.getUrl())
                        .build();
            }

            // image
            FindAllShopLowPriceDto.ImageResponse shopImageResponse = FindAllShopLowPriceDto.ImageResponse.builder()
                    .id(null)
                    .keyName(null)
                    .createdAt(null)
                    .build();

            // customs
            List<FindAllShopLowPriceDto.CustomResponse> customResponses = new ArrayList<>();
            for (int j = 0; j < customs.size(); j++) {
                FindAllShopLowPriceDto.CustomResponse createCustom = null;

                if (customs.get(j) == null) {
                    customResponses.add(createCustom);
                    continue;
                }

                createCustom = FindAllShopLowPriceDto.CustomResponse.builder()
                        .gender(customs.get(j).gender())
                        .nickname(customs.get(j).nickname())
                        .packageType(customs.get(j).packageType())
                        .clothesType(customs.get(j).clothesType())
                        .equipmentType(customs.get(j).equipmentType())
                        .price(shopDto.getCustomPrices().get(customs.get(j).nickname()))
                        .build();

                customResponses.add(createCustom);
            }

            FindAllShopLowPriceDto.ShopResponse shopResponse = FindAllShopLowPriceDto.ShopResponse.builder()
                    .id(shopId)
                    .wishId(memberWish.get(shopId) == null ? null : memberWish.get(shopId).getId())
                    .totalPrice(shopDto.getTotalPrice())
                    .memberName(null)
                    .name(shopDto.getName())
                    .pickUp(shopDto.getPickUp())
                    .storeUrl(shopDto.getStoreUrl())
                    .moaReview(moaReviewResponse)
                    .naverReview(naverReviewResponse)
                    .address(shopAddress)
                    .image(shopImageResponse)
                    .customs(customResponses)
                    .build();

            shopResponses.add(shopResponse);
        }


        return FindAllShopLowPriceDto.Response.builder()
                .visitDate(visitDate)
                .place(placeResponse)
                .shop(shopResponses)
                .build();
    }
}
