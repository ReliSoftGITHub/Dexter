package com.dexter.gcv_life.Repository;

import com.dexter.gcv_life.Entity.AuditTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

//import javax.transaction.Transactional;

public interface AuditTrackerRepository extends JpaRepository<AuditTracker, Integer> {
    
    @Query(value = "SELECT max(last_row_id) FROM audit_tracker", nativeQuery = true)
    Long getLastRowIdAuditMaster();
}
