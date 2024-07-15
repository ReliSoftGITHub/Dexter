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
@Table(name = "monthly_data_master")
public class MonthlyDataMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sr_no;

	private String account_name;

	private String account_type;

	private String product_id;

	private String product_name;

	private String g_product_id;

	private String g_product_name;

	private String brand;

	private String division;

	private String pincode;

	private String state;

	private String city;

	private String zone;

	private String month_year;

	private Date created_at;

	private String created_by;

	private Date updated_at;

	private String updated_by;

	private Integer is_active;
	
	private Integer unit_sold;

	public Integer getSr_no() {
		return sr_no;
	}

	public void setSr_no(Integer sr_no) {
		this.sr_no = sr_no;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getMonth_year() {
		return month_year;
	}

	public void setMonth_year(String month_year) {
		this.month_year = month_year;
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

	public Integer getUnit_sold() {
		return unit_sold;
	}

	public void setUnit_sold(Integer unit_sold) {
		this.unit_sold = unit_sold;
	}
	
	
}
