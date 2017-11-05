package com.bank.validation;

import java.math.BigDecimal;

public interface AccountValidator {
  
  /**
   * Given a name, check whether the name is valid(not NULL or empty String)
   * @param name the name need to be checked.
   * @return the current AccountValidator instance.
   */
  public AccountValidator checkName(String name);
  
  /**
   * Given the balance, check whether the balance is valid.(not null)
   * @param balance the balance need to be checked.
   * @return the current AccountValidator instance.
   */
  public AccountValidator checkBalance(BigDecimal balance, int typeid);
  
  /**
   * Given the accounttype, check whether the account type is in the database.
   * @param accounttype account type need to be checked.
   * @return the current AccountValidator instance.
   */
  public AccountValidator checkAccountType(int accounttype);
  
  /**
   * Return the final result after a series of check.
   * @return a boolean stands for whether all checks have passed.
   */
  public boolean getResult();
}
