package com.bank.account;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;
import com.bank.validation.AccountValidator;
import com.bank.validation.AccountValidatorPc;

import java.math.BigDecimal;

public class AccountFactory {

  /**
   * returns a new account using the given parameters only if these parameters are valid,
   * return else otherwise.
   * @param accType the new account's type.
   * @param id the new account's id.
   * @param name the new account's name.
   * @param balance the new account's balance.
   * @return a new account with the given specifications if they are valid, returns null otherwise.
   */
  public Account getAccount(AccountTypes accType, int id, String name, BigDecimal balance) {
    // instantiate an account validator
    AccountValidator accValidator = new AccountValidatorPc();
    int typeid = AccountTypeMap.getInstance().getId(accType);
    // check all passed in parameters
    accValidator.checkName(name).checkBalance(balance,typeid);
    // given a specific account type, return an implementation of that account type
    // returns null if the enum is not CHEQUING, SAVING, or TFSA
    if (accValidator.getResult()) {
      switch (accType) {
        case CHEQUING:
          return new ChequingAccount(id, name, balance);
        case SAVING:
          return new SavingsAccount(id, name, balance);
        case TFSA:
          return new Tfsa(id, name, balance); 
        case RESTRICTEDSAVINGS:
          return new RestrictedSavings(id, name, balance);
        case BALANCEOWING:
          return new BalanceOwing(id, name, balance);
        default:
          return null;
      }
    }
    // returns null if any parameters are not valid
    return null;
  }
}
