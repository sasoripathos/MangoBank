package com.bank.exceptions;

public class CannotWithdrawlException extends Exception {
  private static final long serialVersionUID = 1L;

  public CannotWithdrawlException() {
    super();
  }
  
  public CannotWithdrawlException(String message) {
    super(message);
  }
}
