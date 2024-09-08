package com.moa.moa.api.shop.shop.application;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.domain.WishProcessor;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.application.mapstruct.ShopMapstructMapper;
import com.moa.moa.api.shop.shop.domain.ShopProcessor;
import com.moa.moa.api.shop.shop.domain.dto.FindAllShopDto;
import com.moa.moa.api.shop.shop.domain.dto.FindShopDto;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopProcessor shopProcessor;
    private final WishProcessor wishProcessor;
    private final ShopMapstructMapper shopMapstructMapper;

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

    public FindShopDto.Response findShop(Long id, Member member) {
        Shop shop = shopProcessor.findShopById(id)
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Shop.NOT_FOUND));

        Optional<Wish> optionalWish = member != null ? wishProcessor.findWishByShopAndMember(shop, member) : Optional.empty();

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

        return shopMapstructMapper.ofFindShop(
                shop,
                optionalWish.isPresent(),
                places,
                null,
                shop.getAddress(),
                moaAvgScore,
                moaTotalCount,
                naverReview
        );
    }
}
