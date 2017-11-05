package com.bank.bank;

import android.content.Context;

import com.bank.exceptions.NoAccessToAccountException;
import com.bank.message.Message;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.Teller;

import java.math.BigDecimal;
import java.util.List;

public interface AdminTerminalInterface {

  /**
   * Create a new admin if it is authenticated.
   * 
   * @param name The new admin's name.
   * @param age The new admin's age.
   * @param address The new admin's current address.
   * @param password The new admin's password.
   * @return The new admin's id.
   */
  public int createAdmin(String name, int age, String address, String password);
    
  /**
   * List all users whose role is admin.
   * 
   * @return The list of admin;
   */
  public List<Admin> listAllAdmins();

  /**
   * Authenticate if the user matches password.
   * @param adminId Id of the admin
   * @param password password of the admin.
   * @return true if authenticated successfully, false if can't authenticate.
   */
  public boolean authenticate(int adminId, String password);
  
  /**
   * List all users whose role is teller.
   * 
   * @return The list of teller;
   */
  public List<Teller> listAllTellers();

  /**
   * List all users whose role is customer.
   * 
   * @return The list of customer;
   */
  public List<Customer> listAllCustomers();

  /**
   * See the total balance of all accounts a given user has open.
   * @param customerid The current customer id.
   * @return The total balance of all accounts of the current user.
   */
  public BigDecimal getuserbalance(int customerid);

  /**
   * See the total balance of all accounts of all users.
   * @return The total balance of all account of all users.
   */
  public BigDecimal getallbalance();
  
  /**
   * Given a messageId, get that message from the database.
   * 
   * @param messageId the id of the message we what to get
   * @return a String which stands for the content of the asking message
   */
  public String viewSpecificMessage(int messageId);

  /**
   * List all messages left for the current admin.
   * 
   * @return a List of all messages left for the current admin
   */
  public List<Message> viewMyMessages();
  
  /**
   * Given a message ID, if this message is left for the current admin, change its state to read.
   * 
   * @param messageId the message ID of the message we want to change the viewed state
   * @return whether the update success
   */
  public boolean changeMyMessageState(int messageId);
  
  /**
   * Given a userId and the content of the message, try to leave a message for the given user.
   * 
   * @param userId the user to whom we want to leave a message
   * @param content the content of the message we want to leave
   * @return whether the leave success
   */
  public boolean leaveMessage(int userId, String content);

  /**
   * Given an admin id, change the teller to an admin.
   * @param tellerid int the id of the teller. 
   * @return true if it is changed successfully. If not, return false.
   * @throws NoAccessToAccountException Throw exceptions if don't have access to the account.
   */
  public boolean changetoadmin(int tellerid) throws NoAccessToAccountException;

  /**
   * Serialize the current database.
   *
   * @return whether the serialize success.
   */
  public boolean serializeDatabase(Context context);

  /**
   * Deserialize a backup version of database.
   *
   * @return whether the deserialize success.
   */
  public boolean deserializeDatabse(Context context);

}

