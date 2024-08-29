package com.moa.moa.api.member.custom.presentation;

import com.moa.moa.api.member.custom.domain.dto.AddCustomDto;
import com.moa.moa.api.member.custom.domain.dto.ModCustomDto;
import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.domain.persistence.CustomRepository;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.common.util.JsonConvertor;
import com.moa.moa.global.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CustomControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CustomRepository customRepository;

    @BeforeEach
    void beforeEach() {
        // 회원 생성
        List<Member> members = createMember();

        // 스키어 생성
        List<Custom> customs = createCustom(members);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        customRepository.deleteAll();
    }

    @Test
    @DisplayName("내 스키어 리스트 조회 성공 - deleteAt 조건 적용 확인")
    void t1() throws Exception {
        ResultActions actions = mvc
                .perform(get("/v1/customs")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CustomController.class))
                .andExpect(handler().methodName("findAllCustom"))
                .andExpect(jsonPath("$.length()", is(2)))

                .andExpect(jsonPath("$.[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.[0].memberId").value(memberRepository.findByEmail("three@moa.com").get().getId()))
                .andExpect(jsonPath("$.[0].gender").value(Gender.FEMALE.toString()))
                .andExpect(jsonPath("$.[0].nickname").value("삼남매 첫째"))
                .andExpect(jsonPath("$.[0].packageType").value(PackageType.LIFT_EQUIPMENT.toString()))
                .andExpect(jsonPath("$.[0].clothesType", nullValue()))
                .andExpect(jsonPath("$.[0].equipmentType").value(EquipmentType.SNOW_BOARD.toString()))
                .andExpect(jsonPath("$.[0].createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)));
    }

    @Test
    @DisplayName("내 스키어 추가 성공")
    void t2() throws Exception {
        AddCustomDto.Request request = AddCustomDto.Request.builder()
                .gender(Gender.FEMALE)
                .nickname("TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        ResultActions actions = mvc
                .perform(post("/v1/customs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConvertor.build(request))
                )
                .andDo(print());

        actions
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(CustomController.class))
                .andExpect(handler().methodName("addCustom"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)));
    }

    @Test
    @DisplayName("내 스키어 수정 성공")
    void t3() throws Exception {
        Member member = memberRepository.findByEmail("three@moa.com").get();
        Custom custom = customRepository.findAllCustomByMember(member).get(0);

        ModCustomDto.Request request = ModCustomDto.Request.builder()
                .gender(Gender.FEMALE)
                .nickname("TEST USER")
                .packageType(PackageType.LIFT_EQUIPMENT_CLOTHES)
                .clothesType(ClothesType.STANDARD)
                .equipmentType(EquipmentType.SNOW_BOARD)
                .build();

        ResultActions actions = mvc
                .perform(put("/v1/customs/" + custom.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConvertor.build(request))
                )
                .andDo(print());

        Custom modCustom = customRepository.findCustomById(custom.getId()).get();

        actions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CustomController.class))
                .andExpect(handler().methodName("modCustom"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)));

        assertThat(modCustom.getMember()).isEqualTo(member);
        assertThat(modCustom.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(modCustom.getNickname()).isEqualTo("TEST USER");
        assertThat(modCustom.getPackageType()).isEqualTo(PackageType.LIFT_EQUIPMENT_CLOTHES);
        assertThat(modCustom.getEquipmentType()).isEqualTo(EquipmentType.SNOW_BOARD);
        assertThat(modCustom.getClothesType()).isEqualTo(ClothesType.STANDARD);
    }

    private List<Member> createMember() {
        List<Member> list = new ArrayList<>();

        list.add(Member.builder().email("admin@moa.com").nickname("admin").role(MemberRole.ADMIN).build());
        list.add(Member.builder().email("one@moa.com").nickname("one").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("two@moa.com").nickname("two").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("three@moa.com").nickname("three").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("four@moa.com").nickname("four").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("five@moa.com").nickname("five").role(MemberRole.MEMBER).build());

        memberRepository.saveAll(list);

        return list;
    }

    private List<Custom> createCustom(List<Member> members) {
        List<Custom> list = new ArrayList<>();

        list.add(Custom.builder().member(members.get(1)).gender(Gender.MALE).nickname("일남이").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).equipmentType(EquipmentType.SKI).clothesType(ClothesType.STANDARD).build());
        list.add(Custom.builder().member(members.get(1)).gender(Gender.MALE).nickname("일친구").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.PREMIUM).build());
        list.add(Custom.builder().member(members.get(2)).gender(Gender.MALE).nickname("남친이").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.INLINE_SKI).build());
        list.add(Custom.builder().member(members.get(2)).gender(Gender.FEMALE).nickname("여친이").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.LUXURY).build());
        list.add(Custom.builder().member(members.get(3)).gender(Gender.FEMALE).nickname("삼남매 첫째").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SNOW_BOARD).build());
        list.add(Custom.builder().member(members.get(3)).gender(Gender.MALE).nickname("삼남매 둘째").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).equipmentType(EquipmentType.INLINE_SKI).clothesType(ClothesType.STANDARD).build());
        list.add(Custom.builder().member(members.get(3)).gender(Gender.FEMALE).nickname("삼남매 셋째").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.PREMIUM).deletedAt(LocalDateTime.now()).build()); // 삭제
        list.add(Custom.builder().member(members.get(4)).gender(Gender.FEMALE).nickname("시링").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SKI).build());
        list.add(Custom.builder().member(members.get(4)).gender(Gender.FEMALE).nickname("사월").packageType(PackageType.LIFT_EQUIPMENT_CLOTHES).clothesType(ClothesType.STANDARD).build());
        list.add(Custom.builder().member(members.get(5)).gender(Gender.MALE).nickname("오호츠크해").packageType(PackageType.LIFT_CLOTHES).clothesType(ClothesType.LUXURY).build());
        list.add(Custom.builder().member(members.get(5)).gender(Gender.FEMALE).nickname("오스트리아").packageType(PackageType.LIFT_EQUIPMENT).equipmentType(EquipmentType.SHORT_SKI).build());

        customRepository.saveAll(list);

        return list;
    }
}