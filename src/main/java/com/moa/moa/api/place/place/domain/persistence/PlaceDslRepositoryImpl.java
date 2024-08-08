package com.moa.moa.api.place.place.domain.persistence;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.moa.moa.api.address.address.domain.entity.QAddress.address1;
import static com.moa.moa.api.place.place.domain.entity.QPlace.place;

@RequiredArgsConstructor
public class PlaceDslRepositoryImpl implements PlaceDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findAllPlaceInMap(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY) {
        String polygon = String.format("POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                leftTopX, leftTopY,   // 왼쪽 위
                rightBottomX, leftTopY, // 오른쪽 위
                rightBottomX, rightBottomY, // 오른쪽 아래
                leftTopX, rightBottomY, // 왼쪽 아래
                leftTopX, leftTopY); // 다시 왼쪽 위로 돌아오기

        BooleanExpression inPolygon = Expressions.booleanTemplate("ST_Contains(ST_PolygonFromText({0}), {1})", polygon, address1.location);

        return queryFactory.selectFrom(place)
                .join(place.address, address1)
                .where(inPolygon
                        .and(place.deletedAt.isNull())
                        .and(address1.deletedAt.isNull()))
                .fetch();
    }
}
