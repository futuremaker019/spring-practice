package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    /**
     *  스프링 컨테이너 안에서 사용하면 어차피 싱글톤이기 떄문에 static을 명시하지 않아도 되지만
     *  new 키워드를 사용하여 객체를 생성하게되면 store가 따로 생성되기 떄문에 문제가 된다.
      */

    private static final Map<Long, Item> store = new HashMap<>();   // static
    private static long sequence = 0L;  // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    // arraylist에 값이 들어가도 store에는 영향이 없기때문에 arraylist로 한번 감싸준다.
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
