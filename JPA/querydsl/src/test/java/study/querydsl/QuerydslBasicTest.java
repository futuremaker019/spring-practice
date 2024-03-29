package study.querydsl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        // member1을 찾아라.
        String qlString = "select m from Member m where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");

        // when

        // then

    }

    @Test
    public void startQuerydsl() {
        QMember m1 = new QMember("m1");

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))    // 파라미터 바인딩처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() {
        // given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 30), null
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void resultFetch() {
        // given
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        results.getTotal();
        List<Member> content = results.getResults();

        long total = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)      // 시작 데이터 위치
                .limit(2)       // 데이터 갯수
                .fetch();

        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)      // 시작 데이터 위치
                .limit(2)       // 데이터 갯수
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation() {
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
    @Test
    public void group() throws Exception {
        // given
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 팀 A에 소속된 모든 회원을 찾는다
     */
    @Test
    public void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
//                .join(member.team, team)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이름과 동일한 같은 회원 조회
     * 모든회원과 모든팀을 가져와 조인시킨후 where 절에서 필터링하는 방식이다.
     * outer 조인이 불가능함 - 연관관계가 없기때문에 불가능 (그러나 on을 사용하면 가능)
     */
    @Test
    public void theta_join() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering() {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
//                .on(team.name.eq("teamA"))        // leftJoin일 경우에만 on절이 의미가 있다. inner join을 사용할때는 on, where 조건 어디든 같은 값을 가져온다.
                .where(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관게가 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    public void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)     // leftJoin 절에 member.team, team으로 해주면 연관관계 키를 기준으로 가져오지만, team만 작성한다면 leftJoin으로 가능하다.
                .on(member.username.eq(team.name))
//                .where(member.username.eq(team.name))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();


        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("폐치 조인 미적용").isFalse(); // 폐치 조인이 적용되지 않은 상태

        /**
         *
         * sql
         *
         select
         *          member0_.id as id1_1_,
         *          member0_.age as age2_1_,
         *          member0_.team_id as team_id4_1_,
         *          member0_.username as username3_1_
         *      from
         *          member member0_
         *      where
         *          member0_.username =?
         *
         */
    }

    @Test
    public void fetchJoinUse() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()    // member를 조회시 member에 연관된 team 또한 가져온다. fetchJoin()을 선언하여 가져온다.
                .where(member.username.eq("member1"))
                .fetchOne();


        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("폐치 조인 적용").isTrue(); // 폐치 조인이 적용되지 않은 상태

        /**
         * 실행된 sql
         *
         *         select
         *             member0_.id as id1_1_0_,
         *             team1_.id as id1_2_1_,
         *             member0_.age as age2_1_0_,
         *             member0_.team_id as team_id4_1_0_,
         *             member0_.username as username3_1_0_,
         *             team1_.name as name2_2_1_
         *         from
         *             member member0_
         *         inner join
         *             team team1_
         *                 on member0_.team_id=team1_.id
         *         where
         *             member0_.username=?
         */
    }

    /**
     * com.querydsl.jpa.JPAExpression 사용
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);

        /**
         * sql
         *
         *         select
         *             member0_.id as id1_1_,
         *             member0_.age as age2_1_,
         *             member0_.team_id as team_id4_1_,
         *             member0_.username as username3_1_
         *         from
         *             member member0_
         *         where
         *             member0_.age=(
         *                 select
         *                     max(member1_.age)
         *                 from
         *                     member member1_
         *             )
         *
         */
    }

    /**
     * 나이가 평균 이상인 회원
     */
    @Test
    public void subQueryGoe() {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);

        /**
         * sql
         *
         *         select
         *             member0_.id as id1_1_,
         *             member0_.age as age2_1_,
         *             member0_.team_id as team_id4_1_,
         *             member0_.username as username3_1_
         *         from
         *             member member0_
         *         where
         *             member0_.age >= (
         *                 select
         *                     avg(cast(member1_.age as double))
         *                 from
         *                     member member1_
         *             )
         *
         */
    }

    @Test
    public void subQueryIn() {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);

        /**
         * sql
         *
         *         select
         *             member0_.id as id1_1_,
         *             member0_.age as age2_1_,
         *             member0_.team_id as team_id4_1_,
         *             member0_.username as username3_1_
         *         from
         *             member member0_
         *         where
         *             member0_.age in (
         *                 select
         *                     member1_.age
         *                 from
         *                     member member1_
         *                 where
         *                     member1_.age>?
         *             )
         *
         */
    }


    @Test
    public void selectSubQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory
                .select(member.username,
//                        JPAExpressions
//                                .select(memberSub.age.avg())
//                                .from(memberSub)
                        select(memberSub.age.avg())
                                .from(memberSub)

                )
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }


        /**
         * sql
         *
         *         select
         *             member0_.username as col_0_0_,
         *             (select
         *                 avg(cast(member1_.age as double))
         *             from
         *                 member member1_) as col_1_0_
         *         from
         *             member member0_
         *
         */
    }

    @Test
    public void basicCase() {
        // given
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

        /**
         * sql
         *
         *         select
         *             case
         *                 when member0_.age=? then ?
         *                 when member0_.age=? then ?
         *                 else '기타'
         *             end as col_0_0_
         *         from
         *             member member0_
         */

    }

    @Test
    public void complexCase() {
        // given
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

        /**
         * sql
         *
         *        select
         *             case
         *                 when member0_.age between ? and ? then ?
         *                 when member0_.age between ? and ? then ?
         *                 else '기타'
         *             end as col_0_0_
         *         from
         *             member member0_
         *
         */

    }

    /**
     * 상수 ??
     */
    @Test
    public void constant() {
        // given
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

        /**
         * sql
         *
         *         select
         *             member0_.username as col_0_0_
         *         from
         *             member member0_
         */
    }

    /**
     * 문자더하기
     * <p>
     * Enum 처리시 stringValue를 사용한단다. 수업 자료 보자
     */
    @Test
    public void concat() {
        // username_age 를 표현하라
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

        /**
         * sql
         *
         *     select
         *         concat(concat(member1.username, ?1), str(member1.age))
         *     from
         *         Member member1
         *     where
         *         member1.username = ?2
         *
         */

    }


    /**
     * 중급문법 시작
     * 프로젝션과 결과 반환 - 기본
     */
    @Test
    public void simpleProjection() {
        // given
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

        /**
         * sql
         *
         *     select
         *         member1.username
         *     from
         *         Member member1
         *
         */

    }

    @Test
    public void tupleProjection() {
        // given
        List<Tuple> result = queryFactory
                .select(member.username, member.age)    // 원하는 데이터를 찍어서 가져온다면 Tuple에 담긴다.
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }

        /**
         *
         * sql
         *
         *     select
         *         member1.username,
         *         member1.age
         *     from
         *         Member member1
         */

    }

    /**
     * JPQL을 이용하면 select 절에 해당 DTO를 정확히 지정해줘야하는 번거로움이 있다.
     */
    @Test
    public void findDtoByJPQL() {
        List<MemberDto> resultList = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto = " + memberDto);
        }

        /**
         *
         * sql
         *
         *         select
         *             new study.querydsl.dto.MemberDto(m.username, m.age)
         *         from
         *             Member m
         *
         *
         *         select
         *             member0_.username as col_0_0_,
         *             member0_.age as col_1_0_
         *         from
         *             member member0_
         *
         */
    }

    /**
     * Projection에 특정 class를 넣어주면 해당하는 DTO의 클래스에 값을 넣어 반환해준다.
     */
    @Test
    public void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

        /**
         *     select
         *         member1.username,
         *         member1.age
         *     from
         *         Member member1
         *
         */
    }

    /**
     * Projections.field를 사용하면 getter setter가 필요없이 값을 필드에 넣어준다.
     */
    @Test
    public void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

        /**
         *
         * sql
         *
         *      select
         *         member1.username,
         *         member1.age
         *     from
         *         Member member1
         *
         */
    }

    /**
     * constructor 방식은 조회시 해당하는 데이터타입이 맞으면 값을 넣어준다.
     */
    @Test
    public void findDtoByConstructor() {
        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (UserDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

        /**
         *
         * sql
         *
         *     select
         *         member1.username,
         *         member1.age
         *     from
         *         Member member1
         *
         */
    }

    @Test
    public void findUserDto() {
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
        /**
         * 필드명이 다르면 조회는 되지만 값은 들어가지 않으므로 alias를 설정해줘야 한다.
         *
         * age 필드가 없을때, age를 subquery를 사용하여 만들어준다.
         *  ExpressionUtils.as(JPAExpressions
         *       .select(memberSub.age.max())
         *       .from(memberSub), "age")
         */

        /**
         *     select
         *         member1.username as name,
         *         member1.age
         *     from
         *         Member member1
         *
         *     // 서브쿼리 사용시
         *     select
         *         member1.username as name,
         *         (select
         *             max(memberSub.age)
         *         from
         *             Member memberSub) as age
         *     from
         *         Member member1
         *
         */
    }

    @Test
    public void findDtoByQueryProjection() throws Exception {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void dynamicQuery_BooleanBuilder() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);

        /**
         *     select
         *         member1
         *     from
         *         Member member1
         *     where
         *         member1.username = ?1
         *         and member1.age = ?2
         */
    }

    // BooleanBuilder를 사용하여 동적쿼리를 생성한다.
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    public void dynamicQuery_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);

        /**
         *     select
         *         member1
         *     from
         *         Member member1
         *     where
         *         member1.username = ?1
         *         and member1.age = ?2
         *
         */

        /**
         *
         * 메서드를 다른 쿼리에서도 재활용이 가능하다.
         * 쿼리 자체의 가독성이 높아진다.
         *
         */
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond == null ? null : member.username.eq(usernameCond);
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    public void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        System.out.println("count = " + count);

        /**
         *     update
         *         Member member1
         *     set
         *         member1.username = ?1
         *     where
         *         member1.age < ?2
         *
         */

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        em.flush();
        em.clear();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }

        /**
         * 벌크 수정 및 삭제를 진행하면 영속성 컨텍스트는 변경이 되지 않고
         * DB에 직접 반영을 하기 떄문에, Select시 업데이트 되지않은 정보가 조회된다.
         *
         * 그러므로 em.flush, em.clear를 통해 영속성 컨텍스트를 초기화 시켜줘야 한다.
         */
    }

    @Test
    public void bulkAdd() {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();

        System.out.println("count = " + count);

        /**
         *    update
         *        member
         *    set
         *        age=age+?
         */
    }

    @Test
    public void bulkDelete() {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();

        System.out.println("count = " + count);
    }

    @Test
    public void sqlFunction() throws Exception {
        List<String> result = queryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();

        /**
         * JPQL
         *    select
         *         function('replace',
         *         member1.username,
         *         ?1,
         *         ?2)
         *     from
         *         Member member1
         *
         * sql
         *         select
         *             replace(member0_.username,
         *             ?,
         *             ?) as col_0_0_
         *         from
         *             member member0_
         */

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sqpFunction2() throws Exception {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq(
//                        Expressions.stringTemplate("function('lower', {0})", member.username)
//                ))
                .where(member.username.eq(member.username.lower()))     // 기본적인 ANSI 표준 sql function은 querydsl에 정의 되어있다.
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

        /**
         * JPQL
         *
         *     select
         *         member1.username
         *     from
         *         Member member1
         *     where
         *         member1.username = lower(member1.username)
         *
         * sql
         *
         *      select
         *          member0_.username as col_0_0_
         *      from
         *          member member0_
         *      where
         *          member0_.username=lower(member0_.username)
         *
         */
    }
}
