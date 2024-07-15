package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
	
	private String group;
	
	private String subGroup;
	
	private String existingNewSubgroup;
	
	private String brand;
	
	private String brandGroup;
	
	private String SKUGproductName;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public String getExistingNewSubgroup() {
		return existingNewSubgroup;
	}

	public void setExistingNewSubgroup(String existingNewSubgroup) {
		this.existingNewSubgroup = existingNewSubgroup;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandGroup() {
		return brandGroup;
	}

	public void setBrandGroup(String brandGroup) {
		this.brandGroup = brandGroup;
	}

	public String getSKUGproductName() {
		return SKUGproductName;
	}

	public void setSKUGproductName(String sKUGproductName) {
		SKUGproductName = sKUGproductName;
	}
	
	
}
