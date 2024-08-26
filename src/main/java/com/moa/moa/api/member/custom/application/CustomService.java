package com.moa.moa.api.member.custom.application;

import com.moa.moa.api.member.custom.application.mapstruct.CustomMapstructMapper;
import com.moa.moa.api.member.custom.domain.CustomProcessor;
import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomService {
    private final CustomProcessor customProcessor;
    private final CustomMapstructMapper customMapstructMapper;

    public List<FindAllCustomDto.Response> findAllCustom(Member member) {
        List<Custom> customs = customProcessor.findAllCustomByMember(member);

        return customs.stream()
                .map(customMapstructMapper::of)
                .collect(Collectors.toList());
    }
}
