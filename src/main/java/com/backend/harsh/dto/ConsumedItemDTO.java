package com.backend.harsh.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
public class ConsumedItemDTO {
	
	private Long id;
    private Long ipdId;
    private List<SelectedItemDto> selectedItems;
    private double totalPrice;
    private LocalDateTime createDate;
private double stock;
    
    

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}    
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIpdId() {
		return ipdId;
	}
	public void setIpdId(Long ipdId) {
		this.ipdId = ipdId;
	}
	

	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<SelectedItemDto> getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(List<SelectedItemDto> selectedItems) {
		this.selectedItems = selectedItems;
	}
		public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
    
    
}