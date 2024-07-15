package com.dexter.gcv_life.Service.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dexter.gcv_life.Entity.GrowthRequest;
import com.dexter.gcv_life.Entity.StoreReachRequest;
import com.dexter.gcv_life.Entity.TotalSalesRequest;
import com.dexter.gcv_life.Service.DashboardService;
import com.dexter.gcv_life.Service.TotalSalesService;

@Service
public class DashboardServiceImpl implements DashboardService{

	@Autowired
	TotalSalesService totalSalesService;
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public double getGrowth(GrowthRequest growthRequest) {
		try {
        	
            TotalSalesRequest totalSalesRequest = new TotalSalesRequest();
            String[] pincode = growthRequest.getPincode();
            String priceType = growthRequest.getPriceType();
            String startMon = growthRequest.getStartMonth();
            String endMon = growthRequest.getEndMonth();
            String[] accountType = growthRequest.getAccountType();
            String[] accountName = growthRequest.getAccountName();
            String[] brand = growthRequest.getBrand();
            String[] sku = growthRequest.getSku();
            String[] division = growthRequest.getDivision();
            String[] zone = growthRequest.getZone();
            String[] state = growthRequest.getState();
            String[] city = growthRequest.getCity();
            
            String startMonth = getPreviousYear(startMon);
            String endMonth = getPreviousYear(endMon);

            
            totalSalesRequest.setPincode(pincode);
            totalSalesRequest.setPriceType(priceType);
            totalSalesRequest.setBrand(brand);
            totalSalesRequest.setSku(sku);
            totalSalesRequest.setDivision(division);
            totalSalesRequest.setZone(zone);
            totalSalesRequest.setState(state);
            totalSalesRequest.setCity(city);
            totalSalesRequest.setStartMonth(startMonth);
            totalSalesRequest.setEndMonth(endMonth);
            totalSalesRequest.setAccountName(accountName);
            totalSalesRequest.setAccountType(accountType);            
            
            
            String totalSalesCurrentYr = growthRequest.getTotalSales();
            double totalSalesCurrentYear = Double.parseDouble(totalSalesCurrentYr);
            
            String totalSalesPreviousYr = totalSalesService.calculateTotalSales(totalSalesRequest);
            double totalSalesPreviousYear = Double.parseDouble(totalSalesPreviousYr);

            
//            double growth = ((totalSalesCurrentYear / totalSalesPreviousYear) - 1) * 100;

            if(totalSalesPreviousYear == 0.0) totalSalesPreviousYear=1;
            
//          double growth = ((totalSalesCurrentYear / totalSalesPreviousYear) - 1) * 100;
          double growth = ((totalSalesCurrentYear - totalSalesPreviousYear) / totalSalesPreviousYear) * 100;

          // Ensure the result is within the 1% to 100% range
          growth = Math.max(1, Math.min(growth, 100));
          
          return growth;
        } catch (Exception e) {
            return 0;
        }
	}
	
    
	private static String getPreviousYear(String inputMonthYear) {
        // Parse the input string into a LocalDate
        LocalDate inputDate = LocalDate.parse("01 " + inputMonthYear, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        
        // Subtract a year
        LocalDate resultDate = inputDate.minusYears(1);
        
        // Format the result in the desired format
        return resultDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }

	
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
    
    @Override
	public Long getStoreReach(StoreReachRequest storeReachRequest) {

    	String[] pincode = storeReachRequest.getPincode();
        String[] brand = storeReachRequest.getBrand();
        String[] sku = storeReachRequest.getSku();
        String[] division = storeReachRequest.getDivision();
        String[] zone = storeReachRequest.getZone();
        String[] state = storeReachRequest.getState();
        String[] city = storeReachRequest.getCity();
        
        String startMonth = storeReachRequest.getStartMonth();
        String endMonth = storeReachRequest.getEndMonth();
        String[] accountType = storeReachRequest.getAccountType();
        String[] accountName = storeReachRequest.getAccountName();
        
        
        List<String> monthsBetween = generateMonthsBetween(startMonth, endMonth);
        StringBuilder resultBuilder = new StringBuilder("(");
        for (int i = 0; i < monthsBetween.size(); i++) {
            resultBuilder.append("\"").append(monthsBetween.get(i)).append("\"");
            if (i < monthsBetween.size() - 1) {
                resultBuilder.append(", ");
            }
        }
        resultBuilder.append(")");
        
        String query = "";

        // Initialize the base query
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(DISTINCT(pincode)) FROM monthly_data_master WHERE");

        // Create a list to hold the conditions
        List<String> conditions = new ArrayList<>();

        conditions.add(" pincode is not null");

        // Check each condition and add it to the list if it's not empty
        if (brand != null && brand.length > 0) {
            StringBuilder brandCondition = new StringBuilder(" brand IN (");
            for (int i = 0; i < brand.length; i++) {
                brandCondition.append("\"").append(brand[i]).append("\"");
                if (i < brand.length - 1) {
                    brandCondition.append(", ");
                }
            }
            brandCondition.append(")");
            conditions.add(brandCondition.toString());
        }
        if (pincode != null && pincode.length > 0) {
            StringBuilder pincodeCondition = new StringBuilder(" pincode IN (");
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
            StringBuilder accountNameCondition = new StringBuilder(" account_name IN (");
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
            StringBuilder accountTypeCondition = new StringBuilder(" account_type IN (");
            for (int i = 0; i < accountType.length; i++) {
            	accountTypeCondition.append("\"").append(accountType[i]).append("\"");
                if (i < accountType.length - 1) {
                	accountTypeCondition.append(", ");
                }
            }
            accountTypeCondition.append(")");
            conditions.add(accountTypeCondition.toString());
        }
        if (sku != null && sku.length > 0) {
            StringBuilder skuCondition = new StringBuilder(" product_name IN (");
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
            StringBuilder divisionCondition = new StringBuilder(" division IN (");
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
            StringBuilder zoneCondition = new StringBuilder(" zone IN (");
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
            StringBuilder stateCondition = new StringBuilder(" state IN (");
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
            StringBuilder cityCondition = new StringBuilder(" city IN (");
            for (int i = 0; i < city.length; i++) {
                cityCondition.append("\"").append(city[i]).append("\"");
                if (i < city.length - 1) {
                    cityCondition.append(", ");
                }
            }
            cityCondition.append(")");
            conditions.add(cityCondition.toString());
        }

        queryBuilder.append(String.join(" AND ", conditions));

        if(conditions.size()!=0)
        queryBuilder.append(" AND month_year IN ").append(resultBuilder);
        
        else queryBuilder.append(" month_year IN ").append(resultBuilder);
        
        query = queryBuilder.toString();
        List<Map<String, Object>> pinCodeBasedData = jdbcTemplate.queryForList(query);
        Long storeReach = 0l;
        for (Map<String, Object> rowPinCodeBasedData : pinCodeBasedData){
            storeReach = (Long) rowPinCodeBasedData.get("COUNT(DISTINCT(pincode))");
        }

        return storeReach;
    }

}
