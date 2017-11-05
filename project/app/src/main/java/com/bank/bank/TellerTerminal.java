package com.bank.bank;

import com.bank.account.Account;
import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.exceptions.OtherCustomerExistException;
import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.message.Message;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TellerTerminal implements TellerTerminalInterface, Serializable {
  private static final long serialVersionUID = 5670966902968016714L;
  /**
   * The current user (teller).
   */
  private Teller currentUser = null;
  
  /**
   * The authenticate state of current user.
   */
  private boolean currentUserAuthenticated = false;
  
  /**
   * The current customer.
   */
  private Customer currentCustomer = null;
  
  /**
   * The authenticate state of current customer.
   */
  private boolean currentCustomerAuthenticated = false;
  
  /**
   * The constructor of the TellerTerminal, try to load a teller according to the given tellerId;
   * if success, try to authenticate him with the given password.
   * 
   * @param tellerId the given teller id.
   * @param password the the given password of the teller.
   */
  public TellerTerminal(int tellerId, String password) {
    //Only when given tellerId is positive and password is not null
    if ((tellerId > 0) && (password != null)) {
      //Try to get a user
      User potential = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(tellerId);
      //If the user exist (given id is valid) and the user's role is teller
      if ((potential != null)
          && (RolesMap.getInstance().getRole(potential.getRoleId()) == Roles.TELLER)) {
        //set the teller customer and try authenticate him
        this.currentUser = (Teller) potential;
        this.currentUserAuthenticated = this.currentUser.authenticate(password);
        // If authenticate fail
        if (!this.currentUserAuthenticated) {
          System.out.println("The given password is wrong.");
        }
      } else {
        //If the user does not exist or the user's role is not customer
        //System.out.println("The given id does not exist or is not a teller id.");
      }
    } else {
      //given tellerId is negative or password is null
      System.out.println("The given id is invalid or didn't provide password.");
    }
  }

  @Override
  public long makeNewAccount(String name, BigDecimal balance, int type) {
    // If teller and customers are both authenticated
    if ((this.currentUserAuthenticated) && (this.currentCustomerAuthenticated)) {
      // First check if the balance of the account id is less than 1000;
      // If it's less than 1000, transfer it to Chequing Account.
      if ((AccountTypeMap.getInstance().getAccount(type) 
          == AccountTypes.SAVING)) {
        if (balance.compareTo(new BigDecimal("1000")) == -1) {
          // transfer the saving account to chequing account.
          type = AccountTypeMap.getInstance().getId(AccountTypes.CHEQUING);
        }
      }
      long accountid = DatabaseDriverAndroidHelper.getInstance().insertAccount(name, balance, type);
      //If success insert, add the account to the current customer's account list
      if (accountid != -1) {
        this.currentCustomer.addAccount(DatabaseDriverAndroidHelper.getInstance().getOneAccountDetails((int)accountid));
        return DatabaseDriverAndroidHelper.getInstance().insertUserAccount(this.currentCustomer.getId(), (int)accountid);
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }

  @Override
  public boolean makeDeposit(BigDecimal amount, int accountId) 
      throws NoAccessToAccountException, IllegalAmountException {
    //If the current customer is not exist, return false
    if (this.currentCustomer == null) {
      return false;
    }
    // If the given amount is null, return false
    if (amount == null) {
      return false;
    }
    //If the given amount is negative, throw IllegalAmountException.
    BigDecimal zero = new BigDecimal("0");
    if (amount.compareTo(zero) < 0) {
      throw (new IllegalAmountException());
    }
    if ((this.currentUserAuthenticated) && (this.currentCustomerAuthenticated)) {
      // Check if the customer and teller have access to the account, throw
      // NoAccessToAccountException if it doesn't.
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
        throw(new NoAccessToAccountException());
      } else {
        //Else, get the current balance
        BigDecimal balance = askingAccount.getBalance();
        //Calculate the new balance
        balance = balance.add(amount);
        //Round the balance to 2 decimal places
        balance = balance.setScale(2, BigDecimal.ROUND_HALF_UP);
        //Update the new balance to database and current user
        askingAccount.setBalance(balance);
        return DatabaseDriverAndroidHelper.getInstance().updateAccountBalance(balance, accountId);
      }
    }
    // If not authenticated
    return false;
  }

  @Override
  public void setCurrentCustomer(Customer customer) throws OtherCustomerExistException {
    // If the current customer is null, set the current customer to the given customer
    if (this.currentCustomer == null) {
      this.currentCustomer = customer;
    } else { // else throw an exception
      throw (new OtherCustomerExistException());
    }
  }

  @Override
  public void authenticateCurrentCustomer(String password) {
    // If current customer does not exist, do nothing
    if (this.currentCustomer == null) {
      return;
    }
    // Try authenticate
    this.currentCustomerAuthenticated = this.currentCustomer.authenticate(password);
  }
  
  @Override
  public int makeNewUser(String name, int age, String address, String password) {
    // If the teller has been authenticated
    if (this.currentUserAuthenticated) {
      //Get the role id of customer
      int roleid = RolesMap.getInstance().getId(Roles.CUSTOMER);
      //Try to return the customerId of the new customer
      //return DatabaseInsertHelper.insertNewUser(name, age, address, roleid, password);
      return (int) DatabaseDriverAndroidHelper.getInstance()
              .insertNewUser(name, age, address, roleid, password);
    } else { // else return -1
      return -1;
    }
  }
  
  @Override
  public boolean makeWithdrawal(BigDecimal amount, int accountId) 
      throws NoAccessToAccountException, InsufficientFundsException, IllegalAmountException {
    // If the given amount is null, return false
    if (amount == null) {
      return false;
    }
    //If the given amount is negative, throw errors.
    BigDecimal zero = new BigDecimal("0");
    if (amount.compareTo(zero) < 0) {
      throw (new IllegalAmountException());
    }
    // If authenticated
    if ((this.currentUserAuthenticated) && (this.currentCustomerAuthenticated)) {
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
        throw(new NoAccessToAccountException());
      } else {
        BigDecimal currbal = this.checkBalance(accountId);
        currbal = currbal.subtract(amount);
        // Compare it with zero after subtraction.
        zero = new BigDecimal("0");
        if ((currbal.compareTo(zero) == -1)
                && (AccountTypeMap.getInstance().getAccount(accountId)
                != AccountTypes.BALANCEOWING)) {
          // Add the money to the withdrawal because you can't withdrawal.
          currbal = currbal.add(amount);
          throw new InsufficientFundsException();
        } else {
          //Round the current balance
          currbal = currbal.setScale(2, BigDecimal.ROUND_HALF_UP);
          // If this is a saving account and the account's balance is less than 1000,
          // Change the account from saving account to chequing account.
          if ((currbal.compareTo(new BigDecimal("1000.00")) == -1) && 
              (AccountTypeMap.getInstance().getAccount(askingAccount.getType()) == AccountTypes.SAVING)) {
            int typeId = AccountTypeMap.getInstance().getId(AccountTypes.CHEQUING);
            DatabaseDriverAndroidHelper.getInstance().updateAccountType(typeId, askingAccount.getId());
            //When account transitions state, leave a message to the current customer
            String msg = "Your account " + Integer.toString(accountId) 
            + " has changed from SAVINGS account to CHEQUING account";
            DatabaseDriverAndroidHelper.getInstance().insertMessage(this.currentCustomer.getId(), msg);
          }
          askingAccount.setBalance(currbal);
          return DatabaseDriverAndroidHelper.getInstance().updateAccountBalance(currbal, accountId);
        }
      }
    }
    // Else return false;
    return false;
  }
  
  @Override
  public BigDecimal checkBalance(int accountId) throws NoAccessToAccountException {
    // Check if the customer and teller have access to the account.
    // Throw NoAccessToAccountException if it doesn't.
    if ((this.currentUserAuthenticated) && (this.currentCustomerAuthenticated)) {
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
        throw(new NoAccessToAccountException());
      } else {
        return askingAccount.getBalance();
      }
    } else {
      throw(new NoAccessToAccountException());
    }
  }
  
  @Override
  public void giveInterest(int accountId) throws NoAccessToAccountException {
    if ((this.currentUserAuthenticated) && (this.currentCustomerAuthenticated)) {
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
      } else if ((AccountTypeMap.getInstance().getAccount(askingAccount.getType()) 
            == AccountTypes.BALANCEOWING)) {
        if (askingAccount.getBalance().compareTo(new BigDecimal("0")) == -1) {
          askingAccount.addInterest();
          //When given interest, leave a message to the current customer
          String msg = "Your account " + Integer.toString(accountId) 
          + " has been given interest. The new balance is "
          + askingAccount.getBalance().toString();
          DatabaseDriverAndroidHelper.getInstance().insertMessage(this.currentCustomer.getId(), msg);
        }
      } else {
        // Use the method in account to give the current account interest rate.
        askingAccount.addInterest();
        //When given interest, leave a message to the current customer
        String msg = "Your account " + Integer.toString(accountId) 
        + " has been given interest. The new balance is "
        + askingAccount.getBalance().toString();
        DatabaseDriverAndroidHelper.getInstance().insertMessage(this.currentCustomer.getId(), msg);
      }
    } else {
      throw (new NoAccessToAccountException());
    }
  }
  
  @Override
  public void giveInterestToAll() throws NoAccessToAccountException {
    if ((this.currentUserAuthenticated) && (this.currentCustomerAuthenticated)) {
      for (Account acc: this.listAccounts()) {
        giveInterest(acc.getId());
      }
    } else {
      throw (new NoAccessToAccountException());
    }
  }

  @Override
  public List<Account> listAccounts() {
    // If either customer or user is not authenticated, return empty list
    List<Account> allAccounts = new ArrayList<>();
    if (!this.currentCustomerAuthenticated || !this.currentUserAuthenticated) {
      return allAccounts;
    }
    allAccounts = this.currentCustomer.getAccounts();
    return allAccounts;
  }

  @Override
  public BigDecimal getallbalance() {
    // Check if the customer and user is authenticated.
    if (!this.currentCustomerAuthenticated || !this.currentUserAuthenticated) {
      return null;
    } else {
      // Total is used to count the account total balance.
      BigDecimal total = new BigDecimal("0");
      List<Account> accounts = this.listAccounts();
      for (Account account:accounts) {
        total = total.add(account.getBalance());
      }
      return total;
    }
  }
  
  @Override
  public void deAuthenticateCustomer() {
    this.currentCustomerAuthenticated = false;
    this.currentCustomer = null;
  }
  
  @Override
  public String customerLoginDisplay() {
    String result = "";
    if (this.currentCustomerAuthenticated) {
      result = this.currentCustomer.toString();
    } else {
      result = "Customer not authenticated";
    }
    return result;
  }

  @Override
  public List<Message> viewMyMessages() {
    // Initialize the answer list
    List<Message> allMessages = new ArrayList<>();
    // If the current teller has not been authenticate, do nothing
    if (!this.currentUserAuthenticated) {
      return allMessages;
    }
    // Else, try to get all messages for the current teller
    allMessages = DatabaseDriverAndroidHelper.getInstance()
            .getOneUserAllMessages(this.currentUser.getId());
    return allMessages;
  }

  @Override
  public boolean changeMyMessageState(int messageId) {
    // If current user is not authenticated, do nothing
    if (!this.currentUserAuthenticated) {
      return false;
    }
    // Else
    // Check whether is message is left for the current teller
    List<Message> myMessages = this.viewMyMessages();
    for (Message current : myMessages) {
      // If yes, try to update its viewed state
      if (current.getMessageId() == messageId) {
        return DatabaseDriverAndroidHelper.getInstance().updateUserMessageState(messageId);
      }
    }
    // If the message is not for the current teller, return false
    return false;
  }

  @Override
  public List<Message> viewCustomerMessages() {
 // Initialize the answer list
    List<Message> allMessages = new ArrayList<>();
    // If the current teller or the current customer has not been authenticate, do nothing
    if ((!this.currentUserAuthenticated) || (!this.currentCustomerAuthenticated)) {
      return allMessages;
    }
    // Else, try to get all messages for the current customer
    allMessages = DatabaseDriverAndroidHelper.getInstance()
            .getOneUserAllMessages(this.currentCustomer.getId());
    return allMessages;
  }

  @Override
  public boolean changeCustomerMessageState(int messageId) {
    // If current teller or the current customer is not authenticated, do nothing
    if ((!this.currentUserAuthenticated) || (!this.currentCustomerAuthenticated)) {
      return false;
    }
    // Else
    // Check whether is message is left for the current customer
    List<Message> customerMessages = this.viewCustomerMessages();
    for (Message current : customerMessages) {
      // If yes, try to update its viewed state
      if (current.getMessageId() == messageId) {
        return DatabaseDriverAndroidHelper.getInstance().updateUserMessageState(messageId);
      }
    }
    // If the message is not for the current customer, return false
    return false;
  }

  @Override
  public long leaveMessage(String content) {
    // If current teller or the current customer is not authenticated, do nothing
    if ((!this.currentUserAuthenticated) || (!this.currentCustomerAuthenticated)) {
      return -1;
    }
    // Else
    // try to insert a message for the given user in the database
    return DatabaseDriverAndroidHelper.getInstance()
            .insertMessage(this.currentCustomer.getId(), content);
  }
  
  @Override
  public boolean changepassword(String password) {
    if ((this.currentCustomerAuthenticated) && (this.currentUserAuthenticated)) {
      return DatabaseDriverAndroidHelper.getInstance()
              .updateUserPassword(password, this.currentCustomer.getId());
    } else {
      return false;
    }
  }
  
  @Override
  public boolean changename(String name) {
    if ((this.currentCustomerAuthenticated) && (this.currentUserAuthenticated)) {
      return DatabaseDriverAndroidHelper.getInstance()
              .updateUserName(name, this.currentCustomer.getId());
    } else {
      return false;
    }
  }
  
  @Override
  public boolean changeaddress(String address) {
    if ((this.currentCustomerAuthenticated) && (this.currentUserAuthenticated)) {
      return DatabaseDriverAndroidHelper.getInstance()
              .updateUserAddress(address, this.currentCustomer.getId());
    } else {
      return false;
    }
  }

  /**
   * Return whether the current customer is authenticated
   * @return whether the current customer is authenticated
   */
  public boolean isCurrentCustomerAuthenticated() {
    return currentCustomerAuthenticated;
  }

  /**
   * Return whether the current user is authenticated.
   *
   * @return whether the current user is authenticated
   */
  public boolean isCurrentUserAuthenticated() {
    return currentUserAuthenticated;
  }

  /**
   * Return the tellerId of the current teller.
   *
   * @return the tellerId of the current teller.
   */
  public int getCurrentTellerId() {
    if (currentUser != null) {
      return currentUser.getId();
    }
    return -1;
  }

  /**
   * Return the customerId of the current customer.
   *
   * @return the customerId of the current customer.
   */
  public int getCurrentCustomerId() {
    if (currentCustomer != null) {
      return currentCustomer.getId();
    }
    return -1;
  }
}
