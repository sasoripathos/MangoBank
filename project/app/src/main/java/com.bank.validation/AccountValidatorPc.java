package com.bank.validation;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

import java.math.BigDecimal;

public class AccountValidatorPc implements AccountValidator {

  /**
   * The boolean which stands for whether all checks have passed, default true.
   */
  private boolean result;
  
  public AccountValidatorPc() {
    // Set this.result to default value.(true)
    this.result = true;
  }

  @Override
  public AccountValidator checkName(String name) {
    // Check if name is null or if it's empty.
    if ((name == null) || (name.equals(""))) {
      this.result = false;
    }
    return this;
  }

  @Override
  public AccountValidator checkBalance(BigDecimal balance, int typeid) {
    // BalanceOwing account allows the negative value.
    // Check if the account is balance owing account.
    if ((AccountTypeMap.getInstance().getAccount(typeid) 
        == AccountTypes.BALANCEOWING)) {
      if (balance == null) {
        this.result = false;
      }
    } else {
      // Check if other four accounts' balance is null or negative
      if ((balance == null) || (balance.doubleValue() < 0)) {
        this.result = false;
      }
    }
    return this;
  }

  @Override
  public AccountValidator checkAccountType(int accounttype) {
    // Check if the given account type id is in the AccountTypeMap.
    if (AccountTypeMap.getInstance().getAccount(accounttype) == null) {
      this.result = false;
    }
    return this;
  }

  @Override
  public boolean getResult() {
    return this.result;
  }

}
