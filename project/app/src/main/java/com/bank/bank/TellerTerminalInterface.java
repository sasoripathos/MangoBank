package com.bank.bank;

import com.bank.account.Account;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.exceptions.OtherCustomerExistException;
import com.bank.message.Message;
import com.bank.user.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface TellerTerminalInterface {
  /**
   * Make a new account for the current customer.
   * 
   * @param name The name of the account.
   * @param balance The balance of the account.
   * @param type The type id of the account
   * @return true if successfully make the account.
   */
  public long makeNewAccount(String name, BigDecimal balance, int type);
  
  /**
   * Deposit some amount of money for the current customer.
   * 
   * @param amount the amount of the money you want to deposit.
   * @param accountId the account id to deposit money.
   * @return Check if successfully deposit the given amount of the money.
   * @throws NoAccessToAccountException If password does not match with id, raise the exception.
   * @throws IllegalAmountException If the amount is negative, raise the exception.
   */
  public boolean makeDeposit(BigDecimal amount, int accountId) 
      throws NoAccessToAccountException, IllegalAmountException;
  
  /**
   * Set the current customer of the current TellerTerminal if it does not exist, otherwise,
   * throw OtherCustomerExistException.
   * 
   * @param customer the current customer of teller.
   * @throws OtherCustomerExistException The exception that the customer already exists.
   */
  public void setCurrentCustomer(Customer customer) throws OtherCustomerExistException;
  
  /**
   * Try to authenticate the current customer. 
   * @param password the current customer's password.
   */
  public void authenticateCurrentCustomer(String password);
  
  /**
   * Make a new user(customer).
   * 
   * @param name the name of the new user.
   * @param age the age of the new user.
   * @param address the address of the new user.
   * @param password the password of the new user.
   * @return the customer id of the new user.
   */
  public int makeNewUser(String name, int age, String address, String password);
  
  /**
   * Withdrawal some amount of money.
   * @param amount the amount of the money you want to withdrawal.
   * @param accountId the account id to withdrawal money.
   * @return Check if successfully withdrawal some amount of the money.
   * @throws NoAccessToAccountException If password does not match with the current customer.
   * @throws InsufficientFundsException If the account don't have enough money after withdrawal.
   * @throws IllegalAmountException If the amount is negative, raise the exception.
   */
  public boolean makeWithdrawal(BigDecimal amount, int accountId) 
      throws NoAccessToAccountException, InsufficientFundsException, IllegalAmountException;
  
  /**
   * Check the balance of the current account.
   * @param accountId the id of the account you want to check.
   * @return the balance of the account.
   * @throws NoAccessToAccountException If password does not match with id, raise the exception.
   */
  public BigDecimal checkBalance(int accountId) throws NoAccessToAccountException;
  
  /**
   * Give the interest to the given account id.
   * 
   * @param accountId the id to give interest.
   * @throws NoAccessToAccountException If password does not match with id, raise the exception.
   */
  public void giveInterest(int accountId) throws NoAccessToAccountException;
  
  /**
   * Give the interest to the all accounts of the current customer.
   * @throws NoAccessToAccountException Throw exceptions if don't have access to the account.
   */
  public void giveInterestToAll() throws NoAccessToAccountException;
  
  /**
   * List the all accounts of the current customer.
   * 
   * @return a list of Accounts which records all accounts of the current customer.
   */
  public List<Account> listAccounts();
  
  /**
   * DeAuthenticateCustomer to the teller.
   */
  public void deAuthenticateCustomer();
  
  /**
   * Display customer information.
   * 
   * @return the String representation of the current customer information
   */
  public String customerLoginDisplay();

  /**
   * See the total balance of all accounts a given user has open.
   * @return The total balance of all accounts of the current user.
   * @throws NoAccessToAccountException Throw exceptions if don't have access to the account.
   */
  public BigDecimal getallbalance() throws NoAccessToAccountException;
  
  /**
   * List all messages left for the current teller.
   * 
   * @return a List of all messages left for the current teller
   */
  public List<Message> viewMyMessages();
  
  /**
   * Given a message ID, if this message is left for the current teller, change its state to read.
   * 
   * @param messageId the message ID of the message we want to change the viewed state
   * @return whether the update success
   */
  public boolean changeMyMessageState(int messageId);
  
  /**
   * List all messages left for the current customer.
   * 
   * @return a List of all messages left for the current teller
   */
  public List<Message> viewCustomerMessages();
  
  /**
   * Given a message ID, if this message is left for the current customer, change its state to read.
   * 
   * @param messageId the message ID of the message we want to change the viewed state
   * @return whether the update success
   */
  public boolean changeCustomerMessageState(int messageId);
  
  /**
   * Given the content of the message, try to leave a message for the current customer.
   * 
   * @param content the content of the message we want to leave
   * @return whether the leave success
   */
  public long leaveMessage(String content);

  /**
   * Given the password, change the password of the user.
   * @param password the String that represents the password.
   * @return true if change successfully. If not, return false.
   * @throws NoAccessToAccountException Throw exceptions if don't have access to the account.
   */
  public boolean changepassword(String password) throws NoAccessToAccountException;

  /**
   * Given the name, change the name of the user.
   * @param name the String that represents the name.
   * @return true if change successfully. If not, return false.
   * @throws NoAccessToAccountException Throw exceptions if don't have access to the account.
   */
  public boolean changename(String name) throws NoAccessToAccountException;

  /**
   * Given the address, change the address of the account.
   * @param address the String that represents the address.
   * @return true if change successfully. If not, return false.
   * @throws NoAccessToAccountException Throw exceptions if don't have access to the account.
   */
  public boolean changeaddress(String address) throws NoAccessToAccountException;
}
