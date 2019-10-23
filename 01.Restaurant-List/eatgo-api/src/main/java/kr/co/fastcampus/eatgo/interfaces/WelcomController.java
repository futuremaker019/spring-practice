package kr.co.fastcampus.eatgo.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomController {

    @GetMapping("/")
    public String hello(){
        return "Hello Basters. and I'm very glad it's running. makes me sick.";
    }
}
