package com.dexter.gcv_life.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Entity.GeographyMaster;
import com.dexter.gcv_life.Entity.GeographyMasterRequest;
import com.dexter.gcv_life.Entity.MonthlyDataMaster;
import com.dexter.gcv_life.Repository.GeographyMasterRepository;
import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@CrossOrigin
public class GeographyMasterFilterController {
	@Autowired
	GeographyMasterRepository repository;

	@Autowired
	MonthlyDataMasterRepo monthlyDataMasterRepo;

	@GetMapping("/officeNameGeoDropdown")
	public ResponseEntity<List> divisionOnly() {
		List list = repository.getDistinctOfficeName();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/zoneGeoDropdown")
	public ResponseEntity<Map<String, List>> zoneOnly() {
		Map map = new HashMap();
		System.out.println("zoneGeoDropdown API called");
		List list = monthlyDataMasterRepo.zonedDistinct();
		map.put("zone", list);
		return new ResponseEntity<Map<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/pincodeGeoDropdown")
	public ResponseEntity<Map<String, List>> pincodeOnly() {
		Map map = new HashMap();
		System.out.println("pincodeGeoDropdown API called");
		List list = monthlyDataMasterRepo.pincodeDistinct();
		map.put("pincode", list);
		return new ResponseEntity<Map<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/stateGeoDropDown")
	public ResponseEntity<Map<String, List>> stateGeoDropDown() {
		Map map = new HashMap();
		System.out.println("stateGeoDropDown API called");
		List list = monthlyDataMasterRepo.stateDistinct();
		map.put("states", list);
		return new ResponseEntity<Map<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/cityGeoDropDown")
	public ResponseEntity<Map<String, List>> cityGeoDropDown() {
		Map map = new HashMap();
		System.out.println("cityGeoDropDown API called");
		List list = monthlyDataMasterRepo.cityDistinct();
		map.put("city", list);
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

	@PostMapping("/getGeoMasterData")
	public ResponseEntity<List<?>> getData(@RequestBody JsonNode request) {
		System.out.println("getGeoMasterData API called");

		JsonNode division = (JsonNode) request.get("division");
		JsonNode zone = (JsonNode) request.get("zone");
		JsonNode state = (JsonNode) request.get("state");
		JsonNode city = (JsonNode) request.get("city");
		JsonNode pincode = (JsonNode) request.get("pincode");

		try {

			String d = convertToJsonString(division);
			String z = convertToJsonString(zone);
			String s = convertToJsonString(state);
			String c = convertToJsonString(city);
			String p = convertToJsonString(pincode);
			System.out.println(d + " | " + z + " | " + s + " | " + c + " | " + p);

			List<MonthlyDataMaster> list = monthlyDataMasterRepo.geographyFilterProcedure(d, z, s, c, p);

			System.out.println(list);
			System.out.println(list.size());
			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		catch (Exception e) {
			System.err.println("SQLException error.");
			System.out.println(e);
			List list = new ArrayList();
			list.add("Error...");
			return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getGeographyMasterData")
	public ResponseEntity<List<GeographyMaster>> getGeographyMasterData(@RequestBody GeographyMasterRequest request) {
		String office_name = request.getOffice_name();
		String zone = request.getZone();
		String pincode = request.getPincode();

		try {

			if (!office_name.isEmpty() && !zone.isEmpty() && !pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getGeographyMasterData(office_name, zone, pincode);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

			if (!office_name.isEmpty() && !zone.isEmpty() && pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getOfficeNameAndZone(office_name, zone);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

			if (office_name.isEmpty() && !zone.isEmpty() && !pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getZoneAndPincode(zone, pincode);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

			if (!office_name.isEmpty() && zone.isEmpty() && !pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getOfficeNameAndPincode(office_name, pincode);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

			if (!office_name.isEmpty() && zone.isEmpty() && pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getOfficeName(office_name);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

			if (office_name.isEmpty() && !zone.isEmpty() && pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getZone(zone);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

			if (office_name.isEmpty() && zone.isEmpty() && !pincode.isEmpty()) {
				List<GeographyMaster> data = repository.getPincode(pincode);
				return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.getMessage();
		}

		return null;
	}

}
