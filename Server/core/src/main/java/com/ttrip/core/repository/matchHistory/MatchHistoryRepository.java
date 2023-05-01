package com.ttrip.core.repository.matchHistory;

import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Integer> {
    List<MatchHistory> findByEvaluator(Member evaluator);
    Optional<MatchHistory> findByMatchHistoryId(Integer matchHistoryId);
}