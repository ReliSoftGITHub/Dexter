package com.dexter.gcv_life.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.MonthlyDataMaster;

public interface MonthlyDataMasterRepo extends JpaRepository<MonthlyDataMaster, Integer>, JpaSpecificationExecutor<MonthlyDataMaster> {

	@Transactional
	@Modifying
	@Query(value = "CALL insert_or_update_monthly_data(:account_name, :productId, :productName, \r\n"
			+ ":unit, :pincode, :monthAndYear);", nativeQuery = true)
	void insertOrUpdateData(@Param("account_name") String account_name, @Param("productId") String productId,
			@Param("productName") String productName, @Param("unit") int unit, @Param("pincode") String pincode,
			@Param("monthAndYear") String monthAndYear);

	@Transactional
	@Modifying
	@Query(value = "CALL insert_data_for_checking(:account_name, :productId, :productName, \r\n"
			+ ":unit, :pincode, :monthAndYear, :transNo);", nativeQuery = true)
	void insertDataForChecking(@Param("account_name") String account_name, @Param("productId") String productId,
			@Param("productName") String productName, @Param("unit") int unit, @Param("pincode") String pincode,
			@Param("monthAndYear") String monthAndYear, @Param("transNo") String transNo);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS product_exists\r\n"
			+ "FROM gcv_product_master\r\n" + "WHERE sku_id = :productId ", nativeQuery = true)
