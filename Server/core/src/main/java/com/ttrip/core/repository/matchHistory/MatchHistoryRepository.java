package com.ttrip.core.repository.matchHistory;

import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Integer> {
    List<MatchHistory> findByEvaluator(Member evaluator);
}