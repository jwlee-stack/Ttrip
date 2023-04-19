package com.ttrip.demo.batch.repository;

import com.ttrip.demo.batch.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestEntityRepository extends JpaRepository<TestEntity, Integer> {
}
