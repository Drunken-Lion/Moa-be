package com.moa.moa.api.shop.naverreview.domain.persistence;

import com.moa.moa.api.shop.naverreview.domain.entity.NaverReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverReviewRepository extends JpaRepository<NaverReview, Long> {
}
