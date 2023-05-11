package com.ttrip.core.repository.landmark;

import com.ttrip.core.entity.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Integer> {
    List<Badge> findByMemberMemberId(Integer memberId);
}