package com.dexter.gcv_life.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Entity.MonthlyDataMaster;
import com.dexter.gcv_life.Repository.GcvProductMasterRepo;
import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;
import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin
@RestController
public class BrandFilterController {

	@Autowired
	private MonthlyDataMasterRepo monthlyDataMasterRepo;
	
	@Autowired
	private GcvProductMasterRepo productMasterRepo;

	@GetMapping(value = "/getBrandDropDown")
	public ResponseEntity<Map<String, List>> getBrandDropDown() {
		System.out.println("getBrandDropDown API called");
		Map map = new HashMap();
		List list = monthlyDataMasterRepo.brandDistinct();
		map.put("brand", list);
		return new ResponseEntity<Map<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping(value = "/getSKUDropDown")
	public ResponseEntity<Map<String, List>> getSKUDropDown() {
		System.out.println("getSKUDropDown API called");
		Map map = new HashMap();
		List list = monthlyDataMasterRepo.SKUDistinct();
		map.put("SKU", list);
		return new ResponseEntity<Map<String, List>>(map, HttpStatus.OK);
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

	@PostMapping("/getBrandMaster")
	public ResponseEntity<List<?>> getFieldMasterData(@RequestBody JsonNode request) {

		JsonNode group = (JsonNode) request.get("group");
		JsonNode subGroup = (JsonNode) request.get("subGroup");
		JsonNode existingNewSubgroup = (JsonNode) request.get("existingNewSubgroup");
		JsonNode brand = (JsonNode) request.get("brand");
		JsonNode brandGroup = (JsonNode) request.get("brandGroup");
		JsonNode SKUGproductName = (JsonNode) request.get("SKUGproductName");

		try {
			String gp = convertToJsonString(group);
			String subGp = convertToJsonString(subGroup);
			String exSubGp = convertToJsonString(existingNewSubgroup);
			String b = convertToJsonString(brand);
			String bGp = convertToJsonString(brandGroup);
			String sku = convertToJsonString(SKUGproductName);

			List<MonthlyDataMaster> list = monthlyDataMasterRepo.brandsFilterProcedure(gp,subGp,exSubGp,b,bGp,sku);

			System.out.println(list);
			System.out.println(list.size());
			return new ResponseEntity<>(list, HttpStatus.OK);
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
