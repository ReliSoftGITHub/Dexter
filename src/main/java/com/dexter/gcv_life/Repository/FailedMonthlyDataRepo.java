package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.FailedMonthlyData;

public interface FailedMonthlyDataRepo extends JpaRepository<FailedMonthlyData, Integer>{
    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM failed_monthly_data WHERE product_name = :product_name", nativeQuery = true)
    List<FailedMonthlyData> getGcvProductIdAndProductName(@Param("product_name") String product_name);

    @Query(value = "SELECT count(sr_no) from failed_monthly_data", nativeQuery = true)
	BigInteger totalCount();

    @Transactional
    @Query(value = "SELECT * FROM failed_monthly_data LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
    List<FailedMonthlyData> getFailedMonthlyDataView(long recordsCount, long startIndex);

    @Transactional
    @Query(value = "SELECT pincode, month_year, unit_sold FROM failed_monthly_data WHERE product_id = :product_id limit 1", nativeQuery = true)
    List<Map<String,Object>> getPcMyUs(@Param("product_id") String product_id);



}
