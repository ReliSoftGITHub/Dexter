package com.dexter.gcv_life.Repository;

import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Entity.NewUsersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LogInRepository extends JpaRepository<LoginTable, Long> {

    @Query("SELECT u.password AS password, u.roleIdFk AS roleIdFk, u.flag AS flag FROM LoginTable u WHERE u.username = :username")
    LoginInfo findPasswordAndRoleIdFkByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM login_table WHERE user_id_fk = :user_id_fk" , nativeQuery = true)
    LoginTable updateUserLoginTable(@Param("user_id_fk") Long user_id_fk);

    Optional<LoginTable> findByUsername(String username);
    
    @Query(value = "SELECT * FROM login_table WHERE username = :username" , nativeQuery = true)
    LoginTable findUser(@Param("username") String username);

    @Query(value = "SELECT * FROM login_table WHERE user_id_fk = :user_id_fk" , nativeQuery = true)
    List<LoginTable> findByUserId(@Param("user_id_fk") int user_id_fk);

}
