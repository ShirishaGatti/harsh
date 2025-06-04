package com.backend.harsh.controller;

import com.backend.harsh.dto.ItemDTO;
import com.backend.harsh.entities.Admin;
import com.backend.harsh.entities.Item;
import com.backend.harsh.service.AdminService;
import com.backend.harsh.service.ItemService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = "*",
	    allowCredentials = "true"
	)
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private HttpSession session;
    
    @Autowired
    private AdminService adminService;

    
    
    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody ItemDTO itemDTO) {
    	   try {
        Item item = mapToEntity(itemDTO);
        Long adminId = (Long)  session.getAttribute("adminId");
        if (adminId == null ) {
            return ResponseEntity.badRequest().body("Admin not found"+adminId);
        }
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseEntity.badRequest().body("Admin not found");
        }

        item.setAdmin(admin);

     
            itemService.addItem(item);
            return ResponseEntity.ok("Item added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("An error occurred while adding the item.\nItem may be already exists");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable int id) {
        Item item = itemService.getItemById(id);
        if (item != null) {
            ItemDTO itemDTO = mapToDTO(item);
            return ResponseEntity.ok(itemDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateItem(@PathVariable int id, @RequestBody ItemDTO itemDTO) {
        Item existingItem = itemService.getItemById(id);
        if (existingItem == null) {
            return ResponseEntity.notFound().build();
        }

        if (itemDTO.getAdminId() != null) {
            Admin admin = adminService.getAdminById(itemDTO.getAdminId());
            if (admin == null) {
                return ResponseEntity.badRequest().body("Admin not found");
            }
            existingItem.setAdmin(admin);
        }
        existingItem.setDiscountPerItem(itemDTO.getDiscountPerItem());
        existingItem.setName(itemDTO.getItemName());
        existingItem.setPrice(itemDTO.getPrice());
        existingItem.setDescription(itemDTO.getDescription());
        existingItem.setStock(itemDTO.getStock());

        try {
            itemService.updateItem(id, existingItem);
            return ResponseEntity.ok("Item updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the item.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
    	
    	Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Item> items = itemService.getAllItems();
        // Map List<Item> to List<ItemDTO>
        List<ItemDTO> itemDTOs = items.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDTOs);
    }

    // Helper Method: Convert ItemDTO to Item Entity
    private Item mapToEntity(ItemDTO dto) {
        Item item = new Item();
        item.setDiscountPerItem(dto.getDiscountPerItem());
        item.setName(dto.getItemName());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        item.setStock(dto.getStock());
       
        return item;
    }

    // Helper Method: Convert Item Entity to ItemDTO
    private ItemDTO mapToDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setItemId(item.getId());
        dto.setDiscountPerItem(item.getDiscountPerItem());
        dto.setItemName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setDescription(item.getDescription());
        dto.setStock(item.getStock());
        dto.setAdminId(item.getAdmin().getId());
        return dto;
    }
}
