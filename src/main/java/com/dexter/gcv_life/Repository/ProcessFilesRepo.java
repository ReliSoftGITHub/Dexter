package com.dexter.gcv_life.Repository;

import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dexter.gcv_life.Entity.ProcessFiles;

public interface ProcessFilesRepo extends JpaRepository<ProcessFiles, Integer>{

	@Transactional
	@Modifying
	@Query(value = "update process_files set status = 'Completed' where account_name = :account_name and file_name = :fileName and thread_count = :threadCount", nativeQuery = true)
	void updateStatus(@Param("account_name") String account_name,@Param("fileName") String fileName,@Param("threadCount") int threadCount);

	@Transactional
	@Modifying
	@Query(value = "update process_files set status = 'Failed' where account_name = :account_name and file_name = :fileName and thread_count = :threadCount", nativeQuery = true)
	void updateFailStatus(@Param("account_name") String account_name,@Param("fileName") String fileName,@Param("threadCount") int threadCount);

	@Transactional
	@Modifying
	@Query(value = "delete from process_files where status = 'Done' and account_name = :account_name and file_name = :fileName and thread_count = :threadCount", nativeQuery = true)
	void deleteExistingFail(@Param("account_name") String account_name,@Param("fileName") String fileName,@Param("threadCount") int threadCount);

	@Transactional
	@Modifying
	@Query(value = "select * from process_files where status IN ('Inprogress', 'Failed', 'Checking')", nativeQuery = true)
	List<ProcessFiles> getProcessFile();

	@Transactional
	@Modifying
	@Query(value = "delete from process_files where account_name = :account_name and status = :status and file_name = :file_name", nativeQuery = true)
	void updateFailedFile(@Param("file_name") String file_name,@Param("account_name") String account_name,@Param("status") String status);

}
