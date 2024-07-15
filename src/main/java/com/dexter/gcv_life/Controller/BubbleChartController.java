package com.dexter.gcv_life.Controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
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
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class BubbleChartController {

	@Autowired
	private DashboardController dashboardController;

	@Autowired
	private MonthlyDataMasterRepo monthlyDataMasterRepo;

	private String convertToJsonString(JsonNode jsonArray) {
		if (jsonArray != null && jsonArray.isArray()) {
			List<String> values = new ArrayList<>();
			for (JsonNode elementNode : jsonArray) {
				values.add("'" + elementNode.asText() + "'");
			}

			return String.join(", ", values);
		}
		return null;
	}

	public JsonNode apostrophy(JsonNode data) throws JsonMappingException, JsonProcessingException {
		if (data.toString().contains(" '") || data.toString().contains("'")) {
			String skuString = data.toString().replace("'", "''");
			ObjectMapper map = new ObjectMapper();
			data = map.readTree(skuString);
			return data;
		}
		return data;
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
    
	@PostMapping("/bubbleChart")
	public ResponseEntity<?> bubbleChart(@RequestBody JsonNode request)
			throws JsonMappingException, JsonProcessingException {
		System.out.println(request);
		if (!request.get("startMonth").equals("") && !request.get("endMonth").equals("") && !request.get("currency").isEmpty()) {
			// left filters
			JsonNode subGroup = (JsonNode) request.get("subGroup");
			JsonNode group = (JsonNode) request.get("group");
			JsonNode existingNewGroup = (JsonNode) request.get("existingNewGroup");
			JsonNode brand = (JsonNode) request.get("brand");
			JsonNode brandGroup = (JsonNode) request.get("brandGroup");
			JsonNode skuRequest = (JsonNode) request.get("sku");
			JsonNode sku = apostrophy(skuRequest);
			JsonNode division = (JsonNode) request.get("division");
			JsonNode zone = (JsonNode) request.get("zone");
			JsonNode state = (JsonNode) request.get("state");
			JsonNode city = (JsonNode) request.get("city");
			JsonNode pincode = (JsonNode) request.get("pincode");
			JsonNode startMonth = (JsonNode) request.get("startMonth");
			JsonNode endMonth = (JsonNode) request.get("endMonth");
			JsonNode currency = (JsonNode) request.get("currency");
			JsonNode accountType = (JsonNode) request.get("accountType");
			JsonNode accountName = (JsonNode) request.get("accountName");

			
			// left filter String
			String subGroupString = convertToJsonString(subGroup);
			String groupString = convertToJsonString(group);
			String existingNewGroupString = convertToJsonString(existingNewGroup);
			String brandString = convertToJsonString(brand);
			String brandGroupString = convertToJsonString(brandGroup);
			String skuString = convertToJsonString(sku);
			String divisionString = convertToJsonString(division);
			String zoneString = convertToJsonString(zone);
			String stateString = convertToJsonString(state);
			String cityString = convertToJsonString(city);
			String pincodeString = convertToJsonString(pincode);
			String currencyString = convertToJsonString(currency);
			String accountNameString = convertToJsonString(accountName);

			String accountTypeString = convertToJsonString(accountType);
			String inMonth = startMonth.textValue();
			String outMonth = endMonth.textValue();
			
	try {
	        List<String> monthsBetween = generateMonthsBetween(inMonth, outMonth);
	        StringBuilder resultBuilder = new StringBuilder("(");
	        for (int i = 0; i < monthsBetween.size(); i++) {
	            resultBuilder.append("\"").append(monthsBetween.get(i)).append("\"");
	            if (i < monthsBetween.size() - 1) {
	                resultBuilder.append(", ");
	            }
	        }
	        resultBuilder.append(")");   
	        
	        List<Object> mappedResults = new ArrayList<>();
			
				List<Object[]> data = null;

				if (currencyString.contains("pts")) {
					data = monthlyDataMasterRepo.ptsProcedure(subGroupString, groupString, existingNewGroupString,
							brandString, brandGroupString, skuString, divisionString, zoneString, stateString,
							cityString, pincodeString, resultBuilder.toString(), accountTypeString, accountNameString);
				}

				if (currencyString.contains("ptr")) {
					data = monthlyDataMasterRepo.ptrProcedure(subGroupString, groupString, existingNewGroupString,
							brandString, brandGroupString, skuString, divisionString, zoneString, stateString,
							cityString, pincodeString, resultBuilder.toString(), accountTypeString, accountNameString);
				}

				if (currencyString.contains("mrp")) {
					data = monthlyDataMasterRepo.mrpProcedure(subGroupString, groupString, existingNewGroupString,
							brandString, brandGroupString, skuString, divisionString, zoneString, stateString,
							cityString, pincodeString, resultBuilder.toString(), accountTypeString, accountNameString);
				}
				
				for (Object[] row : data) {
					Map<String, Object> resultMap = new HashMap<>();

					String brand_name = (String) row[0];
					String month_year = (String) row[1];
					Double total_sale = (Double) row[2];
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
					YearMonth yearMonth = YearMonth.parse(month_year, formatter);
					YearMonth previousYearMonth = yearMonth.minusYears(1);
					String last_year_month = previousYearMonth.format(formatter);

				if (brand_name.isEmpty())
					brand_name = "''";
					
				List<Object[]> twoMonthSale = monthlyDataMasterRepo.getMonthSale(month_year, last_year_month, brand_name);
					BigDecimal currMonthSale = BigDecimal.valueOf(1); 
					BigDecimal lastMonthSale = BigDecimal.valueOf(1); 
					BigInteger storeReach = BigInteger.valueOf(1); 
					
				for (Object[] row1 : twoMonthSale) {
					currMonthSale =  (BigDecimal) row1[0];
					lastMonthSale = (BigDecimal) row1[1];
					storeReach = (BigInteger) row1[2];
				}
				int IcurrMonthSale = currMonthSale.equals(BigDecimal.valueOf(0)) ? 1 : currMonthSale.intValue();
				int IlastMonthSale = lastMonthSale.equals(BigDecimal.valueOf(0)) ? 1 : lastMonthSale.intValue();

					float overAllGrowth = (IcurrMonthSale / IlastMonthSale) - 1;
					overAllGrowth = overAllGrowth == 0.0 ? 1 : overAllGrowth;
					float brandGrowth = IcurrMonthSale / IlastMonthSale;
					brandGrowth = brandGrowth == 0.0 ? 1 : brandGrowth;
					float EI = 100 % +brandGrowth / 100 % +overAllGrowth;

					if (Float.isNaN(EI)) {
					    EI = 0;
					} 

					resultMap.put("brand_name", brand_name);
					resultMap.put("month_year", month_year);
					resultMap.put("total_sale", total_sale);
					resultMap.put("storeReach", storeReach);
					resultMap.put("evaluationIndex", EI);
					mappedResults.add(resultMap);
				}
				System.out.println(data);
	        
				return new ResponseEntity<>(mappedResults, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				List list = new ArrayList<>();
				list.add("Error");
				return new ResponseEntity<List<Object[]>>(list, HttpStatus.BAD_GATEWAY);
			}
		}
		else {
			List list = new ArrayList<>();
			list.add("Motnh or Currency is Empty...plseae select Month and currency first");
			return new ResponseEntity<List<Object[]>>(list, HttpStatus.BAD_REQUEST);
		}
	}


}
