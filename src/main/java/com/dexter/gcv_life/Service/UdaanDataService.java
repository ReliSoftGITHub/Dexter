package com.dexter.gcv_life.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UdaanDataService {

	public ResponseEntity<?> UdaanData(String account_name, MultipartFile excelFile, String month, String year);

}
