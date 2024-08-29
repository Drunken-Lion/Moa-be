package com.moa.moa.api.member.custom.domain.persistence;

import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.member.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface CustomDslRepository {
    List<Custom> findAllCustomByMember(Member member);

    Optional<Custom> findCustomById(Long id);
}
