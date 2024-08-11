package com.moa.moa.api.cs.question.domain.persistence;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.moa.moa.api.cs.question.domain.entity.QQuestion.question;

@RequiredArgsConstructor
public class QuestionDslRepositoryImpl implements QuestionDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Question> findAllMyQuestion(Member authMember, Pageable pageable) {
        BooleanBuilder cond = new BooleanBuilder();

        cond.and(question.member.eq(authMember))
                .and(ltCursorId(pageable.getPageNumber()))
                .and(question.deletedAt.isNull());

        List<Question> questions = queryFactory
                .selectFrom(question)
                .where(cond)
                .orderBy(question.createdAt.desc(), question.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkListPage(questions, pageable);
    }

    private BooleanExpression ltCursorId(int lastId) {
        return lastId == 0 ? null : question.id.lt(lastId);
    }

    private Slice<Question> checkListPage(List<Question> questions, Pageable pageable) {
        boolean hasNext = false;

        if (questions.size() > pageable.getPageSize()) {
            hasNext = true;
            questions.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(questions, pageable, hasNext);
    }
}
