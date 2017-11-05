package com.bank.validation;

import com.bank.generics.AccountTypes;

import java.math.BigDecimal;


public class AccountTypeNameValidatorPc implements AccountTypeNameValidator {

  /**
   * The boolean which stands for whether all checks have passed, default true.
   */
  private boolean result;

  /**
   * Constructor of AccountTypeNameValidatorPc.
   */
  public AccountTypeNameValidatorPc() {
    // Set this.result to default value.(true)
    this.result = true;
  }
  
  @Override
  public AccountTypeNameValidator checkAccountTypeName(String name) {
    // If name is null, set the result to false;
    if (name == null) {
      this.result = false;
    // If name is not in the AccountTypeMap, set the result to false;
    } else if ((!AccountTypes.CHEQUING.toString().equals(name))
        && (!AccountTypes.SAVING.toString().equals(name))
        && (!AccountTypes.TFSA.toString().equals(name))
        && (!AccountTypes.RESTRICTEDSAVINGS.toString().equals(name))
        && (!AccountTypes.BALANCEOWING.toString().equals(name))) {
      this.result = false;
    }
    return this;
  }

  @Override
  public AccountTypeNameValidator checktypeInterestrate(BigDecimal interestrate) {
    // If the given interestRate is null, or the new interest rate is out of bound, 
    // set result to false.
    if (interestrate == null) {
      this.result = false;
    } else if ((interestrate.doubleValue() < 0) || (interestrate.doubleValue() >= 1.0)) {
      this.result = false;
    }
    return this;
  }

  @Override
  public boolean getResult() {
    return this.result;
  }

}
