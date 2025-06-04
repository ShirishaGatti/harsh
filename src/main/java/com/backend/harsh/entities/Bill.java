package com.backend.harsh.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "casePaperId", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "ipdId", nullable = false)
    private Ipd ipd;

    @Column(nullable = false)
    private String billDate;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
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

	public Ipd getIpd() {
		return ipd;
	}

	public void setIpd(Ipd ipd) {
		this.ipd = ipd;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
    
    
}
