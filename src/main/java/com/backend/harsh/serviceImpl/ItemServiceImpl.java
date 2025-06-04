package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.Item;
import com.backend.harsh.repository.ItemRepository;
import com.backend.harsh.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item); 
    }

    @Override
    public Item getItemById(int id) {
        return itemRepository.findById(id).orElse(null);  
    }

    @Override
    public Item updateItem(int id, Item item) {
        if (itemRepository.existsById(id)) {
            item.setId(id);
            return itemRepository.save(item);
        }
        return null;
    }

    @Override
    public void deleteItem(int id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        }
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

	@Override
	public Item getItemByName(String name) {
		return itemRepository.findByName(name).orElse(null);  
	}
}
