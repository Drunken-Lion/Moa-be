package com.moa.moa.api.member.custom.domain;

import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.domain.persistence.CustomRepository;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.place.place.domain.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomProcessor {
    private final CustomRepository customRepository;

    public List<Custom> findAllCustomByMember(Member member) {
        return customRepository.findAllCustomByMember(member);
    }
}
