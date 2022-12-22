package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        
        //then
//        em.flush();     // rollback 어노테이션을 사용하지않고 디비에 데이터가 저장되는것을 보고싶다면 entityManager를 이용하여 flush한다.
        assertEquals(member, memberRepository.findOne(savedId));
        
        // database의 커밋을 하는순간 jpa가 flush하여 디비에 저장시킨다.
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);    // 예외가 발생해야 한다.
        // 사용자의 이름이 중복되어 회원가입을 동시에 시도할시 예외가 발생되어야 한다.
        // try catch로 예외를 잡거나, expected = IllegalStateException을 사용하여 예외를 잡아준다.

        // expected = IllegalStateException을 사용하면 예외 발생시 잡아준다.
//        try {
//            memberService.join(member2);    // 예외가 발생해야 한다.
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 한다.??");
    }

    // 테스트시 메모리 디비를 사용하는 방법
    // 1. test아래 resource 디렉토리를 만든다.
    // 2. application.yml을 복사하여 넣어준다.
    // 3. url: jdbc:h2:mem:test 로 수정한다.
    // springboot 사용시 datasource 를 주석처리해도 알아서 메모리모드로 테스트해준다.
}