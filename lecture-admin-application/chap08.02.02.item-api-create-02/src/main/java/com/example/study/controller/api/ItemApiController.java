package com.example.study.controller.api;

import com.example.study.interfaces.CrudInterface;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.service.ItemApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/item")
public class ItemApiController implements CrudInterface<ItemApiRequest, ItemApiResponse> {

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    @Override
    @PostMapping("")    // api/item/
    public Header<ItemApiResponse> create(@RequestBody Header<ItemApiRequest> request) {
        log.info("request body : {}", request);
        return itemApiLogicService.create(request);
    }

    @Override
    @GetMapping("/{id}")   // api/item/1
    public Header<ItemApiResponse> read(@PathVariable Long id) {
        log.info("id : {}", id);
        return itemApiLogicService.read(id);
    }

    @Override
    @PutMapping("")   // api/item
    public Header<ItemApiResponse> update(@RequestBody Header<ItemApiRequest> request) {
        log.info("request body : {}", request);
        return itemApiLogicService.update(request);
    }

    @Override
    @DeleteMapping("/{id}")   // api/item/1....100
    public Header delete(@PathVariable Long id) {
        log.info("id : {}", id);
        return itemApiLogicService.delete(id);
    }
}
