package com.moa.moa.api.member.member.domain.persistence;

import com.moa.moa.api.member.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
