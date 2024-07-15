package com.dexter.gcv_life.Controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dexter.gcv_life.Entity.ProcessFiles;
import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;
import com.dexter.gcv_life.Repository.ProcessFilesRepo;
import com.dexter.gcv_life.Service.NetmedDataService;
import com.dexter.gcv_life.Service.UdaanDataService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

//import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class MonthlyDataUploadController {
	
	@Autowired
	NetmedDataService netmedDataService;
	
	@Autowired
	UdaanDataService udaanDataService;
	
	@Autowired
	MonthlyDataMasterRepo monthlyDataMasterRepo;
	
	@Autowired
	ProcessFilesRepo processFilesRepo;

	 @PostMapping("/MonthlyDataUpload")
	    public ResponseEntity<?> uploadExcelData(@RequestParam("file") MultipartFile excelFile,
	    		@RequestParam(required = false, value = "account_name")String account_name,
	    		@RequestParam(required = false, value = "month")String month,
	    		@RequestParam(required = false, value = "year")String year) throws ParseException {
	    	
		 switch(account_name) {
		 case "Netmed":
			 return netmedDataService.netmedData(account_name, excelFile);
			 
		 case "Udaan":
			 return udaanDataService.UdaanData(account_name, excelFile, month, year);
		 }

	        return ResponseEntity.ok(account_name+" data process is inprogress.");
	    }
	 
	 @GetMapping("/getProcessingFile")
	 public ResponseEntity<?> getProcessingFile(){
		 List<ProcessFiles> alldata = processFilesRepo.getProcessFile();
		 return ResponseEntity.ok(alldata);
	 }
	 
	 @PostMapping("/updateProcessingFile")
	    public ResponseEntity<?> updateProcessingFile(@RequestParam("file_name") String file_name,
	    		@RequestParam(value = "account_name")String account_name,
	    		@RequestParam(value = "status")String status,
	    		@RequestParam(value = "thread_count")int thread_count) {
		 
		 processFilesRepo.updateFailedFile(file_name, account_name, status);
		 
		 return ResponseEntity.ok(file_name+" data need to re-upload.");
	 }
	 
	 @PostMapping("/getMonthlyData")
	 public String getMonthlyData(final HttpServletRequest request){
		 System.out.println("getMonthlyData API called");
		 
		 int startYear;
		 int endYear;
		    int year = Calendar.getInstance().get(Calendar.YEAR);
		    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		    if (month <= 3) {
		    	startYear = year - 2;
		    	endYear = year -1;
		    } else {
		    	startYear = year -1;
		    	endYear = year ;
		    }
			final JsonObject jsonResponse = new JsonObject();
		    BigInteger totalCount = monthlyDataMasterRepo.monthlyDataTotalCount();
			
			long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
			long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
			
		    
		    List<Object[]> results = monthlyDataMasterRepo.callViewDataMaster(startYear, startIndex, recordsCount);
		    List<Object> mappedResults = new ArrayList<>();

		    
		    for (Object[] row : results) {
		        Map<String, Object> resultMap = new HashMap<>();
		        
		        String account_name = (String) row[1];
		        String account_type = (String) row[2];
		        String product_id = (String) row[3]; 
		        String product_name = (String) row[4]; 
		        String g_product_id = (String) row[5];
		        String division = (String) row[6];
		        String pincode = (String) row[7];
		        String state = (String) row[8]; 
		        String city = (String) row[9]; 
		        String zone = (String) row[10];
		        String g_product_name = (String) row[18]; 
		        String brand = (String) row[19];
		        BigDecimal april = (BigDecimal) row[20]; 
		        BigDecimal may = (BigDecimal) row[21];
		        BigDecimal june = (BigDecimal) row[22]; 
		        BigDecimal july = (BigDecimal) row[23];
		        BigDecimal august = (BigDecimal) row[24]; 
		        BigDecimal sept = (BigDecimal) row[25];
		        BigDecimal oct = (BigDecimal) row[26]; 
		        BigDecimal nov = (BigDecimal) row[27];
		        BigDecimal dec = (BigDecimal) row[28]; 
		        BigDecimal jan = (BigDecimal) row[29];
		        BigDecimal feb = (BigDecimal) row[30]; 
		        BigDecimal mar = (BigDecimal) row[31];
		        BigDecimal Napril = (BigDecimal) row[32];

		        resultMap.put("account_name", account_name);
		        resultMap.put("account_type", account_type);
		        resultMap.put("product_id", product_id);
		        resultMap.put("product_name", product_name);
		        resultMap.put("g_product_id", g_product_id);
		        resultMap.put("division", division);
		        resultMap.put("pincode", pincode);
		        resultMap.put("state", state);
		        resultMap.put("city", city);
		        resultMap.put("zone", zone);
		        resultMap.put("g_product_id", g_product_id);
		        resultMap.put("g_product_name", g_product_name);
		        resultMap.put("brand", brand);

		        resultMap.put("April_"+startYear, april);
		        resultMap.put("May_"+startYear, may);
		        resultMap.put("June_"+startYear, june);
		        resultMap.put("July_"+startYear, july);
		        resultMap.put("August_"+startYear, august);
		        resultMap.put("September_"+startYear, sept);
		        resultMap.put("October_"+startYear, oct);
		        resultMap.put("November_"+startYear, nov);
		        resultMap.put("December_"+startYear, dec);
		        resultMap.put("January_"+endYear, jan);
		        resultMap.put("February_"+endYear, feb);
		        resultMap.put("March_"+endYear, mar);
		        resultMap.put("April_"+endYear, Napril);
		        mappedResults.add(resultMap);
		    }
		    Gson gson = new Gson();
	        String json = gson.toJson(mappedResults);
	        JsonArray jsonArray = gson.fromJson(json, com.google.gson.JsonArray.class);

			jsonResponse.add("aaData", jsonArray);
			jsonResponse.addProperty("iTotalRecords", totalCount);
			jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
	        return jsonResponse.toString();
	 }
}
