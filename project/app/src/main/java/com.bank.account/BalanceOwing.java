package com.bank.account;

import java.math.BigDecimal;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

public class BalanceOwing extends Account{

  private static final long serialVersionUID = 2136113544246011942L;

  /**
   * The constructor of the account.
   * @param id account id.
   * @param name name of the account.
   * @param balance account balance of the account.
   */
  public BalanceOwing(int id, String name, BigDecimal balance) {
    this.setId(id);
    this.setName(name);
    this.setBalance(balance);
    // Get the account id of balance owing account from the AccountTypes EnumMap
    this.setType(AccountTypeMap.getInstance().getId(AccountTypes.BALANCEOWING));
    //Get the interest rate of this account
    this.findAndSetInterestRate();
  }
}
