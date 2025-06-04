package com.backend.harsh.dto;


import java.time.LocalDateTime;
import java.util.Date;

import com.backend.harsh.entities.Patient;

import lombok.Data;

@Data
public class PatientDTO {
    private long patientId;

    private String name;
    private String mobile;
    private String address;
    private String gender;
    private String status;
    private String notes;
    private Long adminId;
    private Date regDate;

	//.Patient patient=new Patient();
 
	public String getName() {
		return name;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public long getPatientId() {
        return patientId;
    }
	 public void setPatientId(long PatientId) {
	//	 Patient patient=new Patient();
	        this.patientId = PatientId;
	    }
    
}
