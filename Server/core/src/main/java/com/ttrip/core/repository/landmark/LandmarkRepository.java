package com.ttrip.core.repository.landmark;

import com.ttrip.core.entity.badge.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandmarkRepository extends JpaRepository<Landmark, Integer> {

}