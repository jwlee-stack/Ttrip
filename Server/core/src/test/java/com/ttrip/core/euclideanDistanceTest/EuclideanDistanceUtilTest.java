package com.ttrip.core.euclideanDistanceTest;

import com.ttrip.core.entity.member.Member;
import com.ttrip.core.entity.survey.Survey;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.repository.survey.SurveyRepository;
import com.ttrip.core.utils.EuclideanDistanceUtil;
import com.ttrip.core.TtripApplication;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TtripApplication.class)
@Transactional
@ActiveProfiles("test")
public class EuclideanDistanceUtilTest {

    @Autowired
    EuclideanDistanceUtil euclideanDistanceUtil;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SurveyRepository surveyRepository;

    @Test
    void Test() throws DimensionMismatchException {
        UUID memberUuid1 = UUID.randomUUID();
        Member member1 = Member.builder().uuid(memberUuid1).build();
        Member savedMember1 = memberRepository.save(member1);

        UUID memberUuid2 = UUID.randomUUID();
        Member member2 = Member.builder().uuid(memberUuid2).build();
        Member savedMember2 = memberRepository.save(member2);
        Survey survey1 = Survey.builder()
                .member(savedMember1)
                .preferCheapHotelThanComfort(5 / 5f)
                .preferCheapTraffic(5 / 5f)
                .preferDirectFlight(5 / 5f)
                .preferGoodFood(5 / 5f)
                .preferDirectFlight(5 / 5f)
                .preferTightSchedule(5 / 5f)
                .preferPersonalBudget(5 / 5f)
                .preferNatureThanCity(5 / 5f)
                .preferShoppingThanTour(1 / 5f)
                .build();
        Survey survey2 = Survey.builder()
                .member(savedMember2)
                .preferCheapHotelThanComfort(5 / 5f)
                .preferCheapTraffic(5 / 5f)
                .preferDirectFlight(5 / 5f)
                .preferGoodFood(5 / 5f)
                .preferDirectFlight(5 / 5f)
                .preferTightSchedule(5 / 5f)
                .preferPersonalBudget(5 / 5f)
                .preferNatureThanCity(5 / 5f)
                .preferShoppingThanTour(5 / 5f)
                .build();
        Survey survey3 = Survey.builder()
                .member(savedMember2)
                .preferCheapHotelThanComfort(5 / 5f)
                .preferCheapTraffic(5 / 5f)
                .preferDirectFlight(5 / 5f)
                .preferGoodFood(5 / 5f)
                .preferDirectFlight(5 / 5f)
                .preferTightSchedule(5 / 5f)
                .preferPersonalBudget(5 / 5f)
                .preferNatureThanCity(5 / 5f)
                .preferShoppingThanTour(5 / 5f)
                .build();
        assertEquals(65, euclideanDistanceUtil.getMatchingRate(survey1, survey2));
        assertEquals(100, euclideanDistanceUtil.getMatchingRate(survey3, survey2));

    }
}