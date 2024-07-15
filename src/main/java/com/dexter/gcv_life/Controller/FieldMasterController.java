package com.dexter.gcv_life.Controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dexter.gcv_life.Entity.FieldMaster;
import com.dexter.gcv_life.Entity.FieldMasterRequest;
import com.dexter.gcv_life.Repository.FieldMasterRepo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class FieldMasterController {

	@Autowired
	FieldMasterRepo repository;
	
	@Autowired
	BulkDataUploadController bulkDataUploadController;

	@GetMapping("/FieldMaster")
	public String getAllData(final HttpServletRequest request,
    		@RequestParam(value = "divisionname", required = false) String divisionname,
    		@RequestParam(value = "zonename", required = false) String zonename,
    		@RequestParam(value = "territorycodename", required = false) String territorycodename) {
		
		if(divisionname.isEmpty())
			divisionname = null;
		
		if(zonename.isEmpty())
			zonename = null;
		
		if(territorycodename.isEmpty())
			territorycodename = null;
		
		final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = repository.totalCount(divisionname, zonename, territorycodename);
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<FieldMaster> userdata = repository.findAllData(recordsCount, startIndex, divisionname, zonename, territorycodename);
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
		
        return jsonResponse.toString();
	}

	@PostMapping("/FieldMasterUpload")
	public ResponseEntity<String> uploadExcelData(@RequestParam("file") MultipartFile excelFile) {

		try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {

					String login_id = compareValuesString(row.getCell(3));
					if (login_id.equals("0")) {
						login_id = "";
					}
					// Check if the data already exists in the database
					List<FieldMaster> existingEntity = repository.checkExistingData(login_id);

					if (existingEntity.isEmpty() || login_id.equals("")) {

						FieldMaster newEntity = new FieldMaster();

						newEntity.setDiv_code(compareValuesString(row.getCell(0)));
						newEntity.setDivision(compareValuesString(row.getCell(1)));
						newEntity.setCluster(compareValuesString(row.getCell(2)));
						newEntity.setLogin_id(login_id);
						newEntity.setEmployee_name(compareValuesString(row.getCell(4)));
						newEntity.setDesignation(compareValuesString(row.getCell(5)));
						newEntity.setDesignation1(compareValuesString(row.getCell(6)));
						newEntity.setHq(compareValuesString(row.getCell(7)));
						newEntity.setTerritory_code(compareValuesString(row.getCell(8)));
						newEntity.setZone_code(compareValuesString(row.getCell(9)));
						newEntity.setZone_name(compareValuesString(row.getCell(10)));
						newEntity.setContact_no1(compareValuesString(row.getCell(11)));
						newEntity.setState(compareValuesString(row.getCell(12)));
						newEntity.setOfficial_email_id(compareValuesString(row.getCell(13)));

						repository.save(newEntity);
					}
				}
			}

			bulkDataUploadController.bulkDataUpload();
			
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

	@PostMapping("/getFieldMaster")
	public ResponseEntity<List<FieldMaster>> getFieldMasterData(@RequestBody FieldMasterRequest request) {
		System.out.println("Field Master API called");
		String division = request.getDivision();
		String zone_name = request.getZone_name();
		String territory_code = request.getTerritory_code();

		try {
			
			// all are filled
			if (!division.isEmpty() && !zone_name.isEmpty() && !territory_code.isEmpty()) {
				List<FieldMaster> data = repository.getFieldMasterData(division, zone_name, territory_code);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

			// division and zone are filled
			if (!division.isEmpty() && !zone_name.isEmpty() && territory_code.isEmpty()) {
				List<FieldMaster> data = repository.DandZ(division, zone_name);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

			// division and territory are filled
			if (!division.isEmpty() && zone_name.isEmpty() && !territory_code.isEmpty()) {
				List<FieldMaster> data = repository.DandT(division, territory_code);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

			// zone and territory are filled
			if (division.isEmpty() && !zone_name.isEmpty() && !territory_code.isEmpty()) {
				List<FieldMaster> data = repository.ZandT(zone_name, territory_code);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

			// only division filled
			if (!division.isEmpty() && zone_name.isEmpty() && territory_code.isEmpty()) {
				List<FieldMaster> data = repository.onlyD(division);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

			// only zone filled
			if (division.isEmpty() && !zone_name.isEmpty() && territory_code.isEmpty()) {
				List<FieldMaster> data = repository.onlyZ(zone_name);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

			// only territory filled
			if (division.isEmpty() && zone_name.isEmpty() && !territory_code.isEmpty()) {
				List<FieldMaster> data = repository.onlyT(territory_code);
				return new ResponseEntity<List<FieldMaster>>(data, HttpStatus.OK);
			}

		} catch (Exception e) {
			List list = new ArrayList<String>();
			list.add("No filter selected");
			return new ResponseEntity<List<FieldMaster>>(list,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<FieldMaster>>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/divisionDropDown")
	public ResponseEntity<List> dropDown(){
		System.out.println("divisionDropDown API called");
		List list = repository.divisionDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("/zonenameDropDown")
	public ResponseEntity<List> zoneName(){
		System.out.println("zonenameDropDown API called");
		List list = repository.zoneDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@GetMapping("/territoryDropDown")
	public ResponseEntity<List> territoryCode(){
		System.out.println("territoryDropDown API called");
		List list = repository.territoryDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
}
