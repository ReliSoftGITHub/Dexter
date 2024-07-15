package com.dexter.gcv_life.Repository;

public interface LoginInfo {
    String getPassword();
    Long getRoleIdFk();
    Integer getIsActive();

    int getFlag();
}
