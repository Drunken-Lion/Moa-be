package com.moa.moa.api.member.custom.domain.entity;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "custom")
public class Custom extends BaseEntity {
    @Comment("회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Comment("성별")
    @Column(name = "gender", unique = true, columnDefinition = "VARCHAR(20)")
    private String gender;

    @Comment("성별")
    @Column(name = "nickname", unique = true, columnDefinition = "VARCHAR(50)")
    private String nickname;

    @Comment("패키지 타입")
    @Column(name = "package_type", unique = true, columnDefinition = "VARCHAR(255)")
    private String packageType;

    @Comment("의류 타입")
    @Column(name = "clothes_type", unique = true, columnDefinition = "VARCHAR(50)")
    private String clothesType;

    @Comment("장비 타입")
    @Column(name = "equipment_type", unique = true, columnDefinition = "VARCHAR(100)")
    private String equipmentType;
}
