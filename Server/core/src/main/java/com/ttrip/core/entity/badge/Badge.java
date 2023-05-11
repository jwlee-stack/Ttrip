package com.ttrip.core.entity.badge;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int badgeId;
    private String badgeName;
    private String badgeImagePath;
    private Double lat;
    private Double lng;
}
