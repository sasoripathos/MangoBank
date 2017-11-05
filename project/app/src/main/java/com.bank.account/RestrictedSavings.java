package com.bank.account;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

import java.math.BigDecimal;

public class RestrictedSavings extends Account {

  private static final long serialVersionUID = 2428964169730223793L;

  /**
   * The constructor of the account.
   * @param id account id.
   * @param name name of the account.
   * @param balance account balance of the account.
   */
  public RestrictedSavings(int id, String name, BigDecimal balance) {
    this.setId(id);
    this.setName(name);
    this.setBalance(balance);
    // Get the account id of restricted saving account from the AccountTypes EnumMap
    this.setType(AccountTypeMap.getInstance().getId(AccountTypes.RESTRICTEDSAVINGS));
    //Get the interest rate of this account
    this.findAndSetInterestRate();
  }
}
