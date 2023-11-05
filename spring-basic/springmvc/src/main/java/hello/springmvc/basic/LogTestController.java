package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j          // 롬복 지원으로 Slf4j 어노테이션을 사용 가능하다.
@RestController
public class LogTestController {

    // class를 직접 지정해줘도 된다.
//    private final Logger log = LoggerFactory.getLogger(LogTestController.class);

//    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        log.trace("trace log={}", name);
        log.debug(" info debug={}", name);
        log.info(" info log={}", name);
        log.warn(" info warn={}", name);
        log.error(" info error={}", name);

        return "ok";
    }
}
