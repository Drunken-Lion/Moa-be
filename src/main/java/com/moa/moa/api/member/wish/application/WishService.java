package com.moa.moa.api.member.wish.application;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.wish.application.mapstruct.WishMapstructMapper;
import com.moa.moa.api.member.wish.domain.WishProcessor;
import com.moa.moa.api.member.wish.domain.dto.FindAllWishDto;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.global.common.response.PageExternalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishProcessor wishProcessor;
    private final WishMapstructMapper wishMapstructMapper;

    public PageExternalDto.Response<List<FindAllWishDto.Response>> findAllWish(Member member, Pageable pageable) {
        Slice<Wish> wishes = wishProcessor.findAllWishByMember(member, pageable);
        List<FindAllWishDto.Response> findAllWishList = new ArrayList<>();

        for (Wish wish : wishes) {
            List<Review> reviews = wish.getShop().getReviews();
            Long moaTotalCount = (long) reviews.size();
            Double moaAvgScore = reviews.stream()
                    .mapToDouble(Review::getScore)
                    .average()
                    .orElse(0.0);

            List<Place> places = wish.getShop().getPlaceShops().stream()
                    .map(PlaceShop::getPlace)
                    .collect(Collectors.toList());

            findAllWishList.add(wishMapstructMapper.ofFindAllWish(
                    wish,
                    wish.getShop(),
                    null,
                    wish.getShop().getAddress(),
                    moaAvgScore,
                    moaTotalCount,
                    places
            ));
        }

        return wishMapstructMapper.of(findAllWishList, pageable, wishProcessor.countMyWish(member));
    }
}
