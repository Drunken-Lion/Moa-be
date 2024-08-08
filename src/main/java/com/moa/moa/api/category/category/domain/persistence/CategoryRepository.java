package com.moa.moa.api.category.category.domain.persistence;

import com.moa.moa.api.category.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
