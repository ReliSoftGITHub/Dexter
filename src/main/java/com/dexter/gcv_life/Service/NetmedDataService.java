package com.dexter.gcv_life.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface NetmedDataService {

	public ResponseEntity<?> netmedData(String account_name, MultipartFile excelFile);

}
