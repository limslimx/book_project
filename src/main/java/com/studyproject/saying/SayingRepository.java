package com.studyproject.saying;

import com.studyproject.domain.Saying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SayingRepository extends JpaRepository<Saying, Long> {
}
