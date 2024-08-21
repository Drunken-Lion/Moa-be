package com.moa.moa.api.member.custom.domain.persistence;

import com.moa.moa.api.member.custom.domain.entity.Custom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepository extends JpaRepository<Custom, Long>, CustomDslRepository {
}
