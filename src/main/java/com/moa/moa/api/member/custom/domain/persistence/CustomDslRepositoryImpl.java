package com.moa.moa.api.member.custom.domain.persistence;

import com.moa.moa.api.member.custom.domain.entity.Custom;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.moa.moa.api.member.custom.domain.entity.QCustom.custom;

@RequiredArgsConstructor
public class CustomDslRepositoryImpl implements CustomDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Custom> findAllCustomByMember(Member member) {
        return queryFactory.selectFrom(custom)
                .where(custom.member.eq(member)
                        .and(custom.deletedAt.isNull()))
                .orderBy(custom.id.asc())
                .fetch();
    }

    @Override
    public Optional<Custom> findCustomById(Long id) {
        Custom customOne = queryFactory.selectFrom(custom)
                .where(custom.id.eq(id)
                        .and(custom.deletedAt.isNull()))
                .fetchOne();

        return Optional.ofNullable(customOne);
    }
}
