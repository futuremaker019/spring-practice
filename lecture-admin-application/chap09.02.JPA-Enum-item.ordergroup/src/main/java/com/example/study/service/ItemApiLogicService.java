package com.example.study.service;

import com.example.study.entity.Item;
import com.example.study.interfaces.CrudInterface;
import com.example.study.model.enumClass.ItemStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ItemApiLogicService implements CrudInterface<ItemApiRequest, ItemApiResponse> {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public Header<ItemApiResponse> create(Header<ItemApiRequest> request) {

        ItemApiRequest body = request.getData();  // todo body must be checked if null or not

        Item item = Item.builder()
                .status(ItemStatus.REGISTERED)
                .name(body.getName())
                .title(body.getTitle())
                .content(body.getContent())
                .price(body.getPrice())
                .brandName(body.getBrandName())
                .registeredAt(LocalDateTime.now())
                .partner(partnerRepository.getOne(body.getPartnerId()))
                .build();

        Item newItem = itemRepository.save(item);
        return response(newItem);
    }

    @Override
    public Header<ItemApiResponse> read(Long id) {
        // id
        return itemRepository.findById(id)
                .map(item -> response(item))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<ItemApiResponse> update(Header<ItemApiRequest> request) {

        ItemApiRequest body = request.getData();

        return itemRepository.findById(body.getId())
                .map(entityItem -> {
                    entityItem.setStatus(body.getStatus())
                            .setName(body.getName())
                            .setTitle(body.getTitle())
                            .setContent(body.getContent())
                            .setPrice(body.getPrice())
                            .setBrandName(body.getBrandName())
                            .setRegisteredAt(body.getRegistered())
                            .setUnregisteredAt(body.getUnRegistered());
                    return entityItem;
                })
                .map(newEntityItem -> itemRepository.save(newEntityItem))
                .map(item -> response(item))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    itemRepository.delete(item);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    private Header<ItemApiResponse> response(Item item) {
        ItemApiResponse body = ItemApiResponse.builder()
                .id(item.getId())
                .status(item.getStatus())
                .name(item.getName())
                .title(item.getTitle())
                .content(item.getContent())
                .price(item.getPrice())
                .brandName(item.getBrandName())
                .registered(item.getRegisteredAt())
                .unRegistered(item.getUnregisteredAt())
                .partnerId(item.getPartner().getId())
                .build();

        return Header.OK(body);
    }

//    private Header<ItemApiResponse> getResponse(Item item) {
////
////        String statusTitle = item.getStatus().getTitle();
////
////        ItemApiResponse body = ItemApiResponse.builder()
////                .id(item.getId())
////                .status(item.getStatus())
////                .name(item.getName())
////                .title(item.getTitle())
////                .content(item.getContent())
////                .price(item.getPrice())
////                .brandName(item.getBrandName())
////                .registered(item.getRegisteredAt())
////                .unRegistered(item.getUnregisteredAt())
////                .partnerId(item.getPartner().getId())
////                .build();
////
////        return Header.OK(body);
////    }
}
