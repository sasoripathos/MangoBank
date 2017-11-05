package com.bank.bank;

import com.bank.account.Account;
import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.CannotWithdrawlException;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.message.Message;
import com.bank.user.Customer;
import com.bank.user.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Atm implements AtmInterface, Serializable {

  private static final long serialVersionUID = 1129868670331771125L;
  /**
   * The current customer.
   */
  private Customer currentCustomer = null;
  
  /**
   * the authenticate state of the current customer.
   */
  private boolean authenticated = false;

  /**
   * The constructor of ATM, try to load a customer according to the given customer id; if success
   * try to authenticate him with the given password.
   * 
   * @param customerId customer's customer's Id.
   * @param password customer's password.
   */
  public Atm(int customerId, String password) {
    //Only when given customerId is positive and password is not null
    if ((customerId > 0) && (password != null)) {
      //Try to get a user
      //User potential = DatabaseSelectHelper.getUserDetails(customerId);
      User potential = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(customerId);
      //If the user exist (given id is valid) and the user's role is customer
      if ((potential != null)
          && (RolesMap.getInstance().getRole(potential.getRoleId()) == Roles.CUSTOMER)) {
        //set the current customer and try authenticate him
        this.currentCustomer = (Customer) potential;
        this.authenticated = this.currentCustomer.authenticate(password);
      }
    }
  }
  
  /**
   * The constructor of ATM, try to load a customer according to given userid.
   * 
   * @param customerId customer's Id.
   */
  public Atm(int customerId) {
    //Only when given customerId is positive
    if (customerId > 0) {
      //Try to get a user
      //User potential = DatabaseSelectHelper.getUserDetails(customerId);
      User potential = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(customerId);
      //If the user exist (given id is valid) and the user's role is customer
      if ((potential != null)
          && (RolesMap.getInstance().getRole(potential.getRoleId()) == Roles.CUSTOMER)) {
        //set the current customer and try authenticate him
        this.currentCustomer = (Customer) potential;
      }
    }
  }

  @Override
  public boolean authenticate(int userId, String password) {
    //If the current customer is not exist, return false
    if (this.currentCustomer == null) {
      return false;
    } //If the given user id is not the userId of the current customer
    if (this.currentCustomer.getId() != userId) {
      return false;
    }
    // return true if the authenticated status is true.
    // Use user's method to verify if current customer's password matches the id.
    if (!this.authenticated) {
      this.authenticated = this.currentCustomer.authenticate(password);
      return this.authenticated;
    } else {
      return true;
    }
  }

  @Override
  public List<Account> listAccounts() {
    // If has been authenticated
    if (this.authenticated) {
      return this.currentCustomer.getAccounts();
    } else {
      return null;
    }
  }

  @Override
  public boolean makeDeposit(BigDecimal amount, int accountId) 
      throws NoAccessToAccountException, IllegalAmountException {
    // If the given amount is null, return false
    if (amount == null) {
      return false;
    }
    //If the given amount is negative, throw errors.
    BigDecimal zero = new BigDecimal("0");
    if (amount.compareTo(zero) < 0) {
      throw (new IllegalAmountException());
    }
    // Check if the account to deposit is in the current customer's account list.
    // Return false if it doesn't.
    if (this.authenticated) {
      //Get all accounts of the user
      List<Account> allAccounts = this.listAccounts();
      //Try to find the right account
      Account askingAccount = null;
      for (Account current: allAccounts) {
        if (current.getId() == accountId) {
          askingAccount = current;
        }
      }
      // Check whether the customer has the access to the asking account
      if (askingAccount == null) {
        //If cannot find the corresponding account, the customer has no access to that account
        //throw an NoAccessToAccount error
        throw (new NoAccessToAccountException());
      } else {
        //Else, get the current balance
        BigDecimal balance = askingAccount.getBalance();
        //Calculate the new balance
        balance = balance.add(amount);
        //Round the balance to 2 decimal places
        balance = balance.setScale(2, BigDecimal.ROUND_HALF_UP);
        //Update the new balance to database and current user
        askingAccount.setBalance(balance);
        //return DatabaseUpdateHelper.updateAccountBalance(balance, accountId);
        return DatabaseDriverAndroidHelper.getInstance().updateAccountBalance(balance, accountId);
      }
    }
    // If not authenticated
    return false;
  }
  
  @Override
  public BigDecimal checkBalance(int accountId) throws NoAccessToAccountException {
    // If the password matches the id.
    if (this.authenticated) {
      //Get all accounts of the user
      List<Account> allAccounts = this.listAccounts();
      Account askingAccount = null;
      //Try to find the right account
      for (Account current: allAccounts) {
        if (current.getId() == accountId) {
          askingAccount = current;
        }
      }
      // Check whether the customer has the access to the asking account
      if (askingAccount == null) {
        //If cannot find the corresponding account, the customer has no access to that account
        //throw an NoAccessToAccount error
        throw (new NoAccessToAccountException());
      } else {
        return askingAccount.getBalance();
      }
    }
    // Else return null
    return null;
  }
  
  @Override
  public boolean makeWithdrawal(BigDecimal amount, int accountId) 
      throws NoAccessToAccountException, InsufficientFundsException, 
      IllegalAmountException, CannotWithdrawlException {
    // If the given amount is null, return false
    if (amount == null) {
      return false;
    }
    //If the given amount is negative, throw errors.
    BigDecimal zero = new BigDecimal("0");
    if (amount.compareTo(zero) < 0) {
      throw (new IllegalAmountException());
    }
    // If authenticated, then do the following step.
    if (this.authenticated) {
      //Get all accounts of the user
      List<Account> allAccounts = this.listAccounts();
      //Try to find the right account
      Account askingAccount = null;
      for (Account current: allAccounts) {
        if (current.getId() == accountId) {
          askingAccount = current;
        }
      }
      // Check whether the customer has the access to the asking account
      if (askingAccount == null) {
        //If cannot find the corresponding account, the customer has no access to that account
        //throw an NoAccessToAccount error
        throw (new NoAccessToAccountException());
      } else {
        //Account newaccount = DatabaseSelectHelper.getAccountDetails(accountId);
        Account newaccount = DatabaseDriverAndroidHelper.getInstance().getOneAccountDetails(accountId);
        if ((AccountTypeMap.getInstance().getAccount(newaccount.getType()) 
            == AccountTypes.RESTRICTEDSAVINGS)) {
          throw (new CannotWithdrawlException());
        }
        BigDecimal currbal = this.checkBalance(accountId);
        currbal = currbal.subtract(amount);
        // Compare it with zero after subtraction.
        zero = new BigDecimal("0");
        if ((currbal.compareTo(zero) == -1)
                && (AccountTypeMap.getInstance().getAccount(newaccount.getType())
                != AccountTypes.BALANCEOWING)) {
          //If the account is not BalanceOwing and the potential new amount
          // Add the money back to the withdrawal because you can't withdrawal.
          currbal = currbal.add(amount);
          throw new InsufficientFundsException();
        } else {
          //round the current balance
          currbal = currbal.setScale(2, BigDecimal.ROUND_HALF_UP);
          // get the database helper instance
          DatabaseDriverAndroidHelper db = DatabaseDriverAndroidHelper.getInstance();
          // If this is a saving account and the account's balance is less than 1000,
          // Change the account from saving account to chequing account.
          if ((currbal.compareTo(new BigDecimal("1000.00")) == -1) && 
              (AccountTypeMap.getInstance().getAccount(askingAccount.getType()) == AccountTypes.SAVING)) {
            int typeId = AccountTypeMap.getInstance().getId(AccountTypes.CHEQUING);
            //DatabaseUpdateHelper.updateAccountType(typeId, askingAccount.getId());
            db.updateAccountType(typeId, askingAccount.getId());
            //When account transitions state, leave a message to the current customer
            String msg = "Your account " + Integer.toString(accountId) 
            + " has changed from SAVINGS account to CHEQUING account";
            //DatabaseInsertHelper.insertMessage(this.currentCustomer.getId(), msg);
            db.insertMessage(this.currentCustomer.getId(), msg);
          }
          //Update the new balance to database and current user
          askingAccount.setBalance(currbal);
          //return DatabaseUpdateHelper.updateAccountBalance(currbal, accountId);
          return db.updateAccountBalance(currbal, accountId);
        }
      }
    }
    // Else return false;
    return false;
  }
  
  @Override
  public String display() {
    String info = "";
    if (this.authenticated) {
      info = this.currentCustomer.toString();
    } else {
      info = "Not an authenticated customer";
    }
    return info;
  }

  @Override
  public List<Message> viewMyMessages() {
    // Initialize the answer list
    List<Message> myMessages = new ArrayList<>();
    // If authenticated
    if (this.authenticated) {
      // Try to get all messages left for the current admin 
      //myMessages = DatabaseSelectHelper.getAllMessages(this.currentCustomer.getId());
      myMessages = DatabaseDriverAndroidHelper.getInstance().
              getOneUserAllMessages(this.currentCustomer.getId());
    }
    // else do nothing
    return myMessages;
  }

  @Override
  public boolean changeMyMessageState(int messageId) {
    // If not authenticated, do nothing
    if (!this.authenticated) {
      return false;
    }
    // Else
    // Check whether is message is left for the current customer
    List<Message> myMessages = this.viewMyMessages();
    for (Message current : myMessages) {
      // If yes, try to update its viewed state
      if (current.getMessageId() == messageId) {
        //return DatabaseUpdateHelper.updateUserMessageState(messageId);
        return  DatabaseDriverAndroidHelper.getInstance().updateUserMessageState(messageId);
      }
    }
    // If the message is not for the current customer, return false
    return false;
  }
}
