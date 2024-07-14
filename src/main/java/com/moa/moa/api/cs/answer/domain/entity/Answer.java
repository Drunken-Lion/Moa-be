package com.moa.moa.api.cs.answer.domain.entity;

import com.moa.moa.api.cs.question.domain.entity.Question;
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
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Comment("문의")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Question question;

    @Comment("문의 답변 회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Comment("문의 답변")
    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;
}
