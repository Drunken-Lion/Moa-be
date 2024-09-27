package com.moa.moa.global.aws.s3.images.domain.persistence;

import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.common.entity.BaseEntity;

import java.util.List;

public interface ImageDslRepository {
    List<Image> findAllImageByEntityIdAndEntityType(BaseEntity entity);
}
