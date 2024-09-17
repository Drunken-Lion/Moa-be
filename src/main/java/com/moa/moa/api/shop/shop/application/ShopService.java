package com.moa.moa.api.shop.shop.application;

import com.moa.moa.api.address.address.AddressProcessor;
import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.member.custom.domain.dto.FindLowPriceCustomDto;
import com.moa.moa.api.member.member.domain.MemberProcessor;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.WishProcessor;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.PlaceProcessor;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.PlaceShopProcessor;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.application.mapstruct.ShopMapstructMapper;
import com.moa.moa.api.shop.shop.domain.ShopProcessor;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopDto;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopLowPriceDto;
import com.moa.moa.api.shop.shop.domain.dto.FindLowPriceShopDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.shop.shop.util.ShopUtil;
import com.moa.moa.api.time.businesstime.domain.BusinessTimeProcessor;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopProcessor shopProcessor;
    private final WishProcessor wishProcessor;
    private final ShopMapstructMapper shopMapstructMapper;
    private final PlaceProcessor placeProcessor;
    private final PlaceShopProcessor placeShopProcessor;
    private final AddressProcessor addressProcessor;
    private final BusinessTimeProcessor businessTimeProcessor;
    private final MemberProcessor memberProcessor;

    public List<FindAllShopDto.Response> findAllShopWithinRange(Double leftTopX,
                                                                Double leftTopY,
                                                                Double rightBottomX,
                                                                Double rightBottomY,
                                                                Member member) {
        List<Shop> shops = shopProcessor.findAllShopWithinRange(leftTopX, leftTopY, rightBottomX, rightBottomY);
        List<FindAllShopDto.Response> findAllShopList = new ArrayList<>();

        for (Shop shop : shops) {
            Optional<Wish> optionalWish = wishProcessor.findWishByShopAndMember(shop, member);
            Wish wish = optionalWish.orElse(null);

            List<Review> reviews = shop.getReviews();
            Long moaTotalCount = (long) reviews.size();
            Double moaAvgScore = reviews.stream()
                    .mapToDouble(Review::getScore)
                    .average()
                    .orElse(0.0);

            NaverReview naverReview = shop.getNaverReview();
            if (naverReview == null) {
                naverReview = NaverReview.builder()
                        .avgScore(0.0)
                        .totalReview(0L)
                        .build();
            }

            List<Place> places = shop.getPlaceShops().stream()
                    .map(PlaceShop::getPlace)
                    .collect(Collectors.toList());

            findAllShopList.add(shopMapstructMapper.ofFindAllShop(
                    shop,
                    wish,
                    null,
                    shop.getItems(),
                    moaAvgScore,
                    moaTotalCount,
                    naverReview,
                    places
            ));
        }

        return findAllShopList;
    }

    public FindAllShopLowPriceDto.Response findAllShopSearchForTheLowestPrice(FindAllShopLowPriceDto.Request request,
                                                                              Member member) {

        // 1. place 정보 및 place에 맞는 shop_id 쿼리 만들기
        Place place = placeProcessor.findPlaceByIdAndDeletedAtIsNull(request.place().id())
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Place.NOT_FOUND));

        // 1-2. place에 맞는 address도 가져오기 TODO 추후에 image 추가해야함
        Address placeAddress = addressProcessor.findAddressById(place.getAddress().getId()).orElse(null);

        // 1-3. place에 맞는 shop_id 가져오기
        List<PlaceShop> shopsRelatedToPlace = placeShopProcessor.findAllShopRelatedToPlace(place);

        if (shopsRelatedToPlace.isEmpty()) throw new BusinessException(FailHttpMessage.Shop.NOT_FOUND_SHOP_RELATED_TO_PLACE);

        // 1-3-1. 비즈니스타임 한꺼번에 조회할 시 shopId 를 List에 넣기
        List<Long> shopIds = new ArrayList<>();
        for (int i = 0; i < shopsRelatedToPlace.size(); i++) {
            shopIds.add(shopsRelatedToPlace.get(i).getShop().getId());
        }

        Map<Long, Long> shopBusinessTimeIds = shopProcessor.findShopBusinessTimeId(shopIds)
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Shop.NOT_FOUND_SHOP_IN_OPERATION));

        // 1-3-2. shop_id와 visitDate를 확인해서 해당 날짜에 렌탈샵 운영하는지 확인하기
        // - shop에서 business_time_id를 list로 가져와서 businessTimeProcessor에서 코드 다시 짜기
        List<Long> shopsInOperation = new ArrayList<>();
        for (Long shopId : shopBusinessTimeIds.keySet()) {
            Long businessTimeId = shopBusinessTimeIds.get(shopId);
            // 해당 요일, visitDate
            DayType day = ShopUtil.date.getDayType(request.place().visitDate());

            Boolean operating = businessTimeProcessor.isShopInOperation(businessTimeId, request.place().visitDate(), day);

            if (operating) shopsInOperation.add(shopId);
        }

        if (shopsInOperation.isEmpty()) throw new BusinessException(FailHttpMessage.Shop.NOT_FOUND_SHOP_IN_OPERATION);

        // 2-1. 본격적으로 shop 조회하기 전에 요청시에 리프트권, 의류, 장비 정보를 받아와야 한다. customDto에 담기
        /*
         enum 데이터들을 문자열에서 숫자로 바꿔서 저장해야 함 - 자동변환이 되서 바꿀 필요없구나 코드에서 확인함
         List<Custom> customByIdAndEquip = customProcessor.findCustomByIdAndEquip(request.custom().get(0).equipmentType());
         */
        List<FindLowPriceCustomDto> customs = new ArrayList<>();
        for (int i=0;i<request.custom().size();i++) {
            FindAllShopLowPriceDto.CustomRequest customRequest = request.custom().get(i);

            // itemName -> "주중 스마트4시간권+장비+의류" 이름으로 조합해야 함 [visitDate, liftType, liftTime] 필요
            String itemName = ShopUtil.mix.getItemName(request.place().visitDate(), customRequest.liftType(),
                                                        customRequest.liftTime(), customRequest.packageType());

            // clotesType이랑 equipmentType을 보고 ItemOptionName을 추가해야 함
            List<ItemOptionName> itemOptionNames = ShopUtil.match.getItemOptionNames(customRequest.clothesType(), customRequest.equipmentType());

            FindLowPriceCustomDto custom = FindLowPriceCustomDto.builder()
                    .gender(customRequest.gender())
                    .nickname(customRequest.nickname())
                    .liftType(customRequest.liftType())
                    .liftTime(customRequest.liftTime())
                    .itemName(itemName)
                    .packageType(customRequest.packageType())
                    .clothesType(customRequest.clothesType())
                    .equipmentType(customRequest.equipmentType())
                    .itemOptionNames(itemOptionNames.isEmpty() ? null : itemOptionNames)
                    .build();

            customs.add(custom);
        }

        // 2-2. 1번에서 받은 shop_id로 조회 TODO 추후에 image 추가해야함
        List<FindLowPriceShopDto> shopLowPriceDtos = new ArrayList<>();
        Map<Long, Address> shopsAddress = new HashMap<>();
        Map<Long, Wish> wishShopsForMember = new HashMap<>();
        Map<Long, Member> shopOwner = new HashMap<>();
        for (Long shopId : shopsInOperation) {
            // 커스텀에 맞는 가격 찾기
            FindLowPriceShopDto shopLowPrice =
                    shopProcessor.findShopWithCustomForSearch(shopId, customs, request.shop().pickUp()).orElse(null);
            shopLowPriceDtos.add(shopLowPrice);

            // shop에 맞는 주소 찾기
            Shop shop = shopProcessor.findShopById(shopId)
                    .orElseThrow(() -> new BusinessException(FailHttpMessage.Shop.NOT_FOUND));
            Address shopAddress = addressProcessor.findAddressById(shop.getAddress().getId()).orElse(null);
            shopsAddress.put(shopId, shopAddress);

            // member와 맞는 wish 가져오기
            if (member != null) {
                Wish wish = wishProcessor.findWishByShopAndMember(shop, member).orElse(null);
                wishShopsForMember.put(shopId, wish);
            }

            // shop의 사장 이름 찾기
            Long memberId = shopProcessor.findMemberIdOfShopById(shopId).orElse(null);
            Member ownerMember = memberProcessor.findMemberByIdAndDeletedAtIsNull(memberId).orElse(null);
            shopOwner.put(shopId, ownerMember);
        }

        return shopMapstructMapper.ofFindAllLowestShops(
                request.place().visitDate(),
                place,
                placeAddress,
                null, // place의 image
                shopLowPriceDtos,
                null, // shop의 image
                request.custom(),
                shopsAddress,
                wishShopsForMember.isEmpty() ? null : wishShopsForMember, // member가 좋아하는 shop의 wish
                shopOwner
        );
    }
}
