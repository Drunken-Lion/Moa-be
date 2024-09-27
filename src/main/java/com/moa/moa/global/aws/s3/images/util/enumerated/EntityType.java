package com.moa.moa.global.aws.s3.images.util.enumerated;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.member.wish.domain.entity.Wish;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.review.domain.entity.Review;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.common.entity.BaseEntity;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

@RequiredArgsConstructor
public enum EntityType {
    IMAGE("image", Image.builder().build()),
    PLACE("place", Place.builder().build()),
    QUESTION("question", Question.builder().build()),
    REVIEW("review", Review.builder().build()),
    SHOP("shop", Shop.builder().build()),
    WISH("wish", Wish.builder().build())
    ;

    private final String entityType;
    private final BaseEntity entityObj;

    public static String getEntityType(BaseEntity entityObj) {
        Class<?> classType = getClassType(entityObj);

        for (EntityType entity : EntityType.values()) {
            if (classType.isInstance(entity.entityObj)) {
                return entity.entityType;
            }
        }

        throw new BusinessException(FailHttpMessage.Image.BAD_REQUEST);
    }

    private static Class<?> getClassType(BaseEntity entityObj) {
        if (entityObj instanceof HibernateProxy proxy) {
            LazyInitializer initializer = proxy.getHibernateLazyInitializer();
            return initializer.getPersistentClass();
        }

        return entityObj.getClass();
    }
}
