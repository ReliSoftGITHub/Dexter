package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PincodeReportsRequest {

	private String startMonth;
	private String endMonth;
//	private String iDisplayLength;	
//	private String iDisplayStart;
	private String[] pincodes;
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
	public String[] getPincodes() {
		return pincodes;
	}
	public void setPincodes(String[] pincodes) {
		this.pincodes = pincodes;
	}
	
	
}
