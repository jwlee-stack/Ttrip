package com.ttrip.core.repository.landmark;

import com.ttrip.core.entity.landmark.Doodle;
import com.ttrip.core.entity.landmark.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoodleRepository extends JpaRepository<Doodle, Integer> {
    Optional<Doodle> findByLandmarkAndDoodleImgPath(Landmark landmark, String imgPath);
    List<Doodle> findByLandmarkLandmarkId(Integer landmarkId);
}