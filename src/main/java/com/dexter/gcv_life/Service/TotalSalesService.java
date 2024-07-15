package com.dexter.gcv_life.Service;

import com.dexter.gcv_life.Entity.TotalSalesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class TotalSalesService {
    @Autowired
    
    private JdbcTemplate jdbcTemplate;
    
    public static List<String> generateMonthsBetween(String startMonth, String endMonth) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

        try {
            Date startDate = dateFormat.parse(startMonth);
            Date endDate = dateFormat.parse(endMonth);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                String formattedMonth = dateFormat.format(calendar.getTime());
                result.add(formattedMonth);
                calendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public String calculateTotalSales(TotalSalesRequest totalSalesRequest) {
        try {
            String[] pincode = totalSalesRequest.getPincode();
            String priceType = totalSalesRequest.getPriceType();
            String[] brand = totalSalesRequest.getBrand();
            String[] sku = totalSalesRequest.getSku();
            String[] division = totalSalesRequest.getDivision();
            String[] zone = totalSalesRequest.getZone();
            String[] state = totalSalesRequest.getState();
            String[] city = totalSalesRequest.getCity();
            
            String startMonth = totalSalesRequest.getStartMonth();
            String endMonth = totalSalesRequest.getEndMonth();
            String[] accountType = totalSalesRequest.getAccountType();
            String[] accountName = totalSalesRequest.getAccountName();
            
            
            List<String> monthsBetween = generateMonthsBetween(startMonth, endMonth);
	        StringBuilder resultBuilder = new StringBuilder("(");
	        for (int i = 0; i < monthsBetween.size(); i++) {
	            resultBuilder.append("\"").append(monthsBetween.get(i)).append("\"");
	            if (i < monthsBetween.size() - 1) {
	                resultBuilder.append(", ");
	            }
	        }
	        resultBuilder.append(")");
	        
	        
            double totalSales = 0;

            String query = "";
            if(priceType == null)priceType="mrp";
            // Initialize the base query
            StringBuilder queryBuilder = new StringBuilder("SELECT sum(mdm.unit_sold*pm."+priceType+") as quantity FROM monthly_data_master mdm join price_master pm on mdm.g_product_id = pm.g_product_id WHERE ");

            // Create a list to hold the conditions
            List<String> conditions = new ArrayList<>();

            // Check each condition and add it to the list if it's not empty
            if (pincode != null && pincode.length > 0) {
                StringBuilder pincodeCondition = new StringBuilder(" mdm.pincode IN (");
                for (int i = 0; i < pincode.length; i++) {
                    pincodeCondition.append("\"").append(pincode[i]).append("\"");
                    if (i < pincode.length - 1) {
                        pincodeCondition.append(", ");
                    }
                }
                pincodeCondition.append(")");
                conditions.add(pincodeCondition.toString());
            }
            if (accountName != null && accountName.length > 0) {
                StringBuilder accountNameCondition = new StringBuilder(" mdm.account_name IN (");
                for (int i = 0; i < accountName.length; i++) {
                	accountNameCondition.append("\"").append(accountName[i]).append("\"");
                    if (i < accountName.length - 1) {
                    	accountNameCondition.append(", ");
                    }
                }
                accountNameCondition.append(")");
                conditions.add(accountNameCondition.toString());
            }
            if (accountType != null && accountType.length > 0) {
                StringBuilder accountTypeCondition = new StringBuilder(" mdm.account_type IN (");
                for (int i = 0; i < accountType.length; i++) {
                	accountTypeCondition.append("\"").append(accountType[i]).append("\"");
                    if (i < accountType.length - 1) {
                    	accountTypeCondition.append(", ");
                    }
                }
                accountTypeCondition.append(")");
                conditions.add(accountTypeCondition.toString());
            }
            if (brand != null && brand.length > 0) {
                StringBuilder brandCondition = new StringBuilder(" mdm.brand IN (");
                for (int i = 0; i < brand.length; i++) {
                    brandCondition.append("\"").append(brand[i]).append("\"");
                    if (i < brand.length - 1) {
                        brandCondition.append(", ");
                    }
                }
                brandCondition.append(")");
                conditions.add(brandCondition.toString());
            }
            if (sku != null && sku.length > 0) {
                StringBuilder skuCondition = new StringBuilder(" mdm.product_name IN (");
                for (int i = 0; i < sku.length; i++) {
                    skuCondition.append("\"").append(sku[i]).append("\"");
                    if (i < sku.length - 1) {
                        skuCondition.append(", ");
                    }
                }
                skuCondition.append(")");
                conditions.add(skuCondition.toString());
            }
            if (division != null && division.length > 0) {
                StringBuilder divisionCondition = new StringBuilder(" mdm.division IN (");
                for (int i = 0; i < division.length; i++) {
                    divisionCondition.append("\"").append(division[i]).append("\"");
                    if (i < division.length - 1) {
                        divisionCondition.append(", ");
                    }
                }
                divisionCondition.append(")");
                conditions.add(divisionCondition.toString());
            }
            if (zone != null && zone.length > 0) {
                StringBuilder zoneCondition = new StringBuilder(" mdm.zone IN (");
                for (int i = 0; i < zone.length; i++) {
                    zoneCondition.append("\"").append(zone[i]).append("\"");
                    if (i < zone.length - 1) {
                        zoneCondition.append(", ");
                    }
                }
                zoneCondition.append(")");
                conditions.add(zoneCondition.toString());
            }
            if (state != null && state.length > 0) {
                StringBuilder stateCondition = new StringBuilder(" mdm.state IN (");
                for (int i = 0; i < state.length; i++) {
                    stateCondition.append("\"").append(state[i]).append("\"");
                    if (i < state.length - 1) {
                        stateCondition.append(", ");
                    }
                }
                stateCondition.append(")");
                conditions.add(stateCondition.toString());
            }
            if (city != null && city.length > 0) {
                StringBuilder cityCondition = new StringBuilder(" mdm.city IN (");
                for (int i = 0; i < city.length; i++) {
                    cityCondition.append("\"").append(city[i]).append("\"");
                    if (i < city.length - 1) {
                        cityCondition.append(", ");
                    }
                }
                cityCondition.append(")");
                conditions.add(cityCondition.toString());
            }

            // Combine the conditions with "AND" and add them to the query
            queryBuilder.append(String.join(" AND ", conditions));

            if(conditions.size()!=0)
            queryBuilder.append(" AND mdm.month_year IN ").append(resultBuilder);
            
            else queryBuilder.append("mdm.month_year IN ").append(resultBuilder);
            
            query = queryBuilder.toString();
            
            List<Map<String, Object>> pinCodeBasedData = jdbcTemplate.queryForList(query);

            totalSales = pinCodeBasedData.get(0).get("quantity") == null ? 0.0 : (double) pinCodeBasedData.get(0).get("quantity");

            DecimalFormat df = new DecimalFormat("#.#########"); // Adjust the number of decimals as needed
            String formattedResult = df.format(totalSales);
            
            return formattedResult;
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}

