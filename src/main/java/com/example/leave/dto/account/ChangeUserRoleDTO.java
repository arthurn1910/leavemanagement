package com.example.leave.dto.account;

import com.example.leave.entity.account.AccessLevel;
import com.example.leave.entity.account.Account;

import java.util.Collection;

/**
 * Created by Medion on 2016-09-19.
 */
public class ChangeUserRoleDTO {
    private String login;
    private Boolean  roleManager;
    private Boolean  roleEmployee;
    private Boolean  roleAccountant;
    private Boolean  roleAdministrator;

    public ChangeUserRoleDTO() {
        this.roleAccountant=false;
        this.roleEmployee=false;
        this.roleAdministrator=false;
        this.roleManager=false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(Boolean roleManager) {
        this.roleManager = roleManager;
    }

    public Boolean getRoleEmployee() {
        return roleEmployee;
    }

    public void setRoleEmployee(Boolean roleEmployee) {
        this.roleEmployee = roleEmployee;
    }

    public Boolean getRoleAccountant() {
        return roleAccountant;
    }

    public void setRoleAccountant(Boolean roleAccountant) {
        this.roleAccountant = roleAccountant;
    }

    public Boolean getRoleAdministrator() {
        return roleAdministrator;
    }

    public void setRoleAdministrator(Boolean roleAdministrator) {
        this.roleAdministrator = roleAdministrator;
    }

    public void setRole(Account account){
        Collection<AccessLevel> accessLevelList=account.getAccessLevelCollection();
        String role="";
        for(AccessLevel accessLevel : accessLevelList){
            role=accessLevel.getLevel();
            if(role.equals("ROLE_MANAGER"))
                this.roleManager=true;
            else if(role.equals("ROLE_ADMINISTRATOR"))
                this.roleAdministrator=true;
            if(role.equals("ROLE_EMPLOYEE"))
                this.roleEmployee=true;
            if(role.equals("ROLE_ACCOUNTANT"))
                this.roleAccountant=true;
        }
    }
}
