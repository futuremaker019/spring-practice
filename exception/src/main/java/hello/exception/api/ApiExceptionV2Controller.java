package hello.exception.api;

import hello.exception.exHandler.ErrorResult;
import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    // @ResponseStatus 를 붙여줌으로써 정상흐름으로 진행하여 200 ok 가 되는 상태값을 변경해준다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    // 커스텀 exception을 만들어 사용이 가능하다
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // Exception 은 최상위 부모이므로 처리되지 못한 모든 예외처리는 여기로 온다.
    // 공통으로 Exception Handling 시 사용할수도 있다.
    // 여기서는 RuntimeException을 처리하기 위해 만들어짐
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
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
    @GetMapping("/api2/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    // ResponseStatusException 을 직접 호출하는 방식
    @GetMapping("/api2/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    // 스프링 내부에서 예외를 자동으로 처리해줌
    // 아래 controller 는 argument typemismatch 의 예외를 확인하기 위한 controller 이다.
    @GetMapping("/api2/default-handler-ex")
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
