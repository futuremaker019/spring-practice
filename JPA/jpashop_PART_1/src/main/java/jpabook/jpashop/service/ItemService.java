package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경감지를 이용한 데이터 수정방법
     *  영속성 컨텍스트에서 관리되는 item을 찾 아서 변경되야하는 필드를 set으로 변경해준다.
     *  변경감지(Dirty checking과 @Transactional에 의해 commit 되며
     *  JPA는 flush를 통해 데비값을 수정한다.
     *
     *  Merge는 해당 엔티티를 반환한다.
     *  Merge는 모든 필드값을 변경하기떄문에 input으로 들어오는 값이 없는 필드가 있다면
     *  NULL로 데이터가 업데이트 될수도 있다.
     *
     *  되도록이면 변경감지 기능을 사용해야한다.
     */
    @Transactional
    public void updateItem(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
