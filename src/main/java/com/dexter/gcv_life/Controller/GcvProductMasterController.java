package com.dexter.gcv_life.Controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

//import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dexter.gcv_life.Entity.GcvProductMaster;
import com.dexter.gcv_life.Repository.GcvProductMasterRepo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class GcvProductMasterController {

	@Autowired
	GcvProductMasterRepo repository;
	
	@GetMapping("/GcvProductMaster")
    public String getAllData(final HttpServletRequest request,
    		@RequestParam(value = "accountname", required = false) String accountname,
    		@RequestParam(value = "productname", required = false) String productname,
    		@RequestParam(value = "divisionname", required = false) String divisionname) {

		if(accountname.isEmpty())
			accountname = null;
		
		if(productname.isEmpty())
			productname = null;
		
		if(divisionname.isEmpty())
			divisionname = null;
		
		final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = repository.totalCount(accountname, productname, divisionname);
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<GcvProductMaster> userdata = repository.findAllData(recordsCount, startIndex, accountname, productname, divisionname);
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
		
        return jsonResponse.toString();
    }
	
	@PostMapping("/GcvProductMasterUpload")
    public ResponseEntity<String> uploadExcelData(@RequestParam("file") MultipartFile excelFile) {
    	    	
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                	
                	String sku_id = compareValuesString(row.getCell(2));
                	String g_product_id = compareValuesString(row.getCell(4));
                   
                    // Check if the data already exists in the database
                    List<GcvProductMaster> existingEntity = repository.checkExistingData(sku_id, g_product_id);

                    if (existingEntity.isEmpty()) {
                        
                    	GcvProductMaster newEntity = new GcvProductMaster();

                    	newEntity.setKeey(compareValuesString(row.getCell(0)));
                    	newEntity.setAccount(compareValuesString(row.getCell(1)));
                    	newEntity.setSku_id(sku_id);
                    	newEntity.setSku_name(compareValuesString(row.getCell(3)));
                    	newEntity.setG_product_id(g_product_id);
                    	newEntity.setG_product_name(compareValuesString(row.getCell(6)));
                    	newEntity.setG_division(compareValuesString(row.getCell(7)));
                    	newEntity.setBrand(compareValuesString(row.getCell(8)));
                    	
                        repository.save(newEntity);
                    }
                }
            }

            return ResponseEntity.ok("Excel data processed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing Excel data.");
        }
    }
	
	private String compareValuesString(Cell row) {

		if(row == null) {
			return "";
		}
		if (row.getCellType() == CellType.STRING) {
			return row.getStringCellValue();
		}
		if (row.getCellType() == CellType.NUMERIC) {
			return String.format("%.0f", row.getNumericCellValue());
		} else {
			return "";
		}
	}
}
