package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandReportsRequest {

	private String startMonth;
	private String endMonth;
	private String iDisplayLength;	
	private String iDisplayStart;
	private String[] brands;
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
	public String getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	public String getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public String[] getBrands() {
		return brands;
	}
	public void setBrands(String[] brands) {
		this.brands = brands;
	}
	
	
}
