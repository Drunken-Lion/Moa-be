package com.moa.moa.api.place.place.domain.persistence;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.placeamenity.domain.entity.PlaceAmenity;
import com.moa.moa.api.place.slope.domain.entity.Slope;
import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.moa.moa.api.address.address.domain.entity.QAddress.address1;
import static com.moa.moa.api.place.place.domain.entity.QPlace.place;
import static com.moa.moa.api.place.placeamenity.domain.entity.QPlaceAmenity.placeAmenity;
import static com.moa.moa.api.place.slope.domain.entity.QSlope.slope;
import static com.moa.moa.api.time.businesstime.domain.entity.QBusinessTime.businessTime;
import static com.moa.moa.api.time.operatingtime.domain.entity.QOperatingTime.operatingTime;
import static com.moa.moa.api.time.specificday.domain.entity.QSpecificDay.specificDay;

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

        List<Place> places = queryFactory.selectFrom(place)
                .leftJoin(place.address, address1).fetchJoin()
                .leftJoin(place.businessTime, businessTime).fetchJoin()
                .where(inPolygon
                        .and(place.deletedAt.isNull())
                        .and(address1.deletedAt.isNull())
                        .and(businessTime.deletedAt.isNull())
                )
                .orderBy(place.id.asc())
                .fetch();

        for (Place place : places) {
            List<OperatingTime> operatingTimes = queryFactory.selectFrom(operatingTime)
                    .where(operatingTime.businessTime.eq(place.getBusinessTime())
                            .and(operatingTime.deletedAt.isNull()))
                    .fetch();
            place.getBusinessTime().addOperatingTimes(operatingTimes);

            List<SpecificDay> specificDays = queryFactory.selectFrom(specificDay)
                    .where(specificDay.businessTime.eq(place.getBusinessTime())
                            .and(specificDay.deletedAt.isNull()))
                    .fetch();
            place.getBusinessTime().addSpecificDays(specificDays);

            List<PlaceAmenity> placeAmenities = queryFactory.selectFrom(placeAmenity)
                    .where(placeAmenity.place.eq(place)
                            .and(placeAmenity.used.isTrue())
                            .and(placeAmenity.deletedAt.isNull())
                            .and(placeAmenity.amenity.deletedAt.isNull()))
                    .fetch();
            place.addPlaceAmenities(placeAmenities);

            List<Slope> slopes = queryFactory.selectFrom(slope)
                    .where(slope.place.eq(place)
                            .and(slope.deletedAt.isNull()))
                    .fetch();
            place.addSlopes(slopes);
        }

        return places;
    }
}
