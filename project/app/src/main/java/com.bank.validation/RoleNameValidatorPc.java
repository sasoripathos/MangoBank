package com.bank.validation;

import com.bank.generics.Roles;

public class RoleNameValidatorPc implements RoleNameValidator {
  /**
   * The boolean which stands for whether all checks have passed, default true.
   */
  private boolean result;
  
  /**
   * Constructor.
   */
  public RoleNameValidatorPc() {
    //Set this.result to default value (true)
    this.result = true;
  }
  
  @Override
  public RoleNameValidator checkRoleName(String roleName) {
    if (roleName == null) { // If is null, this check fails
      this.result = false;
    } else if ((!Roles.ADMIN.toString().equals(roleName))
        && (!Roles.CUSTOMER.toString().equals(roleName)) 
        && (!Roles.TELLER.toString().equals(roleName))) {
      // if not in the Roles enumerator, this check fails
      this.result = false;
    }
    return this;
  }

  @Override
  public boolean getResult() {
    return this.result;
  }

}