//			+ "and g_product_id in ('000000000000002112','000000000000002011','000000000000002081','000000000000002533','000000000000001607','000000000000002144','000000000000002421','000000000000002598','000000000000002955','000000000000001171','000000000000001921','000000000000002085','000000000000002221','000000000000003029','000000000000002069','000000000000000884','000000000000001710','000000000000002935','000000000000002928','000000000000001391','000000000000002169','000000000000002246','000000000000002453','000000000000000086','000000000000001202','000000000000001249','000000000000001818','000000000000001821','000000000000001947','000000000000003057','000000000000001875','000000000000002080','000000000000002149','000000000000002940','000000000000002495','000000000000002515','000000000000002942','000000000000002945','000000000000002944','000000000000002958','000000000000002060','000000000000002191','000000000000002929','000000000000002931')", nativeQuery = true)
	BigInteger checkProduct(@Param("productId") String productId);

	@Query(value = "SELECT DISTINCT account_name, month_year FROM monthly_data_master WHERE month_year BETWEEN :startMonthYear AND :endMonthYear", nativeQuery = true)
	List<Object[]> getNotifyData(@Param("startMonthYear") String startMonthYear,
			@Param("endMonthYear") String endMonthYear);

	@Query(nativeQuery = true, value = "CALL viewDataMaster(:startYear, :startIndex , :recordsCount)")
	List<Object[]> callViewDataMaster(@Param("startYear") int startYear, long startIndex, long recordsCount);

	@Query(value = "SELECT count(row_count) AS total_rows\r\n"
			+ "FROM ( SELECT COUNT(*) AS row_count FROM monthly_data_master GROUP BY account_name, product_id, g_product_id, pincode ) AS subquery;", nativeQuery = true)
	BigInteger monthlyDataTotalCount();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM monthly_data_master WHERE division = :division", nativeQuery = true)
	List<MonthlyDataMaster> getDivisionRoleBasedMonthlyData(@Param("division") String division);

	// JSON Geography
	@Query(nativeQuery = true, value = "CALL GeographyFilter(:division,:zoneParam,:stateParam,:cityParam,:pincodeParam)")
	List<MonthlyDataMaster> geographyFilterProcedure(@Param("division") String division,
			@Param("zoneParam") String zoneParam, @Param("stateParam") String stateParam,
			@Param("cityParam") String cityParam, @Param("pincodeParam") String pincodeParam);

	// JSON Geography
	@Query(nativeQuery = true, value = "CALL BrandsFilter(:groupParam,:subGroupParam,:existingNewSubgroup,:brandParam,:brandGroupParam,:SKUProductNameParam)")
	List<MonthlyDataMaster> brandsFilterProcedure(@Param("groupParam") String groupParam,
			@Param("subGroupParam") String subGroupParam, @Param("existingNewSubgroup") String existingNewSubgroup,
			@Param("brandParam") String brandParam, @Param("brandGroupParam") String brandGroupParam,
			@Param("SKUProductNameParam") String SKUProductNameParam);

	@Query(nativeQuery = true, value = "CALL MRP(:subGroupParam,:groupParam,:existingNewSubgroup,:brandParam,:brandGroupParam,:SKUProductNameParam,:division,:zoneParam,:stateParam,:cityParam,:pincodeParam,:month,:accountParam,:accountName)")
	List<Object[]> mrpProcedure( 
			@Param("subGroupParam") String subGroupParam,
			@Param("groupParam") String groupParam,
			@Param("existingNewSubgroup") String existingNewSubgroup,
			@Param("brandParam") String brandParam,
			@Param("brandGroupParam") String brandGroupParam,
			@Param("SKUProductNameParam") String SKUProductNameParam,
			@Param("division") String division, 
			@Param("zoneParam") String zoneParam,
			@Param("stateParam") String stateParam,
			@Param("cityParam") String cityParam,
			@Param("pincodeParam") String pincodeParam, 
			@Param("month") String month,
			@Param("accountParam") String accountParam,
			@Param("accountName") String accountName);

	@Query(nativeQuery = true, value = "CALL PTS(:groupParam,:subGroupParam,:existingNewSubgroup,:brandParam,:brandGroupParam,:SKUProductNameParam,:division,:zoneParam,:stateParam,:cityParam,:pincodeParam,:month,:accountParam,:accountName)")
	List<Object[]> ptsProcedure( 
			@Param("subGroupParam") String subGroupParam,
			@Param("groupParam") String groupParam,
			@Param("existingNewSubgroup") String existingNewSubgroup,
			@Param("brandParam") String brandParam,
			@Param("brandGroupParam") String brandGroupParam,
			@Param("SKUProductNameParam") String SKUProductNameParam,
			@Param("division") String division, 
			@Param("zoneParam") String zoneParam,
			@Param("stateParam") String stateParam,
			@Param("cityParam") String cityParam,
			@Param("pincodeParam") String pincodeParam, 
			@Param("month") String month,
			@Param("accountParam") String accountParam,
			@Param("accountName") String accountName);

	@Query(nativeQuery = true, value = "CALL PTR(:groupParam,:subGroupParam,:existingNewSubgroup,:brandParam,:brandGroupParam,:SKUProductNameParam,:division,:zoneParam,:stateParam,:cityParam,:pincodeParam,:month,:accountParam,:accountName)")
	List<Object[]> ptrProcedure( 
			@Param("subGroupParam") String subGroupParam,
			@Param("groupParam") String groupParam,
			@Param("existingNewSubgroup") String existingNewSubgroup,
			@Param("brandParam") String brandParam,
			@Param("brandGroupParam") String brandGroupParam,
			@Param("SKUProductNameParam") String SKUProductNameParam,
			@Param("division") String division, 
			@Param("zoneParam") String zoneParam,
			@Param("stateParam") String stateParam,
			@Param("cityParam") String cityParam,
			@Param("pincodeParam") String pincodeParam, 
			@Param("month") String month,
			@Param("accountParam") String accountParam,
			@Param("accountName") String accountName);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM monthly_data_master WHERE pincode = :pincode", nativeQuery = true)
	List<MonthlyDataMaster> getPinCodeBasedMonthlyData(@Param("pincode") String pincode);

	// without month and brand
	@Transactional
	@Modifying
	@Query(value = "SELECT brand, month_year, sum(unit_sold) as totalSale FROM monthly_data_master mdm  WHERE mdm.month_year = :month group by month_year order by totalSale", nativeQuery = true)
	List<Object[]> initialCall(@Param("month") String month);

	// only month
	@Transactional
	@Modifying
	@Query(value = "SELECT brand, month_year, sum(unit_sold) as totalSale FROM monthly_data_master mdm  WHERE mdm.month_year IN (:month) group by month_year order by totalSale", nativeQuery = true)
	List<Object[]> onlyMonth(@Param("month") String month);

	// both month and brand
	@Transactional
	@Modifying
	@Query(value = "SELECT brand, month_year, sum(unit_sold) as totalSale FROM monthly_data_master mdm WHERE mdm.month_year IN (:month is null or mdm.month_year like %:month%) AND (:brand IS NULL OR mdm.brand IN (:brand)) group by mdm.brand, mdm.month_year order by totalSale", nativeQuery = true)
	List<Object[]> bothMonthAndBrnad(@Param("brand") String brand, @Param("month") String month);

	@Query(value = "CALL Bubble(:monthParam,:brandParam)", nativeQuery = true)
	List<Object[]> bubbleChart(@Param("monthParam") String monthParam, @Param("brandParam") String brandParam);
	
