package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.FieldMaster;
import com.dexter.gcv_life.Entity.GcvProductMaster;

public interface FieldMasterRepo extends JpaRepository<FieldMaster, Integer>{

	
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where login_id = :login_id", nativeQuery = true)
	List<FieldMaster> checkExistingData(@Param("login_id")String login_id);

	
	// all fileds filled
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where division = :division AND zone_name = :zone_name AND territory_code = :territory_code", nativeQuery = true)
	List<FieldMaster> getFieldMasterData(@Param("division") String division, @Param("zone_name") String zone_name, @Param("territory_code") String territory_code);
	
	// division and zone filled
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where division = :division AND zone_name = :zone_name", nativeQuery = true)
	List<FieldMaster> DandZ(@Param("division") String division, @Param("zone_name") String zone_name);
	
	// division and territory_code
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where division = :division AND territory_code = :territory_code", nativeQuery = true)
	List<FieldMaster> DandT(@Param("division") String division, @Param("territory_code") String territory_code);
	
	// zone_name and territory_code
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where zone_name = :zone_name AND territory_code = :territory_code", nativeQuery = true)
	List<FieldMaster> ZandT(@Param("zone_name") String zone_name, @Param("territory_code") String territory_code);

	// only division
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where division = :division", nativeQuery = true)
	List<FieldMaster> onlyD(@Param("division") String division);
	
	// only zone_name
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where zone_name = :zone_name", nativeQuery = true)
	List<FieldMaster> onlyZ(@Param("zone_name") String zone_name);
	
	// only territory_code
	@Transactional
	@Modifying
	@Query(value = "select * from field_master where territory_code = :territory_code", nativeQuery = true)
	List<FieldMaster> onlyT(@Param("territory_code") String territory_code);
	
	// division DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT division FROM field_master ORDER BY division ASC", nativeQuery = true)
	List divisionDistinct();
	
	// zone_name DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT zone_name FROM field_master ORDER BY zone_name ASC", nativeQuery = true)
	List zoneDistinct();

	// territory_code DISTINCT
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT territory_code FROM field_master ORDER BY territory_code ASC", nativeQuery = true)
	List territoryDistinct();

	@Query(value = "SELECT max(row_id) FROM field_master", nativeQuery = true)
	Long getLastRowIdFieldMaster();
	
	@Query(value = "SELECT * FROM field_master WHERE"
			+ "(:divisionname is null or division like %:divisionname%)"
			+ "AND (:zonename is null or zone_name like %:zonename%)"
			+ "AND (:territorycodename is null or territory_code like %:territorycodename%)"
			+ "LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<FieldMaster> findAllData(@Param("recordsCount")long recordsCount, @Param("startIndex")long startIndex, String divisionname, String zonename, String territorycodename);

	@Query(value = "SELECT count(row_id) from field_master WHERE"
			+ "(:divisionname is null or division like %:divisionname%)"
			+ "AND (:zonename is null or zone_name like %:zonename%)"
			+ "AND (:territorycodename is null or territory_code like %:territorycodename%)", nativeQuery = true)
	BigInteger totalCount(String divisionname, String zonename, String territorycodename);

	@Query(value = "SELECT * from field_master WHERE row_id = :i", nativeQuery = true)
	List<FieldMaster> getEmployeeData(int i);


}
