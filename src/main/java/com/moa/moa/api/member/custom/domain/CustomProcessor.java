package com.moa.moa.api.member.custom.domain;

import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.domain.persistence.CustomRepository;
import com.moa.moa.api.member.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomProcessor {
    private final CustomRepository customRepository;

    public List<Custom> findAllCustomByMember(Member member) {
        return customRepository.findAllCustomByMember(member);
    }

    public Optional<Custom> findCustomById(Long id) {
        return customRepository.findCustomById(id);
    }

    public Custom addCustom(Custom custom) {
        return customRepository.save(custom);
    }

    public Custom modCustom(Custom custom) {
        return customRepository.save(custom);
    }
}
