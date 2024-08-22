package com.moa.moa.api.member.custom.domain.persistence;

import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CustomRepositoryTest {
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
    public void t1() {
        // given
        Member member = memberRepository.findById(4L).get();

        //when
        List<Custom> customs = customRepository.findAllCustomByMember(member);

        //then
        assertThat(customs.size()).isEqualTo(2);
        assertThat(customs.get(0).getMember()).isEqualTo(member);
        assertThat(customs.get(0).getGender()).isEqualTo(Gender.FEMALE);
        assertThat(customs.get(0).getNickname()).isEqualTo("삼남매 첫째");
        assertThat(customs.get(0).getPackageType()).isEqualTo(PackageType.LIFT_EQUIPMENT);
        assertThat(customs.get(0).getEquipmentType()).isEqualTo(EquipmentType.SNOW_BOARD);
        assertThat(customs.get(0).getClothesType()).isEqualTo(null);
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