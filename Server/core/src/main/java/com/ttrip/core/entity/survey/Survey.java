package com.ttrip.core.entity.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int surveyId;
    @Column(columnDefinition = "TINYINT")
    private int preferNatureThanCity;
    @Column(columnDefinition = "TINYINT")
    private int preferPlan;
    @Column(columnDefinition = "TINYINT")
    private int preferDirectFlight;
    @Column(columnDefinition = "TINYINT")
    private int preferCheapHotelThanComfort;
    @Column(columnDefinition = "TINYINT")
    private int preferGoodFood;
    @Column(columnDefinition = "TINYINT")
    private int preferCheapTraffic;
    @Column(columnDefinition = "TINYINT")
    private int preferPersonalBudget;
    @Column(columnDefinition = "TINYINT")
    private int preferTightSchedule;
    @Column(columnDefinition = "TINYINT")
    private int preferShoppingThanTour;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Member member;
}
