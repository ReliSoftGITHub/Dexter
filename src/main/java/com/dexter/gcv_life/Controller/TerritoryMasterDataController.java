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

import com.dexter.gcv_life.Entity.TerritoryMasterFinal;
import com.dexter.gcv_life.Repository.TerritoryMasterFinalRepository;
import com.dexter.gcv_life.Service.MasterDataService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class TerritoryMasterDataController {
    
	@Autowired
    MasterDataService serviceObj;
    
    @Autowired
    private TerritoryMasterFinalRepository repository;

    @GetMapping("/TerritoryMaster")
    public String getAllData(final HttpServletRequest request,
    		@RequestParam(value = "divisionname", required = false) String divisionname,
    		@RequestParam(value = "territoryname", required = false) String territoryname,
    		@RequestParam(value = "zonename", required = false) String zonename) {

    	if(divisionname.isEmpty())
    		divisionname = null;
		
		if(territoryname.isEmpty())
			territoryname = null;
		
		if(zonename.isEmpty())
			zonename = null;
		
    	final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = repository.totalCount(divisionname, territoryname, zonename);
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<TerritoryMasterFinal> userdata = repository.findAllData(recordsCount, startIndex, divisionname, territoryname, zonename);
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
		
        return jsonResponse.toString();
    }
        
    @PostMapping("/TerritoryMasterUpload")
    public ResponseEntity<String> uploadExcelData(@RequestParam("file") MultipartFile excelFile) {
    	    	
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            /*Row headerRow = sheet.getRow(0);
            int columnCount = headerRow.getLastCellNum();

            List<String> headers = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                headers.add(headerRow.getCell(i).getStringCellValue());
            }*/

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                	
                	String sap_code = compareValuesString(row.getCell(1));
                   
                    // Check if the data already exists in the database
                    List<TerritoryMasterFinal> existingEntity = repository.checkExistingData(sap_code);

                    if (existingEntity.isEmpty() || sap_code.equals("0")) {
                        
                    	TerritoryMasterFinal newEntity = new TerritoryMasterFinal();

                    	newEntity.setDivision(compareValuesString(row.getCell(0)));
                    	newEntity.setSapCode(sap_code);
                    	newEntity.setTerritoryCode(compareValuesString(row.getCell(2)));
                    	newEntity.setTerritoryName(compareValuesString(row.getCell(3)));
                    	newEntity.setDistrictCode(compareValuesString(row.getCell(4)));
                    	newEntity.setDistrictName(compareValuesString(row.getCell(5)));
                    	newEntity.setZoneCode(compareValuesString(row.getCell(6)));
                    	newEntity.setZoneName(compareValuesString(row.getCell(7)));
                    	newEntity.setState(compareValuesString(row.getCell(8)));
                    	newEntity.setNameOfFsoDsmZsm(compareValuesString(row.getCell(9)));
                    	newEntity.setHq(compareValuesString(row.getCell(10)));
                    	newEntity.setPermanentCity(compareValuesString(row.getCell(11)));
                    	newEntity.setPermanentPincode(compareValuesString(row.getCell(12)));
                    	
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
