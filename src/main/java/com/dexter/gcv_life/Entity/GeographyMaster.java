package com.dexter.gcv_life.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeographyMaster {
    @Id
    @Column(name = "row_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rowId;

    @Column(name = "office_name")
    private String officeName;

    @Column(name = "clean_area_name")
    private String cleanAreaName;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "gcv_city")
    private String gcvCity;

    @Column(name = "sub_distname")
    private String subDistname;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "zone")
    private String zone;

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getCleanAreaName() {
		return cleanAreaName;
	}

	public void setCleanAreaName(String cleanAreaName) {
		this.cleanAreaName = cleanAreaName;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getGcvCity() {
		return gcvCity;
	}

	public void setGcvCity(String gcvCity) {
		this.gcvCity = gcvCity;
	}

	public String getSubDistname() {
		return subDistname;
	}

	public void setSubDistname(String subDistname) {
		this.subDistname = subDistname;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
    
    
}