//	@Query(value = "SELECT SUM(unit_sold) AS totalUnitSold FROM monthly_data_master mdm WHERE \r\n"
//			+ "(:prevmonth IS NULL OR mdm.month_year = :prevmonth) \r\n"
//			+ "AND (:brand IS NULL OR mdm.brand = :brand)\r\n"
//			+ "GROUP BY mdm.brand, mdm.month_year ORDER BY totalUnitSold;",nativeQuery = true)
//	BigDecimal getMonthSale(@Param("prevmonth") String prevmonth, @Param("brand") String brand);
	
	@Query(value = "SELECT \r\n"
			+ "COALESCE ((SELECT SUM(unit_sold) AS totalUnitSold FROM monthly_data_master mdm \r\n"
			+ "WHERE (:currmonth IS NULL OR mdm.month_year = :currmonth) AND (:brand IS NULL OR mdm.brand = :brand)\r\n"
			+ "GROUP BY mdm.brand, mdm.month_year ORDER BY totalUnitSold), 1) as  totalUnitSold1,\r\n"
			+ "COALESCE ((SELECT SUM(unit_sold) AS totalUnitSold FROM monthly_data_master mdm \r\n"
			+ "WHERE (:prevmonth IS NULL OR mdm.month_year = :prevmonth) AND (:brand IS NULL OR mdm.brand = :brand)\r\n"
			+ "GROUP BY mdm.brand, mdm.month_year ORDER BY totalUnitSold), 1) as  totalUnitSold2,\r\n"
			+ "COALESCE ((SELECT COUNT(distinct pincode) FROM monthly_data_master WHERE brand = :brand), 1) as storeReach",nativeQuery = true)
	List<Object[]> getMonthSale(@Param("currmonth") String currmonth, @Param("prevmonth") String prevmonth, @Param("brand") String brand);
	
	@Transactional
	@Modifying
	@Query(value = "SELECT COUNT(distinct pincode) FROM monthly_data_master WHERE brand = :brand",nativeQuery = true)
	List storeReach(@Param("brand") String brand);

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT brand FROM monthly_data_master WHERE brand IS NOT NULL AND TRIM(brand) <> '' ORDER BY brand ASC", nativeQuery = true)
	List brandDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT g_product_name FROM monthly_data_master WHERE g_product_name IS NOT NULL AND TRIM(g_product_name) <> '' ORDER BY g_product_name ASC", nativeQuery = true)
	List SKUDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT division FROM monthly_data_master mdm WHERE division IS NOT NULL AND division != '' ORDER BY division ASC", nativeQuery = true)
	List divisionDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT `zone` FROM monthly_data_master mdm WHERE `zone` IS NOT NULL AND `zone` != '' ORDER BY `zone` ASC", nativeQuery = true)
	List zonedDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT state FROM monthly_data_master mdm WHERE state IS NOT NULL AND state != '' ORDER BY state ASC", nativeQuery = true)
	List stateDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT city FROM monthly_data_master mdm WHERE city IS NOT NULL AND city != '' ORDER BY city ASC", nativeQuery = true)
	List cityDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT pincode FROM monthly_data_master mdm WHERE pincode IS NOT NULL AND pincode != '' ORDER BY pincode ASC", nativeQuery = true)
	List pincodeDistinct();
	
}
