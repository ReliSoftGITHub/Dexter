package com.dexter.gcv_life.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvolutionIndexRequest {
//    String[] pincode;
//    String priceType;
//    String month;
//    String year;
//    String[] brand;
//    String[] sku;
//    String[] division;
//    String[] zone;
//    String[] state;
//    String[] city;
    double growth;

	public double getGrowth() {
		return growth;
	}

	public void setGrowth(double growth) {
		this.growth = growth;
	}
    
    
}
