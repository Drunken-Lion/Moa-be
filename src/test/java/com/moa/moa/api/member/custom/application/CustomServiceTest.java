package com.moa.moa.api.member.custom.application;

import com.moa.moa.api.member.custom.application.mapstruct.CustomMapstructMapper;
import com.moa.moa.api.member.custom.domain.CustomProcessor;
import com.moa.moa.api.member.custom.domain.dto.AddCustomDto;
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

    private List<Member> mockMembers;
    private List<Custom> mockCustoms;
    private List<FindAllCustomDto.Response> mockAllCustomResponses;

    @BeforeEach
    void beforeEach() {
        // 회원 생성
        List<Member> members = createMember();

        // 스키어 생성
        List<Custom> customs = createCustom(members);

        mockMembers = members;
        mockCustoms = customs;
        mockAllCustomResponses = createAllCustomResponse(customs);
    }

    @Test
    @DisplayName("내 스키어 리스트 조회 성공")
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

    @Test
    @DisplayName("내 스키어 추가 성공")
    public void t2() {
        // given
        AddCustomDto.Request request = AddCustomDto.Request.builder()
                .gender(Gender.FEMALE)
                .nickname("TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        Custom custom = Custom.builder()
                .id(10L)
                .member(mockMembers.get(3))
                .gender(Gender.FEMALE)
                .nickname("TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        when(customMapstructMapper.addOf(any(), any())).thenReturn(addMapper(request, 4L));
        when(customProcessor.addCustom(any())).thenReturn(custom);
        when(customMapstructMapper.addOf(any())).thenReturn(addMapper(custom));

        // when
        AddCustomDto.Response response = customService.addCustom(request, mockMembers.get(3));

        // then
        assertThat(response.id()).isEqualTo(10L);
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

    private AddCustomDto.Response addMapper(Custom custom) {
        if (custom == null) {
            return null;
        }

        Long id = null;

        id = custom.getId();

        AddCustomDto.Response response = new AddCustomDto.Response(id);

        return response;
    }

    private Custom addMapper(AddCustomDto.Request request, Long memberId) {
        if (request == null && memberId == null) {
            return null;
        }

        Custom.CustomBuilder<?, ?> custom = Custom.builder();

        if (request != null) {
            custom.gender(request.gender());
            custom.nickname(request.nickname());
            custom.packageType(request.packageType());
            custom.equipmentType(request.equipmentType());
            custom.clothesType(request.clothesType());
        }
        custom.member(longToMember(memberId));

        return custom.build();
    }

    protected Member longToMember(Long long1) {
        if (long1 == null) {
            return null;
        }

        Member.MemberBuilder<?, ?> member = Member.builder();

        member.id(long1);

        return member.build();
    }
}