package com.dexter.gcv_life.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Entity.AccountMaster;
import com.dexter.gcv_life.Repository.AccountMasterRepo;
import com.dexter.gcv_life.Service.Impl.DownLoadExcelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class DownloadExcelController {

	@Autowired
	private DownLoadExcelService downLoadExcelService;
	
	@Autowired
	AccountMasterRepo accountMasterRepo;

	@GetMapping("/donwloadProductXls")
	public ResponseEntity<String> downloadProductExcel(HttpServletResponse response) throws IOException {

		try {
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=product_master.xls";

			response.setHeader(headerKey, headerValue);

			downLoadExcelService.generateProductExcel(response);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/downloadTerritoryXls")
	public ResponseEntity<String> downloadTerritoryExcel(HttpServletResponse response) throws IOException {
		try {
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=territory_master.xls";

			response.setHeader(headerKey, headerValue);
			downLoadExcelService.generateTerritory(response);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/downloadFieldXls")
	public ResponseEntity<String> downloadFieldExcel(HttpServletResponse response) throws IOException {

		try {
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=field_master.xls";

			response.setHeader(headerKey, headerValue);
			downLoadExcelService.generateFieldExcel(response);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/downloadGeographyXls")
	public ResponseEntity<String> downloadGeographyExcel(HttpServletResponse response) throws IOException {
		try {
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=geography_master.xls";

			response.setHeader(headerKey, headerValue);
			downLoadExcelService.generateGeographyExcel(response);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/downloadAccountExcel")
	public ResponseEntity<String> downloadAccountExcel(HttpServletResponse response) throws IOException {
		
	try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
    
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=accountone_master.xls";

		response.setHeader(headerKey, headerValue);
		
		workbook.setCompressTempFiles(true);

		List<AccountMaster> accountData = accountMasterRepo.allRecords();
		System.out.println("generateAccountExcel  : " + accountData.size());

		Sheet sheet = workbook.createSheet("account");

		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("row_id");
		headerRow.createCell(1).setCellValue("keey");
		headerRow.createCell(2).setCellValue("type");
		headerRow.createCell(3).setCellValue("account");
		headerRow.createCell(4).setCellValue("details");
		headerRow.createCell(5).setCellValue("state");
		headerRow.createCell(6).setCellValue("city");
		headerRow.createCell(7).setCellValue("raw_pincode");
		headerRow.createCell(8).setCellValue("gcv_pincode");
		headerRow.createCell(9).setCellValue("store_id");
		headerRow.createCell(10).setCellValue("product_id");
		headerRow.createCell(11).setCellValue("product_name");
		headerRow.createCell(12).setCellValue("gcv_product_id");
		headerRow.createCell(13).setCellValue("gcv_product_name");

		int rowNum = 1;
		for (AccountMaster entity : accountData) {
//			System.out.println(entity.getRow_id());
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entity.getRow_id());
			row.createCell(1).setCellValue(entity.getKeey());
			row.createCell(2).setCellValue(entity.getType());
			row.createCell(3).setCellValue(entity.getAccount());
			row.createCell(4).setCellValue(entity.getDetails());
			row.createCell(5).setCellValue(entity.getState());
			row.createCell(6).setCellValue(entity.getCity());
			row.createCell(7).setCellValue(entity.getRaw_pincode());
			row.createCell(8).setCellValue(entity.getGcv_pincode());
			row.createCell(9).setCellValue(entity.getStore_id());
			row.createCell(10).setCellValue(entity.getProduct_id());
			row.createCell(11).setCellValue(entity.getProduct_name());
			row.createCell(12).setCellValue(entity.getGcv_product_id());
			row.createCell(13).setCellValue(entity.getGcv_product_name());
		}
	            
			ServletOutputStream ops = response.getOutputStream();
			workbook.write(ops);
			workbook.close();
			ops.close();

			System.out.println("Account Excel Downloaded");
			
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}
	
	@GetMapping("/downloadPriceExcelXls")
	public ResponseEntity<String> downloadPriceExcel(HttpServletResponse response) throws IOException {
		try {
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=price_master.xls";

			response.setHeader(headerKey, headerValue);
			downLoadExcelService.generatePriceExcel(response);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}
	
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
	
	@PostMapping("/downloadBrandReportExcel")
	public ResponseEntity<String> downloadBrandReportExcel(HttpServletResponse response, @RequestBody JsonNode request) throws IOException {
		try {
			
			JsonNode brand = request.get("brands");
			JsonNode startMonth = request.get("startMonth");
			JsonNode endMonth = request.get("endMonth");
			
			String brands = convertToJsonString(brand);
			if(brands.equals("")) {
				brands = "[]";
			}
			ObjectMapper objectMapper = new ObjectMapper();
	        String[] brandArray = objectMapper.readValue(brand.toString(), String[].class);
	        
	        
			String startMon = startMonth.textValue();
			String endMon = endMonth.textValue();
			
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=price_master.xls";

			response.setHeader(headerKey, headerValue);
			downLoadExcelService.generateBrandReportExcel(response, brandArray, startMon, endMon);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}
	
	@PostMapping("/downloadPincodeReportExcel")
	public ResponseEntity<String> downloadPincodeReportExcel(HttpServletResponse response, @RequestBody JsonNode request) throws IOException {
		try {
			
			JsonNode pincode = request.get("pincodes");
			JsonNode startMonth = request.get("startMonth");
			JsonNode endMonth = request.get("endMonth");
			
			String pincodes = convertToJsonString(pincode);
			if(pincodes.equals("")) {
				pincodes = "[]";
			}
			ObjectMapper objectMapper = new ObjectMapper();
	        String[] pincodeArray = objectMapper.readValue(pincode.toString(), String[].class);
	        
	        
			String startMon = startMonth.textValue();
			String endMon = endMonth.textValue();
			
			response.setContentType("application/octet-stream");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=price_master.xls";

			response.setHeader(headerKey, headerValue);
			downLoadExcelService.generatePincodeReportExcel(response, pincodeArray, startMon, endMon);
			return new ResponseEntity<String>("Excel Downloaded Successfully..!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed To Download", HttpStatus.BAD_GATEWAY);
		}
	}
}
