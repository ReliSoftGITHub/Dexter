package com.dexter.gcv_life.Entity;

import java.sql.Date;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "process_files")
public class ProcessFiles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sr_no;
	
	private String account_name;
	
	private String file_name;
	
	private String status;
	
	private int thread_count;

	public int getSr_no() {
		return sr_no;
	}

	public void setSr_no(int sr_no) {
		this.sr_no = sr_no;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getThread_count() {
		return thread_count;
	}

	public void setThread_count(int thread_count) {
		this.thread_count = thread_count;
	}
	
	

}
