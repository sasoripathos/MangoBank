package com.bank.validation;

public interface RoleNameValidator {
  /**
   * Given a role name, check whether the role name is valid (in the Roles Enumerator).
   * 
   * @param roleName the role name need to be check
   * @return the current RoleNameValidator instance
   */
  public RoleNameValidator checkRoleName(String roleName);
  
  /**
   * Return the final result after a series of check.
   * 
   * @return a boolean stands for whether all checks have passed
   */
  public boolean getResult();
}
