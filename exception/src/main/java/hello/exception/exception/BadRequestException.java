package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Bad-Request 에 대한 공통 예러 처리함
// Spring이 @ResponseStatus 어노테이션의 존재를 확인후 code, reason을 읽어 Exception처리를 한다.
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")

// messages.properties 를 사용하면 error.bad를 코드로 사용 가능하다
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException{


}
