package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TerritoryMasterRequest {
    private String division;
    private String territory_name;
    private String zone_name;
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getTerritory_name() {
		return territory_name;
	}
	public void setTerritory_name(String territory_name) {
		this.territory_name = territory_name;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}
    
    
}
