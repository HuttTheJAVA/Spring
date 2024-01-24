package hello.upload.domain;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {
    private long sequence = 0L;
    private final Map<Long,Item> repository = new HashMap<>();

    public Item save(Item item){
        item.setId(++sequence);
        repository.put(item.getId(),item);
        return item;
    }

    public Item findById(Long id){
        return repository.get(id);
    }
}
