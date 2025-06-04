
package com.backend.harsh.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class IpdDTO {
	
	private Long ipdId;
    private Long casePaperId;
    private Long adminId;
    private LocalDateTime createDate;
    private Date admissionDate;
    private Date dischargeDate;
    private String notes;
    private Double amount; // Total estimated amount
    
    
    
    
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Long getIpdId() {
		return ipdId;
	}
	public void setIpdId(Long id) {
		this.ipdId = id;
	}
	public Long getCasePaperId() {
		return casePaperId;
	}
	public void setCasePaperId(Long casePAperId) {
		this.casePaperId = casePAperId;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = LocalDateTime.now();
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
    
    
}
