package com.bank.exceptions;

public class NoAccessToAccountException extends Exception {
  /**
   * This exception shows that the user has no access to the given account.
   */
  private static final long serialVersionUID = 1L;
  
  public NoAccessToAccountException() {
    super();
  }
  
  public NoAccessToAccountException(String message) {
    super(message);
  }
}
