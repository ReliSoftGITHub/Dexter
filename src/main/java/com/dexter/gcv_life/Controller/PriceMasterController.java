package com.dexter.gcv_life.Controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

import com.dexter.gcv_life.Entity.PriceMaster;
import com.dexter.gcv_life.Entity.PriceMasterRequest;
import com.dexter.gcv_life.Repository.PriceMasterRepo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class PriceMasterController {

	@Autowired
	PriceMasterRepo priceMasterRepo;
	
    @GetMapping("/PriceMaster")
    public String getAllData(final HttpServletRequest request,
    		@RequestParam(value = "skuname", required = false) String skuname,
    		@RequestParam(value = "gproductname", required = false) String gproductname,
    		@RequestParam(value = "skuid", required = false) String skuid) {

    	if(skuname.isEmpty())
    		skuname = null;
		
		if(gproductname.isEmpty())
			gproductname = null;
		
		if(skuid.isEmpty())
			skuid = null;
		
    	final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = priceMasterRepo.totalCount(skuname, gproductname, skuid);
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<PriceMaster> userdata = priceMasterRepo.findAllData(recordsCount, startIndex, skuname, gproductname, skuid);
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
		
        return jsonResponse.toString();
    }
    
    @PostMapping("/PriceMasterUpload")
    public ResponseEntity<String> uploadExcelData(@RequestParam("file") MultipartFile excelFile) {
    	    	
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                	
                	String sku_id = this.compareValuesString(row.getCell(0));
                	int g_product_id = this.compareValuesInt(row.getCell(2)) ;
                   
                List<PriceMaster> existingEntity = priceMasterRepo.checkExistingData(sku_id, g_product_id);

                    if (existingEntity.isEmpty()) {
                        
                    	PriceMaster newEntity = new PriceMaster();

                    	newEntity.setSku_id(sku_id);
                    	newEntity.setSku_name(this.compareValuesString(row.getCell(1)));
                    	newEntity.setgProductId(String.valueOf(g_product_id));
                    	newEntity.setG_product_name(this.compareValuesString(row.getCell(3)));
                    	newEntity.setG_division(this.compareValuesString(row.getCell(4)));
                    	newEntity.setPts(row.getCell(5).getNumericCellValue());
                    	newEntity.setPtr(row.getCell(6).getNumericCellValue());
                    	newEntity.setMrp(row.getCell(7).getNumericCellValue());
                    	newEntity.setBrand(this.compareValuesString(row.getCell(8)));
                    	newEntity.setCreated_at(new Date());
                    	
                    	priceMasterRepo.save(newEntity);
                    }
                    else if (!existingEntity.isEmpty()) {
                        
                    	PriceMaster newEntity = new PriceMaster();

                    	newEntity.setRow_id(existingEntity.get(0).getRow_id());
                    	newEntity.setSku_id(sku_id);
                    	newEntity.setSku_name(this.compareValuesString(row.getCell(1)));
                    	newEntity.setgProductId(String.valueOf(g_product_id));
                    	newEntity.setG_product_name(this.compareValuesString(row.getCell(3)));
                    	newEntity.setG_division(this.compareValuesString(row.getCell(4)));
                    	newEntity.setPts(row.getCell(5).getNumericCellValue());
                    	newEntity.setPtr(row.getCell(6).getNumericCellValue());
                    	newEntity.setMrp(row.getCell(7).getNumericCellValue());
                    	newEntity.setBrand(this.compareValuesString(row.getCell(8)));
                    	newEntity.setCreated_at(new Date());
                    	
                    	priceMasterRepo.save(newEntity);
                    }
                }
            }

            return ResponseEntity.ok("Excel data processed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing Excel data.");
        }
    }
    
    private int compareValuesInt(Cell row) {
	    
		if(row.getCellType() == CellType.STRING) {
   		 	return Integer.parseInt(row.getStringCellValue());
	   	}
		if(row.getCellType() == CellType.NUMERIC) {
	   		return Integer.parseInt(String.format("%.0f", row.getNumericCellValue()));
	   	}
	   	else {
	   		return 0;
	   	}
	}
    private String compareValuesString(Cell row) {
	    
		if(row.getCellType() == CellType.STRING) {
   		 	return row.getStringCellValue();
	   	}
		if(row.getCellType() == CellType.NUMERIC) {
	   		return String.format("%.0f", row.getNumericCellValue());
	   	}
	   	else {
	   		return "";
	   	}
	}
    
    
    @PostMapping("/getPriceMasterData")
    public ResponseEntity<List<PriceMaster>> getDataAccountMaster(@RequestBody PriceMasterRequest request) {
    	System.out.println("getPriceMasterData API called");
		String sku_name = request.getSku_name();
		String g_product_name = request.getG_product_name();
		String sku_id = request.getSku_id();

		try {
			
			// all are filled
			if (!sku_name.isEmpty() && !g_product_name.isEmpty() && !sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.getPriceMasterData(sku_name, g_product_name, sku_id);
				System.out.println(data);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

			// sku_name and g_product_name are filled
			if (!sku_name.isEmpty() && !g_product_name.isEmpty() && sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.skuNameAndGproductName(sku_name, g_product_name);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

			// sku_name and sku_id are filled
			if (!sku_name.isEmpty() && g_product_name.isEmpty() && !sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.sku_nameAndsku_id(sku_name, sku_id);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

			// g_product_name and sku_id are filled
			if (sku_name.isEmpty() && !g_product_name.isEmpty() && !sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.g_product_nameAndsku_id(g_product_name, sku_id);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

			// only sku_name filled
			if (!sku_name.isEmpty() && g_product_name.isEmpty() && sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.onlySku_name(sku_name);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

			// only g_product_name filled
			if (sku_name.isEmpty() && !g_product_name.isEmpty() && sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.onlyG_product_name(g_product_name);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

			// only sku_id filled
			if (sku_name.isEmpty() && g_product_name.isEmpty() && !sku_id.isEmpty()) {
				List<PriceMaster> data = priceMasterRepo.onlySku_id(sku_id);
				return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
			}

		} catch (Exception e) {
			List list = new ArrayList<String>();
			list.add("No filter selected");
			return new ResponseEntity<List<PriceMaster
					>>(list,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<PriceMaster>>(HttpStatus.BAD_REQUEST);
	}
	

	@GetMapping("/skuNameDropDown")
	public ResponseEntity<List> accountDropDown(){
		System.out.println("skuNameDropDown API called");
		List list = priceMasterRepo.sku_nameDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("/gProductNameDropDown")
	public ResponseEntity<List> productNameDropDown(){
		System.out.println("gProductNameDropDown API called");
		List list = priceMasterRepo.g_product_nameDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@GetMapping("/skuIdDropDown")
	public ResponseEntity<List> gcvProductNameDropDown(){
		System.out.println("skuIdDropDown API called");
		List list = priceMasterRepo.sku_idDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
}
