package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor {
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

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    /**
     * Pageable에는 1페이지 2페이지와 같은 형태의 값이 들어온다.
     *
     * countQuery를 분리하고 싶다면 countQuery를 명시하여 사용한다.
     */
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findListByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)   // update 쿼리가 나간후 자동으로 em.clear()를 해준다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    List<UsernameOnly> findProjectionByUsername(@Param("username") String username);

    List<UsernameOnlyDto> findProjectionClassByUsername(@Param("username") String username);

    // 클래스에 따라 사용자의 이름을
    <T> List<T> findProjectionAsClassByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username= ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value="select m.member_id as id, m.username, t.name as teamName " +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
