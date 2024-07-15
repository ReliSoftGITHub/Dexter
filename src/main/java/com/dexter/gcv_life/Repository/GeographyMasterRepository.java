package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import com.dexter.gcv_life.Entity.TerritoryMasterFinal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.GcvProductMaster;
import com.dexter.gcv_life.Entity.GeographyMaster;

public interface GeographyMasterRepository extends JpaRepository<GeographyMaster, Long> {

	@Transactional
	@Modifying
	@Query(value = "select * from geography_master where pincode = :pincode", nativeQuery = true)
	List<GeographyMaster> checkExistingData(@Param("pincode") String pincode);

	//----------- DROPDOWN -----------

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT office_name FROM geography_master", nativeQuery = true)
	List getDistinctOfficeName();

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT zone FROM geography_master WHERE zone IS NOT NULL AND TRIM(zone) <> '' ORDER BY zone ASC", nativeQuery = true)
	List getDistinctZone();

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT pincode FROM geography_master WHERE pincode IS NOT NULL AND TRIM(pincode) <> '' ORDER BY pincode ASC", nativeQuery = true)
	List getDistinctPincode();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT state_name FROM geography_master WHERE state_name IS NOT NULL AND TRIM(state_name) <> '' ORDER BY state_name ASC", nativeQuery = true)
	List getDistinctState();
	
	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT gcv_city FROM geography_master WHERE gcv_city IS NOT NULL AND TRIM(gcv_city) <> '' ORDER BY gcv_city ASC", nativeQuery = true)
	List getDistinctCity();

	// ---------- Methods for getting Data ----------

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE office_name = :office_name AND zone = :zone AND pincode = :pincode", nativeQuery = true)
	List<GeographyMaster> getGeographyMasterData(@Param("office_name") String office_name, @Param("zone") String zone, @Param("pincode") String pincode);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE office_name = :office_name AND zone = :zone", nativeQuery = true)
	List<GeographyMaster> getOfficeNameAndZone(@Param("office_name") String office_name, @Param("zone") String zone);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE zone = :zone AND pincode = :pincode", nativeQuery = true)
	List<GeographyMaster> getZoneAndPincode(@Param("zone") String zone, @Param("pincode") String pincode);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE office_name = :office_name AND pincode = :pincode", nativeQuery = true)
	List<GeographyMaster> getOfficeNameAndPincode(@Param("office_name") String office_name, @Param("pincode") String pincode);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE office_name = :office_name", nativeQuery = true)
	List<GeographyMaster> getOfficeName(@Param("office_name") String office_name);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE zone = :zone", nativeQuery = true)
	List<GeographyMaster> getZone(@Param("zone") String zone);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM geography_master WHERE pincode = :pincode", nativeQuery = true)
	List<GeographyMaster> getPincode(@Param("pincode") String pincode);

	@Transactional
	@Modifying
	@Query(value = "select * from geography_master where zone = :zone", nativeQuery = true)
	List<GeographyMaster> getDataDivisionBasedGeo(@Param("zone") String zone);
	
//	@Transactional
//	@Modifying
//	@Query(value = "SELECT * FROM geography_master where (:zone is null or zone like %:zone%) AND (:gcv_city is null or zone like %:gcv_city%) AND (:state_name is null or zone like %:state_name%) AND (:pincode is null or zone like %:pincode%) AND", nativeQuery = true)
//	List<GeographyMaster> getAllZonePinCodeStateCity(@Param("zone") String zone, @Param("gcv_city") String gcv_city, @Param("state_name") String state_name, @Param("pincode") String pincode);

	




	
	@Query(value = "SELECT zone FROM geography_master WHERE state_name = :state_name limit 1", nativeQuery = true)
	String getZoneNameOnStateBasis(@Param("state_name") String state_name);

	@Query(value = "SELECT * FROM geography_master WHERE"
			+ "(:officename is null or office_name like %:officename%)"
			+ "AND (:zonename is null or zone like %:zonename%)"
			+ "AND (:pincodename is null or pincode like %:pincodename%)"
			+ "LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<GeographyMaster> findAllData(@Param("recordsCount")long recordsCount, @Param("startIndex")long startIndex, String officename, String zonename, String pincodename);

	@Query(value = "SELECT count(row_id) from geography_master WHERE"
			+ "(:officename is null or office_name like %:officename%)"
			+ "AND (:zonename is null or zone like %:zonename%)"
			+ "AND (:pincodename is null or pincode like %:pincodename%)", nativeQuery = true)
	BigInteger totalCount(String officename, String zonename, String pincodename);

}
