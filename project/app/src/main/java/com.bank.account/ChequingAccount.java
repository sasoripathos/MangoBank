package com.bank.account;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

import java.math.BigDecimal;

public class ChequingAccount extends Account {

  private static final long serialVersionUID = 1636456226594220474L;

  /**
   * Constructor of chequing account. 
   * @param id the checking account id.
   * @param name the name of the account. 
   * @param balance BigDecimal number of account balance.
   */
  public ChequingAccount(int id, String name, BigDecimal balance) {
    this.setId(id);
    this.setName(name);
    this.setBalance(balance);
    // Get the account id of chequing account from the AccountTypes EnumMap
    this.setType(AccountTypeMap.getInstance().getId(AccountTypes.CHEQUING));
    //Get the interest rate of this account
    this.findAndSetInterestRate();
  }
}
