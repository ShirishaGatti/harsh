package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.ConsumedItem;
import com.backend.harsh.entities.Item;
import com.backend.harsh.repository.ConsumedItemRepository;
import com.backend.harsh.repository.ItemRepository;
import com.backend.harsh.service.ConsumedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumedItemServiceImpl implements ConsumedItemService {

    @Autowired
    private ConsumedItemRepository consumedItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ConsumedItem addConsumedItem(ConsumedItem consumedItem) {
//        Item item = itemRepository.findById(consumedItem.getItem().getId())
//                .orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + consumedItem.getItem().getId()));
//
//        double totalCost = consumedItem.getQuantity() * item.getPrice();
//        consumedItem.setTotalCost(totalCost);

        return consumedItemRepository.save(consumedItem);
    }

    @Override
    public ConsumedItem getConsumedItemById(Long id) {
        return consumedItemRepository.findById(id).orElse(null);
    }

    @Override
    public List<ConsumedItem> getAllConsumedItems() {
        return consumedItemRepository.findAll();
    }

    @Override
    public void deleteConsumedItem(Long id) {
        consumedItemRepository.deleteById(id);
    }
}
