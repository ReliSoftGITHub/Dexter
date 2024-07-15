package com.dexter.gcv_life.Controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Entity.EvolutionIndexRequest;
import com.dexter.gcv_life.Entity.GrowthRequest;
import com.dexter.gcv_life.Entity.HeatMapChartResponse;
import com.dexter.gcv_life.Entity.StoreReachRequest;
import com.dexter.gcv_life.Entity.TotalSalesRequest;
import com.dexter.gcv_life.Entity.WaveChartDashboardRequest;
import com.dexter.gcv_life.Entity.WaveChartDashboardResponse;
import com.dexter.gcv_life.Repository.GeographyMasterRepository;
import com.dexter.gcv_life.Service.DashboardService;
import com.dexter.gcv_life.Service.TotalSalesService;


@RestController
@CrossOrigin
public class DashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TotalSalesService totalSalesService;

    @Autowired
    private GeographyMasterRepository geographyMasterRepository;

    @Autowired
    private DashboardService dashboardService;
    
    

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    
    @PostMapping("/getWaveChart")
    public ResponseEntity<List<WaveChartDashboardResponse>> waveChart(@RequestBody WaveChartDashboardRequest request) {

        String[] pincode = request.getPincode();
        String priceType = request.getPriceType();
        String[] brand = request.getBrand();
        String[] sku = request.getSku();
        String[] division = request.getDivision();
        String[] zone = request.getZone();
        String[] state = request.getState();
        String[] city = request.getCity();
        
        String startMonth = request.getStartMonth();
        String endMonth = request.getEndMonth();
        String[] accountType = request.getAccountType();
        String[] accountName = request.getAccountName();

        ArrayList<WaveChartDashboardResponse> responseList = new ArrayList<>();

        try {

	        List<String> lastSixMonths = generateMonthsBetween(startMonth, endMonth);
            
            String query = "";

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
            
            queryBuilder.append(String.join(" AND ", conditions));

            for (String month6 : lastSixMonths) {
                System.out.println(month6);
                String stringToRemove;
                
                List<String> Currentlast4Months = getCurrentLast4Months(month6);
                List<String> lastFourMonthsSale = new ArrayList<>();

                for(String monthSale : Currentlast4Months) {
	                if(conditions.size()!=0) {
	                    // Add the date condition (month_year)
	                	stringToRemove = (" AND mdm.month_year = ")+("\"")+(monthSale)+("\"");
	                	queryBuilder.append(stringToRemove);
	                } else {
	                	stringToRemove = (" mdm.month_year = ")+("\"")+(monthSale)+("\"");
	                	queryBuilder.append(stringToRemove);
	                }
	                // Get the final query string
	                query = queryBuilder.toString();
	
	                List<Map<String, Object>> pinCodeBasedData = jdbcTemplate.queryForList(query);
	                
	              if(pinCodeBasedData.get(0).get("quantity") == null) {
	            	  lastFourMonthsSale.add("0");
	              }
	              else {
	                double result = (double) pinCodeBasedData.get(0).get("quantity"); // Replace "column_name" with the actual column name
	                DecimalFormat df = new DecimalFormat("#.#########"); // Adjust the number of decimals as needed
	                String formattedResult = df.format(result);
	                lastFourMonthsSale.add(formattedResult);
	              }

	                int indexToRemove = queryBuilder.indexOf(stringToRemove);
	
	                if (indexToRemove != -1) {
	                    StringBuilder newQueryBuilder = new StringBuilder(queryBuilder.substring(0, indexToRemove));
	                    if (indexToRemove + stringToRemove.length() < queryBuilder.length()) {
	                        newQueryBuilder.append(queryBuilder.substring(indexToRemove + stringToRemove.length()));
	                    }
	                    queryBuilder = newQueryBuilder;
	                }
                }
                System.out.println(lastFourMonthsSale);

                String month1 = lastFourMonthsSale.get(0);
                String month2 = lastFourMonthsSale.get(1);
                String month3 = lastFourMonthsSale.get(2);
                String month4 = lastFourMonthsSale.get(3);
                
                BigDecimal bmonth1 = new BigDecimal(month1);
                BigDecimal bmonth2 = new BigDecimal(month2);
                BigDecimal bmonth3 = new BigDecimal(month3);
                BigDecimal bmonth4 = new BigDecimal(month4);
                
                BigDecimal[] values = { bmonth2, bmonth3, bmonth4 };

             // Calculate the average
             BigDecimal simpleMovingAverage = bmonth1.add(bmonth2).add(bmonth3).divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

             BigDecimal standardDeviation = getSD(values);

             BigDecimal upperBand = bmonth1.add(standardDeviation);
             BigDecimal lowerBand;

             if (bmonth1.compareTo(BigDecimal.ZERO) == 0) {
                 lowerBand = standardDeviation;
             } else {
                 lowerBand = bmonth1.subtract(standardDeviation);
             }
                WaveChartDashboardResponse chartData = new WaveChartDashboardResponse(upperBand, lowerBand, simpleMovingAverage, bmonth1, month6);
                System.out.println(chartData);
                responseList.add(chartData);                
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public BigDecimal getSD(BigDecimal[] values) {
		if (values.length == 0) {
			throw new IllegalArgumentException("Array is empty");
		}

    // Calculate the mean (average) of the values
    BigDecimal sum = BigDecimal.ZERO;
    for (BigDecimal value : values) {
        sum = sum.add(value);
    }
    BigDecimal mean = sum.divide(BigDecimal.valueOf(values.length), MathContext.DECIMAL128);

    // Calculate the sum of squared differences from the mean
    BigDecimal sumSquaredDifferences = BigDecimal.ZERO;
    for (BigDecimal value : values) {
        BigDecimal difference = value.subtract(mean);
        BigDecimal squaredDifference = difference.multiply(difference);
        sumSquaredDifferences = sumSquaredDifferences.add(squaredDifference);
    }

    // Calculate the variance and then the standard deviation
    BigDecimal variance = sumSquaredDifferences.divide(BigDecimal.valueOf(values.length - 1), MathContext.DECIMAL128);
    return BigDecimal.valueOf(Math.sqrt(variance.doubleValue()));    
   }

	private List<String> getCurrentLast4Months(String month6) throws ParseException {
    	Date date1 = dateFormat.parse(month6);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        List<String> lastThreeMonths = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            lastThreeMonths.add(dateFormat.format(calendar1.getTime()));
            calendar1.add(Calendar.MONTH, -1); // Move to the previous month
        }
		return lastThreeMonths;
	}

	public static double calculateStandardDeviation(Double[] array) {

        // get the sum of array
        double sum = 0.0;
        for (double i : array) {
            sum += i;
        }

        // get the mean of array
        int length = array.length;
        double mean = sum / length;

        // calculate the standard deviation
        double standardDeviation = 0.0;
        for (double num : array) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    // Helper method to convert month name to index (e.g., "January" -> 0)
    private int getMonthIndex(String monthName) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthName)) {
                return i;
            }
        }
        return -1; // Invalid month name
    }

    // Helper method to convert month index to name (e.g., 0 -> "January")
    private String getMonthName(int monthIndex) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[monthIndex];
    }

    
    @PostMapping("/getTotalSales")
    public String[] totalSales(@RequestBody TotalSalesRequest totalSalesRequest) {

    	String currTotalSales = totalSalesService.calculateTotalSales(totalSalesRequest);
    	
    	
        String startMonth = totalSalesRequest.getStartMonth();
        String endMonth = totalSalesRequest.getEndMonth();
    	TotalSalesRequest prevYrRequest = totalSalesRequest;
    	
//    	prevYrRequest.setPincode(totalSalesRequest.getPincode());
//    	prevYrRequest.setPriceType(totalSalesRequest.getPriceType());
//    	prevYrRequest.setBrand(totalSalesRequest.getBrand());
//    	prevYrRequest.setSku(totalSalesRequest.getSku());
//    	prevYrRequest.setDivision(totalSalesRequest.getDivision());
//    	prevYrRequest.setZone(totalSalesRequest.getZone());
//    	prevYrRequest.setState(totalSalesRequest.getState());
//    	prevYrRequest.setCity(totalSalesRequest.getCity());
//    	prevYrRequest.setAccountType(totalSalesRequest.getAccountType());
//    	prevYrRequest.setAccountName(totalSalesRequest.getAccountName());
    	
    	prevYrRequest.setStartMonth(getPreviousYear(startMonth));
    	prevYrRequest.setEndMonth(getPreviousYear(endMonth));
    	
    	String prevTotalSales = totalSalesService.calculateTotalSales(prevYrRequest);

    	String status = Double.parseDouble(currTotalSales) < Double.parseDouble(prevTotalSales) ? "low" : "high";
    	
    	String[] result = new String[] {currTotalSales,status};
    	
    	return result;
    }
    
    private static String getPreviousYear(String inputMonthYear) {
        // Parse the input string into a LocalDate
        LocalDate inputDate = LocalDate.parse("01 " + inputMonthYear, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        
        // Subtract a year
        LocalDate resultDate = inputDate.minusYears(1);
        
        // Format the result in the desired format
        return resultDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }
    

    @PostMapping("/getGrowth")
    public double growth(@RequestBody GrowthRequest growthRequest) {
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
    

    @PostMapping("/getEvolutionIndex")
    public double evolutionIndex(@RequestBody EvolutionIndexRequest evolutionIndexRequest) {
        
        double overAllGrowth = evolutionIndexRequest.getGrowth();
        double brandGrowth = (overAllGrowth / 100) + 1;

        double evolutionIndexVariable = (100 + brandGrowth) / (100 + overAllGrowth);

        return evolutionIndexVariable;
    }

    
    @PostMapping("/getStoreReach")
    public String[] storeReach(@RequestBody StoreReachRequest storeReachRequest) {
    	
    	Long currStoreReach = dashboardService.getStoreReach(storeReachRequest);
    	
        String startMonth = storeReachRequest.getStartMonth();
        String endMonth = storeReachRequest.getEndMonth();
        StoreReachRequest prevStoreReachReq = storeReachRequest;
        
        prevStoreReachReq.setStartMonth(getPreviousYear(startMonth));
        prevStoreReachReq.setEndMonth(getPreviousYear(endMonth));

    	Long prevStoreReach = dashboardService.getStoreReach(prevStoreReachReq);
    	
    	String status = currStoreReach < prevStoreReach ? "low" : "high";
    	
    	String[] result = new String[] {String.valueOf(currStoreReach), status};
    	
    	return result;
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
    
    
    @PostMapping("/getHeatMapData")
    public HashMap<String, Object> getHeatMapData(@RequestBody HeatMapChartResponse heatMapChartResponse){

        String[] brand = heatMapChartResponse.getBrand();
        String priceType = heatMapChartResponse.getPriceType();
        String[] sku = heatMapChartResponse.getSku();
        String[] division = heatMapChartResponse.getDivision();
        String startMonth = heatMapChartResponse.getStartMonth();
        String endMonth = heatMapChartResponse.getEndMonth();
        String[] accountName = heatMapChartResponse.getAccount();
        String[] accountType = heatMapChartResponse.getAccountType();
        
        List<String> monthsBetween = generateMonthsBetween(startMonth, endMonth);
        StringBuilder resultBuilder = new StringBuilder("(");
        for (int i = 0; i < monthsBetween.size(); i++) {
            resultBuilder.append("\"").append(monthsBetween.get(i)).append("\"");
            if (i < monthsBetween.size() - 1) {
                resultBuilder.append(", ");
            }
        }
        resultBuilder.append(")");        
        
        double totalSales = 0.0;
        HashMap<String, Object> stateSalesMap = new HashMap<>();

        String query1 = "";

        if(priceType == "" || priceType.equals("")) {
        	priceType = "mrp";
        }        
        StringBuilder queryBuilder = new StringBuilder("SELECT sum(mdm.unit_sold*pm."+priceType+") as quantity FROM monthly_data_master mdm join price_master pm on mdm.g_product_id = pm.g_product_id WHERE");
        List<String> conditions = new ArrayList<>();
        
        // Check each condition and add it to the list if it's not empty
        if (accountName != null && accountName.length > 0) {
            StringBuilder accountNameCondition = new StringBuilder(" mdm.account_name IN (");
            for (int l = 0; l < accountName.length; l++) {
            	accountNameCondition.append("\"").append(accountName[l]).append("\"");
                if (l < accountName.length - 1) {
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
            for (int l = 0; l < brand.length; l++) {
                brandCondition.append("\"").append(brand[l]).append("\"");
                if (l < brand.length - 1) {
                    brandCondition.append(", ");
                }
            }
            brandCondition.append(")");

            conditions.add(brandCondition.toString());
        }
        if (sku != null && sku.length > 0) {
            StringBuilder skuCondition = new StringBuilder(" mdm.product_name IN (");
            for (int l = 0; l < sku.length; l++) {
                skuCondition.append("\"").append(sku[l]).append("\"");
                if (l < sku.length - 1) {
                    skuCondition.append(", ");
                }
            }
            skuCondition.append(")");

            conditions.add(skuCondition.toString());
        }
        if (division != null && division.length > 0) {
            StringBuilder divisionCondition = new StringBuilder(" mdm.division IN (");
            for (int l = 0; l < division.length; l++) {
                divisionCondition.append("\"").append(division[l]).append("\"");
                if (l < division.length - 1) {
                    divisionCondition.append(", ");
                }
            }
            divisionCondition.append(")");

            conditions.add(divisionCondition.toString());
        }
        queryBuilder.append(String.join(" AND ", conditions));
        
        if(conditions.size() != 0) {
        	queryBuilder.append(" AND mdm.month_year IN ").append(resultBuilder);
        } else {
        	queryBuilder.append(" mdm.month_year IN ").append(resultBuilder);
        }
        
        List<String> stateNameList = geographyMasterRepository.getDistinctState();
        for(String state : stateNameList){
        	String stringToRemove;
            List<Map<String, Object>> stateBasedData = null;
            try {
                    stringToRemove = (" AND mdm.state = ")+ "\"" +state+ "\"";
                    queryBuilder.append(stringToRemove);
                
                query1 = queryBuilder.toString();
                stateBasedData = jdbcTemplate.queryForList(query1);
                
                stateSalesMap.put(state,(stateBasedData.get(0).get("quantity") == null ? 0.0 : stateBasedData.get(0).get("quantity")));
                
                int indexToRemove = queryBuilder.indexOf(stringToRemove);

                if (indexToRemove != -1) {
                    StringBuilder newQueryBuilder = new StringBuilder(queryBuilder.substring(0, indexToRemove));
                    if (indexToRemove + stringToRemove.length() < queryBuilder.length()) {
                        newQueryBuilder.append(queryBuilder.substring(indexToRemove + stringToRemove.length()));
                    }
                    queryBuilder = newQueryBuilder;
                }
                
            } catch (Exception e){
                System.out.println("No record of this month in Monthly Data");
            }
        }
        return stateSalesMap;
    }
    
}
