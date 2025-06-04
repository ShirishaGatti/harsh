package com.backend.harsh.service;

import com.backend.harsh.entities.ConsumedItem;
import java.util.List;

public interface ConsumedItemService {
    ConsumedItem addConsumedItem(ConsumedItem consumedItem);
    ConsumedItem getConsumedItemById(Long id);
    List<ConsumedItem> getAllConsumedItems();
    void deleteConsumedItem(Long id);
}
