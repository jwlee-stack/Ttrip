package com.ttrip.core.repository.landmark;

import com.ttrip.core.entity.landmark.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandmarkRepository extends JpaRepository<Landmark, Integer> {
    Optional<Landmark> findByLandmarkId(Integer id);
}