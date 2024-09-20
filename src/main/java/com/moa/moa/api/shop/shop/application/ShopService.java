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

import java.math.BigDecimal;
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

        // 1-1. place 정보
        Place place = placeProcessor.findPlaceByIdAndDeletedAtIsNull(request.place().id())
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Place.NOT_FOUND));

        // 1-2. place에 맞는 address 정보
        Address placeAddress = addressProcessor.findAddressByIdAndDeletedAtIsNull(place.getAddress().getId()).orElse(null);

        // 1-3. TODO image 기능 완성 시 구현 추가

        // 1-4. place에 맞는 shop_id 조회
        List<PlaceShop> shopsRelatedToPlace = placeShopProcessor.findAllShopRelatedToPlace(place);

        if (shopsRelatedToPlace.isEmpty()) throw new BusinessException(FailHttpMessage.Shop.NOT_FOUND_SHOP_RELATED_TO_PLACE);

        // 1-4-1. shopId List 생성
        List<Long> shopIds = new ArrayList<>();
        for (int i = 0; i < shopsRelatedToPlace.size(); i++) {
            shopIds.add(shopsRelatedToPlace.get(i).getShop().getId());
        }

        // 1-5. shop에서 businessTimeId 조회
        Map<Long, Long> shopBusinessTimeIds = shopProcessor.findBusinessTimeIdOfShops(shopIds)
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Shop.NOT_FOUND_SHOP_IN_OPERATION));

        // 1-6. shop_id와 visitDate를 확인해서 해당 날짜에 렌탈샵 운영하는지 확인
        List<Long> shopsInOperation = new ArrayList<>();
        for (Long shopId : shopBusinessTimeIds.keySet()) {
            Long businessTimeId = shopBusinessTimeIds.get(shopId);

            DayType day = ShopUtil.date.getDayType(request.place().visitDate()); // 요일 확인 후 enum으로 변환

            Boolean operating = businessTimeProcessor.isShopInOperation(businessTimeId, request.place().visitDate(), day);

            if (operating) shopsInOperation.add(shopId); // 운영하는 샵만 추가
        }

        if (shopsInOperation.isEmpty()) throw new BusinessException(FailHttpMessage.Shop.NOT_FOUND_SHOP_IN_OPERATION);

        // 2. 조회 하기 편하게 요청 시에 받은 리프트권, 의류, 장비 정보 가공 후 customDto에 담기
        List<FindLowPriceCustomDto> customs = new ArrayList<>();
        for (int i = 0; i<request.customs().size(); i++) {
            FindAllShopLowPriceDto.CustomRequest customRequest = request.customs().get(i);

            // itemName -> "주중 스마트4시간권+장비+의류" 이름으로 조합
            String itemName = ShopUtil.mix.getItemName(request.place().visitDate(), customRequest.liftType(),
                                                        customRequest.liftTime(), customRequest.packageType());

            // clotesType이랑 equipmentType을 보고 ItemOptionName으로 변경
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

        // 3. 1번에서 받은 운영 중인 shop_id로 조회 TODO image 기능 완성 시 구현 추가
        List<FindLowPriceShopDto> shopLowPriceDtos = new ArrayList<>();
        Map<Long, Address> shopsAddress = new HashMap<>();
        Map<Long, Wish> wishShopsForMember = new HashMap<>();
        Map<Long, Member> shopsOwner = new HashMap<>();
        for (Long shopId : shopsInOperation) {
            // 커스텀에 맞는 가격 조회
            FindLowPriceShopDto shopLowPrice =
                    shopProcessor.findShopWithCustomForSearch(shopId, customs, request.shop().pickUp()).orElse(null);

            if (shopLowPrice == null) {
                // shop 조회로 데이터를 가져오지 못하면 shopLowPriceDtos 에 추가 하지 않음
                continue;
            }

            // 만약 커스텀 price에 하나라도 0원이 있으면 shopLowPriceDtos에 추가 하지 않고 다음 로직을 수행하지 않음
            boolean shopLowPriceDtoAdd = true;
            for (BigDecimal price : shopLowPrice.getCustomPrices()) {
                if (price.equals(BigDecimal.ZERO) || price.equals(BigDecimal.valueOf(0.0))) {
                    shopLowPriceDtoAdd = false;
                    break;
                }
            }
            if (!shopLowPriceDtoAdd) continue;

            shopLowPriceDtos.add(shopLowPrice);

            // shop에 맞는 주소 조회
            Shop shop = shopProcessor.findShopByIdAndDeletedAtIsNull(shopId)
                    .orElseThrow(() -> new BusinessException(FailHttpMessage.Shop.NOT_FOUND));
            Address shopAddress = addressProcessor.findAddressByIdAndDeletedAtIsNull(shop.getAddress().getId()).orElse(null);
            shopsAddress.put(shopId, shopAddress);

            // member와 맞는 wish 가져오기
            if (member != null) {
                Wish wish = wishProcessor.findWishByShopAndMember(shop, member).orElse(null);
                wishShopsForMember.put(shopId, wish);
            }

            // shop의 사장 이름 찾기
            Long memberId = shopProcessor.findMemberIdOfShopById(shopId).orElse(null);
            Member ownerMember = memberProcessor.findMemberByIdAndDeletedAtIsNull(memberId).orElse(null);
            shopsOwner.put(shopId, ownerMember);
        }

        // shopLowPriceDtos에 값이 없을 경우 '커스텀이 일치하는 게 없다'는 뜻으로 에러 발생
        if (shopLowPriceDtos.isEmpty()) throw new BusinessException(FailHttpMessage.Shop.NOT_FOUND_MATCHING_SHOP);

        return shopMapstructMapper.ofFindAllLowestShops(
                request.place().visitDate(),
                place,
                placeAddress,
                null, // place의 image
                shopLowPriceDtos,
                null, // shop의 image
                request.customs(),
                shopsAddress,
                wishShopsForMember.isEmpty() ? null : wishShopsForMember,
                shopsOwner
        );
    }
}
