package com.dexter.gcv_life.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dexter.gcv_life.Entity.EmailData;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Integer> {
	
	@Query(value = "SELECT * FROM email_data ORDER BY id DESC LIMIT 1", nativeQuery = true)
	EmailData findUser();
	
}
