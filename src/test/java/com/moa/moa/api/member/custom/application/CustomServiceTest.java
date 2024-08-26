package com.moa.moa.api.member.custom.application;

import com.moa.moa.api.member.custom.application.mapstruct.CustomMapstructMapper;
import com.moa.moa.api.member.custom.domain.CustomProcessor;
import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomServiceTest {
    @Mock
    private CustomProcessor customProcessor;

    @Mock
    private CustomMapstructMapper customMapstructMapper;

    @InjectMocks
    private CustomService customService;

    private List<Custom> mockCustoms;
    private List<FindAllCustomDto.Response> mockAllCustomResponses;

    @BeforeEach
    void beforeEach() {
        // 회원 생성
        List<Member> members = createMember();

        // 스키어 생성
        List<Custom> customs = createCustom(members);

        mockCustoms = customs;
        mockAllCustomResponses = createAllCustomResponse(customs);
    }

    @Test
    @DisplayName("스키장 목록 조회 성공")
    public void t1() {
        // given
        when(customProcessor.findAllCustomByMember(any())).thenReturn(mockCustoms);

        for (int i = 0; i < mockCustoms.size(); i++) {
            when(customMapstructMapper.of(
                    eq(mockCustoms.get(i))
            )).thenReturn(mockAllCustomResponses.get(i));
        }

        // when
        List<FindAllCustomDto.Response> customs = customService.findAllCustom(any());

        // then
        assertThat(customs.size()).isEqualTo(3);

        FindAllCustomDto.Response customResponse = customs.get(0);
        assertThat(customResponse.id()).isEqualTo(1L);
        assertThat(customResponse.memberId()).isEqualTo(4L);
        assertThat(customResponse.gender()).isEqualTo(Gender.FEMALE);
        assertThat(customResponse.nickname()).isEqualTo("삼남매 첫째");
        assertThat(customResponse.packageType()).isEqualTo(PackageType.LIFT_EQUIPMENT);
        assertThat(customResponse.clothesType()).isEqualTo(null);
        assertThat(customResponse.equipmentType()).isEqualTo(EquipmentType.SNOW_BOARD);
    }

    private List<Member> createMember() {
        List<Member> list = new ArrayList<>();

        list.add(Member.builder().id(1L).email("admin@moa.com").nickname("admin").role(MemberRole.ADMIN).build());
        list.add(Member.builder().id(2L).email("one@moa.com").nickname("one").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(3L).email("two@moa.com").nickname("two").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(4L).email("three@moa.com").nickname("three").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(5L).email("four@moa.com").nickname("four").role(MemberRole.MEMBER).build());
        list.add(Member.builder().id(6L).email("five@moa.com").nickname("five").role(MemberRole.MEMBER).build());

        return list;
    }

    private List<Custom> createCustom(List<Member> members) {
        List<Custom> list = new ArrayList<>();

        list.add(Custom.builder().id(1L).member(members.get(3)).gender(Gender.FEMALE).nickname("삼남매 첫째").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SNOW_BOARD).build());
        list.add(Custom.builder().id(2L).member(members.get(3)).gender(Gender.MALE).nickname("삼남매 둘째").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).equipmentType(EquipmentType.INLINE_SKI).clothesType(ClothesType.STANDARD).build());
        list.add(Custom.builder().id(3L).member(members.get(3)).gender(Gender.FEMALE).nickname("삼남매 셋째").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.PREMIUM).build());

        return list;
    }

    private List<FindAllCustomDto.Response> createAllCustomResponse(List<Custom> customs) {
        return customs.stream()
                .map(this::allMapper)
                .collect(Collectors.toList());
    }

    private FindAllCustomDto.Response allMapper(Custom custom) {
        return FindAllCustomDto.Response.builder()
                .id(custom.getId())
                .memberId(custom.getMember().getId())
                .gender(custom.getGender())
                .nickname(custom.getNickname())
                .packageType(custom.getPackageType())
                .clothesType(custom.getClothesType())
                .equipmentType(custom.getEquipmentType())
                .createdAt(custom.getCreatedAt())
                .build();
    }
}