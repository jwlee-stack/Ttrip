package com.ttrip.core.repository.survey;

import com.ttrip.core.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Integer> {
    Optional<Survey> findBySurveyId(Integer memberId);
}
