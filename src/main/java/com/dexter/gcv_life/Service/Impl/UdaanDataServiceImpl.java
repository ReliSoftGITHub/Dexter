package com.dexter.gcv_life.Service.Impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dexter.gcv_life.Entity.FailedMonthlyData;
import com.dexter.gcv_life.Entity.ProcessFiles;
import com.dexter.gcv_life.Repository.AccountMasterRepo;
import com.dexter.gcv_life.Repository.FailedMonthlyDataRepo;
import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;
import com.dexter.gcv_life.Repository.ProcessFilesRepo;
import com.dexter.gcv_life.Repository.TempMonthlyDataRepo;
import com.dexter.gcv_life.Service.UdaanDataService;

@Component
public class UdaanDataServiceImpl implements UdaanDataService{

	@Autowired
	MonthlyDataMasterRepo monthlyDataMasterRepo;

	@Autowired
	FailedMonthlyDataRepo failedMonthlyDataRepo;

	@Autowired
	AccountMasterRepo accountMasterRepo;
	
	@Autowired
	ProcessFilesRepo processFilesRepo;

	@Autowired
	TempMonthlyDataRepo tempMonthlyDataRepo;
	
	@Override
	public ResponseEntity<?> UdaanData(String account_name, MultipartFile excelFile, String month, String year) {
		try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			Row headerRow = sheet.getRow(0);
			int columnCount = headerRow.getLastCellNum();

			List<String> headers = new ArrayList<>();
			for (int i = 0; i < columnCount; i++) {
				headers.add(headerRow.getCell(i).getStringCellValue());
			}

			String prodId = "SAP article";
			String itemName = "Item Desc 1";
			String orderQuantity = "MTD'" + year + " Total Units Sold";
			String clubNbr = "Club Nbr";

			int prodIdIndex = headers.indexOf(prodId);
			int itemNameIndex = headers.indexOf(itemName);
			int orderQuantityIndex = headers.indexOf(orderQuantity);
			int clubNbrIndex = headers.indexOf(clubNbr);
			String monthAndYear = month + " " + year;

		if(prodIdIndex == -1 || itemNameIndex == -1 || orderQuantityIndex == -1 || clubNbrIndex == -1) {
			return ResponseEntity.badRequest().body("Incorrect Excel data.");
		}
			int numOfThreads = 1;

			int startIndex = 1;
			int lastIndex = sheet.getLastRowNum();
			for (int i = 1; i <= numOfThreads; i++) {
				ProcessFiles fileStatus = new ProcessFiles();
				
				fileStatus.setAccount_name(account_name);
				fileStatus.setFile_name(excelFile.getOriginalFilename());
				fileStatus.setStatus("Checking");
				fileStatus.setThread_count(i);
				
				processFilesRepo.save(fileStatus);
				
				processFilesRepo.deleteExistingFail(account_name, excelFile.getOriginalFilename(), i);

				ProcessRecordWalmartCheck processRecordBlinkitCheck = new ProcessRecordWalmartCheck(sheet, startIndex, lastIndex, account_name, i, excelFile.getOriginalFilename(), prodIdIndex, itemNameIndex, orderQuantityIndex, clubNbrIndex, monthAndYear);
				processRecordBlinkitCheck.start();
			}

			return ResponseEntity.ok("Excel data process is inprogress.");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error processing Excel data.");
		}
	}
	
	private class ProcessRecordWalmartCheck extends Thread {

		private Sheet sheet;
		private int startIndex;
		private int limit;
		private String account_name;
		private int threadCount;
		private String fileName;

		private int prodIdIndex;
		private int itemNameIndex;
		private int orderQuantityIndex;
		private int clubNbrIndex;
		private String monthAndYear;


		public ProcessRecordWalmartCheck(Sheet sheet,int startIndex,int limit,String account_name,int threadCount,String fileName,int prodIdIndex,int itemNameIndex,int orderQuantityIndex,int clubNbrIndex,String monthAndYear) {
			this.sheet = sheet;
			this.startIndex = startIndex;
			this.limit = limit;
			this.account_name = account_name;
			this.threadCount = threadCount;
			this.fileName = fileName;
			this.prodIdIndex = prodIdIndex;
			this.itemNameIndex = itemNameIndex;
			this.orderQuantityIndex = orderQuantityIndex;
			this.clubNbrIndex = clubNbrIndex;
			this.monthAndYear = monthAndYear;
		}

		@Override
		public void run() {
			String generatedId = "";
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
			Date getdate = new Date();
			generatedId = formatter1.format(getdate);
			SecureRandom randomNum = null;
			try {
				randomNum = SecureRandom.getInstanceStrong();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			generatedId = generatedId + randomNum.nextInt(10000);
			try {
//			for (int i = startIndex; i <= sheet.getLastRowNum() && i <= limit; i++) {
//				Row row = sheet.getRow(i);
//				if (row != null) {
//
//					String productId = this.compareValuesString(row.getCell(prodIdIndex));
//					String productName = this.compareValuesString(row.getCell(itemNameIndex));
//					int unit = AmazonDataServiceImpl.getIntQuantity(row.getCell(orderQuantityIndex));//used common method from declared in amazonservice
//					String clubNbrIn = this.compareValuesString(row.getCell(clubNbrIndex));
//
//					String pincode = accountMasterRepo.getPincodeFromStoreId(clubNbrIn);
//
//					BigInteger productExists = monthlyDataMasterRepo.checkProduct(productId);
//					boolean productExistsBoolean = productExists.intValue() == 1;
//
//					if (productExistsBoolean) {
//						monthlyDataMasterRepo.insertDataForChecking(account_name, productId, productName, unit, pincode, monthAndYear, generatedId);
//					}
//				}
//			}
//			Object[] avgData = tempMonthlyDataRepo.getAvgData(generatedId);
			
			boolean status = true;

//			    for (Object element : avgData) {
//			        if (element instanceof BigInteger && ((BigInteger) element).equals(BigInteger.ZERO)) {
//			            status = false;
//			            break;
//			        }
//			    }
//			System.out.println("Status: " + status);
			tempMonthlyDataRepo.deleteData(generatedId);
			
			
			if(status == true) {
				processFilesRepo.updateStatus(account_name ,fileName, threadCount);
				
				int numOfThreads = 15;

				int noOfRecordsPerThread;
				if (sheet.getLastRowNum() > numOfThreads) {
					noOfRecordsPerThread = sheet.getLastRowNum() / numOfThreads + 1;
				}else {
					noOfRecordsPerThread = sheet.getLastRowNum();
					numOfThreads = 1;
				}
				int startIndex = 1;
				int lastIndex = noOfRecordsPerThread;
				for (int i = 1; i <= numOfThreads; i++) {
					ProcessFiles fileStatus = new ProcessFiles();
					
					fileStatus.setAccount_name(account_name);
					fileStatus.setFile_name(fileName);
					fileStatus.setStatus("Inprogress");
					fileStatus.setThread_count(i);
					
					processFilesRepo.save(fileStatus);
					
					ProcessRecordWalmart processRecordBlinkit = new ProcessRecordWalmart(sheet, startIndex, lastIndex, account_name, i, fileName, prodIdIndex, itemNameIndex, orderQuantityIndex, clubNbrIndex, monthAndYear);
					processRecordBlinkit.start();
					startIndex = startIndex + noOfRecordsPerThread;
					lastIndex = lastIndex + noOfRecordsPerThread;
				}
				System.out.println("Data insertion is in progress...!");
			}else {
				processFilesRepo.updateFailStatus(account_name ,fileName, threadCount);
				System.out.println("Data insertion is failed...!");
			}
		} catch (Exception e) {
			processFilesRepo.updateFailStatus(account_name ,fileName, threadCount);
			System.out.println("Data insertion is failed...!");
				e.printStackTrace();
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
	
	private class ProcessRecordWalmart extends Thread {

		private Sheet sheet;
		private int startIndex;
		private int limit;
		private String account_name;
		private int threadCount;
		private String fileName;

		private int prodIdIndex;
		private int itemNameIndex;
		private int orderQuantityIndex;
		private int clubNbrIndex;
		private String monthAndYear;


		public ProcessRecordWalmart(Sheet sheet,int startIndex,int limit,String account_name,int threadCount,String fileName,int prodIdIndex,int itemNameIndex,int orderQuantityIndex,int clubNbrIndex,String monthAndYear) {
			this.sheet = sheet;
			this.startIndex = startIndex;
			this.limit = limit;
			this.account_name = account_name;
			this.threadCount = threadCount;
			this.fileName = fileName;
			this.prodIdIndex = prodIdIndex;
			this.itemNameIndex = itemNameIndex;
			this.orderQuantityIndex = orderQuantityIndex;
			this.clubNbrIndex = clubNbrIndex;
			this.monthAndYear = monthAndYear;
		}

		@Override
		public void run() {
			try {
			for (int i = startIndex; i <= sheet.getLastRowNum() && i <= limit; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {

					String productId = this.compareValuesString(row.getCell(prodIdIndex));
					String productName = this.compareValuesString(row.getCell(itemNameIndex));
					int unit = NetmedDataServiceImpl.getIntQuantity(row.getCell(orderQuantityIndex));//used common method from declared in netmedservice
					String clubNbrIn = this.compareValuesString(row.getCell(clubNbrIndex));

					String pincode = accountMasterRepo.getPincodeFromStoreId(clubNbrIn);

					BigInteger productExists = monthlyDataMasterRepo.checkProduct(productId);
					boolean productExistsBoolean = productExists.intValue() == 1;

					if (productExistsBoolean) {

						monthlyDataMasterRepo.insertOrUpdateData(account_name, productId, productName, unit, pincode, monthAndYear);

					} else {

						FailedMonthlyData failedData = new FailedMonthlyData();

						failedData.setAccount_name(account_name);
						failedData.setProduct_id(productId);
						failedData.setProduct_name(productName);
						failedData.setPincode(pincode);
						failedData.setMonth_year(monthAndYear);
						failedData.setUnit_sold(unit);
						failedData.setCreated_at(new Date());
						failedData.setUpdated_at(new Date());
						failedData.setIs_active(1);

						failedMonthlyDataRepo.save(failedData);
					}
				}
			}
			processFilesRepo.updateStatus(account_name ,fileName, threadCount);
			System.out.println("Thread End !");
			} catch (Exception e) {
				processFilesRepo.updateFailStatus(account_name ,fileName, threadCount);
				System.out.println("Data insertion is failed...!");
					e.printStackTrace();
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
}

