package com.dexter.gcv_life.Service.Impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dexter.gcv_life.Entity.FieldMaster;
import com.dexter.gcv_life.Entity.GcvProductMaster;
import com.dexter.gcv_life.Entity.GeographyMaster;
import com.dexter.gcv_life.Entity.PriceMaster;
import com.dexter.gcv_life.Entity.TerritoryMasterFinal;
import com.dexter.gcv_life.Repository.FieldMasterRepo;
import com.dexter.gcv_life.Repository.GcvProductMasterRepo;
import com.dexter.gcv_life.Repository.GeographyMasterRepository;
import com.dexter.gcv_life.Repository.PriceMasterRepo;
import com.dexter.gcv_life.Repository.TerritoryMasterFinalRepository;

@Service
public class DownLoadExcelService {

	@Autowired
	private FieldMasterRepo fieldMasterRepo;

	@Autowired
	private GcvProductMasterRepo gcvProductMasterRepo;

	@Autowired
	private GeographyMasterRepository geographyMasterRepository;

	@Autowired
	private TerritoryMasterFinalRepository territoryMasterFinalRepository;

	@Autowired
	private PriceMasterRepo priceMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void generateProductExcel(HttpServletResponse response) throws IOException {

		List<GcvProductMaster> data = gcvProductMasterRepo.findAll();
		System.out.println("generateProductExcel  : " + data.size());

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet("product");

		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("row_id");
		headerRow.createCell(1).setCellValue("keey");
		headerRow.createCell(2).setCellValue("account");
		headerRow.createCell(3).setCellValue("sku_id");
		headerRow.createCell(4).setCellValue("sku_name");
		headerRow.createCell(5).setCellValue("g_product_id");
		headerRow.createCell(6).setCellValue("g_product_name");
		headerRow.createCell(7).setCellValue("g_division");
		headerRow.createCell(8).setCellValue("brand");

		int rowNum = 1;
		for (GcvProductMaster entity : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entity.getRow_id());
			row.createCell(1).setCellValue(entity.getKeey());
			row.createCell(2).setCellValue(entity.getAccount());
			row.createCell(3).setCellValue(entity.getSku_id());
			row.createCell(4).setCellValue(entity.getSku_name());
			row.createCell(5).setCellValue(entity.getG_product_id());
			row.createCell(6).setCellValue(entity.getG_product_name());
			row.createCell(7).setCellValue(entity.getG_division());
			row.createCell(8).setCellValue(entity.getBrand());
		}
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}

	public void generateTerritory(HttpServletResponse response) throws IOException {

		List<TerritoryMasterFinal> data = territoryMasterFinalRepository.findAll();
		System.out.println("generateTerritory  : " + data.size());

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet("territory");

		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("row_id");
		headerRow.createCell(1).setCellValue("division");
		headerRow.createCell(2).setCellValue("sap_code");
		headerRow.createCell(3).setCellValue("territory_code");
		headerRow.createCell(4).setCellValue("territory_name");
		headerRow.createCell(5).setCellValue("district_code");
		headerRow.createCell(6).setCellValue("district_name");
		headerRow.createCell(7).setCellValue("zone_code");
		headerRow.createCell(8).setCellValue("zone_name");
		headerRow.createCell(9).setCellValue("state");
		headerRow.createCell(10).setCellValue("name_of_fso_dsm_zsm");
		headerRow.createCell(11).setCellValue("hq");
		headerRow.createCell(12).setCellValue("permanent_city");
		headerRow.createCell(13).setCellValue("permanent_pincode");

		int rowNum = 1;
		for (TerritoryMasterFinal entity : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entity.getRowId());
			row.createCell(1).setCellValue(entity.getDivision());
			row.createCell(2).setCellValue(entity.getSapCode());
			row.createCell(3).setCellValue(entity.getTerritoryCode());
			row.createCell(4).setCellValue(entity.getTerritoryName());
			row.createCell(5).setCellValue(entity.getDistrictCode());
			row.createCell(6).setCellValue(entity.getDistrictName());
			row.createCell(7).setCellValue(entity.getZoneCode());
			row.createCell(8).setCellValue(entity.getZoneName());
			row.createCell(9).setCellValue(entity.getState());
			row.createCell(10).setCellValue(entity.getNameOfFsoDsmZsm());
			row.createCell(11).setCellValue(entity.getHq());
			row.createCell(12).setCellValue(entity.getPermanentCity());
			row.createCell(13).setCellValue(entity.getPermanentPincode());
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

	public void generateGeographyExcel(HttpServletResponse response) throws IOException {

		List<GeographyMaster> data = geographyMasterRepository.findAll();
		System.out.println("generateGeographyExcel  : " + data.size());

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet("geography");

		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("row_id");
		headerRow.createCell(1).setCellValue("office_name");
		headerRow.createCell(2).setCellValue("clean_area_name");
		headerRow.createCell(3).setCellValue("pincode");
		headerRow.createCell(4).setCellValue("gcv_city");
		headerRow.createCell(5).setCellValue("sub_distname");
		headerRow.createCell(6).setCellValue("district_name");
		headerRow.createCell(7).setCellValue("state_name");
		headerRow.createCell(8).setCellValue("zone");

		int rowNum = 1;
		for (GeographyMaster entity : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entity.getRowId());
			row.createCell(1).setCellValue(entity.getOfficeName());
			row.createCell(2).setCellValue(entity.getCleanAreaName());
			row.createCell(3).setCellValue(entity.getPincode());
			row.createCell(4).setCellValue(entity.getGcvCity());
			row.createCell(5).setCellValue(entity.getSubDistname());
			row.createCell(6).setCellValue(entity.getDistrictName());
			row.createCell(7).setCellValue(entity.getStateName());
			row.createCell(8).setCellValue(entity.getZone());
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

	public void generateFieldExcel(HttpServletResponse response) throws IOException {

		List<FieldMaster> data = fieldMasterRepo.findAll();
		System.out.println("generateFieldExcel  : " + data.size());

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet("field");

		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("row_id");
		headerRow.createCell(1).setCellValue("div_code");
		headerRow.createCell(2).setCellValue("division");
		headerRow.createCell(3).setCellValue("cluster");
		headerRow.createCell(4).setCellValue("login_id");
		headerRow.createCell(5).setCellValue("employee_name");
		headerRow.createCell(6).setCellValue("designation");
		headerRow.createCell(7).setCellValue("designation1");
		headerRow.createCell(8).setCellValue("hq");
		headerRow.createCell(9).setCellValue("territory_code");
		headerRow.createCell(10).setCellValue("zone_code");
		headerRow.createCell(11).setCellValue("zone_name");
		headerRow.createCell(12).setCellValue("contact_no1");
		headerRow.createCell(13).setCellValue("state");
		headerRow.createCell(14).setCellValue("official_email_id");

		int rowNum = 1;
		for (FieldMaster entity : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entity.getRow_id());
			row.createCell(1).setCellValue(entity.getDiv_code());
			row.createCell(2).setCellValue(entity.getDivision());
			row.createCell(3).setCellValue(entity.getCluster());
			row.createCell(4).setCellValue(entity.getLogin_id());
			row.createCell(5).setCellValue(entity.getEmployee_name());
			row.createCell(6).setCellValue(entity.getDesignation());
			row.createCell(7).setCellValue(entity.getDesignation1());
			row.createCell(8).setCellValue(entity.getHq());
			row.createCell(9).setCellValue(entity.getTerritory_code());
			row.createCell(10).setCellValue(entity.getZone_code());
			row.createCell(11).setCellValue(entity.getZone_name());
			row.createCell(12).setCellValue(entity.getContact_no1());
			row.createCell(13).setCellValue(entity.getState());
			row.createCell(14).setCellValue(entity.getOfficial_email_id());
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

	public void generatePriceExcel(HttpServletResponse response) throws IOException {

		List<PriceMaster> data = priceMasterRepo.findAll();
		System.out.println("generatePriceExcel  : " + data.size());

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet("price");

		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("row_id");
		headerRow.createCell(1).setCellValue("sku_id");
		headerRow.createCell(2).setCellValue("sku_name");
		headerRow.createCell(3).setCellValue("g_product_id");
		headerRow.createCell(4).setCellValue("g_product_name");
		headerRow.createCell(5).setCellValue("pts");
		headerRow.createCell(6).setCellValue("ptr");
		headerRow.createCell(7).setCellValue("mrp");

		int rowNum = 1;
		for (PriceMaster entity : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entity.getRow_id());
			row.createCell(1).setCellValue(entity.getSku_id());
			row.createCell(2).setCellValue(entity.getSku_name());
			row.createCell(3).setCellValue(entity.getgProductId());
			row.createCell(4).setCellValue(entity.getG_product_name());
			row.createCell(5).setCellValue(entity.getPts());
			row.createCell(6).setCellValue(entity.getPtr());
			row.createCell(7).setCellValue(entity.getMrp());
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

	@SuppressWarnings("unchecked")
	public void generateBrandReportExcel(HttpServletResponse response, String[] brands, String startMonth,
			String endMonth) throws IOException {

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

			String countQuery = "SELECT\r\n" + " SUM(CASE WHEN mdm.month_year IN " + currentMonthYears
					+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrTotalSale,\r\n"
					+ "    COUNT(DISTINCT mdm.brand) AS brandCount\r\n" + "FROM\r\n" + "    monthly_data_master mdm\r\n"
					+ "JOIN\r\n" + "    price_master pm ON mdm.g_product_id = pm.g_product_id\r\n"
					+ "WHERE mdm.month_year IN " + bothYearsIN + " AND mdm.unit_sold != 0";

			if (brands.length > 0) {
				countQuery = countQuery + " AND mdm.brand IN " + brnadsIN;
			}

			List<Map<String, Object>> countList = jdbcTemplate.queryForList(countQuery);

			Double currYrTotalSale = (Double) countList.get(0).get("currYrTotalSale");

			String query = "SELECT brand, currYrSale, prevYrSale, COALESCE (percent,0) as percent, currYrSale - prevYrSale as incremental from (\r\n"
					+ "	SELECT mdm.brand,\r\n" + "	SUM(CASE WHEN mdm.month_year IN " + prevMonthYears
					+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS prevYrSale," + " SUM(CASE WHEN mdm.month_year IN"
					+ currentMonthYears + " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrSale,"
					+ " (SUM(CASE WHEN mdm.month_year IN " + currentMonthYears
					+ "THEN mdm.unit_sold * pm.mrp ELSE 0 END) / " + currYrTotalSale + " ) * 100 AS percent\r\n"
					+ "	FROM monthly_data_master mdm\r\n"
					+ "	JOIN price_master pm ON mdm.g_product_id = pm.g_product_id";

			if (brands.length > 0) {
				query = query + " WHERE mdm.brand IN " + brnadsIN;
			}

			query = query + "  GROUP BY mdm.brand\r\n" + ") as alldata\r\n"
					+ " WHERE currYrSale != 0 OR prevYrSale != 0\r\n";

			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(query);

			List<Object> mappedResults = new ArrayList<>();

			for (Map<String, Object> data : queryForList) {

				Map<String, Object> resultMap = new HashMap<>();

				resultMap.put("brand", data.get("brand"));
				resultMap.put("prev_year_sale", data.get("prevYrSale"));
				resultMap.put("current_year_sale", data.get("currYrSale"));
				resultMap.put("incremental_value", data.get("incremental"));
				resultMap.put("contribution", data.get("percent"));

				mappedResults.add(resultMap);
			}

			SXSSFWorkbook workbook = new SXSSFWorkbook();
			workbook.setCompressTempFiles(true);
			Sheet sheet = workbook.createSheet("Brand Report");

			Row headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue("Brand");
			headerRow.createCell(1).setCellValue("Previous Year Sale");
			headerRow.createCell(2).setCellValue("Current Year Sale");
			headerRow.createCell(3).setCellValue("Incremental Value");
			headerRow.createCell(4).setCellValue("Contribution");

			int rowNum = 1;
			for (int i = 0; i < mappedResults.size(); i++) {

				System.out.println(i);
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue((String) ((Map<String, Object>) mappedResults.get(i)).get("brand"));
				row.createCell(1).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("prev_year_sale"));
				row.createCell(2).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("current_year_sale"));
				row.createCell(3).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("incremental_value"));
				row.createCell(4).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("contribution"));
			}

			ServletOutputStream ops = response.getOutputStream();
			workbook.write(ops);
			workbook.close();
			ops.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	@SuppressWarnings("unchecked")
	public void generatePincodeReportExcel(HttpServletResponse response, String[] pincodes, String startMonth, String endMonth) {

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

			String countQuery = "SELECT\r\n" + " SUM(CASE WHEN mdm.month_year IN " + currentMonthYears
					+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrTotalSale,\r\n"
					+ "    COUNT(DISTINCT mdm.pincode) AS pincodeCount\r\n" + "FROM\r\n"
					+ "    monthly_data_master mdm\r\n" + "JOIN\r\n"
					+ "    price_master pm ON mdm.g_product_id = pm.g_product_id\r\n" + "WHERE mdm.month_year IN "
					+ bothYearsIN + " AND mdm.unit_sold != 0";

			if (pincodes.length > 0) {
				countQuery = countQuery + " AND mdm.pincode IN " + pincodesIN;
			}

			List<Map<String, Object>> countList = jdbcTemplate.queryForList(countQuery);

			Double currYrTotalSale = (Double) countList.get(0).get("currYrTotalSale");

			String query = "SELECT pincode, currYrSale, prevYrSale, COALESCE (percent,0) as percent, currYrSale - prevYrSale as incremental from (\r\n"
					+ "	SELECT mdm.pincode,\r\n" + "	SUM(CASE WHEN mdm.month_year IN " + prevMonthYears
					+ " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS prevYrSale," + " SUM(CASE WHEN mdm.month_year IN"
					+ currentMonthYears + " THEN mdm.unit_sold * pm.mrp ELSE 0 END) AS currYrSale,"
					+ " (SUM(CASE WHEN mdm.month_year IN " + currentMonthYears
					+ "THEN mdm.unit_sold * pm.mrp ELSE 0 END) / " + currYrTotalSale + " ) * 100 AS percent\r\n"
					+ "	FROM monthly_data_master mdm\r\n"
					+ "	JOIN price_master pm ON mdm.g_product_id = pm.g_product_id";

			if (pincodes.length > 0) {
				query = query + " WHERE mdm.pincode IN " + pincodesIN;
			}

			query = query + "  GROUP BY mdm.pincode\r\n" + ") as alldata\r\n"
					+ "WHERE currYrSale != 0 OR prevYrSale != 0\r\n";

			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(query);

			List<Object> mappedResults = new ArrayList<>();

			for (Map<String, Object> data : queryForList) {

				Map<String, Object> resultMap = new HashMap<>();

				resultMap.put("pincode", data.get("pincode"));
				resultMap.put("prev_year_sale", data.get("prevYrSale"));
				resultMap.put("current_year_sale", data.get("currYrSale"));
				resultMap.put("incremental_value", data.get("incremental"));
				resultMap.put("contribution", data.get("percent"));

				mappedResults.add(resultMap);
			}
			
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			workbook.setCompressTempFiles(true);
			Sheet sheet = workbook.createSheet("Pincode Report");

			Row headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue("Pincode");
			headerRow.createCell(1).setCellValue("Previous Year Sale");
			headerRow.createCell(2).setCellValue("Current Year Sale");
			headerRow.createCell(3).setCellValue("Incremental Value");
			headerRow.createCell(4).setCellValue("Contribution");

			int rowNum = 1;
			for (int i = 0; i < mappedResults.size(); i++) {

				System.out.println(i);
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue((String) ((Map<String, Object>) mappedResults.get(i)).get("pincode"));
				row.createCell(1).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("prev_year_sale"));
				row.createCell(2).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("current_year_sale"));
				row.createCell(3).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("incremental_value"));
				row.createCell(4).setCellValue((Double) ((Map<String, Object>) mappedResults.get(i)).get("contribution"));
			}

			ServletOutputStream ops = response.getOutputStream();
			workbook.write(ops);
			workbook.close();
			ops.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
