package com.moa.moa.api.member.member.domain.entity;

import com.moa.moa.api.member.member.util.convert.MemberRoleConverter;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "member")
public class Member extends BaseEntity {
    @Comment("회원 이메일")
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(50)")
    private String email;

    @Comment("회원 별칭")
    @Column(name = "nickname", columnDefinition = "VARCHAR(50)")
    private String nickname;

    @Comment("회원 역할")
    @Column(name = "role", columnDefinition = "BIGINT")
    @Convert(converter = MemberRoleConverter.class)
    private MemberRole role;
}
