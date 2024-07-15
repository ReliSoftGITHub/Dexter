package com.dexter.gcv_life.Entity;

import java.util.Date;

//import javax.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "failed_monthly_data")
public class FailedMonthlyData {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sr_no;

	@Column
	private String account_name;

	@Column
	private String product_id;

	@Column
	private String product_name;

	@Column
	private String nearest_product_name;

	@Column
	private String pincode;

	@Column
	private String month_year;

	@Column
	private Integer unit_sold;

	private Date created_at;

	private String created_by;

	private Date updated_at;

	private String updated_by;

	private Integer is_active;

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

	public String getNearest_product_name() {
		return nearest_product_name;
	}

	public void setNearest_product_name(String nearest_product_name) {
		this.nearest_product_name = nearest_product_name;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getMonth_year() {
		return month_year;
	}

	public void setMonth_year(String month_year) {
		this.month_year = month_year;
	}

	public Integer getUnit_sold() {
		return unit_sold;
	}

	public void setUnit_sold(Integer unit_sold) {
		this.unit_sold = unit_sold;
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
