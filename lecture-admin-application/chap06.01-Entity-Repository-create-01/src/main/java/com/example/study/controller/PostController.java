package com.example.study.controller;

import com.example.study.model.SearchParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    // HTML <Form>
    // ajax 검색
    // http post body -> data
    // json, xml, multipart-form / text-plain

    @RequestMapping("/postMethod")
    public SearchParam postMapping(@RequestBody SearchParam searchParam) {

        return searchParam;
    }
}
