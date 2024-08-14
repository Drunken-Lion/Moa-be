package com.moa.moa.api.shop.review.domain.persistence;

import com.moa.moa.api.shop.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
