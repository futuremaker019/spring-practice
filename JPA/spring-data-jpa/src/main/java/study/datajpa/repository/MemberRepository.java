package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findTop3HelloBy();

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery는 실무에서 거의 사용하지 않는다.
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    /**
     * 쿼리내부에 오타가 있으면 애플리케이션 로딩 시점에 에러를 발생시킨다.
     *  애플리케이션 로딩시 정적쿼리들을 다 파싱한다. 파싱시 문법적 오류를 찾으면 에러를 발생시킨다.
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * dto를 명시하여 dto를 반환하게 만들어준다.
     *  dto의 패키지 경로를 모두 작성해줘야 한다.
     * @return
     */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
