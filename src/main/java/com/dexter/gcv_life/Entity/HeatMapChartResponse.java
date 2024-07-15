package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatMapChartResponse {
    String[] brand;
    String priceType;
    String[] sku;
    String[] division;
    String startMonth;
    String endMonth;
    String[] accountType;
    String[] account;
	public String[] getBrand() {
		return brand;
	}
	public void setBrand(String[] brand) {
		this.brand = brand;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String[] getSku() {
		return sku;
	}
	public void setSku(String[] sku) {
		this.sku = sku;
	}
	public String[] getDivision() {
		return division;
	}
	public void setDivision(String[] division) {
		this.division = division;
	}
	public String getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
	public String getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	public String[] getAccountType() {
		return accountType;
	}
	public void setAccountType(String[] accountType) {
		this.accountType = accountType;
	}
	public String[] getAccount() {
		return account;
	}
	public void setAccount(String[] account) {
		this.account = account;
	}
    
}
