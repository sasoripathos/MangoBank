package com.bank.account;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.generics.AccountTypeMap;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class Account implements Serializable{
  private static final long serialVersionUID = -3289644005454135068L;
  /**
   * The accountId of this account.
   */
  private int id;
  /**
   * The name of this account.
   */
  private String name;
  /**
   * The balance of this account.
   */
  private BigDecimal balance;
  /**
   * The typeId of this account.
   */
  private int type;
  /**
   * The interest rate for this account.
   */
  private BigDecimal interestRate;
  
  /**
   * Get the id of the account.
   * @return id of the account
   */
  public int getId() {
    return this.id;
  }
  
  /**
   * Get the name of the account.
   * @return name of the account.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Get the balance of the account.
   * @return balance of the account.
   */
  public BigDecimal getBalance() {
    return this.balance;
  }
  
  /**
   * Set the type id.
   * @param type the type id to set.
   */
  protected void setType(int type) {
    this.type = type;
  }
  
  /**
   * Get the type of the account.
   * @return type of the account.
   */
  public int getType() {
    return this.type;
  }

  /**
   * Set the id of the account.
   * @param id the id of the account
   */
  public void setId(int id) {
    this.id = id;
  }
  
  /**
   * Set the name of the account.
   * @param name the name of the account.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the balance of the account.
   * @param balance the account's balance.
   */
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  /**
   * Find the current interest rate of this account type.
   */
  public void findAndSetInterestRate() {
    int acctype;
    acctype = this.getType();
    //this.interestRate = DatabaseSelectHelper.getInterestRate(acctype);
    this.interestRate = DatabaseDriverAndroidHelper.getInstance().getInterestRate(acctype);
  }
  
  /**
   * Get the interest rate of the account.
   * @return interest rate of the account.
   */
  public BigDecimal getInterestRate() {
    return this.interestRate;
  }
  
  /**
   * Add the interest to the current account.
   */
  public void addInterest() {
    //get current interest
    BigDecimal bal = this.getBalance();
    //update the interest rate
    this.findAndSetInterestRate();
    BigDecimal currinterestrate = this.getInterestRate();
    // new balance = previous balance * (1 + interest rate);
    BigDecimal one = new BigDecimal("1");
    BigDecimal plusinterestrate = currinterestrate.add(one);
    bal = bal.multiply(plusinterestrate);
    bal = bal.setScale(2, BigDecimal.ROUND_HALF_UP);
    // Set the new interest rate.
    this.setBalance(bal);
    // Update the new account balance to the account.
    //DatabaseUpdateHelper.updateAccountBalance(bal, this.getId());
    DatabaseDriverAndroidHelper.getInstance().updateAccountBalance(bal, this.getId());
  }
  
  @Override
  public String toString() {
    String rep = "";
    //Display account name
    rep += "Account Name (Account Type): " + this.getName() + " ";
    //Display account type
    rep += "(" + AccountTypeMap.getInstance().getAccount(this.getType()).toString() + ")\n";
    //Display balance
    rep += "Balance: $" + this.getBalance().toString() + "\n";
    return rep;
  }
  
}
