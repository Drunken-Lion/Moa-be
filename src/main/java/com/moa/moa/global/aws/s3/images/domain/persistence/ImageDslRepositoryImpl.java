package com.moa.moa.global.aws.s3.images.domain.persistence;

import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.aws.s3.images.util.enumerated.EntityType;
import com.moa.moa.global.common.entity.BaseEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.moa.moa.global.aws.s3.images.domain.entity.QImage.image;

@RequiredArgsConstructor
public class ImageDslRepositoryImpl implements ImageDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Image> findAllImageByEntityIdAndEntityType(BaseEntity entity) {
        return queryFactory.selectFrom(image)
                .where(image.entityId.eq(entity.getId())
                    .and(image.entityType.eq(EntityType.getEntityType(entity)))
                )
                .orderBy(image.id.asc())
                .fetch();
    }
}
