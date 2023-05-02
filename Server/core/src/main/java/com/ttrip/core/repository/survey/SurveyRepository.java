package com.ttrip.core.repository.survey;

import com.ttrip.core.entity.member.Member;
import com.ttrip.core.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Integer> {
    boolean existsByMember(Member member);
    Optional<Survey> findByMember(Member member);
}
