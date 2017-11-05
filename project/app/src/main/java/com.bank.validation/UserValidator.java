package com.bank.validation;

/**
 * This interface should be used to check whether the provided information related to a user is
 * valid.
 */
public interface UserValidator {
  
  /**
   * Given a name, check whether the name is valid (not NULL and not empty string).
   * 
   * @param name the name need to be check
   * @return the current UserValidator instance
   */
  public UserValidator checkName(String name);
  
  /**
   * Given an age, check whether the age is valid (greater than or equal to 0).
   * 
   * @param age the age need to be check
   * @return the current UserValidator instance
   */
  public UserValidator checkAge(int age);
  
  /**
   * Given an address, check whether the address is valid (not NULL, not empty string and length
   * no more than 100).
   * 
   * @param address the address need to be check
   * @return the current UserValidator instance
   */
  public UserValidator checkAddress(String address);
  
  /**
   * Given a roleId, check whether the roleId exist in the database.
   * 
   * @param roleId the roleId need to be check
   * @return the current UserValidator instance
   */
  public UserValidator checkRoleId(int roleId);
  
  /**
   * Given a password, check whether the password is valid (not NULL and not empty string).
   * 
   * @param password the password need to be check
   * @return the current UserValidator instance
   */
  public UserValidator checkPassword(String password);
  
  /**
   * Return the final result after a series of check.
   * 
   * @return a boolean stands for whether all checks have passed
   */
  public boolean getResult();
}
