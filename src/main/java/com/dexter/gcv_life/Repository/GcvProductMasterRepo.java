package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import com.dexter.gcv_life.Entity.AccountMaster;
import com.dexter.gcv_life.Entity.FieldMaster;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.GcvProductMaster;

public interface GcvProductMasterRepo extends JpaRepository<GcvProductMaster, Integer>{

	
	@Transactional
	@Modifying
	@Query(value = "select * from gcv_product_master "
			+ "where g_product_id = :g_product_id", nativeQuery = true)
	List<GcvProductMaster> checkGcvProdId(@Param("g_product_id") String g_product_id);

	
	@Transactional
	@Modifying
	@Query(value = "select * from gcv_product_master "
			+ "where sku_id = :sku_id and g_product_id = :g_product_id", nativeQuery = true)
	List<GcvProductMaster> checkExistingData(@Param("sku_id") String sku_id, @Param("g_product_id") String g_product_id);

	// Methods for Dropdown
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT account FROM gcv_product_master", nativeQuery = true)
	List getDistinctAccount();

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT g_product_name FROM gcv_product_master", nativeQuery = true)
	List getDistinctGProductName();

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT g_division FROM gcv_product_master", nativeQuery = true)
	List getDistinctGDivision();

	// Methods for getting Data
	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where account = :account AND g_product_name = :g_product_name AND g_division = :g_division", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterData(@Param("account") String account, @Param("g_product_name") String g_product_name, @Param("g_division") String g_division);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where account = :account AND g_product_name = :g_product_name", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterDataAccountAndGProductName(@Param("account") String account, @Param("g_product_name") String g_product_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where g_product_name = :g_product_name AND g_division = :g_division", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterDataGProductNameAndGDivision(@Param("g_product_name") String g_product_name, @Param("g_division") String g_division);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where account = :account AND g_division = :g_division", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterDataAccountAndGDivision(@Param("account") String account, @Param("g_division") String g_division);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where account = :account", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterDataAccount(@Param("account") String account);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where g_product_name = :g_product_name", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterDataGProductName(@Param("g_product_name") String g_product_name);
	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM gcv_product_master where g_division = :g_division", nativeQuery = true)
	List<GcvProductMaster> getGcvProductMasterDataGDivision(@Param("g_division") String g_division);

	// Get productId by account name of nearest match
	@Transactional
	@Query(value = "SELECT DISTINCT sku_id FROM gcv_product_master WHERE g_product_name = :g_product_name ORDER BY sku_id DESC LIMIT 1", nativeQuery = true)
	String getProductIdByAccountName(@Param("g_product_name") String g_product_name);

	@Transactional
	@Query(value = "SELECT DISTINCT g_division FROM gcv_product_master WHERE g_product_name = :g_product_name ORDER BY sku_id DESC LIMIT 1", nativeQuery = true)
	String getGDivisionByAccountName(@Param("g_product_name") String g_product_name);

	@Transactional
	@Query(value = "SELECT DISTINCT brand FROM gcv_product_master WHERE g_product_name = :g_product_name ORDER BY sku_id DESC LIMIT 1", nativeQuery = true)
	String getBrandByAccountName(@Param("g_product_name") String g_product_name);

	@Transactional
	@Modifying
	@Query(value = "select * from gcv_product_master where g_division = :g_division", nativeQuery = true)
	List<GcvProductMaster> getDataDivisionBasedGcvProd(@Param("g_division") String g_division);
	
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT brand FROM gcv_product_master WHERE brand IS NOT NULL AND TRIM(brand) <> '' ORDER BY brand ASC;\r\n"
			+ "", nativeQuery = true)
	List brandDistinct();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT g_product_name FROM gcv_product_master WHERE g_product_name IS NOT NULL AND TRIM(g_product_name) <> '' ORDER BY g_product_name ASC", nativeQuery = true)
	List SKUDistinct();


	@Query(value = "SELECT * FROM gcv_product_master WHERE"
			+ "(:accountname is null or account like %:accountname%)"
			+ "AND (:productname is null or g_product_name like %:productname%)"
			+ "AND (:divisionname is null or g_division like %:divisionname%)"
			+ "LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<GcvProductMaster> findAllData(@Param("recordsCount")long recordsCount, @Param("startIndex")long startIndex, String accountname, String productname, String divisionname);

	@Query(value = "SELECT count(row_id) from gcv_product_master WHERE"
			+ "(:accountname is null or account like %:accountname%)"
			+ "AND (:productname is null or g_product_name like %:productname%)"
			+ "AND (:divisionname is null or g_division like %:divisionname%)", nativeQuery = true)
	BigInteger totalCount(String accountname, String productname, String divisionname);

	@Transactional
	@Modifying
	@Query(value = "select distinct g_product_id from gcv_product_master where brand = :brand", nativeQuery = true)
	List<String> getDistinctGPID(@Param("brand") String brand);

}
