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

import com.dexter.gcv_life.Entity.GeographyMaster;
import com.dexter.gcv_life.Repository.GeographyMasterRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class GeographyMasterController {
    @Autowired
    private GeographyMasterRepository repository;
    
    @GetMapping("/GeographyMaster")
    public String getAllData(final HttpServletRequest request,
    		@RequestParam(value = "officename", required = false) String officename,
    		@RequestParam(value = "zonename", required = false) String zonename,
    		@RequestParam(value = "pincodename", required = false) String pincodename) {

    	if(officename.isEmpty())
    		officename = null;
		
		if(zonename.isEmpty())
			zonename = null;
		
		if(pincodename.isEmpty())
			pincodename = null;
		
    	final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = repository.totalCount(officename, zonename, pincodename);
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<GeographyMaster> userdata = repository.findAllData(recordsCount, startIndex, officename, zonename, pincodename);
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
		
        return jsonResponse.toString();
    }
    
    @PostMapping("/GeographyMasterUpload")
    public ResponseEntity<String> uploadExcelData(@RequestParam("file") MultipartFile excelFile) {
    	    	
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                	
                	String pincode = String.format("%.0f", row.getCell(3).getNumericCellValue());
                	
                    // Check if the data already exists in the database
                    List<GeographyMaster> existingEntity = repository.checkExistingData(pincode);

                    if (existingEntity.isEmpty()) {
                        
                    	GeographyMaster newEntity = new GeographyMaster();

                    	newEntity.setOfficeName(compareValuesString(row.getCell(1)));
                    	newEntity.setCleanAreaName(compareValuesString(row.getCell(2)));
                    	newEntity.setPincode(pincode);
                    	newEntity.setGcvCity(compareValuesString(row.getCell(4)));
                    	newEntity.setSubDistname(compareValuesString(row.getCell(5)));
                    	newEntity.setDistrictName(compareValuesString(row.getCell(6)));
                    	newEntity.setStateName(compareValuesString(row.getCell(7)));
                    	newEntity.setZone(compareValuesString(row.getCell(8)));
                    	
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
