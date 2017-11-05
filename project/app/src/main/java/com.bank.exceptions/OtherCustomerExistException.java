package com.bank.exceptions;

public class OtherCustomerExistException extends Exception {
  /**
   * This exception shows that the user has no access to the given account.
   */
  private static final long serialVersionUID = 1L;
  
  public OtherCustomerExistException() {
    super();
  }
  
  public OtherCustomerExistException(String message) {
    super(message);
  }
}
