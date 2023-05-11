package com.ttrip.core.repository.badge;

import com.ttrip.core.entity.badge.Badge;
import com.ttrip.core.entity.badge.MyBadge;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<MyBadge, Integer> {
    List<Integer> findBadgeIdByMember(Member member);
    List<Badge> findAllByBadgeBadgeIdIn(List<Integer> badges);
}