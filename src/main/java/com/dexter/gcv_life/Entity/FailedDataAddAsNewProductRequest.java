package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FailedDataAddAsNewProductRequest {

    String keey;
    String account;
    String sku_id;
    String sku_name;
    String g_product_id;
    String g_product_name;
    String g_division;
    String brand;
    Double mrp;
    Double pts;
    Double ptr;
    String sr_no;
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
	public String getSku_id() {
		return sku_id;
	}
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}
	public String getSku_name() {
		return sku_name;
	}
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
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
	public Double getMrp() {
		return mrp;
	}
	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
	public Double getPts() {
		return pts;
	}
	public void setPts(Double pts) {
		this.pts = pts;
	}
	public Double getPtr() {
		return ptr;
	}
	public void setPtr(Double ptr) {
		this.ptr = ptr;
	}
	public String getSr_no() {
		return sr_no;
	}
	public void setSr_no(String sr_no) {
		this.sr_no = sr_no;
	}


}
