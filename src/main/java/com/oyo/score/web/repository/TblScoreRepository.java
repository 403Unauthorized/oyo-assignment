package com.oyo.score.web.repository;

import com.oyo.score.web.entity.db.TblScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * {@link TblScore} JPA repository
 */
public interface TblScoreRepository extends JpaRepository<TblScore, Long>, JpaSpecificationExecutor<TblScore> {
}
