package com.dexter.gcv_life.Controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.servlet.http.HttpServletRequest;

import com.dexter.gcv_life.Entity.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Repository.FailedMonthlyDataRepo;
import com.dexter.gcv_life.Repository.GcvProductMasterRepo;
import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;
import com.dexter.gcv_life.Repository.PriceMasterRepo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class FailedMonthlyDataController {

	@Autowired
	FailedMonthlyDataRepo repository;

    @Autowired
    GcvProductMasterRepo gcvProductMasterRepository;

    @Autowired
    MonthlyDataMasterRepo monthlyDataMasterRepo;

    @Autowired
    PriceMasterRepo priceMasterRepository;
	
	@GetMapping("/getFailedMonthlyData")
    public List<FailedMonthlyData> getAllData() {

        return repository.findAll();
    }


    // Calculate Levenshtein distance between two strings
    private int calculateLevenshteinDistance(String s1, String s2) {
        // Logic to calculate Levenshtein distance between two strings
        // You can implement this method or use a library
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(dp[i - 1][j] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost));
            }
        }

        return dp[s1.length()][s2.length()];
//        return 0;
    }

    @PostMapping("/getNearestProductName")
    public Map<String,String> getNearestProductName(@RequestBody String[] excelProductNamesArray){
//        List<String> data = getProductNamesFromDatabase(excelProductName);
        List<String> productNames = gcvProductMasterRepository.getDistinctGProductName();
        String nearestMatch = null;

        Map<String,String> nearestMatchMap = new HashMap<>();

        for( String excelProductName : excelProductNamesArray) {
            int minDistance = Integer.MAX_VALUE;
            for (String productName : productNames) {
                if (productName != null && !productName.isEmpty()) {
                    int distance = calculateLevenshteinDistance(excelProductName, productName);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestMatch = productName;
                    }
                }
            }
            nearestMatchMap.put(excelProductName,nearestMatch);
        }
        return nearestMatchMap;
    }

    @GetMapping("/mapToDisplayedProduct/{product_name}/{nearestProductName}/{sr_no}")
    public String mapToDisplayedProduct(@PathVariable String product_name, @PathVariable String nearestProductName, @PathVariable String sr_no){
        List<FailedMonthlyData> data = repository.getGcvProductIdAndProductName(product_name);
        // Iterate over the data list and process the key-value pairs
        for (FailedMonthlyData failedMonthlyData : data) {

            String skuId = failedMonthlyData.getProduct_id();
            String skuName = failedMonthlyData.getProduct_name();
//            String productName = getNearestProductName(failedMonthlyData.getProduct_name());
            String productId = gcvProductMasterRepository.getProductIdByAccountName(nearestProductName);
            String gDivision = gcvProductMasterRepository.getGDivisionByAccountName(nearestProductName);
            String brand = gcvProductMasterRepository.getBrandByAccountName(nearestProductName);
            String accountName = failedMonthlyData.getAccount_name();
            String pincode = failedMonthlyData.getPincode();
            String monthYear = failedMonthlyData.getMonth_year();
            int unitSold = failedMonthlyData.getUnit_sold();

//            BigInteger productExists = monthlyDataMasterRepo.checkProduct(productId);// Query productMaster table for existence of GCV_product_id
//            boolean productExistsBoolean = productExists.intValue() == 1;

            if (true) {

                monthlyDataMasterRepo.insertOrUpdateData(accountName, productId, nearestProductName, unitSold, pincode, monthYear);
                GcvProductMaster gcvProductMasterEntityObj = new GcvProductMaster();
                gcvProductMasterEntityObj.setKeey(accountName+skuId);
                gcvProductMasterEntityObj.setAccount(accountName);
                gcvProductMasterEntityObj.setSku_id(skuId);
                gcvProductMasterEntityObj.setSku_name(skuName);
                gcvProductMasterEntityObj.setG_product_name(nearestProductName);
                gcvProductMasterEntityObj.setG_product_id(productId);
                gcvProductMasterEntityObj.setG_division(gDivision);
                gcvProductMasterEntityObj.setBrand(brand);
                gcvProductMasterRepository.save(gcvProductMasterEntityObj);
                repository.deleteById(Integer.valueOf(sr_no));

                return "Successfully Mapped Product";
            }
//            else {
//
//                FailedMonthlyData failedData = new FailedMonthlyData();
//
//                failedData.setAccount_name(accountName);
//                failedData.setProduct_id(productId);
//                failedData.setProduct_name(nearestProductName);
//                failedData.setPincode(pincode);
//                failedData.setMonth_year(monthYear);
//                failedData.setUnit_sold(unitSold);
//                failedData.setCreated_at(new Date());
//                failedData.setUpdated_at(new Date());
//                failedData.setIs_active(1);
//
//                repository.save(failedData);
//                return "Data goes to Failed Data Table";
//            }
        }
        return "Something is wrong";
    }

    @GetMapping("/manuallyMapProduct/{product_name}/{wrongName}/{sr_no}")
    public String manuallyMapProduct(@PathVariable String product_name,@PathVariable String wrongName, @PathVariable String sr_no) throws UnsupportedEncodingException {
//        String encodedWrongName = URLEncoder.encode(wrongName, "UTF-8");
//        System.out.println(encodedWrongName);

        List<FailedMonthlyData> data = repository.getGcvProductIdAndProductName(wrongName);
        for (FailedMonthlyData failedMonthlyData : data){
            String skuId = failedMonthlyData.getProduct_id();
            String skuName = failedMonthlyData.getProduct_name();
            String productId = gcvProductMasterRepository.getProductIdByAccountName(product_name);
            String gDivision = gcvProductMasterRepository.getGDivisionByAccountName(product_name);
            String brand = gcvProductMasterRepository.getBrandByAccountName(product_name);
            String accountName = failedMonthlyData.getAccount_name();
            String pincode = failedMonthlyData.getPincode();
            String monthYear = failedMonthlyData.getMonth_year();
            int unitSold = failedMonthlyData.getUnit_sold();

//            BigInteger productExists = monthlyDataMasterRepo.checkProduct(productId);// Query productMaster table for existence of GCV_product_id
//            boolean productExistsBoolean = productExists.intValue() == 1;

            if (true) {

                monthlyDataMasterRepo.insertOrUpdateData(accountName, productId, product_name, unitSold, pincode, monthYear);
                GcvProductMaster gcvProductMasterEntityObj = new GcvProductMaster();
                gcvProductMasterEntityObj.setKeey(accountName+skuId);
                gcvProductMasterEntityObj.setAccount(accountName);
                gcvProductMasterEntityObj.setSku_id(skuId);
                gcvProductMasterEntityObj.setSku_name(skuName);
                gcvProductMasterEntityObj.setG_product_name(product_name);
                gcvProductMasterEntityObj.setG_product_id(productId);
                gcvProductMasterEntityObj.setG_division(gDivision);
                gcvProductMasterEntityObj.setBrand(brand);
                gcvProductMasterRepository.save(gcvProductMasterEntityObj);
                repository.deleteById(Integer.valueOf(sr_no));
                return "Successfully Mapped Product";
            } else {

                FailedMonthlyData failedData = new FailedMonthlyData();

                failedData.setAccount_name(accountName);
                failedData.setProduct_id(productId);
                failedData.setProduct_name(product_name);
                failedData.setPincode(pincode);
                failedData.setMonth_year(monthYear);
                failedData.setUnit_sold(unitSold);
                failedData.setCreated_at(new Date());
                failedData.setUpdated_at(new Date());
                failedData.setIs_active(1);

                repository.save(failedData);
                return "Data goes to Failed Data Table";
            }

        }
            return "Something is wrong";
    }

    @GetMapping("/viewFailedData")
    public String getFailedMonthlyData(final HttpServletRequest request){
    	
    	final JsonObject jsonResponse = new JsonObject();
		BigInteger totalCount = repository.totalCount();
		
		long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
		long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
		
		List<FailedMonthlyData> userdata = repository.getFailedMonthlyDataView(recordsCount, startIndex);
		

        List<String> productNames = gcvProductMasterRepository.getDistinctGProductName();
        String nearestMatch = null;

        for(int i = 0 ; i < userdata.size() ; i ++) {
            int minDistance = Integer.MAX_VALUE;
            for (String productName : productNames) {
                if (productName != null && !productName.isEmpty()) {
                    int distance = calculateLevenshteinDistance(userdata.get(i).getProduct_name(), productName);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestMatch = productName;
                        userdata.get(i).setNearest_product_name(nearestMatch);
                    }
                }
            }
        }
        
		JsonElement raw_data = new Gson().toJsonTree(userdata);
		jsonResponse.add("aaData", raw_data);
		jsonResponse.addProperty("iTotalRecords", totalCount);
		jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
        return jsonResponse.toString();
    }

    @PostMapping("/addAsNewProduct")
    public String addAsNewProduct(@RequestBody FailedDataAddAsNewProductRequest request){

        String keey = request.getKeey();
        String account = request.getAccount();
        String sku_id = request.getSku_id();
        String sku_name = request.getSku_name();
        String gProductId = request.getG_product_id();
        String g_product_name = request.getG_product_name();
        String g_division = request.getG_division();
        String brand = request.getBrand();
        Double mrp = request.getMrp();
        Double pts = request.getPts();
        Double ptr = request.getPtr();
        String sr_no = request.getSr_no();

        GcvProductMaster entityObject = new GcvProductMaster();

        entityObject.setKeey(keey);
        entityObject.setAccount(account);
        entityObject.setSku_id(sku_id);
        entityObject.setSku_name(sku_name);
        entityObject.setG_product_id(String.valueOf(gProductId));
        entityObject.setG_product_name(g_product_name);
        entityObject.setG_division(g_division);
        entityObject.setBrand(brand);
        gcvProductMasterRepository.save(entityObject);

//        Optional<List<PriceMaster>> existingProductIdData = priceMasterRepository.findByGProductId(gProductId);

        List<PriceMaster> existingProductIdData = priceMasterRepository.getData(gProductId);
        if (!existingProductIdData.isEmpty()) {
        for(int j=0;j<existingProductIdData.size();j++) {
                PriceMaster existingData = existingProductIdData.get(j);
                existingData.setMrp(mrp);
                existingData.setPts(pts);
                existingData.setPtr(ptr);

                priceMasterRepository.save(existingData);
        	}
        }else {
        	PriceMaster newData = new PriceMaster();
            newData.setSku_id(sku_id);
            newData.setSku_name(sku_name);
//            newData.setGProductId(gProductId);
            newData.setgProductId(gProductId);
            newData.setG_product_name(g_product_name);
            newData.setG_division(g_division);
            newData.setPts(pts);
            newData.setPtr(ptr);
            newData.setMrp(mrp);
            newData.setBrand(brand);

            priceMasterRepository.save(newData);
        }
//        BigInteger productExists = monthlyDataMasterRepo.checkProduct(String.valueOf(gProductId));// Query productMaster table for existence of GCV_product_id
//        boolean productExistsBoolean = productExists.intValue() == 1;

        if (true) {

            List<Map<String,Object>> dataList = repository.getPcMyUs(String.valueOf(sku_id));
            String pincode = "";
            String monthYear = "";
            int unitSold = 0;
            for(int j=0;j<dataList.size();j++) {
                Map<String,Object> data = dataList.get(j);

                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (key.equals("pincode"))
                        pincode = (String) value;
                    else if (key.equals("month_year")) {
                        monthYear = (String) value;
                    } else if (key.equals("unit_sold")) {
                        unitSold = (int) value;
                    }
                }
            }
            monthlyDataMasterRepo.insertOrUpdateData(account, sku_id, sku_name, unitSold, pincode, monthYear);
//            MonthlyDataMaster addAsNewProdObj = new MonthlyDataMaster();
//            addAsNewProdObj.setG_product_id(gProductId);
//            addAsNewProdObj.setG_product_name(g_product_name);
//            addAsNewProdObj.setBrand(brand);
//            addAsNewProdObj.setDivision(g_division);
//            addAsNewProdObj.setAccount_name(account);
//            addAsNewProdObj.setPincode(pincode);
//            addAsNewProdObj.setProduct_id(sku_id);
//            addAsNewProdObj.setProduct_name(sku_name);
//            addAsNewProdObj.setUnit_sold(unitSold);
//            addAsNewProdObj.setMonth_year(monthYear);
//            monthlyDataMasterRepo.save(addAsNewProdObj);

            try {
                repository.deleteById(Integer.valueOf(sr_no));
            } catch(Exception e){
                return "No record found with sr_no : "+sr_no;
            }
            return "Successfully Mapped Product";
        }
        return "Data saved successfully";
    }

    @GetMapping("/truncateFailedData")
    public String truncateFailedData() {
        // Step 3: Truncate the table to remove all records
        repository.deleteAll();

        // Return a response to indicate that the table has been truncated
        return "Table truncated successfully";
    }

}
