package com.ttrip.core.entity.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int surveyId;
    private int preferNatureThanCity;
    private int preferPlan;
    private int preferDirectFlight;
    private int preferCheapHotelThanComfort;
    private int preferGoodFood;
    private int preferCheapTraffic;
    private int preferPersonalBudget;
    private int preferTightSchedule;
    private int preferShoppingThanTour;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    @JsonBackReference
    private Member member;
}
