package com.demo.exercise.repository;

import com.demo.exercise.domain.Member;
import com.demo.exercise.dto.MemberDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        /**
         * pagble을 상속받은 pageRequest를 구현하여 넘겨준다.
         * 0 페이지에서 3개의 데이터를 가져옴
         */
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        /**
         * Page를 반환타입으로 할시 totalCount 쿼리를 같이 날려준다.
         */
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        /**
         *     select
         *         member0_.member_id as member_i1_4_,
         *         member0_.city as city2_4_,
         *         member0_.street as street3_4_,
         *         member0_.zipcode as zipcode4_4_,
         *         member0_.age as age5_4_,
         *         member0_.team_id as team_id7_4_,
         *         member0_.username as username6_4_
         *     from
         *         member member0_
         *     where
         *         member0_.age=?
         *     order by
         *         member0_.username desc limit ?
         *
         *
         *     select
         *         count(member0_.member_id) as col_0_0_
         *     from
         *         member member0_
         *     where
         *         member0_.age=?
         */

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        PageRequest pageRequestBySlice = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        /**
         * Slice를 사용시 가져오는 데이터보다 하나더 가져와서 다음페이지가 존재한다는것을 명시적으로 보여준다.
         */
        Slice<Member> pageBySlice = memberRepository.findListByAge(age, pageRequestBySlice);

        /**
         * select
         *      member0_.member_id as member_i1_4_,
         *      member0_.city as city2_4_,
         *      member0_.street as street3_4_,
         *      member0_.zipcode as zipcode4_4_,
         *      member0_.age as age5_4_,
         *      member0_.team_id as team_id7_4_,
         *      member0_.username as username6_4_
         * from
         *      member member0_
         * where
         *      member0_.age=10 order by member0_.username desc
         * limit 4;  -> 3개를 호출헸지만 4개를 불러온다.
         */

        assertThat(pageBySlice.getContent().size()).isEqualTo(3);
//        assertThat(pageBySlice.getTotalElements()).isEqualTo(5);
        assertThat(pageBySlice.getNumber()).isEqualTo(0);
//        assertThat(pageBySlice.getTotalPages()).isEqualTo(2);
        assertThat(pageBySlice.isFirst()).isTrue();
        assertThat(pageBySlice.hasNext()).isTrue();

    }
}