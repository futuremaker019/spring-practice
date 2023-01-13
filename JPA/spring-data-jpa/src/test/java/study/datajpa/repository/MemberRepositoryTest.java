package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sound.midi.MetaMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamJpaRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void JPA를_이용한_저장된_회원비교() throws Exception {
        //given
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 12);
        //then

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void namedQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);

    }

    @Test
    public void testQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto s : memberDto) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

//        List<Member> aaa = memberRepository.findListByUsername("AAA");
//        Member findMember = memberRepository.findMemberByUsername("AAA");
        /**
         *  같은 이름을 가진
         */
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("dfsdafa");
        System.out.println("optionalMember = " + optionalMember);
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // 페이지 계산 공식 적용
        // totalPage = totalCount / size ...
        // 마지막 페이지
        int age = 10;

        // when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));


        // totalCount query가 필요가 없다. Page에서 같이 totalCount를 return 해준다.
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // DTO로 변환하여 데이터를 response 해줘야 API 스펙에 어긋나지 않는다.
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        // 검색된 데이터 갯수
        assertThat(content.size()).isEqualTo(3);
        // 데이터의 해당 페이지의 데이터 총갯수
        assertThat(page.getTotalElements()).isEqualTo(5);
        // 해당 페이지 넘버
        assertThat(page.getNumber()).isEqualTo(0);
        // 총 페이지 수
        assertThat(page.getTotalPages()).isEqualTo(2);
        // 현재페이지가 처음 페이지인지 boolean return
        assertThat(page.isFirst()).isTrue();
        // 다음페이지가 존재하는지 boolean return
        assertThat(page.hasNext()).isTrue();

        // slice에서는 3개를 요청해도 3+1인 4개를 조회해서 더보기 등등에 활용이 가능하다.
        Slice<Member> slicedPage = memberRepository.findListByAge(age, pageRequest);
        List<Member> content1 = slicedPage.getContent();

        // 검색된 데이터 갯수
        assertThat(content1.size()).isEqualTo(3);
        // 데이터의 해당 페이지의 데이터 총갯수
//        assertThat(slicedPage.getTotalElements()).isEqualTo(5);
        // 해당 페이지 넘버
        assertThat(slicedPage.getNumber()).isEqualTo(0);
        // 총 페이지 수
//        assertThat(slicedPage.getTotalPages()).isEqualTo(2);
        // 현재페이지가 처음 페이지인지 boolean return
        assertThat(slicedPage.isFirst()).isTrue();
        // 다음페이지가 존재하는지 boolean return
        assertThat(slicedPage.hasNext()).isTrue();

        // 반환타입이 List라면 리스트만 조회한다. (totalCount는 조회하지 않는다.)
//        List<Member> page = memberRepository.findListByAge(age, pageRequest);

    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
//        em.flush();     // update 후 남아있는 영속성 컨텍스트의 데이터를 flush하여 DB에 반영한다.
//        em.clear();     // 반영 후, 영속성 컨텍스트를 초기화 한다.

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);

        // 영속성 컨텍스트에서 값을 가져오기떄문에 41이 아니라 40이 된다
        // 이러한 상황을 방지하기위해 em.flush(), em.clear()를 사용한다.
        // 초기화 후 DB에서 다시 조회해온다.
        System.out.println("member5 = " + member5);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when  N + 1 문제가 일어남
        // 하나의 Member를 조회시 member와 연관된 모든 항목들을 디비에서 가져온다.
        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            // 가짜 객체를 만들어 Team의 프록시 객체를 생성한다.
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            // Team의 이름을 디비에서 가져와 Team 객체에 넣어준다.
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
        // then

    }

    @Test
    public void lock() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        List<Member> result = memberRepository.findLockByUsername("member1");
        // then

    }

    @Test
    public void callCustom() {
        // given
        List<Member> memberCustom = memberRepository.findMemberCustom();

        for (Member member : memberCustom) {
            System.out.println("member = " + member);
        }
        // when

        // then

    }


    // 굳이 사용자정의 리포지토리를 만들지 않아도 아래와 같이 쿼리를 쪼갤수 있는 클래스를 만들어 Autowired 형식으로 받아 사용해도 무방하다.
    // 기술이 중요한게 아니라 어떻게 분리하여 사용하는지가 중요하다.
    @Autowired MemberQueryRepository memberQueryRepository;

    @Test
    public void callCustomRepositoryClass() {
        // given
        List<Member> member = memberQueryRepository.findMember();

        // when

        // then

    }


    @Test
    public void JpaEventBaseEntity() throws InterruptedException {
        // given
        Member member = new Member("member1");
        memberRepository.save(member);  // @PrePersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdatedDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }

    @Test
    public void specBasic() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // then
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List result = memberRepository.findAll(spec);

        assertThat(result.size()).isEqualTo(1);
    }
}
