package hello.itemservice.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    private static Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(),item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId,Item paramItem){
        Item findItem = findById(itemId);
        findItem.setItemName(paramItem.getItemName());
        findItem.setPrice(paramItem.getPrice());
        findItem.setQuantity(paramItem.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
