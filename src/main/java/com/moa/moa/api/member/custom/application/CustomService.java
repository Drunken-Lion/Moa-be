package com.moa.moa.api.member.custom.application;

import com.moa.moa.api.member.custom.application.mapstruct.CustomMapstructMapper;
import com.moa.moa.api.member.custom.domain.CustomProcessor;
import com.moa.moa.api.member.custom.domain.dto.AddCustomDto;
import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.dto.ModCustomDto;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public AddCustomDto.Response addCustom(AddCustomDto.Request request, Member member) {
        Custom custom = customProcessor.addCustom(customMapstructMapper.addOf(request, member.getId()));

        return customMapstructMapper.addOf(custom);
    }

    public ModCustomDto.Response modCustom(Long id, ModCustomDto.Request request, Member member) {
        Custom custom = customProcessor.findCustomById(id)
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Custom.NOT_FOUND));

        if(!Objects.equals(custom.getMember().getId(), member.getId())){
            throw new BusinessException(FailHttpMessage.Custom.FORBIDDEN);
        }

        Custom modCustom = customProcessor.modCustom(customMapstructMapper.modOf(custom, request));
        return customMapstructMapper.modOf(modCustom);
    }
}
