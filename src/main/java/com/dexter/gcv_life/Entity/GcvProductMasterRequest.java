package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GcvProductMasterRequest {
    
	private String account;
	private String g_product_name;
	private String g_division;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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


}
