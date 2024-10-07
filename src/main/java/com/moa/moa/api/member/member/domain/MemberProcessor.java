package com.moa.moa.api.member.member.domain;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberProcessor {
    private final MemberRepository memberRepository;

    public Optional<Member> findMemberByIdAndDeletedAtIsNull(Long memberId) {
        return memberRepository.findMemberByIdAndDeletedAtIsNull(memberId);
    }
}
