package com.demo.exercise.repository;

import com.demo.exercise.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Page<Member> findByAge(int age, PageRequest pageRequest);

    Slice<Member> findListByAge(int age, PageRequest pageRequest);

}
