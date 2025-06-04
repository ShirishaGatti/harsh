package com.backend.harsh.controller;

import com.backend.harsh.dto.ConsumedItemDTO;
import com.backend.harsh.dto.SelectedItemDto;
import com.backend.harsh.entities.ConsumedItem;
import com.backend.harsh.entities.Ipd;
import com.backend.harsh.entities.Item;
import com.backend.harsh.entities.SelectedItems;
import com.backend.harsh.service.ConsumedItemService;
import com.backend.harsh.service.IpdService;
import com.backend.harsh.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consumedItems")
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = "*",
	    allowCredentials = "true"
	)
public class ConsumedItemController {

    @Autowired
    private ConsumedItemService consumedItemService;

    @Autowired
    private IpdService ipdService;

    @Autowired
    private ItemService itemService;

    // Add ConsumedItem (handling DTO)
    @PostMapping("/add")
    public ResponseEntity<String> addConsumedItem(@RequestBody ConsumedItemDTO consumedItemDTO) {
        try {
            Ipd ipd = ipdService.getIpdById(consumedItemDTO.getIpdId());
            if (ipd == null) {
                return ResponseEntity.badRequest().body("IPD record not found.");
            }

            List<SelectedItems> selectedItemsList = new ArrayList<>();
            double totalCost = 0;
//
//            for (SelectedItemDto selectedItemDTO : consumedItemDTO.getSelectedItems()) {
//                Item item = itemService.getItemByName(selectedItemDTO.getItemName());
//                if (item == null) {
//                    return ResponseEntity.badRequest().body("Item not found: " + selectedItemDTO.getItemName());
//                }
//
//                totalCost += selectedItemDTO.getQuantity() * (item.getPrice() * (1 - item.getDiscountPerItem() / 100.0));
//
//                // Create SelectedItems entity
//                SelectedItems selectedItem = new SelectedItems();
//                if(item.getStock()>=selectedItemDTO.getQuantity())
//                {
//                selectedItem.setItemName(selectedItemDTO.getItemName());
//                selectedItem.setQuantity(selectedItemDTO.getQuantity());
//                selectedItem.setPrice(item.getPrice());
//                item.setStock(item.getStock()-selectedItemDTO.getQuantity());
//                Item itemId=itemService.getItemByName(selectedItem.getItemName());
//                itemService.updateItem(itemId.getId(), item);
//
//                selectedItemsList.add(selectedItem);
//                }
//                else {
//                	return ResponseEntity.ok().body("Item out of stock"+selectedItemDTO.getItemName());
//                }
//            }
            for (SelectedItemDto selectedItemDTO : consumedItemDTO.getSelectedItems()) {
                Item item = itemService.getItemByName(selectedItemDTO.getItemName());
                if (item == null) {
                    return ResponseEntity.badRequest().body("Item not found: " + selectedItemDTO.getItemName());
                }

                // Check if stock is sufficient
                if (item.getStock() < selectedItemDTO.getQuantity()) {
                    return ResponseEntity.badRequest().body("Item out of stock: " + selectedItemDTO.getItemName());
                }
            }

            // Process the items since all validations passed
//            for (SelectedItemDto selectedItemDTO : consumedItemDTO.getSelectedItems()) {
//                Item item = itemService.getItemByName(selectedItemDTO.getItemName());
//
//                totalCost += selectedItemDTO.getQuantity() * (item.getPrice() * (1 - item.getDiscountPerItem() / 100.0));
//
//                // Create SelectedItems entity
//                SelectedItems selectedItem = new SelectedItems();
//                selectedItem.setItemName(selectedItemDTO.getItemName());
//                selectedItem.setQuantity(selectedItemDTO.getQuantity());
//                selectedItem.setPrice(item.getPrice());
//
//                // Deduct stock
//                item.setStock(item.getStock() - selectedItemDTO.getQuantity());
//                itemService.updateItem(item.getId(), item);
//
//                selectedItemsList.add(selectedItem);
//            }
            for (SelectedItemDto selectedItemDTO : consumedItemDTO.getSelectedItems()) {
                Item item = itemService.getItemByName(selectedItemDTO.getItemName());
                if (item == null) {
                    return ResponseEntity.badRequest().body("Item not found: " + selectedItemDTO.getItemName());
                }

                // Check if stock is sufficient
                if (item.getStock() < selectedItemDTO.getQuantity()) {
                    return ResponseEntity.badRequest().body("Item out of stock: " + selectedItemDTO.getItemName());
                }

                // Apply discount to calculate the correct item price
                double discountedPrice = item.getPrice() * (1 - item.getDiscountPerItem() / 100.0);

                // Calculate the total cost for this item (Quantity * Discounted Price)
                totalCost += selectedItemDTO.getQuantity() * discountedPrice;

                // Create SelectedItems entity
                SelectedItems selectedItem = new SelectedItems();
                selectedItem.setItemName(selectedItemDTO.getItemName());
                selectedItem.setQuantity(selectedItemDTO.getQuantity());
                selectedItem.setPrice(totalCost);  // Set the discounted price

                // Deduct stock
                item.setStock(item.getStock() - selectedItemDTO.getQuantity());
                itemService.updateItem(item.getId(), item);

                selectedItemsList.add(selectedItem);
            }


            ConsumedItem consumedItem = mapToEntity(consumedItemDTO, totalCost, ipd, selectedItemsList);

            // First, persist the ConsumedItem entity WITHOUT setting SelectedItems
            for (SelectedItems selectedItem : selectedItemsList) {
                selectedItem.setConsumedItems(consumedItem); // Ensure proper linking
            }

            consumedItem.setSelectedItems(selectedItemsList); // Attach selected items

            // Now, persist everything in a single transaction
            consumedItemService.addConsumedItem(consumedItem);

            return ResponseEntity.ok("Consumed Item added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding consumed items: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ConsumedItemDTO>> getAllConsumedItems() {
        List<ConsumedItem> items = consumedItemService.getAllConsumedItems();
        return ResponseEntity.ok(items.stream().map(this::mapToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumedItemDTO> getConsumedItemById(@PathVariable Long id) {
        ConsumedItem item = consumedItemService.getConsumedItemById(id);
        if (item == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapToDTO(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateConsumedItem(@PathVariable Long id, @RequestBody ConsumedItemDTO consumedItemDTO) {
        try {
            ConsumedItem existingItem = consumedItemService.getConsumedItemById(id);
            if (existingItem == null) {
                return ResponseEntity.notFound().build();
            }
            ConsumedItem updatedItem = mapToEntity(consumedItemDTO, consumedItemDTO.getTotalPrice(), existingItem.getIpd(), existingItem.getSelectedItems());
            updatedItem.setId(id);
            consumedItemService.addConsumedItem(updatedItem);
            return ResponseEntity.ok("Consumed Item updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsumedItem(@PathVariable Long id) {
        consumedItemService.deleteConsumedItem(id);
        return ResponseEntity.ok("Consumed Item deleted successfully.");
    }

 
    private ConsumedItem mapToEntity(ConsumedItemDTO dto, double totalCost, Ipd ipd, List<SelectedItems> selectedItemsList) {
        ConsumedItem consumedItem = new ConsumedItem();
        consumedItem.setIpd(ipd);
      //  consumedItem.setItem(item);
        consumedItem.setSelectedItems(selectedItemsList);
        if (dto.getTotalPrice()==totalCost)
			 consumedItem.setTotalCost(totalCost);
		else
		 consumedItem.setTotalCost(dto.getTotalPrice());
        
        consumedItem.setCreateDate(LocalDateTime.now());
        return consumedItem;
    }
    private ConsumedItemDTO mapToDTO(ConsumedItem consumedItem) {
        ConsumedItemDTO dto = new ConsumedItemDTO();
        dto.setId(consumedItem.getId());
        dto.setIpdId(consumedItem.getIpd().getId());
        dto.setTotalPrice(consumedItem.getTotalCost());
        dto.setCreateDate(consumedItem.getCreateDate());
        dto.setSelectedItems(consumedItem.getSelectedItems().stream().map(selectedItem -> {
            SelectedItemDto selectedItemDTO = new SelectedItemDto();
            selectedItemDTO.setItemName(selectedItem.getItemName());
            selectedItemDTO.setQuantity(selectedItem.getQuantity());
            selectedItemDTO.setPrice(selectedItem.getPrice());
            return selectedItemDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

//    // Get ConsumedItem by ID (handling DTO)
//    @GetMapping("/{id}")
//    public ResponseEntity<ConsumedItemDTO> getConsumedItemById(@PathVariable Long id) {
//        ConsumedItem consumedItem = consumedItemService.getConsumedItemById(id);
//        if (consumedItem != null) {
//            ConsumedItemDTO consumedItemDTO = mapToDTO(consumedItem);
//            return ResponseEntity.ok(consumedItemDTO);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    // Get all ConsumedItems (handling DTO)
//    @GetMapping("/all")
//    public ResponseEntity<List<ConsumedItemDTO>> getAllConsumedItems() {
//        List<ConsumedItem> consumedItems = consumedItemService.getAllConsumedItems();
//        if (consumedItems.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Convert entities to DTOs
//        List<ConsumedItemDTO> consumedItemDTOs = consumedItems.stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(consumedItemDTOs);
//    }
//
//    // Delete ConsumedItem by ID
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteConsumedItem(@PathVariable Long id) {
//        consumedItemService.deleteConsumedItem(id);
//        return ResponseEntity.ok("Consumed Item deleted successfully.");
//    }

    // Helper Method: Convert ConsumedItemDTO to ConsumedItem Entity

    // Helper Method: Convert ConsumedItem Entity to ConsumedItemDTO
//    private ConsumedItemDTO mapToDTO(ConsumedItem consumedItem) {
//        ConsumedItemDTO dto = new ConsumedItemDTO();
//        dto.setIpdId(consumedItem.getIpd().getId());
//        //dto.setName(consumedItem.getItem().getName());
//        dto.setQuantity(consumedItem.getQuantity());
//        dto.setTotalCost(consumedItem.getTotalCost());
//        dto.setCreateDate(consumedItem.getCreateDate());
//        return dto;
//    }
}
