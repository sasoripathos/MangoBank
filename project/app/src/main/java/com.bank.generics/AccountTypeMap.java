package com.bank.generics;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AccountTypeMap {
  private static AccountTypeMap singleton = new AccountTypeMap();
  private Map<AccountTypes, Integer> map = new HashMap<AccountTypes, Integer>();
  private Map<Integer, AccountTypes> inverseMap = new HashMap<Integer, AccountTypes>();

  private AccountTypeMap() {
    update();
  }
  
  public static AccountTypeMap getInstance() {
    return singleton;
  }
  
  /**
   * updates the enumMap with values on the database.
   * @return returns true if all values are mapped correctly, returns false otherwise.
   */
  public boolean update() {
    // Instantiate new maps to hold the new key/values
    Map<AccountTypes, Integer> newMap = new HashMap<AccountTypes, Integer>();
    Map<Integer, AccountTypes> newInverseMap = new HashMap<Integer, AccountTypes>();
    // instantiate String to hold current role name
    String accountType;
    
    // get id of all accounts from the database
    //List<Integer> accountTypeIds = DatabaseSelectHelper.getAccountTypesIds();
    List<Integer> accountTypeIds = DatabaseDriverAndroidHelper.getInstance().getAccountTypesIds();
    // for each account type id, find its enum counterpart and map the enum to the id.
    // if any accountType from the database is not ADMIN, CUSTOMER, or TELLER return false.
    for (Integer id: accountTypeIds) {
      //accountType = DatabaseSelectHelper.getAccountTypeName(id);
      accountType = DatabaseDriverAndroidHelper.getInstance().getAccountTypeName(id);
      if (accountType.equalsIgnoreCase("CHEQUING")) {
        newMap.put(AccountTypes.CHEQUING, id);
        newInverseMap.put(id, AccountTypes.CHEQUING);
      } else if (accountType.equalsIgnoreCase("SAVING")) {
        newMap.put(AccountTypes.SAVING, id);
        newInverseMap.put(id, AccountTypes.SAVING);
      } else if (accountType.equalsIgnoreCase("TFSA")) {
        newMap.put(AccountTypes.TFSA, id);
        newInverseMap.put(id, AccountTypes.TFSA);
      } else if (accountType.equalsIgnoreCase("RESTRICTEDSAVINGS")) {
        newMap.put(AccountTypes.RESTRICTEDSAVINGS, id);
        newInverseMap.put(id, AccountTypes.RESTRICTEDSAVINGS);
      } else if (accountType.equalsIgnoreCase("BALANCEOWING")) {
        newMap.put(AccountTypes.BALANCEOWING, id);
        newInverseMap.put(id, AccountTypes.BALANCEOWING);
      } else {
        return false;
      }
    }
    // if everything is mapped successfully, set map to the newMap and return true
    map = newMap;
    inverseMap = newInverseMap;
    return true;
  }
  
  public int getId(AccountTypes accType) {
    return map.get(accType);
  }
  
  public AccountTypes getAccount(int id) {
    return inverseMap.get(id);
  }

}
