package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    // Exception 을 직접 수정해서 사용하기 위해 HandlerExceptionResolver 를 상속받아 사용한다.
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            if (ex instanceof IllegalArgumentException) {
                // 서버에서 IllegalArgumentException으로 터지면 400 에러가 나도록 함
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                // 정상적으로 ModelAndView로 return 해준면 예외를 삼켜(?)버릴수 있다.
                return new ModelAndView();
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
