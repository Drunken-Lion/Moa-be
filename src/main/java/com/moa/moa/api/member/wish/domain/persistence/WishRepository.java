package com.moa.moa.api.member.wish.domain.persistence;

import com.moa.moa.api.member.wish.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
}
