package com.bank.user;

import com.bank.generics.Roles;
import com.bank.generics.RolesMap;


public class Admin extends User {

  private static final long serialVersionUID = 1238393026153201217L;

  /**
   * The constructor of the administrator, initialize id, name, age and address.
   * @param id id of the administrator.
   * @param name name of the administrator.
   * @param age the age of the administrator.
   * @param address the address of the administrator.
   */
  public Admin(int id, String name, int age, String address) {
    this.setId(id);
    this.setAge(age); 
    this.setName(name);
    this.setAddress(address);
    //Get the role id of admin from the RolesMap
    this.setRoleId(RolesMap.getInstance().getId(Roles.ADMIN));
  }

  /**
   * The constructor of the administrator, initialize id, name, age and if it's authenticated.
   * @param id id of the administrator.
   * @param name name of the administrator.
   * @param age the age of the administrator.
   * @param address the address of the administrator.
   * @param authenticated true if the teller's password matches the teller's password, else false.
   */
  public Admin(int id, String name, int age, String address, boolean authenticated) {
    this.setId(id);
    this.setAge(age);
    this.setName(name);
    this.setAddress(address);
    this.setAuthenticated(authenticated);
    //Get the role id of admin from the RolesMap
    this.setRoleId(RolesMap.getInstance().getId(Roles.ADMIN));
  }
}
