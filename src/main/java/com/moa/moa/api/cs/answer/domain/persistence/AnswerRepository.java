package com.moa.moa.api.cs.answer.domain.persistence;

import com.moa.moa.api.cs.answer.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
