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
    @Column(name = "id", nullable = false)
    private int surveyId;
    private float preferNatureThanCity;
    private float preferPlan;
    private float preferDirectFlight;
    private float preferCheapHotelThanComfort;
    private float preferGoodFood;
    private float preferCheapTraffic;
    private float preferPersonalBudget;
    private float preferTightSchedule;
    private float preferShoppingThanTour;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Member member;

    public double[] toVector() {
        double[] vector = new double[9];
        vector[0] = this.getPreferNatureThanCity();
        vector[1] = this.getPreferPlan();
        vector[2] = this.getPreferDirectFlight();
        vector[3] = this.getPreferCheapHotelThanComfort();
        vector[4] = this.getPreferGoodFood();
        vector[5] = this.getPreferCheapTraffic();
        vector[6] = this.getPreferPersonalBudget();
        vector[7] = this.getPreferTightSchedule();
        vector[8] = this.getPreferShoppingThanTour();
        return vector;
    }
}
