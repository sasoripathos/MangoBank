package com.bank.bank;

import android.content.Context;

import com.bank.account.Account;
import com.bank.account.BalanceOwing;
import com.bank.account.ChequingAccount;
import com.bank.account.RestrictedSavings;
import com.bank.account.SavingsAccount;
import com.bank.account.Tfsa;
import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.message.Message;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminTerminal implements AdminTerminalInterface, Serializable {
  private static final long serialVersionUID = 3763920687408732847L;
  /**
   * The current Admin.
   */
  private Admin currentAdmin = null;
  /**
   * the authenticate state of the current Admin.
   */
  private boolean authenticated = false;
  /**
   * The constructor of AdminTerminal, try to load an admin according to the given admin id;
   * if success try to authenticate him with the given password.
   * 
   * @param adminId the given admin id.
   * @param password the given password of this admin.
   */
  public AdminTerminal(int adminId, String password) {
    //Only when given adminId is positive and password is not null
    if ((adminId > 0) && (password != null)) {
      //Try to get a users.
      User potential = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(adminId);
      //If the user exist (given id is valid) and the user's role is admin
      if ((potential != null)
          && (RolesMap.getInstance().getRole(potential.getRoleId()) == Roles.ADMIN)) {
        //set the current admin and try authenticate him
        this.currentAdmin = (Admin) potential;
        this.authenticated = this.currentAdmin.authenticate(password);
      }
    }
  }

  /**
   * The constructor of AdminTerminal, try to load an admin according to the given admin id.
   * 
   * @param adminId the given admin id.
   */
  public AdminTerminal(int adminId) {
    //Only when given adminId is positive
    if (adminId > 0) {
      //Try to get a user

      User potential = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(adminId);
      //If the user exist (given id is valid) and the user's role is admin
      if ((potential != null)
          && (RolesMap.getInstance().getRole(potential.getRoleId()) == Roles.ADMIN)) {
        //set the current customer and try authenticate him
        this.currentAdmin = (Admin) potential;
      } else {
        //If the user does not exist or the user's role is not admin
        System.out.println("The given id does not exist or is not an admin id.");
      }
    } else {
      //given adminId is negative
      System.out.println("The given id is invalid.");
    }
  }

  @Override
  public boolean authenticate(int adminId, String password) {
    //Only when given adminId is positive and password is not null
    if ((adminId > 0) && (password != null)) {
      if ((this.currentAdmin != null) && (this.authenticated)
              && (RolesMap.getInstance().getRole(this.currentAdmin.getRoleId()) == Roles.ADMIN)) {
        // return true if all of these are satisfied.
        return true;
      }
    }
    return false;
  }

  @Override
  public int createAdmin(String name, int age, String address, String password) {
    int adminid = -1;
    //If the current admin has been authenticated
    if (this.authenticated == true) {
      //Get the role id of admin
      int roleId = RolesMap.getInstance().getId(Roles.ADMIN);
      adminid = (int) DatabaseDriverAndroidHelper.getInstance().insertNewUser(name, age, address, roleId, password);

    }
    return adminid;
  }
  
  @Override
  public List<Admin> listAllAdmins() {
    //Initialize the list for all admin
    List<Admin> adminList = new ArrayList<Admin>();
    if (this.authenticated == true) {
      // Find all user.
      List<User> users = DatabaseDriverAndroidHelper.getInstance().getAllUsersDetails();
      for (User current: users) {
        //If the current user is an admin, add him to the list
        if (RolesMap.getInstance().getRole(current.getRoleId()) == Roles.ADMIN) {
          adminList.add((Admin) current);
        }
      }
    }
    return adminList;
  }
  
  @Override
  public List<Teller> listAllTellers() {
    //Initialize the list for all Teller
    List<Teller> tellerList = new ArrayList<Teller>();
    if (this.authenticated == true) {
      // Find all users
      List<User> users = DatabaseDriverAndroidHelper.getInstance().getAllUsersDetails();
      for (User current: users) {
        //If the current user is a teller, add him to the list
        if (RolesMap.getInstance().getRole(current.getRoleId()) == Roles.TELLER) {
          tellerList.add((Teller) current);
        }
      }
    }
    return tellerList;
  }
  
  @Override
  public List<Customer> listAllCustomers() {
    //Initialize the list for all Customer
    List<Customer> customerList = new ArrayList<Customer>();
    if (this.authenticated == true) {
      // Find all users
      List<User> users = DatabaseDriverAndroidHelper.getInstance().getAllUsersDetails();
      for (User current: users){
        //If the current user is a customer, add him to the list
        if (RolesMap.getInstance().getRole(current.getRoleId()) == Roles.CUSTOMER) {
          customerList.add((Customer) current);
        }
      }
    }
    return customerList;
  }
  
  @Override
  public BigDecimal getuserbalance(int customerid) {
    // Get the current customer's information
    Customer currentcustomer = (Customer) DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(customerid);
    // Check if the admin is authenticated.
    if (!this.authenticated == true) {
      return null;
    } else {
      // Total is used to count the account total balance.
      BigDecimal total = new BigDecimal("0");
      List<Account> accounts = currentcustomer.getAccounts();
      for (Account account:accounts) {
        total = total.add(account.getBalance());
      }
      return total;
    }
  }
  
  @Override
  public BigDecimal getallbalance() {
    // Total is used to count the total balance.
    BigDecimal total = new BigDecimal("0");
    if (this.authenticated == true) {
      List<Customer> customers = this.listAllCustomers();
      for (Customer customer:customers) {
        total = total.add(this.getuserbalance(customer.getId()));
      }
    }
    return total;
  }

  @Override
  public String viewSpecificMessage(int messageId) {
    // If not authenticated, do nothing return null
    if (this.authenticated == false) {
      return null;
    }
    // Else
    // Try to get the message
    String content = null;
    // The database helper will take care of whether that message is valid
    content = DatabaseDriverAndroidHelper.getInstance().getSpecificMessage(messageId);
    return content;
  }

  @Override
  public List<Message> viewMyMessages() {
    // Initialize the answer list
    List<Message> myMessages = new ArrayList<Message>();
    // If authenticated
    if (this.authenticated == true) {
      // Try to get all messages left for the current admin 
      myMessages = DatabaseDriverAndroidHelper.getInstance().getOneUserAllMessages(this.currentAdmin.getId());

    }
    // else do nothing
    return myMessages;
  }

  @Override
  public boolean changeMyMessageState(int messageId) {
    // If not authenticated, do nothing
    if (this.authenticated == false) {
      return false;
    }
    // Else
    // Check whether is message is left for the current admin
    List<Message> myMessages = this.viewMyMessages();
    for (Message current : myMessages) {
      // If yes, try to update its viewed state
      if (current.getMessageId() == messageId) {
        return DatabaseDriverAndroidHelper.getInstance().updateUserMessageState(messageId);
      }
    }
    // If the message is not for the current admin, return false
    return false;
  }

  @Override
  public boolean leaveMessage(int userId, String content) {
    // If not authenticated, do nothing
    if (this.authenticated == false) {
      return false;
    }
    // Else
    // try to insert a message for the given user in the database
	if (DatabaseDriverAndroidHelper.getInstance().insertMessage(userId, content) != -1) {
	  return true;
	} else {
	  return false;
	}
  }
  
  @Override
  public boolean changetoadmin(int tellerid) throws NoAccessToAccountException {
    if (this.authenticated == false) {
      return false;
    } else {
      int oldroleid = DatabaseDriverAndroidHelper.getInstance().getUserRole(tellerid);
      int newroleid = RolesMap.getInstance().getId(Roles.ADMIN);
      if (RolesMap.getInstance().getRole(oldroleid) == Roles.TELLER) {
        return DatabaseDriverAndroidHelper.getInstance().updateUserRole(newroleid, tellerid);
      } else {
        throw (new NoAccessToAccountException());
      }
    }
  }

  @Override
  public boolean serializeDatabase(Context context) {
	// If is not authenticated, return false
	if (!this.authenticated) {
	  return false;
	}
	// Else, try to serialize
	try {
      File file = new File(context.getFilesDir(), "database_copy.ser");
      FileOutputStream fileout= new FileOutputStream(file);
	  //FileOutputStream fileout = new FileOutputStream("./database_copy.ser");
	  ObjectOutputStream out = new ObjectOutputStream(fileout);
	  // Get the biggest account ID
	  List<Customer> allCustomer = this.listAllCustomers();
	  int maxAccountId = 0;
	  for (Customer currentCustomer : allCustomer) {
		List<Account> allAccounts = currentCustomer.getAccounts();
		for (Account currentAccount : allAccounts) {
		  maxAccountId = (currentAccount.getId() > maxAccountId)
				  ? currentAccount.getId() : maxAccountId;
		}
	  }
	  // write out total accounts number
	  out.writeInt(maxAccountId);
	  // write out each account one by one
	  for (int i = 1; i <= maxAccountId; i++) {
		out.writeObject(DatabaseDriverAndroidHelper.getInstance().getOneAccountDetails(i));
	  }

	  // Get all users and total user number
	  List<User> allUser = DatabaseDriverAndroidHelper.getInstance().getAllUsersDetails();
	  int maxUserId = allUser.size();
	  //write out total user number
	  out.writeInt(maxUserId);
	  // write out each user's information one by one
	  for (int i = 1; i <= maxUserId; i++) {
		// User object
		out.writeObject(DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(i));
		// the user's password
		out.writeObject(DatabaseDriverAndroidHelper.getInstance().getPassword(i));
		// the message number for this user
		List<Message> allMessages = DatabaseDriverAndroidHelper.getInstance().getOneUserAllMessages(i);
		out.writeInt(allMessages.size());
		// all the messages for this user
		for (Message current: allMessages) {
		  out.writeObject(current);
		}
	  }

      // write out the account type number, account type name and corresponding interest rate
      out.writeInt(AccountTypes.values().length);
      for (AccountTypes current: AccountTypes.values()) {
        BigDecimal rate = DatabaseDriverAndroidHelper.getInstance()
                .getInterestRate(AccountTypeMap.getInstance().getId(current));
        out.writeObject(current.toString());
        out.writeObject(rate);
      }
	  //close the output file
	  out.close();
	  fileout.close();

	} catch (Exception e) {
	  // If any exception caught, return false
	  return false;
	}
	return true;
  }

  @Override
  public boolean deserializeDatabse(Context context) {
	// If is not authenticated, return false
	if (!this.authenticated) {
	  return false;
	}
	// Else
	// First keep the current admin's password and all messages
	String adminPassword = DatabaseDriverAndroidHelper.getInstance().getPassword(this.currentAdmin.getId());
	List<Message> adminMessages = this.viewMyMessages();

	// Second, clean the current database
	DatabaseDriverAndroidHelper.getInstance()
            .onUpgrade(DatabaseDriverAndroidHelper.getInstance().getWritableDatabase(), 1, 2);

	// Insert all roles
	for (Roles current: Roles.values()) {
	  // If insert fail, return -1;
	  if (DatabaseDriverAndroidHelper.getInstance().insertRole(current.toString()) == -1) {
		return false;
	  }
	}
	// Update RolesMap
	RolesMap.getInstance().update();


	// Insert all account types
	for (AccountTypes current: AccountTypes.values()) {
	  // If insert fail, return false
	  if (DatabaseDriverAndroidHelper.getInstance()
			  .insertAccountType(current.toString(), new BigDecimal("0.0")) == -1) {
		return false;
	  }
	}
	// Update AccountTypeMap
	AccountTypeMap.getInstance().update();

	FileInputStream filein = null;
	ObjectInputStream copyIn = null;
	// Third, try to deserialize a backup
	try {
      File file = new File(context.getFilesDir(), "database_copy.ser");
	  filein = new FileInputStream(file);
      //filein = new FileInputStream("database_copy.ser");
	  copyIn = new ObjectInputStream(filein);
	  // Get the biggest account ID
	  int maxAccountId = copyIn.readInt();
	  // Insert all account
	  for (int i = 1; i <= maxAccountId; i++) {
		Account current = (Account) copyIn.readObject();
		int typeId = -1;
		// get account typeId
		if (current instanceof BalanceOwing) {
		  typeId = AccountTypeMap.getInstance().getId(AccountTypes.BALANCEOWING);
		} else if (current instanceof ChequingAccount) {
		  typeId = AccountTypeMap.getInstance().getId(AccountTypes.CHEQUING);
		} else if (current instanceof RestrictedSavings) {
		  typeId = AccountTypeMap.getInstance().getId(AccountTypes.RESTRICTEDSAVINGS);
		} else if (current instanceof SavingsAccount) {
		  typeId = AccountTypeMap.getInstance().getId(AccountTypes.SAVING);
		} else if (current instanceof Tfsa) {
		  typeId = AccountTypeMap.getInstance().getId(AccountTypes.TFSA);
		}
		// If type is invalid, return false
		if (typeId == -1) {
		  copyIn.close();
		  filein.close();
		  return false;
		}
		// Update the interest rate
		DatabaseDriverAndroidHelper.getInstance().updateAccountTypeInterestRate(current.getInterestRate(), typeId);
		// insert this account to database
		DatabaseDriverAndroidHelper.getInstance().insertAccount(current.getName(), current.getBalance(), typeId);
	  }

	  // Get the user number
	  int maxUserId = copyIn.readInt();
	  // Insert all user information
	  for (int i = 1; i <= maxUserId; i++) {
		User current = (User) copyIn.readObject();
		// get user roleId
		int roleId = -1;
		if (current instanceof Admin) {
		  roleId = RolesMap.getInstance().getId(Roles.ADMIN);
		} else if (current instanceof Teller) {
		  roleId = RolesMap.getInstance().getId(Roles.TELLER);
		} else if (current instanceof Customer) {
		  roleId = RolesMap.getInstance().getId(Roles.CUSTOMER);
		}

		// If role is invalid, return false
		if (roleId == -1) {
		  copyIn.close();
		  filein.close();
		  return false;
		}

		// Insert the user
		long userId = DatabaseDriverAndroidHelper.getInstance().insertNewUser(current.getName(), current.getAge(),
				current.getAddress(), roleId, "1");
		// Read in the hashed password of this user
		String hashedPassword = (String) copyIn.readObject();
		DatabaseDriverAndroidHelper.getInstance().reloadUserHashedPassword(hashedPassword, (int) userId);

		// Insert all Messages for this user
		int messageNumber = copyIn.readInt();
		for (int j = 1; j <= messageNumber; j++) {
		  Message currentMessage = (Message) copyIn.readObject();
		  DatabaseDriverAndroidHelper.getInstance().insertMessage((int)userId, currentMessage.getContent());
          /*if (currentMessage.getViewedState() == 1) {
            DatabaseUpdateHelper.updateUserMessageState()
          }*/
		}

		// If current user is an customer, insert user account relationship
		if (current instanceof Customer) {
		  Customer currentCustomer = (Customer) current;
		  List<Account> userAccounts = currentCustomer.getAccounts();
		  for (Account currentAccount: userAccounts) {
			DatabaseDriverAndroidHelper.getInstance().insertUserAccount((int)userId, currentAccount.getId());
		  }
		}
	  }

      // get interest rate
      int accountTypeNumber = copyIn.readInt();
      for (int i = 1; i <= accountTypeNumber; i++) {
        // read in one pair of name and interest rate
        String name = (String) copyIn.readObject();
        BigDecimal rate = (BigDecimal) copyIn.readObject();
        for (AccountTypes current: AccountTypes.values()) {
          // update the corresponding accounts' interest rate
          if (current.toString().equals(name)) {
            DatabaseDriverAndroidHelper.getInstance()
                    .updateAccountTypeInterestRate(rate, AccountTypeMap.getInstance()
                            .getId(current));
          }
        }
      }

	  // If the maxUserId is less than the current Admin's
	  if (maxUserId < this.currentAdmin.getId()) {
		long userId = DatabaseDriverAndroidHelper.getInstance().insertNewUser(currentAdmin.getName(),
				currentAdmin.getAge(), currentAdmin.getAddress(),
				RolesMap.getInstance().getId(Roles.ADMIN), "1");
		DatabaseDriverAndroidHelper.getInstance().reloadUserHashedPassword(adminPassword, (int) userId);
		for (Message currentMessage : adminMessages) {
		  DatabaseDriverAndroidHelper.getInstance().insertMessage((int) userId, currentMessage.getContent());
		}
	  }
	  // close the input file
	  copyIn.close();
	  filein.close();
	} catch (Exception e) {
	  // If any exception caught, return false
	  return false;
	}

	// close the input file
	try {
	  copyIn.close();
	  filein.close();
	} catch (IOException e) {
	  //do nothing
	}

	return true;
  }
}
