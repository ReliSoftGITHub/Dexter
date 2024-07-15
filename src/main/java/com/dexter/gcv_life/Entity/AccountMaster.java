package com.dexter.gcv_life.Entity;

import java.sql.Date;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "account_master")
public class AccountMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer row_id;
	
	private String keey;
	
	private String type;
	
	private String account;
	
	private String details;
	
	private String state;
	
	private String city;
	
	private String raw_pincode;
	
	private String gcv_pincode;
	
	private String store_id;
	
	private String product_id;
	
	private String product_name;
	
	private String gcv_product_id;
	
	private String gcv_product_name;
	
	private Date created_at;
	
	private String created_by;
	
	private Date updated_at;
	
	private String updated_by;
	
	private Integer is_active;

	public Integer getRow_id() {
		return row_id;
	}

	public void setRow_id(Integer row_id) {
		this.row_id = row_id;
	}

	public String getKeey() {
		return keey;
	}

	public void setKeey(String keey) {
		this.keey = keey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRaw_pincode() {
		return raw_pincode;
	}

	public void setRaw_pincode(String raw_pincode) {
		this.raw_pincode = raw_pincode;
	}

	public String getGcv_pincode() {
		return gcv_pincode;
	}

	public void setGcv_pincode(String gcv_pincode) {
		this.gcv_pincode = gcv_pincode;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getGcv_product_id() {
		return gcv_product_id;
	}

	public void setGcv_product_id(String gcv_product_id) {
		this.gcv_product_id = gcv_product_id;
	}

	public String getGcv_product_name() {
		return gcv_product_name;
	}

	public void setGcv_product_name(String gcv_product_name) {
		this.gcv_product_name = gcv_product_name;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	
}
