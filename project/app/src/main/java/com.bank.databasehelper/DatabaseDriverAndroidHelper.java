package com.bank.databasehelper;

import android.content.Context;
import android.database.Cursor;

import com.bank.account.Account;
import com.bank.account.AccountFactory;
import com.bank.database.DatabaseDriverA;
import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.message.Message;
import com.bank.message.MessageImpl;
import com.bank.security.PasswordHelpers;
import com.bank.user.Customer;
import com.bank.user.User;
import com.bank.user.UserFactory;
import com.bank.validation.AccountTypeNameValidator;
import com.bank.validation.AccountTypeNameValidatorPc;
import com.bank.validation.AccountValidator;
import com.bank.validation.AccountValidatorPc;
import com.bank.validation.MessageValidator;
import com.bank.validation.MessageValidatorPc;
import com.bank.validation.RoleNameValidator;
import com.bank.validation.RoleNameValidatorPc;
import com.bank.validation.UserValidator;
import com.bank.validation.UserValidatorPc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2017/7/29.
 */

public class DatabaseDriverAndroidHelper extends DatabaseDriverA{

    private static DatabaseDriverAndroidHelper DatabaseHelper;
    /**
     * Constructor for an instance of DatabaseDriverAndroidHelper.
     *
     * @param context the context of the application
     */
    private DatabaseDriverAndroidHelper(Context context) {
        super(context);
    }

    /**
     * Initialize the singleton instance.
     *
     * @param context the context of the application
     */
    public static void initialize(Context context) {
        DatabaseHelper = new DatabaseDriverAndroidHelper(context);
    }

    /**
     * Get the singleton instance.
     *
     * @return the singleton instance of DatabaseDriverAndroidHelper
     */
    public static DatabaseDriverAndroidHelper getInstance() {
        return DatabaseHelper;
    }

    // InsertHelper Function
    /**
     * Given a string stands for the name of a role, if the given string is one of the string in
     * the Roles enumerator, insert this role into the database and return true, otherwise, return
     * false.
     *
     * @param role a string stands for the name of a role
     * @return the roleId if successes, otherwise -1
     */
    public long insertRole(String role) {
        //If the given String is null or not in the Roles Enumerator, return false
        RoleNameValidator checker = new RoleNameValidatorPc();
        if (!checker.checkRoleName(role).getResult()) {
            return -1;
        }
        //Try insert the role to the database, -1 when error or not success
        return super.insertRole(role);
    }

