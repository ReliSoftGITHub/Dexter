package com.dexter.gcv_life.Repository;

import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.TempMonthlyData;

public interface TempMonthlyDataRepo extends JpaRepository<TempMonthlyData, Integer>{

	@Transactional
	@Modifying
	@Query(value = "SELECT * from temp_monthly_data where trans_no = :trans_no", nativeQuery = true)
	List<TempMonthlyData> fetchCurrentRecord(@Param("trans_no") String trans_no);

	@Query(value = "SELECT CASE WHEN (a.average_unit_sold - tmd.unit_sold) BETWEEN -1 AND 1 THEN TRUE ELSE FALSE END AS Diff \r\n"
			+ "FROM (SELECT tmd.account_name, tmd.product_id, tmd.pincode, tmd.month_year, AVG(mdm.unit_sold) AS average_unit_sold\r\n"
			+ "FROM temp_monthly_data tmd\r\n"
			+ "INNER JOIN monthly_data_master mdm\r\n"
			+ "ON mdm.account_name = tmd.account_name AND mdm.product_id = tmd.product_id AND mdm.pincode = tmd.pincode\r\n"
			+ "WHERE STR_TO_DATE(CONCAT('01 ', mdm.month_year), '%d %M %Y') >= DATE_SUB(STR_TO_DATE(CONCAT('01 ', tmd.month_year), '%d %M %Y'), INTERVAL 3 MONTH)\r\n"
			+ "and tmd.trans_no = :generatedId\r\n"
			+ "GROUP BY tmd.account_name, tmd.product_id, tmd.pincode) as a\r\n"
			+ "INNER JOIN temp_monthly_data tmd\r\n"
			+ "ON a.account_name = tmd.account_name AND a.product_id = tmd.product_id AND a.pincode = tmd.pincode AND a.month_year = tmd.month_year",nativeQuery = true)
	Object[] getAvgData(@Param("generatedId") String generatedId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM temp_monthly_data WHERE trans_no = :generatedId", nativeQuery = true)
	void deleteData(@Param("generatedId") String generatedId);
}
