package com.bank.user;

import com.bank.generics.Roles;
import com.bank.generics.RolesMap;

public class Teller extends User {

  private static final long serialVersionUID = -8377093601860056600L;

  /**
   * The constructor of the teller, initialize id, name, age and address.
   * @param id id of the teller.
   * @param name name of the teller.
   * @param age the age of the teller.
   * @param address the address of the teller.
   */
  public Teller(int id, String name, int age, String address) {
    this.setId(id);
    this.setAge(age); 
    this.setName(name);
    this.setAddress(address);
    //Get the role id of teller from the RolesMap
    this.setRoleId(RolesMap.getInstance().getId(Roles.TELLER));
  }

  /**
   * The constructor of the teller, initialize id, name, age and if it's authenticated.
   * @param id id of the teller.
   * @param name name of the teller.
   * @param age the age of the teller.
   * @param address the address of the teller.
   * @param authenticated true if the teller's password matches the teller's password, else false.
   */
  public Teller(int id, String name, int age, String address, boolean authenticated) {
    this.setId(id);
    this.setAge(age);
    this.setName(name);
    this.setAddress(address);
    this.setAuthenticated(authenticated);
    //Get the role id of teller from the RolesMap
    this.setRoleId(RolesMap.getInstance().getId(Roles.TELLER));
  }
}
