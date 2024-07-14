package com.moa.moa.api.cs.question.domain.entity;

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
@Table(name = "question")
public class Question extends BaseEntity {
    @Comment("문의 회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Comment("문의 타입")
    @Column(name = "type", columnDefinition = "VARCHAR(50)")
    private String type;

    @Comment("문의 제목")
    @Column(name = "title", columnDefinition = "VARCHAR(200)")
    private String title;

    @Comment("문의 내용")
    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    @Comment("문의 상태")
    @Column(name = "status", columnDefinition = "VARCHAR(20)")
    private String status;
}
