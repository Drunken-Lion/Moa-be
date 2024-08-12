package com.moa.moa.api.cs.question.domain.persistence;

import com.moa.moa.api.cs.question.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
