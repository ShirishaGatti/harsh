package com.backend.harsh.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Data
public class Ipd {

    @Id
  //  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "casePaperId", nullable = false)
    private Patient patient;

    @JsonFormat(pattern = "yyyy-MM-dd")  // Adjust format to match with timezone
    private Date admissionDate;

    @JsonFormat(pattern = "yyyy-MM-dd")  // Adjust format to match with timezone
    private Date dischargeDate;

    @Column(nullable = true, length = 1000)
    private String notes;

    @OneToMany(mappedBy = "ipd", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ConsumedItem> consumedItems = new ArrayList<>();

    @OneToMany(mappedBy = "ipd", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Bill> bills = new ArrayList<>();

    @Column
    @Nullable
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "adminId")
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



	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<ConsumedItem> getConsumedItems() {
		return consumedItems;
	}

	public void setConsumedItems(List<ConsumedItem> consumedItems) {
		this.consumedItems = consumedItems;
	}

	public List<Bill> getBills() {
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

//	public void setCreateDate(LocalDateTime createDate) {
//		this.createDate = createDate;
//	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public void setCreateDate(LocalDateTime createdDate) {
	    if (this.createDate == null && createdDate != null) {
	        this.createDate = createdDate;
	    }
	}

}
