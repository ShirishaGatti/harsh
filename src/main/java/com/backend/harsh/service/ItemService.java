package com.backend.harsh.service;

import com.backend.harsh.entities.Item;
import java.util.List;

public interface ItemService {
    Item addItem(Item item);
    Item getItemById(int id);
    Item updateItem(int id, Item item);
    void deleteItem(int id);
    List<Item> getAllItems();
	Item getItemByName(String name);
}
