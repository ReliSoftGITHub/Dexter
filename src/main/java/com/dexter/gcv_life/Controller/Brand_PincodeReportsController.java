package com.dexter.gcv_life.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class Brand_PincodeReportsController {

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
	
	public static String subtractOneYear(String inputDate) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("MMMM yyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM yyyy");

        try {
            Date date = inputDateFormat.parse(inputDate);
            if (date != null) {
                // Subtract one year from the date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.YEAR, -1);

                // Format the modified date to "MMMM yyyy" format
                return outputDateFormat.format(calendar.getTime());
            }
        } catch (ParseException e) {
            System.err.println("Error parsing input date: " + e.getMessage());
        }

        return null;
    }

	@PostMapping("/getBrandReports")
	public String getBrnadReports(final HttpServletRequest requestParam,
			@RequestParam(value = "brands", required = false) String brandList,
    		@RequestParam(value = "startMonth", required = false) String startMonth,
    		@RequestParam(value = "endMonth", required = false) String endMonth,
    		@RequestParam(value = "iSortCol_0", required = false) Integer sortingColumn,
    		@RequestParam(value = "sSortDir_0", required = false) String sortdir) throws JsonMappingException, JsonProcessingException {
		
		long iDisplayLength = Long.parseLong(requestParam.getParameter("iDisplayLength"));
		long iDisplayStart = Long.parseLong(requestParam.getParameter("iDisplayStart"));
        
		String orderByCol = "";
        if(sortingColumn != null) {
        	
	        if(sortingColumn == 3) {
	        	orderByCol = " ORDER BY incremental "+sortdir;
	        }
	        if(sortingColumn == 4) {
	        	orderByCol = " ORDER BY percent "+sortdir;
	        }
        }
		
		ObjectMapper objectMapper = new ObjectMapper();
        String[] brands = objectMapper.readValue(brandList, String[].class);
		
		String startPrevYearMonth = subtractOneYear(startMonth);
		
		String endPrevYearMonth = subtractOneYear(endMonth);	
		
		String brnadsIN = null;

		try {

			if (brands != null && brands.length > 0) {
				StringBuilder brand = new StringBuilder("(");
				for (int i = 0; i < brands.length; i++) {
					brand.append("\"").append(brands[i]).append("\"");
					if (i < brands.length - 1) {
						brand.append(", ");
					}
				}
				brand.append(")");
				brnadsIN = brand.toString();
			}

			List<String> monthsBetweenCurrentYear = generateMonthsBetween(startMonth, endMonth);			
			
			StringBuilder currentMonthYears = new StringBuilder("(");
			for (int i = 0; i < monthsBetweenCurrentYear.size(); i++) {
				currentMonthYears.append("\"").append(monthsBetweenCurrentYear.get(i)).append("\"");
				if (i < monthsBetweenCurrentYear.size() - 1) {
					currentMonthYears.append(", ");
				}
			}
			currentMonthYears.append(")");
			
			List<String> monthsBetweenPrevYear = generateMonthsBetween(startPrevYearMonth, endPrevYearMonth);
			
			StringBuilder prevMonthYears = new StringBuilder("(");
			for (int i = 0; i < monthsBetweenPrevYear.size(); i++) {
				prevMonthYears.append("\"").append(monthsBetweenPrevYear.get(i)).append("\"");
				if (i < monthsBetweenPrevYear.size() - 1) {
					prevMonthYears.append(", ");
				}
			}
			prevMonthYears.append(")");
			
			List<String> bothYears = new ArrayList<>();

			bothYears.addAll(monthsBetweenCurrentYear);
			bothYears.addAll(monthsBetweenPrevYear);

			StringBuilder bothYearsIN = new StringBuilder("(");
			for (int i = 0; i < bothYears.size(); i++) {
				bothYearsIN.append("\"").append(bothYears.get(i)).append("\"");
				if (i < bothYears.size() - 1) {
					bothYearsIN.append(", ");
				}
			}
			bothYearsIN.append(")");
				
			
			String countQuery = "SELECT\r\n"
					+ " SUM(CASE WHEN mdm.month_year IN "+currentMonthYears
					+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrTotalSale,\r\n"
					+ "    COUNT(DISTINCT mdm.brand) AS brandCount\r\n"
					+ "FROM\r\n"
					+ "    monthly_data_master mdm\r\n"
					+ "JOIN\r\n"
					+ "    price_master pm ON mdm.g_product_id = pm.g_product_id\r\n"
					+ "WHERE mdm.month_year IN "+bothYearsIN
					+ " AND mdm.unit_sold != 0";
			
					if (brands.length > 0) {
						countQuery = countQuery + " AND mdm.brand IN "+brnadsIN;
					}
					
			List<Map<String, Object>> countList = jdbcTemplate.queryForList(countQuery);
			
			Long brandCount = (Long) countList.get(0).get("brandCount");
			
			Double currYrTotalSale = (Double) countList.get(0).get("currYrTotalSale");

			String query = "SELECT brand, currYrSale, prevYrSale, COALESCE (percent,0) as percent, currYrSale - prevYrSale as incremental from (\r\n"
					+ "	SELECT mdm.brand,\r\n"
					+ "	SUM(CASE WHEN mdm.month_year IN "+prevMonthYears + " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS prevYrSale,"
					+ " SUM(CASE WHEN mdm.month_year IN"+currentMonthYears+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrSale,"
					+ " (SUM(CASE WHEN mdm.month_year IN "+currentMonthYears + "THEN mdm.unit_sold * pm.mrp ELSE 0 END) / "+  currYrTotalSale +" ) * 100 AS percent\r\n"
							+ "	FROM monthly_data_master mdm\r\n"
							+ "	JOIN price_master pm ON mdm.g_product_id = pm.g_product_id"; 
			
				if (brands.length > 0) {
					query = query + " WHERE mdm.brand IN "+brnadsIN;
				}
				
				query = query + "  GROUP BY mdm.brand\r\n"
						+ ") as alldata\r\n"
						+ " WHERE currYrSale != 0 OR prevYrSale != 0\r\n";
				
				if(sortingColumn == 3 || sortingColumn == 4)
					query = query + orderByCol;
						
				query += " LIMIT " + iDisplayLength + " OFFSET "+iDisplayStart;
			
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(query);
										
			List<Object> mappedResults = new ArrayList<>();
			
	    	final JsonObject jsonResponse = new JsonObject();
			for(Map<String, Object> data : queryForList) {
			
				Map<String, Object> resultMap = new HashMap<>();

			    resultMap.put("brand", data.get("brand"));
				resultMap.put("prev_year_sale", data.get("prevYrSale"));
				resultMap.put("current_year_sale", data.get("currYrSale"));
				resultMap.put("incremental_value", data.get("incremental"));
				resultMap.put("contribution", data.get("percent"));
				
				mappedResults.add(resultMap);

			}
			
			JsonElement raw_data = new Gson().toJsonTree(mappedResults);
			jsonResponse.add("aaData", raw_data);
			jsonResponse.addProperty("iTotalRecords", brandCount);
			jsonResponse.addProperty("iTotalDisplayRecords", brandCount);
			
	        return jsonResponse.toString();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@PostMapping("/getPincodeReports")
	public String getPincodeReports(final HttpServletRequest requestParam,
			@RequestParam(value = "pincodes", required = false) String pincodeList,
    		@RequestParam(value = "startMonth", required = false) String startMonth,
    		@RequestParam(value = "endMonth", required = false) String endMonth,
    		@RequestParam(value = "iSortCol_0", required = false) Integer sortingColumn,
    		@RequestParam(value = "sSortDir_0", required = false) String sortdir) throws JsonMappingException, JsonProcessingException {
		
		long iDisplayLength = Long.parseLong(requestParam.getParameter("iDisplayLength"));
		long iDisplayStart = Long.parseLong(requestParam.getParameter("iDisplayStart"));
        String orderByCol = "";
        if(sortingColumn != null) {
        	
	        if(sortingColumn == 3) {
	        	orderByCol = " ORDER BY incremental "+sortdir;
	        }
	        if(sortingColumn == 4) {
	        	orderByCol = " ORDER BY percent "+sortdir;
	        }
        }
		
		ObjectMapper objectMapper = new ObjectMapper();
        String[] pincodes = objectMapper.readValue(pincodeList, String[].class);
		
		String startPrevYearMonth = subtractOneYear(startMonth);
		
		String endPrevYearMonth = subtractOneYear(endMonth);	
		
		String pincodesIN = null;

		try {

			if (pincodes != null && pincodes.length > 0) {
				StringBuilder pincode = new StringBuilder("(");
				for (int i = 0; i < pincodes.length; i++) {
					pincode.append("\"").append(pincodes[i]).append("\"");
					if (i < pincodes.length - 1) {
						pincode.append(", ");
					}
				}
				pincode.append(")");
				pincodesIN = pincode.toString();
			}

			List<String> monthsBetweenCurrentYear = generateMonthsBetween(startMonth, endMonth);			
			
			StringBuilder currentMonthYears = new StringBuilder("(");
			for (int i = 0; i < monthsBetweenCurrentYear.size(); i++) {
				currentMonthYears.append("\"").append(monthsBetweenCurrentYear.get(i)).append("\"");
				if (i < monthsBetweenCurrentYear.size() - 1) {
					currentMonthYears.append(", ");
				}
			}
			currentMonthYears.append(")");
			
			List<String> monthsBetweenPrevYear = generateMonthsBetween(startPrevYearMonth, endPrevYearMonth);
			
			StringBuilder prevMonthYears = new StringBuilder("(");
			for (int i = 0; i < monthsBetweenPrevYear.size(); i++) {
				prevMonthYears.append("\"").append(monthsBetweenPrevYear.get(i)).append("\"");
				if (i < monthsBetweenPrevYear.size() - 1) {
					prevMonthYears.append(", ");
				}
			}
			prevMonthYears.append(")");
			
			List<String> bothYears = new ArrayList<>();

			bothYears.addAll(monthsBetweenCurrentYear);
			bothYears.addAll(monthsBetweenPrevYear);

			StringBuilder bothYearsIN = new StringBuilder("(");
			for (int i = 0; i < bothYears.size(); i++) {
				bothYearsIN.append("\"").append(bothYears.get(i)).append("\"");
				if (i < bothYears.size() - 1) {
					bothYearsIN.append(", ");
				}
			}
			bothYearsIN.append(")");
			
			String countQuery = "SELECT\r\n"
					+ " SUM(CASE WHEN mdm.month_year IN "+currentMonthYears
					+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrTotalSale,\r\n"
					+ "    COUNT(DISTINCT mdm.pincode) AS pincodeCount\r\n"
					+ "FROM\r\n"
					+ "    monthly_data_master mdm\r\n"
					+ "JOIN\r\n"
					+ "    price_master pm ON mdm.g_product_id = pm.g_product_id\r\n"
					+ "WHERE mdm.month_year IN "+bothYearsIN
					+ " AND mdm.unit_sold != 0";
			
					if (pincodes.length > 0) {
						countQuery = countQuery + " AND mdm.pincode IN "+pincodesIN;
					}
					
		List<Map<String, Object>> countList = jdbcTemplate.queryForList(countQuery);
		
		Long pincodeCount = (Long) countList.get(0).get("pincodeCount");
		
		Double currYrTotalSale = (Double) countList.get(0).get("currYrTotalSale");

		String query = "SELECT pincode, currYrSale, prevYrSale, COALESCE (percent,0) as percent, currYrSale - prevYrSale as incremental from (\r\n"
				+ "	SELECT mdm.pincode,\r\n"
				+ "	SUM(CASE WHEN mdm.month_year IN "+prevMonthYears + " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS prevYrSale,"
				+ " SUM(CASE WHEN mdm.month_year IN"+currentMonthYears+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrSale,"
				+ " (SUM(CASE WHEN mdm.month_year IN "+currentMonthYears + "THEN mdm.unit_sold * pm.mrp ELSE 0 END) / "+  currYrTotalSale +" ) * 100 AS percent\r\n"
						+ "	FROM monthly_data_master mdm\r\n"
						+ "	JOIN price_master pm ON mdm.g_product_id = pm.g_product_id"; 
		
			if (pincodes.length > 0) {
				query = query + " WHERE mdm.pincode IN "+pincodesIN;
			}
			
			query = query + "  GROUP BY mdm.pincode\r\n"
					+ ") as alldata\r\n"
					+ "WHERE currYrSale != 0 OR prevYrSale != 0\r\n";
			
			if(sortingColumn == 3 || sortingColumn == 4)
				query = query + orderByCol;
					
			query += " LIMIT " + iDisplayLength + " OFFSET "+iDisplayStart;
		
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(query);
									
			List<Object> mappedResults = new ArrayList<>();
			
	    	final JsonObject jsonResponse = new JsonObject();
			for(Map<String, Object> data : queryForList) {
								
				Map<String, Object> resultMap = new HashMap<>();

			    resultMap.put("pincode", data.get("pincode"));
				resultMap.put("prev_year_sale", data.get("prevYrSale"));
				resultMap.put("current_year_sale", data.get("currYrSale"));
				resultMap.put("incremental_value", data.get("incremental"));
				resultMap.put("contribution", data.get("percent"));
					
				mappedResults.add(resultMap);

			}
						
			JsonElement raw_data = new Gson().toJsonTree(mappedResults);
			jsonResponse.add("aaData", raw_data);
			jsonResponse.addProperty("iTotalRecords", pincodeCount);
			jsonResponse.addProperty("iTotalDisplayRecords", pincodeCount);
			
	        return jsonResponse.toString();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}



}
