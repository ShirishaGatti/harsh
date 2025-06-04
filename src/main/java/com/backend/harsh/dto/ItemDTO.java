package com.backend.harsh.dto;

import lombok.Data;

@Data
public class ItemDTO {
	
	
	private int itemId;
    private String itemName;
    private String description;
    private double price;
    private int stock;
    private Long adminId;
    private double discountPerItem;
    private String status;
    //discount code change
    
    
    
    
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getDiscountPerItem() {
		return discountPerItem;
	}
	public void setDiscountPerItem(double discountPerItem) {
		this.discountPerItem = discountPerItem;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
    
    
}
