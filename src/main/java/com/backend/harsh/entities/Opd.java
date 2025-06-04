package com.backend.harsh.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Opd {

    @Id
 //   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "casePaperId", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private Date opdDate;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = true, length = 1000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "adminId",nullable=true)
    private Admin admin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Date getOpdDate() {
		return opdDate;
	}

	public void setOpdDate(Date opdDate) {
		this.opdDate = opdDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createdDate) {
	    if (this.createDate == null && createdDate != null) {
	        this.createDate = createdDate;
	    }
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
    
    
}
