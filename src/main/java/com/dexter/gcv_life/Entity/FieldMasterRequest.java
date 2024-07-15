package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldMasterRequest {

	private String division;
	private String zone_name;
	private String territory_code;
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}
	public String getTerritory_code() {
		return territory_code;
	}
	public void setTerritory_code(String territory_code) {
		this.territory_code = territory_code;
	}
	
}


