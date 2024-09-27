package com.moa.moa.global.aws.s3.images.domain.vaildation;

import com.moa.moa.global.aws.s3.bucket.application.BucketService;
import com.moa.moa.global.common.entity.BaseEntity;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class ImageValidator {
    private final BucketService bucketService;

    public ImageValidator(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public void validateEntityId(BaseEntity entity) {
        if (entity.getId() == null) {
            throw new BusinessException(FailHttpMessage.Image.BAD_REQUEST);
        }
    }

    public void validateExistImage(String keyName) {
        bucketService.read(keyName);
    }
}
