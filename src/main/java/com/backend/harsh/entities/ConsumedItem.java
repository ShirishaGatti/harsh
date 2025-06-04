package com.backend.harsh.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class ConsumedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ipdId", nullable = true)
    private Ipd ipd;

//    @ManyToOne
//    @JoinColumn(name = "itemId", nullable = false)
//    private Item item;
    
    @OneToMany(mappedBy = "consumedItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SelectedItems> selectedItems ;
    

    @Column(nullable = false)
    private double totalCost;

    @Column(nullable = false)
    private LocalDateTime createDate;
    
    @Column(nullable=false)
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

	public Ipd getIpd() {
		return ipd;
	}

	public void setIpd(Ipd ipd) {
		this.ipd = ipd;
	}

	public List<SelectedItems> getSelectedItems() {
		return selectedItems;
	}

	 public void setSelectedItems(List<SelectedItems> selectedItems) {
	        this.selectedItems = selectedItems;
	        for (SelectedItems item : selectedItems) {
	            item.setConsumedItems(this); // Ensure proper association
	        }
	    }

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	 public void setCreateDate(LocalDateTime createdDate) {
		 if (this.createDate == null) { // Ensure it's not overridden if already set
	            this.createDate = LocalDateTime.now();
	        }
	 }
}
