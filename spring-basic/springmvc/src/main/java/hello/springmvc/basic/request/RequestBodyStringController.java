package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }


    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWritor) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        responseWritor.write("ok");
    }

    /**
     * HttpEntity를 이용하여 http의 body를 직접 가져올수도 있다.
     *
     * @param httpEntity
     * @return
     * @throws IOException
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException{
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok");
    }


    /**
     *
     * HttpEntiry를 상속받은 RequestEntity와 ResponseEntity를 이용할 수도 있다.
     *
     * @param httpEntity
     * @return
     * @throws IOException
     */
    @PostMapping("/request-body-string-v4")
    public HttpEntity<String> requestBodyStringV4(RequestEntity<String> httpEntity) throws IOException{

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    /**
     *
     * Annotation을 활용하여 사용할 수도 있다.
     *
     * @param messageBody
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/request-body-string-v5")
    public String requestBodyStringV5(@RequestBody String messageBody) throws IOException{

        log.info("messageBody={}", messageBody);

        return "ok";
    }

    /**
     *
     *  받아온 body를 Http message Convertor가 String으로 변환해준다.
     *
     *  아래의 일련의 과정을 message convertor가 해준다.
     *  ServletInputStream inputStream = request.getInputStream();
     *  String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
     *
     */

}
