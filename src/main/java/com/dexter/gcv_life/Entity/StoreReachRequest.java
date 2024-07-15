package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreReachRequest {
    String[] pincode;
    String startMonth;
    String endMonth;
    String[] accountType;
    String[] accountName;
    String[] brand;
    String[] sku;
    String[] division;
    String[] zone;
    String[] state;
    String[] city;
	public String[] getPincode() {
		return pincode;
	}
	public void setPincode(String[] pincode) {
		this.pincode = pincode;
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
	public String[] getAccountName() {
		return accountName;
	}
	public void setAccountName(String[] accountName) {
		this.accountName = accountName;
	}
	public String[] getBrand() {
		return brand;
	}
	public void setBrand(String[] brand) {
		this.brand = brand;
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
	public String[] getZone() {
		return zone;
	}
	public void setZone(String[] zone) {
		this.zone = zone;
	}
	public String[] getState() {
		return state;
	}
	public void setState(String[] state) {
		this.state = state;
	}
	public String[] getCity() {
		return city;
	}
	public void setCity(String[] city) {
		this.city = city;
	}
    
    
}
