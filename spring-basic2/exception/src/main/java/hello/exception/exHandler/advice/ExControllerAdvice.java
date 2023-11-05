package hello.exception.exHandler.advice;

import hello.exception.exHandler.ErrorResult;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 여러 컨트롤에서 발생하는 모든 에외 처리를 해준다.
@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api") // 페키지명을 등록하여 예외를 글로벌하게 처리할수 있다.
public class ExControllerAdvice {

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
}
