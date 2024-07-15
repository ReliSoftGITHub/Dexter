package com.dexter.gcv_life.Controller;

import java.math.BigDecimal;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;

@RestController
@CrossOrigin
public class TopBrandTopStateController {

	@Autowired
	MonthlyDataMasterRepo repository;
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

//	LocalDate currentDate = LocalDate.now();
//    LocalDate currentMonthDate = currentDate.minusMonths(1);
//    LocalDate previousMonthDate = currentDate.minusMonths(2);
//    
//    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
//    
//    String currentMonth = currentMonthDate.format(dateFormatter);
//    String previousMonth = previousMonthDate.format(dateFormatter);
    
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
    
    private static String getPreviousYear(String inputMonthYear) {
        // Parse the input string into a LocalDate
        LocalDate inputDate = LocalDate.parse("01 " + inputMonthYear, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        
        // Subtract a year
        LocalDate resultDate = inputDate.minusYears(1);
        
        // Format the result in the desired format
        return resultDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }
    
	@GetMapping("/getTopBrands")
    public Object getTopBrands(@RequestParam("currentMonth") String currentMonth,@RequestParam("previousMonth") String previousMonth) {
		        		
        String startMonth = getPreviousYear(currentMonth);
        String endMonth = getPreviousYear(previousMonth);
		List<String> monthsBetweenCurrentYear = generateMonthsBetween(currentMonth, previousMonth);			
		List<String> monthsBetweenPreviousYear = generateMonthsBetween(startMonth, endMonth);			
		
		StringBuilder prevMonthYears = new StringBuilder("(");
		for (int i = 0; i < monthsBetweenPreviousYear.size(); i++) {
			prevMonthYears.append("\"").append(monthsBetweenPreviousYear.get(i)).append("\"");
			if (i < monthsBetweenPreviousYear.size() - 1) {
				prevMonthYears.append(", ");
			}
		}
		prevMonthYears.append(")");
		String prevMonthParam = prevMonthYears.toString();
		
		StringBuilder currentMonthYears = new StringBuilder("(");
		for (int i = 0; i < monthsBetweenCurrentYear.size(); i++) {
			currentMonthYears.append("\"").append(monthsBetweenCurrentYear.get(i)).append("\"");
			if (i < monthsBetweenCurrentYear.size() - 1) {
				currentMonthYears.append(", ");
			}
		}
		currentMonthYears.append(")");
		String currMonthParam = currentMonthYears.toString();
		
        StringBuilder queryBuilder = new StringBuilder("select brand, month_year, sum(unit_sold) as totalUnitSold from monthly_data_master mdm where mdm.month_year IN "+currMonthParam+" group by brand order by totalUnitSold desc limit 10");
        String query = queryBuilder.toString();
    	
        List<Map<String, Object>> topBrandsData = jdbcTemplate.queryForList(query);
        
		List<Object> mappedResults = new ArrayList<>();

	    for (Map<String, Object> row : topBrandsData) {
	    	Map<String, Object> resultMap = new HashMap<>();
	    	
	    	String brand = (String) row.get("brand"); 
	        BigDecimal currUnitSold = (BigDecimal) row.get("totalUnitSold");
	        	        
	        StringBuilder queryBuilder1 = new StringBuilder("select sum(unit_sold) as totalUnitSold from monthly_data_master mdm where mdm.month_year IN "+prevMonthParam+" and mdm.brand = '"+brand+"' group by brand");
	        String query1 = queryBuilder1.toString();
	    	
	        List<Map<String, Object>> brandsData = jdbcTemplate.queryForList(query1);
	        BigDecimal prevUnitSold ;
	        
	        if(brandsData.isEmpty()) 
	         prevUnitSold = BigDecimal.valueOf(0);
	        else
	         prevUnitSold = (BigDecimal) brandsData.get(0).get("totalUnitSold");
		        if(prevUnitSold != null) {
			        resultMap.put("brand", brand);
			        resultMap.put("status", currUnitSold.intValue() < prevUnitSold.intValue() ? "low" : "high" );
			        
			        mappedResults.add(resultMap);
		        }
	        
	    }
		return mappedResults;
	}
	
	@GetMapping("/getTopStates")
    public Object getTopStates(@RequestParam("currentMonth") String currentMonth,@RequestParam("previousMonth") String previousMonth) {
		
		String startMonth = getPreviousYear(currentMonth);
        String endMonth = getPreviousYear(previousMonth);
		List<String> monthsBetweenCurrentYear = generateMonthsBetween(currentMonth, previousMonth);			
		List<String> monthsBetweenPreviousYear = generateMonthsBetween(startMonth, endMonth);			
		
		StringBuilder prevMonthYears = new StringBuilder("(");
		for (int i = 0; i < monthsBetweenPreviousYear.size(); i++) {
			prevMonthYears.append("\"").append(monthsBetweenPreviousYear.get(i)).append("\"");
			if (i < monthsBetweenPreviousYear.size() - 1) {
				prevMonthYears.append(", ");
			}
		}
		prevMonthYears.append(")");
		String prevMonthParam = prevMonthYears.toString();
		
		StringBuilder currentMonthYears = new StringBuilder("(");
		for (int i = 0; i < monthsBetweenCurrentYear.size(); i++) {
			currentMonthYears.append("\"").append(monthsBetweenCurrentYear.get(i)).append("\"");
			if (i < monthsBetweenCurrentYear.size() - 1) {
				currentMonthYears.append(", ");
			}
		}
		currentMonthYears.append(")");
		String currMonthParam = currentMonthYears.toString();
		
        StringBuilder queryBuilder = new StringBuilder("select state, month_year, sum(unit_sold) as totalUnitSold from monthly_data_master mdm where mdm.month_year IN "+currMonthParam+" group by state order by totalUnitSold desc limit 10");
        String query = queryBuilder.toString();
        List<Map<String, Object>> topStatesData = jdbcTemplate.queryForList(query);

		List<Object> mappedResults = new ArrayList<>();

		for (Map<String, Object> row : topStatesData) {
	    	Map<String, Object> resultMap = new HashMap<>();
	    	
	    	String state = (String) row.get("state"); 
	        BigDecimal currUnitSold = (BigDecimal) row.get("totalUnitSold");
	        	        
	        StringBuilder queryBuilder1 = new StringBuilder("select sum(unit_sold) as totalUnitSold from monthly_data_master mdm where mdm.month_year IN "+prevMonthParam+" and mdm.state = '"+state+"' group by state");
	        String query1 = queryBuilder1.toString();
	    	
	        List<Map<String, Object>> statesData = jdbcTemplate.queryForList(query1);
	        BigDecimal prevUnitSold ;
	        
	        if(statesData.isEmpty()) 
	         prevUnitSold = BigDecimal.valueOf(0);
	        else
	         prevUnitSold = (BigDecimal) statesData.get(0).get("totalUnitSold");
		        if(prevUnitSold != null) {
			        resultMap.put("brand", state);
			        resultMap.put("status", currUnitSold.intValue() < prevUnitSold.intValue() ? "low" : "high" );
			        
			        mappedResults.add(resultMap);
		        }
	    }
		return mappedResults;
	}
}
