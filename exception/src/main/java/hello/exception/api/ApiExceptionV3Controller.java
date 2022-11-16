package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionV3Controller {

    @GetMapping("/api3/members/{id}")
    public ApiExceptionController.MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new ApiExceptionController.MemberDto(id, "hello " + id);
    }


    /**
     * ResponseStatusException 을 클래스 형식의 Annotation 을 이용하여 호출하는 방식
     *
     * 위치 : BadRequestException
     * 사용 : @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
     * @return
     */
    @GetMapping("/api3/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    // ResponseStatusException 을 직접 호출하는 방식
    @GetMapping("/api3/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    // 스프링 내부에서 예외를 자동으로 처리해줌
    // 아래 controller 는 argument typemismatch 의 예외를 확인하기 위한 controller 이다.
    @GetMapping("/api3/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
