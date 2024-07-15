package com.dexter.gcv_life.Service.Impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.dexter.gcv_life.Service.NetmedDataService;

@Component
public class NetmedDataServiceImpl implements NetmedDataService{

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
	public ResponseEntity<?> netmedData(String account_name, MultipartFile excelFile) {
		try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			Row headerRow = sheet.getRow(0);
			int columnCount = headerRow.getLastCellNum();

			List<String> headers = new ArrayList<>();
			for (int i = 0; i < columnCount; i++) {
				headers.add(headerRow.getCell(i).getStringCellValue());
			}

			String prodId = "FG code";
			String itemName = "Item_Name";
			String orderQuantity = "Qty";
			String postalCode = "Pincode";
			String fullDate = "Date_Of_Sale";			

			int prodIdIndex = headers.indexOf(prodId);
			int itemNameIndex = headers.indexOf(itemName);
			int orderQuantityIndex = headers.indexOf(orderQuantity);
			int postalCodeIndex = headers.indexOf(postalCode);
			int fullDateIndex = headers.indexOf(fullDate);
		if(prodIdIndex == -1 || itemNameIndex == -1 || orderQuantityIndex == -1 || postalCodeIndex == -1 || fullDateIndex == -1) {
			return ResponseEntity.badRequest().body("Incorrect Excel data.");
		}	
			int numOfThreadsCheck = 1;

			int startIndexCheck = 1;
			int lastIndexCheck = sheet.getLastRowNum();
			for (int i = 1; i <= numOfThreadsCheck; i++) {
				ProcessFiles fileStatus = new ProcessFiles();
				
				fileStatus.setAccount_name(account_name);
				fileStatus.setFile_name(excelFile.getOriginalFilename());
				fileStatus.setStatus("Checking");
				fileStatus.setThread_count(i);
				
				processFilesRepo.save(fileStatus);
				
				processFilesRepo.deleteExistingFail(account_name, excelFile.getOriginalFilename(), i);
				
				ProcessRecordAmazonCheck processRecordBlinkitCheck = new ProcessRecordAmazonCheck(sheet, startIndexCheck, lastIndexCheck, account_name, i, excelFile.getOriginalFilename(), prodIdIndex, itemNameIndex, orderQuantityIndex, postalCodeIndex, fullDateIndex);
				processRecordBlinkitCheck.start();
				
			}
			
			return ResponseEntity.ok("Excel data process is inprogress.");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error processing Excel data.");
		}
	}
	
	private class ProcessRecordAmazonCheck extends Thread {

		private Sheet sheet;
		private int startIndex;
		private int limit;
		private String account_name;
		private int threadCount;
		private String fileName;

		private int prodIdIndex;
		private int itemNameIndex;
		private int orderQuantityIndex;
		private int postalCodeIndex;
		private int fullDateIndex;

		public ProcessRecordAmazonCheck(Sheet sheet, int startIndex, int limit, String account_name, int threadCount, String fileName, int prodIdIndex, int itemNameIndex, int orderQuantityIndex, int postalCodeIndex, int fullDateIndex) {
			this.sheet = sheet;
			this.startIndex = startIndex;
			this.limit = limit;
			this.account_name = account_name;
			this.threadCount = threadCount;
			this.fileName = fileName;
			this.prodIdIndex = prodIdIndex;
			this.itemNameIndex = itemNameIndex;
			this.orderQuantityIndex = orderQuantityIndex;
			this.postalCodeIndex = postalCodeIndex;
			this.fullDateIndex = fullDateIndex;
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
			for (int i = startIndex; i <= sheet.getLastRowNum() && i <= limit; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {

					String productId = this.compareValuesString(row.getCell(prodIdIndex));
					String productName = this.compareValuesString(row.getCell(itemNameIndex));
//					int unit = this.compareValuesInt(row.getCell(orderQuantityIndex));
					int unit = getIntQuantity(row.getCell(orderQuantityIndex));
					String inPincode = this.compareValuesString(row.getCell(postalCodeIndex));

					String pincode = accountMasterRepo.getPincode(inPincode);

					LocalDateTime newDate = row.getCell(fullDateIndex).getLocalDateTimeCellValue();
			        String dateString = newDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
					
					String monthString = getMonthString(Integer.parseInt(new SimpleDateFormat("MM").format(date)));
					String yearString = String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(date)));
					
					String monthAndYear = monthString + " " + yearString;
					
					BigInteger productExists = monthlyDataMasterRepo.checkProduct(productId);
					boolean productExistsBoolean = productExists.intValue() == 1;

					if (productExistsBoolean) {
						monthlyDataMasterRepo.insertDataForChecking(account_name, productId, productName, unit, pincode, monthAndYear, generatedId);
					}
				}
			}
							
		Object[] avgData = tempMonthlyDataRepo.getAvgData(generatedId);
		
		boolean status = true;

		    for (Object element : avgData) {
		        if (element instanceof BigInteger && ((BigInteger) element).equals(BigInteger.ZERO)) {
		            status = false;
		            break;
		        }
		    }
		System.out.println("Status: " + status);
		tempMonthlyDataRepo.deleteData(generatedId);
		
		
		if(status == true) {
			processFilesRepo.updateStatus(account_name ,fileName, threadCount);
			
			int numOfThreads = 15;

			int noOfRecordsPerThread;
			if (sheet.getLastRowNum() > numOfThreads)
				noOfRecordsPerThread = sheet.getLastRowNum() / numOfThreads + 1;
			else {
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
				
				ProcessRecordAmazon processRecordBlinkit = new ProcessRecordAmazon(sheet, startIndex, lastIndex, account_name, i, fileName, prodIdIndex, itemNameIndex, orderQuantityIndex, postalCodeIndex, fullDateIndex);
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
		
		private int compareValuesInt(Cell row) {

			if (row.getCellType() == CellType.STRING) {
				return Integer.parseInt(row.getStringCellValue());
			}
			if (row.getCellType() == CellType.NUMERIC) {
				return Integer.parseInt(String.format("%.0f", row.getNumericCellValue()));
			} else {
				return 0;
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
	
	private class ProcessRecordAmazon extends Thread {

		private Sheet sheet;
		private int startIndex;
		private int limit;
		private String account_name;
		private int threadCount;
		private String fileName;

		private int prodIdIndex;
		private int itemNameIndex;
		private int orderQuantityIndex;
		private int postalCodeIndex;
		private int fullDateIndex;

		public ProcessRecordAmazon(Sheet sheet, int startIndex, int limit, String account_name, int threadCount, String fileName, int prodIdIndex, int itemNameIndex, int orderQuantityIndex, int postalCodeIndex, int fullDateIndex) {
			this.sheet = sheet;
			this.startIndex = startIndex;
			this.limit = limit;
			this.account_name = account_name;
			this.threadCount = threadCount;
			this.fileName = fileName;
			this.prodIdIndex = prodIdIndex;
			this.itemNameIndex = itemNameIndex;
			this.orderQuantityIndex = orderQuantityIndex;
			this.postalCodeIndex = postalCodeIndex;
			this.fullDateIndex = fullDateIndex;
		}

		@Override
		public void run() {
			try {
			for (int i = startIndex; i <= sheet.getLastRowNum() && i <= limit; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {

					String productId = this.compareValuesString(row.getCell(prodIdIndex));
					String productName = this.compareValuesString(row.getCell(itemNameIndex));
					int unit = getIntQuantity(row.getCell(orderQuantityIndex));
					String inPincode = this.compareValuesString(row.getCell(postalCodeIndex));

					String pincode = accountMasterRepo.getPincode(inPincode);

					LocalDateTime newDate = row.getCell(fullDateIndex).getLocalDateTimeCellValue();
			        String dateString = newDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
					
					String monthString = getMonthString(Integer.parseInt(new SimpleDateFormat("MM").format(date)));
					String yearString = String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(date)));
					String monthAndYear = monthString + " " + yearString;

					BigInteger productExists = monthlyDataMasterRepo.checkProduct(productId);
					boolean productExistsBoolean = productExists.intValue() == 1;

					if (productExistsBoolean) {

						monthlyDataMasterRepo.insertOrUpdateData(account_name, productId, productName, unit, pincode,
								monthAndYear);

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
		
		private int compareValuesInt(Cell row) {

			if (row.getCellType() == CellType.STRING) {
				return Integer.parseInt(row.getStringCellValue());
			}
			if (row.getCellType() == CellType.NUMERIC) {
				return Integer.parseInt(String.format("%.0f", row.getNumericCellValue()));
			} else {
				return 0;
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
	
	public static boolean isNumeric(String str) {
	    try {
	        // Attempt to parse the string as a double
	        Double.parseDouble(str);
	        return true;
	    } catch (NumberFormatException e) {
	        // The string is not a valid number
	        return false;
	    }
	}
	
	static int getIntQuantity(Cell row) {
		if (row.getCellType() == CellType.STRING) {
			
		if(isNumeric(row.getStringCellValue())){
				return (int) Long.parseLong(row.getStringCellValue());
		}
		else {
			String input = row.getStringCellValue();
			boolean isValidInput = true;
		    long result = 0;
		    long finalResult = 0;
		    List<String> allowedStrings = Arrays.asList
		    (
		    "zero","one","two","three","four","five","six","seven",
		    "eight","nine","ten","eleven","twelve","thirteen","fourteen",
		    "fifteen","sixteen","seventeen","eighteen","nineteen","twenty",
		    "thirty","forty","fifty","sixty","seventy","eighty","ninety",
		    "hundred","thousand","lakh"
		    );

		    if(input != null && input.length()> 0)
		    {
		        input = input.replaceAll("-", " ");
		        input = input.toLowerCase().replaceAll(" and", " ");
		        String[] splittedParts = input.trim().split("\\s+");

		        for(String str : splittedParts)
		        {
		            if(!allowedStrings.contains(str))
		            {
		                isValidInput = false;
		                System.out.println("Invalid word found : "+str);
		                break;
		            }
		        }
		        if(isValidInput)
		        {
		            for(String str : splittedParts)
		            {
		                if(str.equalsIgnoreCase("zero")) {
		                    result += 0;
		                }
		                else if(str.equalsIgnoreCase("one")) {
		                    result += 1;
		                }
		                else if(str.equalsIgnoreCase("two")) {
		                    result += 2;
		                }
		                else if(str.equalsIgnoreCase("three")) {
		                    result += 3;
		                }
		                else if(str.equalsIgnoreCase("four")) {
		                    result += 4;
		                }
		                else if(str.equalsIgnoreCase("five")) {
		                    result += 5;
		                }
		                else if(str.equalsIgnoreCase("six")) {
		                    result += 6;
		                }
		                else if(str.equalsIgnoreCase("seven")) {
		                    result += 7;
		                }
		                else if(str.equalsIgnoreCase("eight")) {
		                    result += 8;
		                }
		                else if(str.equalsIgnoreCase("nine")) {
		                    result += 9;
		                }
		                else if(str.equalsIgnoreCase("ten")) {
		                    result += 10;
		                }
		                else if(str.equalsIgnoreCase("eleven")) {
		                    result += 11;
		                }
		                else if(str.equalsIgnoreCase("twelve")) {
		                    result += 12;
		                }
		                else if(str.equalsIgnoreCase("thirteen")) {
		                    result += 13;
		                }
		                else if(str.equalsIgnoreCase("fourteen")) {
		                    result += 14;
		                }
		                else if(str.equalsIgnoreCase("fifteen")) {
		                    result += 15;
		                }
		                else if(str.equalsIgnoreCase("sixteen")) {
		                    result += 16;
		                }
		                else if(str.equalsIgnoreCase("seventeen")) {
		                    result += 17;
		                }
		                else if(str.equalsIgnoreCase("eighteen")) {
		                    result += 18;
		                }
		                else if(str.equalsIgnoreCase("nineteen")) {
		                    result += 19;
		                }
		                else if(str.equalsIgnoreCase("twenty")) {
		                    result += 20;
		                }
		                else if(str.equalsIgnoreCase("thirty")) {
		                    result += 30;
		                }
		                else if(str.equalsIgnoreCase("forty")) {
		                    result += 40;
		                }
		                else if(str.equalsIgnoreCase("fifty")) {
		                    result += 50;
		                }
		                else if(str.equalsIgnoreCase("sixty")) {
		                    result += 60;
		                }
		                else if(str.equalsIgnoreCase("seventy")) {
		                    result += 70;
		                }
		                else if(str.equalsIgnoreCase("eighty")) {
		                    result += 80;
		                }
		                else if(str.equalsIgnoreCase("ninety")) {
		                    result += 90;
		                }
		                else if(str.equalsIgnoreCase("hundred")) {
		                    result *= 100;
		                }
		                else if(str.equalsIgnoreCase("thousand")) {
		                    result *= 1000;
		                    finalResult += result;
		                    result=0;
		                }
		                else if(str.equalsIgnoreCase("lakh")) {
		                    result *= 100000;
		                    finalResult += result;
		                    result=0;
		                }
		            }

		            finalResult += result;
		            result=0;
		        }
		    }

			return (int) finalResult;
		}
	}
		if (row.getCellType() == CellType.NUMERIC) {
			return Integer.parseInt(String.format("%.0f", row.getNumericCellValue()));
		} else {
			return 0;
		}
	}
	
	public static String getMonthString(int month) {
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		if (month >= 1 && month <= 12) {
			return months[month - 1];
		} else {
			throw new IllegalArgumentException("Invalid month value");
		}
	}
}
