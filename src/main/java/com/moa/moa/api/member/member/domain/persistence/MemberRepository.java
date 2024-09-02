package com.moa.moa.api.member.member.domain.persistence;

import com.moa.moa.api.member.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // TODO: 회원 관련 기능이 완성되면 삭제할 것
    Optional<Member> findByEmail(String email);
}
