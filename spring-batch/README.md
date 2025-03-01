# 스프링 배치 기본 강의 

spring batch 5를 이용한 간단한 배치 실행 방법 확인 

참고: [강의 블로그 - 개발자 유미](https://www.devyummi.com/page?id=66951d4d823bbb8bc327ba0e)

배치 기술에 대한 간단한 개요를 확인하고 싶어 정리했다.

# 다중 DB 설정 

meta 데이터가 들어갈 meta db 설정과 실제 데이터가 들어갈 data db를 하나의 어플리케이션에서 구현하기 위한 다중 DB 설정을 세팅한다.

```
# 스프링 버전
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.4.3'
    id 'io.spring.dependency-management' version '1.1.7'
}
```

## DataSource 설정

블로그 설명에서는 properties 확장자를 사용했지만 yml을 사용하고 싶어서 아래와 같이 수정했다.<br>
애플리케이션 실행시 메타 테이터의 테이블이 생성되도록 설정

```yml
spring:
  datasource:
    hikari:
      meta:
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/meta_db?useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true
        username: root
        password: 1234
      data:
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/data_db?useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true
        username: root
        password: 1234

# yml에서 설정한 jpa 설정은 적용되지 않는다. config에서 직접 적용해줘야 함 
#  jpa:
#    hibernate:
#      ddl-auto: update
#    properties:
#      hibernate:
#        show_sql: true
#        format_sql: true
  batch:
    job:
      enabled: false  # 애플리케이션 실행 시 job의 자동 실행을 비활성화
    jdbc:
      initialize-schema: always      # batch의 메타 데이터 테이블을 자동으로 생성하게 함
      schema: classpath:schema.sql   # 메타 데이터 테이블 schema 의 위치를 명시함
```

메타 데이터가 활용되는 데이터가 활용되는 Datasource 설정, `@Primary`를 사용하여 기본 DataSource라는 것을 명시해, 메타 데이터가 등록 될수 있도록 한다. 

```java
@Configuration
public class MetaDbConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.meta")  // ✅ 설정 파일의 "meta" 사용
    public DataSource metaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager metaTransactionManager() {
        return new DataSourceTransactionManager(metaDataSource());
    }
}
```

실제 데이터 값을 사용하기 위한 DataSource 설정, yml 에서 설정해야 하는 ddl_auto와 같은 기능을 여기서 dataEntityManger에 설정해주어야 한다.

배치를 이용한 읽기, 가공, 쓰기를 위해 사용되는 데이터를 위한 설정 

```java
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.springbatch.dataRepository",
        entityManagerFactoryRef = "dataEntityManager",
        transactionManagerRef = "dataTransactionManager"
)
public class DataDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.data")  // ✅ 설정 파일의 "data" 사용
    public DataSource dataDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean dataEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataDataSource());
        em.setPackagesToScan("com.example.springbatch.dataEntity");

        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> prop = new HashMap<>();
        prop.put("hibernate.hbm2ddl.auto", "update");
//        prop.put("hibernate.format_sql", true);
        prop.put("hibernate.show_sql", true);
        em.setJpaPropertyMap(prop);

        return em;
    }

    @Bean
    public PlatformTransactionManager dataTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dataEntityManager().getObject());
        return transactionManager;
    }
}
```

## 배치 구성

하나의 Job은 여러개의 Step으로 구성되며, Step 내부에는 쓰기, 가공, 읽기의 기본적인 구성으로 만들었다.

단순히 BeforeEntity 테이블의 사용자명을 AfterEntity의 사용자명으로 이동하는 방식이다.

```java
@Configuration
public class FirstBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final BeforeJpaRepository beforeRepository;
    private final AfterJpaRepository afterRepository;

    public FirstBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, BeforeJpaRepository beforeRepository, AfterJpaRepository afterRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.beforeRepository = beforeRepository;
        this.afterRepository = afterRepository;
    }

    @Bean
    public Job firstJob() {
        System.out.println("first job");
        return new JobBuilder("firstJob", jobRepository)
                .start(firstStep())
                .build();
    }

    @Bean
    public Step firstStep() {
        System.out.println("first step");
        return new StepBuilder("firstStep", jobRepository)
                .<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager)
                .reader(beforeReader())
                .processor(middleProcessor())
                .writer(afterWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader() {
        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("beforeReader")
                .pageSize(10)
                .methodName("findAll")
                .repository(beforeRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {
        return item -> {
            AfterEntity afterEntity = new AfterEntity();
            afterEntity.setUsername(item.getUsername());
            return afterEntity;
        };
    }

    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter() {
        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }

}
```

## 배치 실행

배치가 실행되는 방법을 확인하기 위해 간단히 컨트롤러를 만들어 실행하는 방법과 스케줄러를 만들어 실행하는 방법을 만듬

배치는 JobParameter에 의해 현재 Job이 실행됬는지 여부를 판단한다. 같은 파라미터가 존재한다면 해당하는 Job은 다시 실행되지 않는다.

```java
@RestController
@RequiredArgsConstructor
public class MainController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/first")
    public String proceedJob(@RequestParam("value") String value) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);

        return "ok";
    }

}


@Configuration
public class SchedulingBatch {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public SchedulingBatch(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Scheduled(cron = "10 * * * * *", zone = "Asia/Seoul")
    public void runFirstJob() throws Exception {

        System.out.println("first schedule start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);
    }

}
```