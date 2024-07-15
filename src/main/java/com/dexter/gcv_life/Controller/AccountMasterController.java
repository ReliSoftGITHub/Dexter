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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dexter.gcv_life.Entity.AccountMaster;
import com.dexter.gcv_life.Entity.AccountMasterRequest;
import com.dexter.gcv_life.Entity.GcvProductMaster;
import com.dexter.gcv_life.Repository.AccountMasterRepo;
import com.dexter.gcv_life.Repository.GcvProductMasterRepo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class AccountMasterController {

	@Autowired
	AccountMasterRepo repository;
	
	@Autowired
	GcvProductMasterRepo gcvProductMasterRepo;
	
	@GetMapping("/AccountMaster")
    public String getAllData(final HttpServletRequest request,
    		@RequestParam(value = "account", required = false) String account,
    		@RequestParam(value = "product_name", required = false) String product_name,
    		@RequestParam(value = "gcv_product", required = false) String gcv_product) {
		
		if(account.isEmpty())
			account = null;
		
		if(product_name.isEmpty())
			product_name = null;
		
		if(gcv_product.isEmpty())
			gcv_product = null;
		
		final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = repository.totalCount(account, product_name, gcv_product);
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<AccountMaster> userdata = repository.findAllData(recordsCount, startIndex, account, product_name, gcv_product);
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
        return jsonResponse.toString();
    }
	
	@PostMapping("/AccountMasterUpload")
    public ResponseEntity<String> uploadExcelData(@RequestParam("file") MultipartFile excelFile) {
    	    	
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                	
                	String key = compareValuesString(row.getCell(0));
                	String product_id = compareValuesString(row.getCell(9));
                	String gcv_product_id = compareValuesString(row.getCell(11));
                   
                	if(gcv_product_id.equals("")) {
                		List<GcvProductMaster> gcvProdData = gcvProductMasterRepo.checkGcvProdId(product_id);
                		
                		gcv_product_id = gcvProdData.get(0).getG_product_id();
                	}
                    // Check if the data already exists in the database
                    List<AccountMaster> existingEntity = repository.checkExistingData(key, product_id, gcv_product_id);

                    if (existingEntity.isEmpty()) {
                        
                    	AccountMaster newEntity = new AccountMaster();

                    	newEntity.setKeey(key);
                    	newEntity.setType(compareValuesString(row.getCell(1)));
                    	newEntity.setAccount(compareValuesString(row.getCell(2)));
                    	newEntity.setDetails(compareValuesString(row.getCell(3)));
                    	newEntity.setState(compareValuesString(row.getCell(4)));
                    	newEntity.setCity(compareValuesString(row.getCell(5)));
                    	newEntity.setRaw_pincode(compareValuesString(row.getCell(6)));
                    	newEntity.setGcv_pincode(compareValuesString(row.getCell(7)));
                    	newEntity.setStore_id(compareValuesString(row.getCell(8)));
                    	newEntity.setProduct_id(product_id);
                    	newEntity.setProduct_name(compareValuesString(row.getCell(10)));
                    	newEntity.setGcv_product_id(gcv_product_id);
                    	newEntity.setGcv_product_name(compareValuesString(row.getCell(12)));
                    	
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
	
	@PostMapping("/getAccountMaster")
	public ResponseEntity<List<AccountMaster>> getDataAccountMaster(@RequestBody AccountMasterRequest request) {
		System.out.println("getAccountMaster API called");
		String account = request.getAccount();
		String productname = request.getProduct_name();
		String gcvproductname = request.getGcv_product_name();

		try {
			
			// all are filled
			if (!account.isEmpty() && !productname.isEmpty() && !gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.getAccountMasterData(account, productname, gcvproductname);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

			// division and zone are filled
			if (!account.isEmpty() && !productname.isEmpty() && gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.AccountandProductName(account, productname);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

			// division and territory are filled
			if (!account.isEmpty() && productname.isEmpty() && !gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.AccountandGcvProductName(account, gcvproductname);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

			// zone and territory are filled
			if (account.isEmpty() && !productname.isEmpty() && !gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.ProductNameandGcvProductName(productname, gcvproductname);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

			// only division filled
			if (!account.isEmpty() && productname.isEmpty() && gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.onlyAccount(account);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

			// only zone filled
			if (account.isEmpty() && !productname.isEmpty() && gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.onlyProductName(productname);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

			// only territory filled
			if (account.isEmpty() && productname.isEmpty() && !gcvproductname.isEmpty()) {
				List<AccountMaster> data = repository.onlyGcvProductName(gcvproductname);
				return new ResponseEntity<List<AccountMaster>>(data, HttpStatus.OK);
			}

		} catch (Exception e) {
			List list = new ArrayList<String>();
			list.add("No filter selected");
			return new ResponseEntity<List<AccountMaster>>(list,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<AccountMaster>>(HttpStatus.BAD_REQUEST);
	}
	

	@GetMapping("/getAccountDropDown")
	public ResponseEntity<List> accountDropDown(){
		System.out.println("getAccountDropDown API called");
		List list = repository.accountDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("/productNameDropDown")
	public ResponseEntity<List> productNameDropDown(){
		System.out.println("productNameDropDown API called");
		List list = repository.productNameDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@GetMapping("/gcvProductNameDropDown")
	public ResponseEntity<List> gcvProductNameDropDown(){
		System.out.println("gcvProductNameDropDown API called");
		List list = repository.gcvProductNameDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	
}
