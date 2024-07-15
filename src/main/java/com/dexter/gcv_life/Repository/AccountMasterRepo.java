package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.AccountMaster;

public interface AccountMasterRepo extends JpaRepository<AccountMaster, Integer>{

	
	@Query(value = "SELECT * FROM account_master WHERE "
			+ "(:account is null or account like %:account%)"
			+ "AND (:product_name is null or product_name like %:product_name%)"
			+ "AND (:gcv_product is null or gcv_product_name like %:gcv_product%)"
			+ "LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<AccountMaster> findAllData(@Param("recordsCount")long recordsCount, @Param("startIndex")long startIndex, String account, String product_name, String gcv_product);
	
	@Query(value = "SELECT count(row_id) from account_master WHERE"
			+ "(:account is null or account like %:account%)"
			+ "AND (:product_name is null or product_name like %:product_name%)"
			+ "AND (:gcv_product is null or gcv_product_name like %:gcv_product%)", nativeQuery = true)
	BigInteger totalCount(String account, String product_name, String gcv_product);
	
	@Transactional
	@Modifying
	@Query(value = "select * from account_master "
			+ "where keey = :keey and product_id = :product_id and gcv_product_id = :gcv_product_id"
			, nativeQuery = true)
	List<AccountMaster> checkExistingData(@Param("keey")String keey, @Param("product_id")String product_id, @Param("gcv_product_id")String gcv_product_id);

	@Query(value = "SELECT type FROM account_master WHERE account = :account LIMIT 1", nativeQuery = true)
	List<String> findTypeByAccount(@Param("account")String account);

	@Query(value = "SELECT gcv_product_id AND gcv_product_name FROM account_master WHERE product_id = :product_id", nativeQuery = true)
	List<HashMap<Object, String>> findGcvProductIdAndGcvProductNameByProductId(@Param("product_id") String product_id);

	
	@Query(value = "SELECT\r\n"
			+ "    CASE\r\n"
			+ "        WHEN gcv_pincode = :pincode THEN gcv_pincode\r\n"
			+ "        WHEN raw_pincode = :pincode THEN gcv_pincode\r\n"
			+ "        ELSE NULL\r\n"
			+ "    END AS matched_pincode\r\n"
			+ "FROM account_master\r\n"
			+ "WHERE gcv_pincode = :pincode OR raw_pincode = :pincode ORDER BY matched_pincode DESC LIMIT 1;", nativeQuery = true)
	String getPincode(@Param("pincode") String pincode);

	@Query(value = "select gcv_pincode from account_master where store_id = :clubNbrIn ORDER BY store_id DESC LIMIT 1;", nativeQuery = true)
	String getPincodeFromStoreId(@Param("clubNbrIn") String clubNbrIn);

	// all fileds filled
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where account = :account AND product_name = :product_name AND gcv_product_name = :gcv_product_name", nativeQuery = true)
	List<AccountMaster> getAccountMasterData(@Param("account") String account, @Param("product_name") String product_name,
			@Param("gcv_product_name") String gcv_product_name);

	// account and product_name filled
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where account = :account AND product_name = :product_name", nativeQuery = true)
	List<AccountMaster> AccountandProductName(@Param("account") String account, @Param("product_name") String product_name);

	// account and gcv_product_name
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where account = :account AND gcv_product_name = :gcv_product_name", nativeQuery = true)
	List<AccountMaster> AccountandGcvProductName(@Param("account") String account, @Param("gcv_product_name") String gcv_product_name);

	// product_name and gcv_product_name
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where product_name = :product_name AND gcv_product_name = :gcv_product_name", nativeQuery = true)
	List<AccountMaster> ProductNameandGcvProductName(@Param("product_name") String product_name, @Param("gcv_product_name") String gcv_product_name);

	// only account
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where account = :account", nativeQuery = true)
	List<AccountMaster> onlyAccount(@Param("account") String account);

	// only product_name
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where product_name = :product_name", nativeQuery = true)
	List<AccountMaster> onlyProductName(@Param("product_name") String product_name);

	// only gcv_product_name
	@Transactional
	@Modifying
	@Query(value = "select * from account_master where gcv_product_name = :gcv_product_name", nativeQuery = true)
	List<AccountMaster> onlyGcvProductName(@Param("gcv_product_name") String gcv_product_name);

	// account DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT account FROM account_master ORDER BY account ASC", nativeQuery = true)
	List accountDistinct();

	// product_name DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT product_name FROM account_master ORDER BY product_name ASC", nativeQuery = true)
	List productNameDistinct();

	// gcv_product_name DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT gcv_product_name FROM account_master ORDER BY gcv_product_name ASC", nativeQuery = true)
	List gcvProductNameDistinct();

	@Transactional
	@Modifying
	@Query(value = "select COUNT(distinct gcv_pincode) from account_master where gcv_product_id = :g_product_id", nativeQuery = true)
	int countDistinctPinCode(@Param("g_product_id") String g_product_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM account_master LIMIT :start OFFSET :end", nativeQuery = true)
	List<AccountMaster> limitRecords(@Param("start") int start,@Param("end") int end);
	
	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM account_master", nativeQuery = true)
	List<AccountMaster> allRecords();	
}
