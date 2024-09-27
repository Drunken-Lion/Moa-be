package com.moa.moa.global.aws.s3.images.domain.persistence;

import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageDslRepository {
}
