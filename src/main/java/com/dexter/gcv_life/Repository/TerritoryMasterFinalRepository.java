package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.GcvProductMaster;
import com.dexter.gcv_life.Entity.TerritoryMasterFinal;

public interface TerritoryMasterFinalRepository extends JpaRepository<TerritoryMasterFinal, String> {

	
	@Transactional
	@Modifying
	@Query(value = "select * from territory_master_final "
			+ "where sap_code = :sap_code"
			, nativeQuery = true)
	List<TerritoryMasterFinal> checkExistingData(@Param("sap_code")String sap_code);

	//----------- DROPDOWN -----------

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT division FROM territory_master_final", nativeQuery = true)
	List getDistinctDivision();

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT territory_name FROM territory_master_final", nativeQuery = true)
	List getDistinctTerritoryName();

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT zone_name FROM territory_master_final", nativeQuery = true)
	List getDistinctZoneName();

	// ---------- Methods for getting Data ----------

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE division = :division AND territory_name = :territory_name AND zone_name = :zone_name", nativeQuery = true)
	List<TerritoryMasterFinal> getTerritoryMasterData(@Param("division") String division, @Param("territory_name") String territory_name, @Param("zone_name") String zone_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE division = :division AND territory_name = :territory_name", nativeQuery = true)
	List<TerritoryMasterFinal> getDivisionAndTerritoryName(@Param("division") String division, @Param("territory_name") String territory_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE division = :division AND zone_name = :zone_name", nativeQuery = true)
	List<TerritoryMasterFinal> getDivisionAndZoneName(@Param("division") String division, @Param("zone_name") String zone_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE territory_name = :territory_name AND zone_name = :zone_name", nativeQuery = true)
	List<TerritoryMasterFinal> getTerritoryNameAndZoneName(@Param("territory_name") String territory_name, @Param("zone_name") String zone_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE division = :division", nativeQuery = true)
	List<TerritoryMasterFinal> getDivisionOny(@Param("division") String division);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE territory_name = :territory_name", nativeQuery = true)
	List<TerritoryMasterFinal> getTerritoryNameOnly(@Param("territory_name") String territory_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE zone_name = :zone_name", nativeQuery = true)
	List<TerritoryMasterFinal> getZoneNameOnly(@Param("zone_name") String zone_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM territory_master_final WHERE division = :division", nativeQuery = true)
	List<TerritoryMasterFinal> getDivisionRoleBasedData(@Param("division") String division);

	@Query(value = "SELECT * FROM territory_master_final WHERE"
			+ "(:divisionname is null or division like %:divisionname%)"
			+ "AND (:territoryname is null or territory_name like %:territoryname%)"
			+ "AND (:zonename is null or zone_name like %:zonename%)"
			+ "LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<TerritoryMasterFinal> findAllData(@Param("recordsCount")long recordsCount, @Param("startIndex")long startIndex, String divisionname, String territoryname, String zonename);

	@Query(value = "SELECT count(row_id) from territory_master_final WHERE"
			+ "(:divisionname is null or division like %:divisionname%)"
			+ "AND (:territoryname is null or territory_name like %:territoryname%)"
			+ "AND (:zonename is null or zone_name like %:zonename%)", nativeQuery = true)
	BigInteger totalCount(String divisionname, String territoryname, String zonename);

}
