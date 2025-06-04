package com.backend.harsh.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class OpdDTO {
	
	private Long opdId;
    private Long casePaperId;
    private Date opdDate;
    private LocalDateTime createDate;
    private double totalAmount;
    private String notes;
    private Long adminId;
	
    
    
	public Long getOpdId() {
		return opdId;
	}
	public void setOpdId(Long opdId) {
		this.opdId = opdId;
	}
	public Long getCasePaperId() {
		return casePaperId;
	}
	public void setCasePaperId(Long casePaperId) {
		this.casePaperId = casePaperId;
	}
	public Date getOpdDate() {
		return opdDate;
	}
	public void setOpdDate(Date opdDate) {
		this.opdDate = opdDate;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
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
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
    
    
}