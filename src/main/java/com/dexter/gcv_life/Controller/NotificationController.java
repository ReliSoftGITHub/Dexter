package com.dexter.gcv_life.Controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Repository.MonthlyDataMasterRepo;

@RestController
@CrossOrigin
public class NotificationController {

	@Autowired
	MonthlyDataMasterRepo monthlyDataMasterRepo;
	
	@GetMapping("/getNotification")
	public List<String> MissingDataChecker() throws java.text.ParseException {
		
		try {
			
			String startYear;
			String endYear;
		    int year = Calendar.getInstance().get(Calendar.YEAR);
		    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		    if (month <= 3) {
		    	startYear = String.valueOf(year - 1);
		    	endYear = String.valueOf(year);
		    } else {
		    	startYear = String.valueOf(year);
		    	endYear = String.valueOf(year + 1);
		    }
			
			List<String> accountName = new ArrayList<>();
            accountName.add("Amazon");
            accountName.add("Blinkit");
            accountName.add("Walmart");	
            accountName.add("Medplus");
			
			List<String> monthsInYear = generateMonthsInFinancialYear("April "+startYear, "March "+endYear);
            List<String> accountNames = getAccountNamesWithDate(accountName, monthsInYear);
            List<String> missingData = findMissingData(accountNames, monthsInYear, "April "+startYear, "March "+endYear);

            return missingData;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	private List<String> getAccountNamesWithDate(List<String> accountNames, List<String> monthsInYear) {
		List<String> combinedList = new ArrayList<>();

		for (String accountName : accountNames) {
		    for (String monthYear : monthsInYear) {
		        combinedList.add(accountName + " " + monthYear);
		    }
		}
		return combinedList;
	}

	private static List<String> generateMonthsInFinancialYear(String startMonthYear, String endMonthYear) throws java.text.ParseException {
		List<String> months = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        Date startDate = sdf.parse(startMonthYear);
        Date endDate1 = sdf.parse(endMonthYear);
		Date endDate = new Date().after(endDate1) ? endDate1 : new Date();

		startCalendar.setTime(startDate);
		endCalendar.setTime(endDate);

		while (!startCalendar.after(endCalendar)) {
		    String formattedMonth = sdf.format(startCalendar.getTime());
		    months.add(formattedMonth);
		    startCalendar.add(Calendar.MONTH, 1);
		}

        return months;
    }
	
	   private List<String> findMissingData(List<String> accountNames, List<String> monthsInYear, String startMonthYear, String endMonthYear) throws SQLException {
		List<String> missingData = new ArrayList<>();
		
		List<Object[]> resultSet = monthlyDataMasterRepo.getNotifyData(startMonthYear, endMonthYear);
					
		for (Object[] result : resultSet) {
		    String accountName = (String) result[0];  
		    String monthYear = (String) result[1];
		    missingData.add(accountName + " " + monthYear);
		}

		List<String> remainingData = new ArrayList<>();
		for (String accountName : accountNames) {
		    if (!missingData.contains(accountName)) {
		        remainingData.add(accountName);
		    }
		}
		return remainingData;
	   }
	}
