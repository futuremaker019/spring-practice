package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    /**
     * tymeleaf를 이용한 템플릿 구현
     * resource - templates 아래 html을 구현함
     * @return
     */
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");

        return mav;
    }

    /**
     *
     * Model 파라미터를 이용한 response 데이터 전송
     *
     * @param model
     * @return
     */
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");

        return "response/hello";
    }

    /**
     * mapping 경로와 전달하는 html의 경로가 같으면 html return 경로 생략이 가능하다.
     * 추천하지 않는 방법
     * @param model
     */
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!");
    }
}
