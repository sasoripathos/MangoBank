package com.bank.user;

import com.bank.account.Account;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;

import java.util.ArrayList;
import java.util.List;


public class Customer extends User {
  private static final long serialVersionUID = -7614766210740141767L;
  /**
   * The list of all accounts of the customer.
   */
  private List<Account> account = new ArrayList<>();
  
  /**
   * The constructor of the customer, initialize id, name and age.
   * @param id id of the customer.
   * @param name name of the customer.
   * @param age the age of the customer.
   * @param address the address of the customer.
   */
  public Customer(int id, String name, int age, String address) {
    this.setId(id);
    this.setAge(age); 
    this.setName(name);
    this.setAddress(address);
    //Get the role id of customer from the RolesMap
    this.setRoleId(RolesMap.getInstance().getId(Roles.CUSTOMER));
  }

  /**
   * The constructor of the customer, initialize id, name, age and if it's authenticated.
   * @param id id of the customer.
   * @param name name of the customer.
   * @param age the age of the customer.
   * @param address the address of the customer.
   * @param authenticated true if the customer's password matches the teller's password, else false.
   */
  public Customer(int id, String name, int age, String address, boolean authenticated) {
    this.setId(id);
    this.setAge(age);
    this.setName(name);
    this.setAddress(address);
    this.setAuthenticated(authenticated);
    //Get the role id of customer from the RolesMap
    this.setRoleId(RolesMap.getInstance().getId(Roles.CUSTOMER));
  }
  
  /**
   * Get all of the accounts associated with the user.
   * @return List of Account associated with the user.
   */
  public List<Account> getAccounts() {
    return this.account;
  }
  
  /**
   * Add an account to the user.
   * @param account the account that will be added to the user.
   */
  public void addAccount(Account account) {
    this.account.add(account);
  }
  
//  @Override
//  public String toString() {
//    //Get the user name and address
//    String rep = super.toString();
//    for (Account account: this.account) {
//      rep += account.toString();
//    }
//    return rep;
//  }
}

  
