package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
}
