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
@Table(name = "gcv_product_master")
public class GcvProductMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer row_id;

	private String keey;
	
	private String account;

	private String sku_name;

	private String sku_id;

	private String g_product_id;

	private String g_product_name;

	private String g_division;

	private String brand;

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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getG_product_id() {
		return g_product_id;
	}

	public void setG_product_id(String g_product_id) {
		this.g_product_id = g_product_id;
	}

	public String getG_product_name() {
		return g_product_name;
	}

	public void setG_product_name(String g_product_name) {
		this.g_product_name = g_product_name;
	}

	public String getG_division() {
		return g_division;
	}

	public void setG_division(String g_division) {
		this.g_division = g_division;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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
