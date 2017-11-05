package com.bank.validation;

import com.bank.generics.RolesMap;

public class UserValidatorPc implements UserValidator {
  /**
   * The boolean which stands for whether all checks have passed, default true.
   */
  private boolean result;
  
  /**
   * Constructor.
   */
  public UserValidatorPc() {
    //Set this.result to default value (true)
    this.result = true;
  }

  @Override
  public UserValidator checkName(String name) {
    //If the given name is NULL or empty string, this check fails
    if (name == null || name.equals("")) {
      this.result = false;
    }
    return this;
  }

  @Override
  public UserValidator checkAge(int age) {
    //If the given age is negative, this check fails
    if (age < 0) {
      this.result = false;
    }
    return this;
  }

  @Override
  public UserValidator checkAddress(String address) {
    //if the given address is NULL, empty string or its length greater than 100, this check fails
    if (address == null || address.equals("") || address.length() > 100) {
      this.result = false;
    }
    return this;
  }

  @Override
  public UserValidator checkRoleId(int roleId) {
    // If the given roleId in not in the RolesMap, it is an invalid roleId
    if (RolesMap.getInstance().getRole(roleId) == null) {
      this.result = false;
    }
    return this;
  }

  @Override
  public UserValidator checkPassword(String password) {
    if (password == null || password.equals("")) {
      // If is null or empty string, this check fails
      this.result = false;
    }
    return this;
  }
  
  @Override
  public boolean getResult() {
    return this.result;
  }

}
