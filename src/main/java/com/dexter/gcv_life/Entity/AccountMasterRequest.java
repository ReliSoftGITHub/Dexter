package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMasterRequest {

	String account;
	String product_name;
	String gcv_product_name;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getGcv_product_name() {
		return gcv_product_name;
	}
	public void setGcv_product_name(String gcv_product_name) {
		this.gcv_product_name = gcv_product_name;
	}
	
}
