package com.moa.moa.global.aws.s3.images.domain;

import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.aws.s3.images.domain.persistence.ImageRepository;
import com.moa.moa.global.common.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageProcessor {
    private final ImageRepository imageRepository;

    public Image addImage(Image image) {
        return imageRepository.save(image);
    }

    public List<Image> findAllImageByEntityIdAndEntityType(BaseEntity entity) {
        return imageRepository.findAllImageByEntityIdAndEntityType(entity);
    }
}
