package com.practice.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) {
                // 서버에서 IllegalArgumentException으로 터지며 400 에러가 나도록 함
                // IllegalArgumentException 이면 Exception 여기서 먹고 뱉도록 만들어준다.
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                // 정상적으로 ModelAndView로 return 해주면 ServletContainer 는 정상 리턴으로 인식한다.
                // 예외를 삼켜(?)버릴수 있다.
                return new ModelAndView();
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        // null 로 return 이 되면 예외가 was까지 쭉 날라간다.
        return null;
    }
}
