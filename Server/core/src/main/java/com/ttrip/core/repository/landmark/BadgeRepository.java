package com.ttrip.core.repository.landmark;

import com.ttrip.core.entity.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Integer> {

}