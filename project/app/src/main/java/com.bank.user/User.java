package com.bank.user;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.security.PasswordHelpers;

import java.io.Serializable;

public abstract class User implements Serializable {

  private static final long serialVersionUID = 7144312272281986719L;
  /**
   * The userId of this user.
   */
  private int id;
  
  /**
   * The name of this user.
   */
  private String name;
  
  /**
   * The age of this user.
   */
  private int age;
  
  /**
   * The address of this user.
   */
  private String address;
  
  /**
   * The roldId of this user.
   */
  private int roleId;
  
  /**
   * The authenticated state of this user, default false.
   */
  private boolean authenticated = false;

  /**
   * Get the user's id
   * @return user's id.
   */
  public int getId() {
    return this.id;
  }
  
  /**
   * Get user's address.
   * @return user's address.
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * Get if user's id matches password.
   * @return true if it matches.
   */
  protected boolean getAuthenticated() {
    return this.authenticated;
  }
  
  /**
   * Get the name of the user.
   * @return the user's name.
   */
  public String getName() {
    return this.name;
  }
  
  /** Get the age of the user
   * @return user's age.
   */
  public int getAge() {
    return this.age;
  }
  
  /**
   * Get the role id of the user.
   * @return role id.
   */
  public int getRoleId() {
    return this.roleId;
  }
  
  /**
   * Set the role id.
   * @param id user's role id.
   */
  protected void setRoleId(int id) {
    this.roleId = id;
  }
  
  /**
   * Set the account id.
   * @param id user's id.
   */
  public void setId(int id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Set user's age.
   * @param age user's age
   */
  public void setAge(int age) {
    this.age = age;
  }
  
  /**
   * Set user's address.
   * @param address user's address.
   */
  protected void setAddress(String address) {
    this.address = address;
  }
  
  /**
   * Set if id matches password.
   * @param authenticated true if id matches password.
   */
  protected void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  /**
   * Check if password matches with the user id.
   * @param password the password of the user.
   * @return true if password matches the user id, else return false.
   */
  public final boolean authenticate(String password) {
    // If the given password is null, directly set the authenticate state to false
    if (password == null) {
      this.authenticated = false;
      return false;
    } 
    // Compare the password with the password in the database.
    //String datapassword = DatabaseSelectHelper.getPassword(this.getId());
    String datapassword = DatabaseDriverAndroidHelper.getInstance().getPassword(this.getId());
    this.authenticated = PasswordHelpers.comparePassword(datapassword, password);
    return this.authenticated;
  }
  
  @Override
  public String toString() {
    String rep = "";
    //Display user name
    rep += "Name: " + this.name + "\n";
    //Display address
    rep += "Address: " + this.address + "\n";
    return rep;
  }

}
