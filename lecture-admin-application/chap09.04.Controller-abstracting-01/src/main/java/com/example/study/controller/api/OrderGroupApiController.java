package com.example.study.controller.api;

import com.example.study.interfaces.CrudInterface;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.OrderGroupApiRequest;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.service.OrderGroupApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ordergroup")
public class OrderGroupApiController implements CrudInterface<OrderGroupApiRequest, OrderGroupApiResponse> {

    @Autowired
    private OrderGroupApiService orderGroupApiService;

    @Override
    @PostMapping("")
    public Header<OrderGroupApiResponse> create(@RequestBody Header<OrderGroupApiRequest> request) {
        log.info("request : {}", request);
        return orderGroupApiService.create(request);
    }

    @Override
    @GetMapping("/{id}")
    public Header<OrderGroupApiResponse> read(@PathVariable Long id) {
        log.info("read id: {}", id);
        return orderGroupApiService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<OrderGroupApiResponse> update(@RequestBody Header<OrderGroupApiRequest> request) {
        log.info("request body : {}", request);
        return orderGroupApiService.update(request);
    }

    @Override
    @DeleteMapping("/{id}")
    public Header delete(@PathVariable Long id) {
        log.info("read id: {}", id);
        return orderGroupApiService.delete(id);
    }
}
