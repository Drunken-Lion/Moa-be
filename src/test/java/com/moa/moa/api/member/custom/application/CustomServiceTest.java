package com.moa.moa.api.member.custom.application;

import com.moa.moa.api.member.custom.application.mapstruct.CustomMapstructMapper;
import com.moa.moa.api.member.custom.application.mapstruct.CustomMapstructMapperImpl;
import com.moa.moa.api.member.custom.domain.CustomProcessor;
import com.moa.moa.api.member.custom.domain.dto.AddCustomDto;
import com.moa.moa.api.member.custom.domain.dto.FindAllCustomDto;
import com.moa.moa.api.member.custom.domain.dto.ModCustomDto;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomServiceTest {
    @Mock
    private CustomProcessor customProcessor;

    @Mock
    private CustomMapstructMapper customMapstructMapper;

    private CustomMapstructMapperImpl customMapstructMapperImpl;

    @InjectMocks
    private CustomService customService;

    private List<Member> mockMembers;
    private List<Custom> mockCustoms;
    private List<FindAllCustomDto.Response> mockAllCustomResponses;

    @BeforeEach
    void beforeEach() {
        customMapstructMapperImpl = new CustomMapstructMapperImpl();

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

        when(customMapstructMapper.addOf(any(), any())).thenReturn(customMapstructMapperImpl.addOf(request, 4L));
        when(customProcessor.addCustom(any())).thenReturn(custom);
        when(customMapstructMapper.addOf(any())).thenReturn(customMapstructMapperImpl.addOf(custom));

        // when
        AddCustomDto.Response response = customService.addCustom(request, mockMembers.get(3));

        // then
        assertThat(response.id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("내 스키어 수정 성공")
    public void t3() {
        // given
        ModCustomDto.Request request = ModCustomDto.Request.builder()
                .gender(Gender.FEMALE)
                .nickname("NEW TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        Custom preCustom = Custom.builder()
                .id(10L)
                .member(mockMembers.get(3))
                .gender(Gender.MALE)
                .nickname("PRE TEST USER")
                .packageType(PackageType.LIFT_CLOTHES)
                .clothesType(ClothesType.LUXURY)
                .equipmentType(null)
                .build();

        Custom newCustom = Custom.builder()
                .id(10L)
                .member(mockMembers.get(3))
                .gender(Gender.FEMALE)
                .nickname("NEW TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        when(customProcessor.findCustomById(anyLong())).thenReturn(Optional.of(preCustom));
        when(customMapstructMapper.modOf(any(), any())).thenReturn(customMapstructMapperImpl.modOf(preCustom, request));
        when(customProcessor.modCustom(any())).thenReturn(newCustom);
        when(customMapstructMapper.modOf(any())).thenReturn(customMapstructMapperImpl.modOf(newCustom));

        // when
        ModCustomDto.Response response = customService.modCustom(10L, request, mockMembers.get(3));

        // then
        assertThat(response.id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("내 스키어 수정 실패 - 존재하지 않는 스키어")
    public void t4() {
        // given
        ModCustomDto.Request request = ModCustomDto.Request.builder()
                .gender(Gender.FEMALE)
                .nickname("NEW TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        when(customProcessor.findCustomById(anyLong())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customService.modCustom(10L, request, mockMembers.get(3));
        });

        // then
        assertEquals(FailHttpMessage.Custom.NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(FailHttpMessage.Custom.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("내 스키어 수정 실패 - 권한이 없는 사용자")
    public void t5() {
        // given
        ModCustomDto.Request request = ModCustomDto.Request.builder()
                .gender(Gender.FEMALE)
                .nickname("NEW TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        Custom preCustom = Custom.builder()
                .id(10L)
                .member(mockMembers.get(2))
                .gender(Gender.MALE)
                .nickname("PRE TEST USER")
                .packageType(PackageType.LIFT_CLOTHES)
                .clothesType(ClothesType.LUXURY)
                .equipmentType(null)
                .build();

        when(customProcessor.findCustomById(anyLong())).thenReturn(Optional.of(preCustom));

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customService.modCustom(10L, request, mockMembers.get(3));
        });

        // then
        assertEquals(FailHttpMessage.Custom.FORBIDDEN.getStatus(), exception.getStatus());
        assertEquals(FailHttpMessage.Custom.FORBIDDEN.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("내 스키어 삭제 성공")
    public void t6() {
        // given
        Custom preCustom = Custom.builder()
                .id(10L)
                .member(mockMembers.get(3))
                .gender(Gender.MALE)
                .nickname("PRE TEST USER")
                .packageType(PackageType.LIFT_CLOTHES)
                .clothesType(ClothesType.LUXURY)
                .equipmentType(null)
                .build();

        when(customProcessor.findCustomById(anyLong())).thenReturn(Optional.of(preCustom));
        ArgumentCaptor<Custom> categoryCaptor = ArgumentCaptor.forClass(Custom.class);

        // when
        customService.delCustom(10L, mockMembers.get(3));

        // then
        verify(customProcessor).delCustom(categoryCaptor.capture());
        Custom savedCustom = categoryCaptor.getValue();
        Assertions.assertThat(savedCustom.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("내 스키어 삭제 실패 - 존재하지 않는 스키어")
    public void t7() {
        // given
        when(customProcessor.findCustomById(anyLong())).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customService.delCustom(10L, mockMembers.get(3));
        });

        // then
        assertEquals(FailHttpMessage.Custom.NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(FailHttpMessage.Custom.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("내 스키어 삭제 실패 - 권한이 없는 사용자")
    public void t8() {
        // given
        Custom preCustom = Custom.builder()
                .id(10L)
                .member(mockMembers.get(2))
                .gender(Gender.MALE)
                .nickname("PRE TEST USER")
                .packageType(PackageType.LIFT_CLOTHES)
                .clothesType(ClothesType.LUXURY)
                .equipmentType(null)
                .build();

        when(customProcessor.findCustomById(anyLong())).thenReturn(Optional.of(preCustom));

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customService.delCustom(10L, mockMembers.get(3));
        });

        // then
        assertEquals(FailHttpMessage.Custom.FORBIDDEN.getStatus(), exception.getStatus());
        assertEquals(FailHttpMessage.Custom.FORBIDDEN.getMessage(), exception.getMessage());
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
        List<FindAllCustomDto.Response> responses = new ArrayList<>();
        for (Custom custom : customs) {
            FindAllCustomDto.Response response = customMapstructMapperImpl.of(custom);
            responses.add(response);
        }
        return responses;
    }
}