package com.bank.account;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

import java.math.BigDecimal;

public class SavingsAccount extends Account {


  private static final long serialVersionUID = 8303085864206583123L;

  /**
   * Constructor of Saving account. 
   * @param id the checking account id.
   * @param name the name of the account. 
   * @param balance BigDecimal number of account balance.
   */

  public SavingsAccount(int id, String name, BigDecimal balance) {
    this.setId(id);
    this.setName(name);
    this.setBalance(balance);
    // Get the account id of Saving account from the AccountTypes EnumMap
    this.setType(AccountTypeMap.getInstance().getId(AccountTypes.SAVING));
    //Get the interest rate of this account
    this.findAndSetInterestRate();
  }

}
