package com.bank.bank;

import com.bank.account.Account;
import com.bank.exceptions.CannotWithdrawlException;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.message.Message;

import java.math.BigDecimal;
import java.util.List;

public interface AtmInterface {

  /**
   * Check if the ID matches the password.
   * @param userId the id of the user.
   * @param password the password of the user.
   * @return true if the user id matches user's password.
   */
  public boolean authenticate(int userId, String password);
  
  /**
   * Get all of the account of the current customer.
   * @return All of the accounts of the current customer.
   */
  public List<Account> listAccounts();
  
  /**
   * Deposit some amount of money.
   * @param amount the amount of the money you want to deposit.
   * @param accountId the account id to deposit money.
   * @return Check if successfully deposit some amount of the money.
   * @throws NoAccessToAccountException If password does not match with id, raise the exception.
   * @throws IllegalAmountException If the amount is negative, raise the exception.
   */
  public boolean makeDeposit(BigDecimal amount, int accountId)
      throws NoAccessToAccountException, IllegalAmountException;
  
  /**
   * Check the balance of the current account.
   * @param accountId the id of the account you want to check.
   * @return the balance of the account.
   * @throws NoAccessToAccountException If password does not match with id, raise the exception.
   */
  public BigDecimal checkBalance(int accountId) throws NoAccessToAccountException;
  
  /**
   * Withdrawal some amount of money.
   * @param amount the amount of the money you want to withdrawal.
   * @param accountId the account id to withdrawal money.
   * @return Check if successfully withdrawal some amount of the money.
   * @throws NoAccessToAccountException If password does not match with id, raise the exception.
   * @throws InsufficientFundsException If the account don't have enough money after withdrawal.
   * @throws IllegalAmountException If the amount is negative, raise the exception.
   * @throws CannotWithdrawlException Throws when users withdrawals money from Restricted_Savings Account.
   */
  public boolean makeWithdrawal(BigDecimal amount, int accountId)
      throws NoAccessToAccountException, InsufficientFundsException, 
      IllegalAmountException, CannotWithdrawlException;
  
  /**
   * Method that will collect the name, address, and info of
   * all accounts for the current customer using the atm.
   * 
   * @return info the info that is collected on the user
   */
  public String display();
  
  /**
   * List all messages left for the current customer.
   * 
   * @return a List of all messages left for the current customer
   */
  public List<Message> viewMyMessages();
  
  /**
   * Given a message ID, if this message is left for the current customer, change its state to read.
   * 
   * @param messageId the message ID of the message we want to change the viewed state
   * @return whether the update success
   */
  public boolean changeMyMessageState(int messageId);
}