    /**
     * Given the name(String), age(int), address(String), roleId(int) and password(String) of a new
     * user, insert a new user into the database and return true, otherwise, return false.
     *
     * @param name a String stands for the name of the new user
     * @param age an integer stands for the age of the new user
     * @param address a String stands for the address of the new user
     * @param roleId an integer stands for the roleId of the new user
     * @param password a String stands for the password of the new user
     * @return the user id of the new user if successful, -1 otherwise
     */
    public long insertNewUser(String name, int age, String address, int roleId, String password) {
        // Check whether the given information are all valid
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkName(name).checkAge(age).checkAddress(address).checkRoleId(roleId)
                .checkPassword(password).getResult()) {
            //If some information is invalid, return -1
            return -1;
        }
        // If all inputs are valid, try insert the new user
        return super.insertNewUser(name, age, address, roleId, password);
    }

    /**
     * Given the name of new account type and its interest rate, if the account type is in the
     * AccountTyprs enumerator and the interest rate is between 0 and 1, insert the new accountType
     * into the accountType table,.
     *
     * @param name the name of the type of account.
     * @param interestRate the interest rate for this type of account.
     * @return the typeId if successful, -1 otherwise.
     */
    public long insertAccountType(String name, BigDecimal interestRate) {
        // Check the account type name of the account.
        AccountTypeNameValidator checker = new AccountTypeNameValidatorPc();
        if (!checker.checkAccountTypeName(name).checktypeInterestrate(interestRate).getResult()) {
            return -1;
        }
        //If both inputs are valid, try to insert the new account type
        return super.insertAccountType(name, interestRate);
    }

    /**
     * Given the name, the initial balance and typeId of a new account, try to insert a new account
     * into account table. If success, return the account id of the new account, otherwise return -1.
     *
     * @param name the name of the account.
     * @param balance the balance currently in account.
     * @param typeId the id of the type of the account.
     * @return the account id of the new account if successful, -1 otherwise
     */
    public long insertAccount(String name, BigDecimal balance, int typeId) {
        // check the name of the account.
        AccountValidator checker = new AccountValidatorPc();
        if (!checker.checkName(name).checkBalance(balance, typeId).checkAccountType(typeId).getResult()) {
            // If the name/balance/typeId is invalid, return 1.
            return -1;
        }
        // Round given balance to 2 decimal places
        balance = balance.setScale(2, BigDecimal.ROUND_HALF_UP);
        //Try to insert the new account
        return super.insertAccount(name, balance, typeId);
    }

    /**
     * Given an userId and an accountId, try to insert a new user-account relation into UserAccount
     * table. If success, return whether the insert success.
     *
     * @param userId an integer stands for the user
     * @param accountId an integer stands for the account
     * @return the unique id of the relationship if success, otherwise -1
     */
    public long insertUserAccount(int userId, int accountId) {
        //Check whether the user and account exist
        //If not, return false;
        if (this.getUserRole(userId) == -1 || this.getAccountType(accountId) == -1) {
            return -1;
        }
        //Try insert the user account relation
        return super.insertUserAccount(userId, accountId);
    }

    /**
     * Given an userId and a message, try to insert a new message into UserMessage table. Return
     * whether the insert success.
     *
     * @param userId an integer stands for the user
     * @param message a string stands for the content of the message
     * @return the messageId if success, -1 otherwise
     */
    public long insertMessage(int userId, String message) {
        // Check whether the user exist, if not, return false;
        if (this.getUserRole(userId) == -1) {
            return -1;
        }
        // Check the message validation
        MessageValidator checker = new MessageValidatorPc();
        if (!checker.checkContent(message).getResult()) {
            // If the message is not valid, return false
            return -1;
        }
        //Try insert the message
        return super.insertMessage(userId, message);
    }

    // SelectHelper methods
    /**
     * Get all roleId of all the roles in the role table.
     *
     * @return a List of Integer which records all role ids.
     */
    public List<Integer> getAllRoles() {
        // Initialize the answer list
        List<Integer> ids = new ArrayList<>();
        Cursor cursor = super.getRoles();
        // if the cursor is not empty
        if (cursor.moveToFirst()) {
            // Add every roleId in the list
            while (!cursor.isAfterLast()) {
                ids.add(cursor.getInt(cursor.getColumnIndex("ID")));
                //move to next
                cursor.moveToNext();
            }
        }
        // Close the cursor
        cursor.close();
        // return the answer list
        return ids;
    }

    /**
     * Given a roleId, return the string representation of this role.
     *
     * @param id an integer stands for the roleId
     * @return a String represents the asking role is roleId valid, NULL otherwise
     */
    public String getRole(int id) {
      try {
        return super.getRole(id);
      } catch (Exception e) {
        return null;
      }
    }

    /**
     * Given a userId , return the roleId of this user.
     *
     * @param userId an integer stands for the userId
     * @return an integer stands for the the roleId of this user if no error occurs,
     *     otherwise return -1
     */
    public int getUserRole(int userId) {
      int roleId = -1;
      try {
        roleId = super.getUserRole(userId);
      } catch (Exception e) {
        return -1;
      }
      return roleId;
    }

    /**
     * Given a userId, return a User instance which records all information of this user.
     *
     * @param userId a integer stands for a user id
     * @return an object of User which recodes all information of this user if no error occurs,
     *     otherwise, return null.
     */
    public User getOneUserDetails(int userId) {
        // Initialize the answer
        User askingUser = null;
        //Try to create an instance
        Cursor cursor = super.getUserDetails(userId);
        //If the cursot is not empty
        if(cursor.moveToFirst()) {
            // Get the user id of the current user
            while(!cursor.isAfterLast()) {
                //Get the role id, age, name and address of this user
                //since these information have been in database, they should be all valid information
                int roleId = cursor.getInt(cursor.getColumnIndex("ROLEID"));
                int age = cursor.getInt(cursor.getColumnIndex("AGE"));
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                String address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
                //Get the the role name of this user according to his roleId
                Roles role = RolesMap.getInstance().getRole(roleId);
                //Use the user factory to get the right type of instance of User
                UserFactory factory = new UserFactory();
                askingUser = factory.getUser(role, userId, name, age, address);
                //If this user is a customer, add all accounts of this customer to his customer list
                if (role == Roles.CUSTOMER) {
                    Customer askingCustomer = (Customer) askingUser;
                    //Get all accountIds of this customer
                    List<Integer> allAccountIds = this.getOneUserAccountIds(userId);
                    for (int accountId : allAccountIds) {
                        //For each account id, get the account details
                        askingCustomer.addAccount(this.getOneAccountDetails(accountId));
                    }
                    askingUser = askingCustomer;
                }
                //move to next
                cursor.moveToNext();
            }
        }
        // Close the cursor
        cursor.close();
        // return the final answer
        return askingUser;
    }

    /**
     * Get all users' detail in the database.
     *
     * @return a list of User which records all details of all users in the database
     */
    public List<User> getAllUsersDetails() {
        // Initialize the answer list
        List<User> allUsers = new ArrayList<>();
        Cursor cursor = super.getUsersDetails();
        // if the cursor is not empty
        if(cursor.moveToFirst()) {
            // Get the user id of the current user
            while(!cursor.isAfterLast()) {
                int userId = cursor.getInt(cursor.getColumnIndex("ID"));
                User current = this.getOneUserDetails(userId);
                allUsers.add(current);
                // move to next
                cursor.moveToNext();
            }
        }
        // Close the cursor
        cursor.close();
        // return answer list
        return allUsers;
    }

    /**
     * Given a user id, return the hashed version of the password of this user.
     *
     * @param userId an integer stands for the user's id.
     * @return a String stands for the hashed password of the given user if success, NULL otherwise
     */
    public String getPassword(int userId) {
      try {
        return super.getPassword(userId);
      } catch (Exception e) {
        return null;
      }
    }

    /**
     * Given a user Id, return a list which records all account ids for this user.
     *
     * @param userId an integer stands for the user
     * @return a list of integer which records all account ids for this user
     */
    public List<Integer> getOneUserAccountIds(int userId) {
        // Initialize the answer list
        List<Integer> accountIds = new ArrayList<>();
        Cursor cursor = super.getAccountIds(userId);
        // if the cursor is not empty
        if (cursor.moveToFirst()) {
            // Get all account id
            while (!cursor.isAfterLast()){
                int accountId = cursor.getInt(cursor.getColumnIndex("ACCOUNTID"));
                accountIds.add(accountId);
                // move to next
                cursor.moveToNext();
            }
        }
        //close the cursor
        cursor.close();
        // return the answer
        return accountIds;
    }

    /**
     * Given a accountId, return a Account instance which records all information of this account.
     *
     * @param accountId an integer stands for the accountId
     * @return an object of Account which recodes all information of this account if no error occurs,
     *     otherwise, return null.
     */
    public Account getOneAccountDetails(int accountId) {
        // Initialize the answer
        Account askingAccount = null;
        Cursor cursor = super.getAccountDetails(accountId);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //Get the name, balance and type of this account
                //Since these information has been in database, they should all be valid
                String accountName = cursor.getString(cursor.getColumnIndex("NAME"));
                String balanceString = cursor.getString(cursor.getColumnIndex("BALANCE"));
                int accountTypeId = cursor.getInt(cursor.getColumnIndex("TYPE"));
                // Create a BigDecimal to store the balance of the account
                BigDecimal balance = new BigDecimal(balanceString);
                //Get the account type of this account
                AccountTypes accountTypeName = AccountTypeMap.getInstance().getAccount(accountTypeId);
                //Create the right type of account according to its type
                AccountFactory factory = new AccountFactory();
                askingAccount = factory.getAccount(accountTypeName, accountId, accountName, balance);
                // move to next
                cursor.moveToNext();
            }
        }
        // close the cursor
        cursor.close();
        return askingAccount;
    }

    /**
     * Given an accountId, return the balance of this account.
     *
     * @param accountId an integer stands for the accountId
     * @return a BigDecimal which records the balance of the account if success, otherwise NULL
     */
    public BigDecimal getBalance(int accountId) {
        BigDecimal balance;
        try {
            balance = super.getBalance(accountId);
        } catch (Exception e) {
            return null;
        }
        return balance;
    }

    /**
     * Given an accountId, return the typeId of this account.
     *
     * @param accountId an integer stands for the accountId
     * @return an integer stands for the typeId of this account if no error occurs,
     *     otherwise return -1.
     */
    public int getAccountType(int accountId) {
      int accountTypeId = -1;
      try {
        accountTypeId =super.getAccountType(accountId);
      } catch (Exception e) {
        return -1;
      }
      return accountTypeId;
    }

    /**
     * Given an accountTypeId, return the string representation of this account type.
     *
     * @param accountTypeId an integer stands for the accountTypeId
     * @return the string representation of this account type if success, NUll otherwise
     */
    public String getAccountTypeName(int accountTypeId) {
      try {
        return super.getAccountTypeName(accountTypeId);
      } catch (Exception e) {
        return null;
      }
    }

    /**
     * Return all existing account type ids.
     *
     * @return a list of integer which records all existing account type ids
     */
    public List<Integer> getAccountTypesIds() {
        //Initialize the answer list
        List<Integer> ids = new ArrayList<>();
        //Try to get all account type ids
        Cursor cursor = super.getAccountTypesId();
        // if the cursor is not empty
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ids.add(cursor.getInt(cursor.getColumnIndex("ID")));
                // move to next
                cursor.moveToNext();
            }
        }
        // close the cursor
        cursor.close();
        return ids;
    }

    /**
     * Given a accountTypeId , return the interest rate of this type of account.
     * @param accountType an integer stands for the accountType
     * @return a BigDecimal stands for the interest rate of this kind of account if success, NULL
     *     otherwise
     */
    public BigDecimal getInterestRate(int accountType) {
        BigDecimal interestRate;
        try {
            interestRate = super.getInterestRate(accountType);
        } catch (Exception e) {
            return null;
        }
        return interestRate;
    }

    /**
     * Given a userId, try to get a list of messages which are left for this user.
     *
     * @param userId an integer stands for the user
     * @return a list of Messages which contains all messages which are left for the given user
     */
    public List<Message> getOneUserAllMessages(int userId) {
        // Initialize the answer
        List<Message> allMessages = new ArrayList<>();
        //Try to get the messages list
        Cursor cursor = super.getAllMessages(userId);
        // if the cursor is not empty
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //Get the information of the current message
                //Since these information has been in database, they should all be valid
                String content = cursor.getString(cursor.getColumnIndex("MESSAGE"));
                int messageId = cursor.getInt(cursor.getColumnIndex("ID"));
                int viewedState =cursor.getInt(cursor.getColumnIndex("VIEWED"));
                // Create a Message instance for the current message
                Message current = new MessageImpl(content, messageId, userId, viewedState);
                // Add the message to the list
                allMessages.add(current);
                //move to next
                cursor.moveToNext();
            }
        }
        //close the cursor
        cursor.close();
        return allMessages;
    }

    /**
     * Given a messageId, try to get the message with the given id.
     *
     * @param messageId the messageId of a message we want to find
     * @return the message with the given messageId if success, NULL otherwise
     */
    public String getSpecificMessage(int messageId) {
	  try {
		return super.getSpecificMessage(messageId);
	  } catch (Exception e) {
		return null;
	  }
    }


    //UpdateHelper Methods
    /**
     * Given a name and a roleId, try to update the role name of the given role, return whether the
     * update success.
     *
     * @param name a String stands for the new name of the role
     * @param id an integer stands for the role
     * @return whether the update success
     */
    public boolean updateRoleName(String name, int id) {
        //If the given String is null or not in the Roles Enumerator, return false
        RoleNameValidator checker = new RoleNameValidatorPc();
        if (!checker.checkRoleName(name).getResult()) {
            return false;
        }
        // try to update role name
        return super.updateRoleName(name, id);
    }

    /**
     * Given a name and a userId, try to update the name of the given user, return whether the
     * update success.
     *
     * @param name a String stands for the new name of the user
     * @param id an integer stands for the user
     * @return whether the update success.
     */
    public boolean updateUserName(String name, int id) {
        //if the given name is null, return false
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkName(name).getResult()) {
            return false;
        }
        // try to update user name
        return super.updateUserName(name, id);
    }

    /**
     * Given a name and a userId, try to update the age of the given user, return whether the
     * update success.
     *
     * @param age an integer stands for the new age of the user
     * @param id an integer stands for the user
     * @return whether the update success.
     */
    public boolean updateUserAge(int age, int id) {
        //if the given age is invalid, return false
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkAge(age).getResult()) {
            return false;
        }
        // try to update user age
        return super.updateUserAge(age, id);
    }

    /**
     * Given a roleId and a userId, try to update the role of the given user, return whether the
     * update success.
     *
     * @param roleId an integer stands for the new role of the user
     * @param id an integer stands for the user
     * @return whether the update success.
     */
    public boolean updateUserRole(int roleId, int id) {
        //if the given roleId does not exist, return false
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkRoleId(roleId).getResult()) {
            return false;
        }
        // try to update user role
        return super.updateUserRole(roleId, id);
    }

    /**
     * Given an address and a userId, try to update the address of the given user, return whether the
     * update success.
     *
     * @param address an String stands for the new role of the user
     * @param id an integer stands for the user
     * @return whether the update success.
     */
    public boolean updateUserAddress(String address, int id) {
        //if the length of the given address is more than 100 or the given address is null, return
        //false
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkAddress(address).getResult()) {
            return false;
        }
        //try to update user address
        return super.updateUserAddress(address, id);
    }

    /**
     * Given a name and an accountId, try to update the name of the given account, return whether
     * the update success.
     *
     * @param name a String stands for the new name of the account
     * @param id an integer stands for the account
     * @return whether the update success.
     */
    public boolean updateAccountName(String name, int id) {
        // check the name of the account.
        AccountValidator checker = new AccountValidatorPc();
        if (!checker.checkName(name).getResult()) {
            return false;
        }
        // try to update account name
        return super.updateAccountName(name, id);
    }

    /**
     * Given a balance and an accountId, try to update the balance of the given account, return
     * whether the update success.
     *
     * @param balance a BigDecimal which records the new balance of the account
     * @param id an integer stands for the account
     * @return whether the update success.
     */
    public boolean updateAccountBalance(BigDecimal balance, int id) {
        int typeid = this.getOneAccountDetails(id).getType();
        // check the balance of the account.
        AccountValidator checker = new AccountValidatorPc();
        if (!checker.checkBalance(balance, typeid).getResult()) {
            return false;
        }
        return super.updateAccountBalance(balance, id);
    }

    /**
     * Given a typeId and an accountId, try to update the type of the given account, return
     * whether the update success.
     *
     * @param typeId an integer stands for the new type
     * @param id an integer stands for the account
     * @return whether the update success.
     */
    public boolean updateAccountType(int typeId, int id) {
        // check the typeid of the account.
        AccountValidator checker = new AccountValidatorPc();
        if (!checker.checkAccountType(typeId).getResult()) {
            return false;
        }
        return super.updateAccountType(typeId, id);
    }

    /**
     * Given a name and an accountTypeId, try to update the name of the given account type, return
     * whether the update success.
     *
     * @param name a String stands for the new name of the account type
     * @param id an integer stands for the account type
     * @return whether the update success.
     */
    public boolean updateAccountTypeName(String name, int id) {
        // Check the account type name of the account.
        AccountTypeNameValidator checker = new AccountTypeNameValidatorPc();
        if (!checker.checkAccountTypeName(name).getResult()) {
            return false;
        }
        // try to update account type name
        return super.updateAccountTypeName(name, id);
    }

    /**
     * Given an new interest rate and an accountTypeId, try to update the interest rate of the given
     * account type, return whether the update success.
     *
     * @param interestRate a BigDecimal which records the new interest rate of the account type
     * @param id an integer stands for the account type
     * @return whether the update success.
     */
    public boolean updateAccountTypeInterestRate(BigDecimal interestRate, int id) {
        // Check the account interest rate of the account.
        AccountTypeNameValidator checker = new AccountTypeNameValidatorPc();
        if (!checker.checktypeInterestrate(interestRate).getResult()) {
            return false;
        }
        // try update account type interest rate
        return super.updateAccountTypeInterestRate(interestRate, id);
    }

    /**
     * Given a userId and a plain password, try to update the password of the given user with the
     * given password.
     *
     * @param password the plain password of the user
     * @param id the id of the user
     * @return true if update succeeded, false otherwise.
     */
    public boolean updateUserPassword(String password, int id) {
        // Check whether the given password is valid
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkPassword(password).getResult()) {
            return false;
        }
        // Get the hashed password
        String hashedPassword = PasswordHelpers.passwordHash(password);
        // try to update user password
        return super.updateUserPassword(hashedPassword, id);
    }

    /**
     * Given a userId and a hashed password, try to set the password of the given user with the
     * given hashed password.
     *
     * @param hashedPassword the hashed password of the user
     * @param userId the id of the user
     * @return true if update succeeded, false otherwise.
     */
    public boolean reloadUserHashedPassword(String hashedPassword, int userId) {
        // Check whether the given password is valid
        UserValidator checker = new UserValidatorPc();
        if (!checker.checkPassword(hashedPassword).getResult()) {
            return false;
        }
        // try to update user password
        return super.updateUserPassword(hashedPassword, userId);
    }

    /**
     * Given a messageId, try to update the viewed state of the given message.
     *
     * @param id the message id of the message we want to change
     * @return true if update succeeded, false otherwise.
     */
    public boolean updateUserMessageState(int id) {
        return super.updateUserMessageState(id);
    }
}
