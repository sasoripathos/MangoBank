package com.bank.validation;

import java.math.BigDecimal;

public interface AccountTypeNameValidator {
  
  /**
   * Give the Accounttypename, check if it is valid. (in the AccountTypes Enumerator)
   * @param typename the typename need to be checked.
   * @return the current AccountValidator instance.
   */
  public AccountTypeNameValidator checkAccountTypeName(String typename);
  
  /**
   * Given the interestrate, check whether the interest rate is valid.(0 <= interestrate< 1
   * and not null)
   * @param interestrate interest rate to be checked.
   * @return the current AccountValidator instance.
   */
  public AccountTypeNameValidator checktypeInterestrate(BigDecimal interestrate);

  /**
   * Return the final result after a series of check.
   * @return a boolean stands for whether all checks have passed.
   */
  public boolean getResult();
}
