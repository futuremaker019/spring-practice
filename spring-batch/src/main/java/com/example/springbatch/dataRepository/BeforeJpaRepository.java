package com.example.springbatch.dataRepository;

import com.example.springbatch.dataEntity.BeforeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeforeJpaRepository extends JpaRepository<BeforeEntity, Long> {
}
