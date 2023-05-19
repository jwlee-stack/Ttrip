package com.ttrip.core.repository.landmark;

import com.ttrip.core.entity.landmark.Badge;
import com.ttrip.core.entity.landmark.Landmark;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Integer> {
    List<Badge> findByMember(Member member);
    boolean existsByMemberAndLandmark(Member member, Landmark landmark);
}