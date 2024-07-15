package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.PriceMaster;

public interface PriceMasterRepo extends JpaRepository<PriceMaster, Integer> {

	@Transactional
	@Modifying
	@Query(value = "select * from price_master where sku_id = :sku_id and g_product_id = :g_product_id", nativeQuery = true)
	List<PriceMaster> checkExistingData(@Param("sku_id") String sku_id, @Param("g_product_id") int g_product_id);

	
	// all fileds filled
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where sku_name = :sku_name AND g_product_name = :g_product_name AND  sku_id = :sku_id", nativeQuery = true)
	List<PriceMaster> getPriceMasterData(@Param("sku_name") String sku_name, @Param("g_product_name") String g_product_name, @Param("sku_id") String sku_id);

	
	// sku_name and g_product_name filled
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where sku_name = :sku_name AND g_product_name = :g_product_name", nativeQuery = true)
	List<PriceMaster> skuNameAndGproductName(@Param("sku_name") String sku_name, @Param("g_product_name") String g_product_name);

	
	// sku_name and sku_id
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where sku_name = :sku_name AND sku_id = :sku_id", nativeQuery = true)
	List<PriceMaster> sku_nameAndsku_id(@Param("sku_name") String sku_name, @Param("sku_id") String sku_id);

	
	// g_product_name and sku_id
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where g_product_name = :g_product_name AND sku_id = :sku_id", nativeQuery = true)
	List<PriceMaster> g_product_nameAndsku_id(@Param("g_product_name") String g_product_name, @Param("sku_id") String sku_id);

	
	// only sku_name
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where sku_name = :sku_name", nativeQuery = true)
	List<PriceMaster> onlySku_name(@Param("sku_name") String sku_name);

	
	// only g_product_name
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where g_product_name = :g_product_name", nativeQuery = true)
	List<PriceMaster> onlyG_product_name(@Param("g_product_name") String g_product_name);

	
	// only sku_id
	@Transactional
	@Modifying
	@Query(value = "select * from price_master where sku_id = :sku_id", nativeQuery = true)
	List<PriceMaster> onlySku_id(@Param("sku_id") String sku_id);

	
	// sku_name DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT sku_name FROM price_master ORDER BY sku_name ASC", nativeQuery = true)
	List sku_nameDistinct();

	
	// g_product_name DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT g_product_name FROM price_master ORDER BY g_product_name ASC", nativeQuery = true)
	List g_product_nameDistinct();

	
	// sku_id DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT sku_id FROM price_master ORDER BY sku_id ASC", nativeQuery = true)
	List sku_idDistinct();


//	Optional<List<PriceMaster>> findByGProductId(int gProductId);

	@Query(value = "SELECT * FROM price_master WHERE g_product_id = :g_product_id", nativeQuery = true)
	List<PriceMaster> getData(@Param("g_product_id") String g_product_id);

	@Transactional
	@Modifying
	@Query(value = "select * from price_master where g_division = :g_division", nativeQuery = true)
	List<PriceMaster> getDataDivisionBased(@Param("g_division") String g_division);

	@Query(value = "SELECT * FROM price_master WHERE"
			+ "(:skuname is null or sku_name like %:skuname%)"
			+ "AND (:gproductname is null or g_product_name like %:gproductname%)"
			+ "AND (:skuid is null or sku_id like %:skuid%)"
			+ "LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<PriceMaster> findAllData(@Param("recordsCount")long recordsCount, @Param("startIndex")long startIndex, String skuname, String gproductname, String skuid);

	@Query(value = "SELECT count(row_id) from price_master WHERE"
			+ "(:skuname is null or sku_name like %:skuname%)"
			+ "AND (:gproductname is null or g_product_name like %:gproductname%)"
			+ "AND (:skuid is null or sku_id like %:skuid%)", nativeQuery = true)
	BigInteger totalCount(String skuname, String gproductname, String skuid);

}
