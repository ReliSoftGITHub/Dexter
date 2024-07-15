package com.dexter.gcv_life.Repository;

import java.math.BigInteger;
import java.util.List;

//import javax.transaction.Transactional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dexter.gcv_life.Entity.AccountMaster;
import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Entity.NewUsersDetails;

@Repository
public interface NewUserDetailsRepository extends JpaRepository<NewUsersDetails, Long> {
    boolean existsByEmailId(String email);

    boolean existsByMobileNo(String mobileNo);
    boolean existsByUsername(String username);
    
    @Query(value = "SELECT * FROM new_users_details WHERE user_id = :user_id" , nativeQuery = true)
    NewUsersDetails findByUserId(@Param("user_id") Long user_id);
        
    @Query(value = "SELECT * FROM new_users_details" , nativeQuery = true )
    List<NewUsersDetails> findAllActiveUser();
    
    @Query(value = "SELECT user_name FROM new_users_details" , nativeQuery = true)
    List findByUserName();
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE new_users_details set password = :password where user_id = :user_id" , nativeQuery = true)
    void updatePassword(@Param("password") String password, @Param("user_id") Long user_id);
    
    @Query(value = "SELECT * FROM new_users_details WHERE role_id_fk = :role_id_fk AND user_name = :user_name" , nativeQuery = true )
    List<NewUsersDetails> findRoleAndUserNames(@Param("role_id_fk") Integer role_id_fk, @Param("user_name") String user_name);

    @Query(value = "SELECT * FROM new_users_details WHERE role_id_fk = :role_id_fk" , nativeQuery = true )
    List<NewUsersDetails> findOnlyRole(@Param("role_id_fk") Integer role_id_fk);
    
    @Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT user_name FROM new_users_details ORDER BY user_name ASC", nativeQuery = true)
	List usernameDistinct();
    
    @Query(value = "SELECT * FROM new_users_details WHERE user_name = :user_name" , nativeQuery = true)
    NewUsersDetails findUserName(@Param("user_name") String user_name);

   // @Query(value = "SELECT * FROM new_users_details WHERE user_id = :user_id" , nativeQuery = true)
   // List<NewUsersDetails> findByUserId(@Param("user_id") int user_id);

    @Query(value = "select count(user_id) from new_users_details fm where user_name = :employeeId" , nativeQuery = true)
	BigInteger getByUserName(String employeeId);

    @Query(value = "SELECT count(user_id) from new_users_details WHERE"
    		+ "(:userrole is null or role_id_fk like %:userrole%)"
			+ "AND (:employeeid is null or user_name like %:employeeid%)", nativeQuery = true)
	BigInteger totalCount(String userrole, String employeeid);

    @Query(value = "SELECT * FROM new_users_details WHERE "
    		+ "(:userrole is null or role_id_fk like %:userrole%)"
			+ "AND (:employeeid is null or user_name like %:employeeid%)"
    		+ " LIMIT :recordsCount OFFSET :startIndex", nativeQuery = true)
	List<NewUsersDetails> findAllData(long recordsCount, long startIndex, String userrole, String employeeid);

}
