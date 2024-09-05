package com.moa.moa.api.cs.question.domain.persistence;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.util.pagination.CursorPaginationUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

import static com.moa.moa.api.cs.answer.domain.entity.QAnswer.answer;
import static com.moa.moa.api.cs.question.domain.entity.QQuestion.question;
import static com.moa.moa.api.member.member.domain.entity.QMember.member;

@RequiredArgsConstructor
public class QuestionDslRepositoryImpl implements QuestionDslRepository {
    private final JPAQueryFactory queryFactory;
    private final CursorPaginationUtil<Question> cursorPaginationUtil;

    @Override
    public Slice<Question> findAllMyQuestion(Member authMember, Pageable pageable) {
        BooleanBuilder cond = new BooleanBuilder();

        cond.and(question.member.eq(authMember))
                .and(cursorPaginationUtil.ltCursorId(question.id, pageable.getPageNumber()))
                .and(question.deletedAt.isNull());

        List<Question> questions = queryFactory
                .selectFrom(question)
                .where(cond)
                .orderBy(question.createdAt.desc(), question.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return cursorPaginationUtil.checkLastPage(questions, pageable);
    }

    @Override
    public Optional<Question> findQuestionById(Long id) {
        BooleanBuilder cond = new BooleanBuilder();

        cond.and(question.id.eq(id))
                .and(question.deletedAt.isNull());

        Question findQuestion = queryFactory
                .selectFrom(question)
                .leftJoin(question.member, member)
                .leftJoin(question.answers, answer)
                .fetchJoin()
                .where(cond)
                .fetchOne();

        return Optional.ofNullable(findQuestion);
    }
}
