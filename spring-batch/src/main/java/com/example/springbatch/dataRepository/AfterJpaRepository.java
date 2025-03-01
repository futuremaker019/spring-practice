package com.example.springbatch.dataRepository;

import com.example.springbatch.dataEntity.AfterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfterJpaRepository extends JpaRepository<AfterEntity, Long> {
}
