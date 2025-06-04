package com.backend.harsh.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;

    
    @Column
    private double discountPerItem;
    
    
    @Column

    private String status;
//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ConsumedItem> consumedItems = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public double getDiscountPerItem() {
		
		return discountPerItem;
	}

	public void setDiscountPerItem(double discountPerItem) {
		 if(discountPerItem<0.0 )
		    	discountPerItem=0.0;
		 else
		this.discountPerItem = discountPerItem;
	}

//	public List<ConsumedItem> getConsumedItems() {
//		return consumedItems;
//	}
//
//	public void setConsumedItems(List<ConsumedItem> consumedItems) {
//		this.consumedItems = consumedItems;
//	}
    
    
}
