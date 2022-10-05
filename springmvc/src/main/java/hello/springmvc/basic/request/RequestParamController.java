package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {
        log.info("username = {}, age = {}", memberName, memberAge);

        return "ok";
    }

    /**
     *
     * request로 들어오는 파라미터의 변수명과 @RequestParam의 변수명이 같으면 ("속성")은 생략이 가능하다
     *
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     *
     * request의 파라미터가 변수명과 같으면 @RequestParam까지도 생략이 가능하다
     *
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }


    /**
     *
     * required false 이면 요청파라미터에 값이 없다면 null 값이 된다.
     * primitive 타입으로 파라미터에 데이터 타입을 주면 500 서버에러를 뱉는다.
     *
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamV5(@RequestParam(required = true) String username,
                                 @RequestParam(required = false) Integer age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     *  default value를 이용함
     *  빈 문자열까지도 defaultValue로 들어간다.
     *
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamV6(@RequestParam(required = true, defaultValue = "guest") String username,
                                 @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }


    /**
     *  모든 요청을 Map 형태로 받을수 있다.
     *
     * @RequestParam MultiValueMap의 형태로 만들어주면 같은 파라미터의 다른 값을 동시에 받아올수 있다.
     * ex) key=userId, value=[id1, id2]
     *
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
}
