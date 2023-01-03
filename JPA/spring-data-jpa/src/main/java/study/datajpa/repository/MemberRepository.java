package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery는 실무에서 거의 사용하지 않는다.
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
