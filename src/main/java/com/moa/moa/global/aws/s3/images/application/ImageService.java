package com.moa.moa.global.aws.s3.images.application;

import com.moa.moa.global.aws.s3.images.domain.ImageProcessor;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.aws.s3.images.domain.vaildation.ImageValidator;
import com.moa.moa.global.aws.s3.images.util.enumerated.EntityType;
import com.moa.moa.global.common.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageProcessor imageProcessor;
    private final ImageValidator imageValidator;

    public Long addImage(BaseEntity entity, String keyName) {
        imageValidator.validateExistImage(keyName);
        imageValidator.validateEntityId(entity);

        Image image = Image.builder()
                        .entityType(EntityType.getEntityType(entity))
                        .entityId(entity.getId())
                        .keyName(keyName)
                        .build();

        imageProcessor.addImage(image);

        return image.getId();
    }

    public List<Image> findAllImage(BaseEntity entity) {
        List<Image> images = imageProcessor.findAllImageByEntityIdAndEntityType(entity);

        return images;
    }
}