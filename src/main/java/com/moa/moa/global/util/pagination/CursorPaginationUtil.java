package com.moa.moa.global.util.pagination;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CursorPaginationUtil<T> {
    /**
     * 오름차순으로 데이터를 조회하기 위한 조건 메서드
     *
     * @param idPath ID 필드를 나타내는 NumberPath 객체
     * @param lastId 기준이 되는 ID 값
     * @return lastId보다 큰 ID를 가진 데이터를 조회하기 위한 조건식
     */
    public BooleanExpression gtCursorId(NumberPath<Long> idPath, int lastId) {
        return lastId == 0 ? null : idPath.gt(lastId);
    }

    /**
     * 내림차순으로 데이터를 조회하기 위한 조건 메서드
     *
     * @param idPath ID 필드를 나타내는 NumberPath 객체
     * @param lastId 기준이 되는 ID 값
     * @return lastId보다 작은 ID를 가진 데이터를 조회하기 위한 조건식
     */
    public BooleanExpression ltCursorId(NumberPath<Long> idPath, int lastId) {
        return lastId == 0 ? null : idPath.lt(lastId);
    }

    /**
     * 주어진 객체 목록에서 마지막 페이지 여부를 확인하고,
     * 페이지 크기에 맞게 객체 목록을 조정하여 Slice 형태로 반환해주는 메서드
     *
     * @param objects  조회된 객체 목록
     * @param pageable 페이징 정보 (페이지 번호 및 페이지 크기)
     * @return 요청한 페이지의 객체 목록과 마지막 페이지 여부를 포함하는 Slice 객체
     */
    public Slice<T> checkLastPage(List<T> objects, Pageable pageable) {
        boolean hasNext = false;

        if (objects.size() > pageable.getPageSize()) {
            hasNext = true;
            objects.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(objects, pageable, hasNext);
    }
}
