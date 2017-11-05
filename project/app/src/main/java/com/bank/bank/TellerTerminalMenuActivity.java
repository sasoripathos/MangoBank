package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.exceptions.OtherCustomerExistException;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;
import com.bank.validation.MessageValidator;
import com.bank.validation.MessageValidatorPc;
import com.bank.validation.UserValidator;
import com.bank.validation.UserValidatorPc;

public class TellerTerminalMenuActivity extends AppCompatActivity {

  TellerTerminal tt;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_teller_terminal_menu);
    // get a tellerTerminal from login screen
    Intent start = getIntent();
    tt = (TellerTerminal) start.getSerializableExtra("tellerTerminal");
    // find set the welcome text to be the user's name and Address
    TextView welcomeText = (TextView)findViewById(R.id.tellerTerminalWelcomeText);
    Teller teller = (Teller) DatabaseDriverAndroidHelper
        .getInstance().getOneUserDetails(tt.getCurrentTellerId());
    welcomeText.setText("Welcome, " + teller.getName() + "\nAddress: " + teller.getAddress());
  }

  /**
   * will start the deposit function when the deposit button is pressed.
   * @param v the view.
   */
  public void depositOnClick(View v) {
    // check if the current user is authenticated
    if (tt.isCurrentCustomerAuthenticated()) {
      // pass in the tellerTerminal to the deposit Activity
      Intent intent = new Intent(TellerTerminalMenuActivity.this, ListAccounts.class);
      intent.putExtra("call", 1);
      intent.putExtra("tellerTerminal", tt);
      // start deposit activity
      this.startActivity(intent);
    } else {
      // pop up dialogue to prevent user from using function
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void withdrawOnClick(View v) {
    // check if the current customer is authenticated
    if (tt.isCurrentCustomerAuthenticated()) {
      // pass in the tellerTerminal to the withdraw activity
      Intent intent = new Intent(TellerTerminalMenuActivity.this, ListAccounts.class);
      intent.putExtra("call", 2);
      intent.putExtra("tellerTerminal", tt);
      this.startActivity(intent);
    } else {
      // pop up dialogue to prevent user from using function
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void makeNewCustomerOnClick(View v) {
    // when pressed, pass in tellerTerminal to the make new customer activity
    Intent intent = new Intent(TellerTerminalMenuActivity.this, MakeNewCustomerActivity.class);
    intent.putExtra("tellerTerminal", tt);
    // start make new Customer Activity
    this.startActivity(intent);
  }

  public void makeNewAccountOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if the customer is authenticated, pass in the tellerTerminal the make new account Activity
      Intent intent = new Intent(TellerTerminalMenuActivity.this, MakeNewAccountActivity.class);
      intent.putExtra("tellerTerminal", tt);
      // start activity
      this.startActivity(intent);
    } else {
      // pop up dialoge to prevent user from using function
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void listAccountsOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if the customer is authenticated, pass in the tellerTerminal to the next activity
      Intent intent = new Intent(TellerTerminalMenuActivity.this, ListAccounts.class);
      intent.putExtra("call", 0);
      intent.putExtra("tellerTerminal", tt);
      // start the activity
      this.startActivity(intent);
    } else {
      // pop up dialogue to prevent user from using function
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void changeAddressOnClick(View v) {
    // change the address of the current customer
    if (tt.isCurrentCustomerAuthenticated()) {
      // if the customer is authenticated, create an input dialog
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Change Customer Address");
      dialog.setMessage("Please enter customer's new address");
      final EditText edittext = new EditText(this);
      dialog.setView(edittext);
      // get the address placed in the input box
      dialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          boolean success = false;
          // if confirmed, then attempt to change address
          // use the string the user entered
          String address = edittext.getText().toString();
          UserValidator m = new UserValidatorPc();
          if (m.checkAddress(address).getResult() && address != null) {
            if (tt.changeaddress(address)) {
              success = true;
            }
          }
          // Create a dialog for successful change of the address
          AlertDialog.Builder succDialog = new AlertDialog.Builder(TellerTerminalMenuActivity.this);
          String prompt = success ? "Address Successfully Changed!" : "Failed to Change Address.";
          succDialog.setMessage(prompt);
          succDialog.setPositiveButton("OK", null);
          succDialog.show();
        }
      });
      dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          // on cancel
        }
      });
      dialog.show();
    } else {
      // if there is not a valid cusotmer entered, then send error alert
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void changePasswordOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is a valid customer, then create a prompt for input
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Change Customer Password");
      dialog.setMessage("Please enter customer's new password");
      final EditText edittext = new EditText(this);
      edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
      dialog.setView(edittext);
      // get the input from the user in the prompt
      dialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          boolean success = false;
          // if confirmed to change password, then attempt change
          // use the string the user entered
          String password = edittext.getText().toString();
          UserValidator m = new UserValidatorPc();
          if(m.checkPassword(password).getResult()) {
            if(tt.changepassword(password)) {
              success = true;
            }
          }
          // create a dialog for successful change
          AlertDialog.Builder succDialog = new AlertDialog.Builder(TellerTerminalMenuActivity.this);
          String prompt = success ? "Password Successfully Changed!" : "Failed to Change Password.";
          succDialog.setMessage(prompt);
          succDialog.setPositiveButton("OK", null);
          succDialog.show();
        }
      });
      // if cancel selected, do nothing
      dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          // on cancel
        }
      });
      dialog.show();
    } else {
      // if there is no valid customer logged in then send error
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void leaveMessageOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is a valid customer then attempt to leave message
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Leave Message");
      dialog.setMessage("Please enter the message you want to leave for you customer");
      final EditText edittext = new EditText(this);
      dialog.setView(edittext);
      // get the info in the input prompt for the message to be left
      dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          boolean success = false;
          // use the string the user entered
          String message = edittext.getText().toString();
          MessageValidator m = new MessageValidatorPc();
          if (m.checkContent(message).getResult() && message != null) {
            // check content of the message and then leave it
            if (tt.leaveMessage(message) != 0) {
              // if left successfully then set boolean to true
              success = true;
            }
          }
          // add a display dialog for a successful message left
          AlertDialog.Builder succDialog = new AlertDialog.Builder(TellerTerminalMenuActivity.this);
          String prompt = success ? "Message Successfully Sent!" : "Message Failed to Send.";
          succDialog.setMessage(prompt);
          succDialog.setPositiveButton("OK", null);
          succDialog.show();
        }
      });
      // if cancel option selected, do nothing
      dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          // on cancel
        }
      });
      dialog.show();
    } else {
      // if there is not a valid customer, send error alert
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void viewMessagesOnClick(View v) {
    // create new intent that lets you view all messages, and pass in a serialization of
    // the teller terminal being used
    Intent intent = new Intent(TellerTerminalMenuActivity.this, MessageSelectActivity.class);
    intent.putExtra("id", tt.getCurrentTellerId());
    this.startActivity(intent);
  }

  public void viewCustomerMessagesOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is a valid customer, create intent to make new activity that will let you
      // see the customer's messages
      Intent intent = new Intent(TellerTerminalMenuActivity.this, MessageSelectActivity.class);
      intent.putExtra("id", tt.getCurrentCustomerId());
      this.startActivity(intent);
    } else {
      // if there is no valid user then send error alert
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void changeCustomerName(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is a valid user, then attempt to change name
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Change Name");
      dialog.setMessage("Please enter the customer's new name");
      final EditText edittext = new EditText(this);
      dialog.setView(edittext);
      // get new name from input text
      dialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          boolean success = false;
          // use the string the user entered
          String name = edittext.getText().toString();
          UserValidator m = new UserValidatorPc();
          // check if the name is valid
          if (m.checkName(name).getResult()) {
            if (tt.changename(name)) {
              // if name was successfully changed, set the boolean true
              success = true;
            }
          }
          // make a dialog for successful change
          AlertDialog.Builder succDialog = new AlertDialog.Builder(TellerTerminalMenuActivity.this);
          String prompt = success ? "Name Successfully Changed!" : "Failed to Change Name.";
          succDialog.setMessage(prompt);
          succDialog.setPositiveButton("OK", null);
          succDialog.show();
        }
      });
      dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          // on cancel
        }
      });
      dialog.show();
    } else {
      // if there is no valid customer then create error dialog
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void customerSignOutOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is already a customer signed in, then deauthenticate them and change the
      // text being displayed
      tt.deAuthenticateCustomer();
      TextView tv = (TextView) findViewById(R.id.tellerTerminalCustomerWelcomeText);
      tv.setText("No Customer Signed In");
      Button b = (Button) findViewById(R.id.tellerTerminalCustomerSignOutButton);
      b.setText("Customer Sign In");
    }
    else {
      // if there is no current customer then set one up
      String id;
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Enter Customer Credentials");
      dialog.setMessage("Please enter the customer's id and password");
      LayoutInflater inflater = getLayoutInflater();
      View rootView = inflater.inflate(R.layout.customer_log_in_layout, null);
      dialog.setView(rootView);
      final EditText nameET = (EditText) rootView.findViewById(R.id.customerSignInNameEditText);
      final EditText passET = (EditText) rootView.findViewById(R.id.customerSignInPasswordEditText);
      // create prompt to get customer id and password
      dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int whichButton) {
          if (tt.isCurrentCustomerAuthenticated()) {
            // error checking for an already authenticated customer
            tt.deAuthenticateCustomer();
          }
          else {
            boolean loginSucc = false;
            // if no current customer create one
            if (nameET.getText().length() != 0 && passET.getText().length() != 0) {
              User customer = DatabaseDriverAndroidHelper.getInstance()
                  .getOneUserDetails(Integer.parseInt(nameET.getText().toString()));
              // create a customer with given credentials and check if valid customer
              if (customer != null) {
                // if user does exist make sure they are a customer
                if (RolesMap.getInstance().getRole(customer.getRoleId()) == Roles.CUSTOMER) {
                  try {
                    // deauthenticate in case and then set the new customer
                    tt.deAuthenticateCustomer();
                    tt.setCurrentCustomer((Customer) customer);
                    tt.authenticateCurrentCustomer(passET.getText().toString());
                    if (tt.isCurrentCustomerAuthenticated()) {
                      // attempt to authenticate the current customer
                      TextView tv = (TextView) findViewById(R.id.tellerTerminalCustomerWelcomeText);
                      tv.setText("Current Customer: " + customer.getName() + "\n Address: " + customer.getAddress());
                      Button b = (Button) findViewById(R.id.tellerTerminalCustomerSignOutButton);
                      // change the display of the button to reflect that there is a customer now
                      b.setText("Sign Out");
                      loginSucc = true;
                    }
                  } catch (OtherCustomerExistException e) {
                    e.printStackTrace();
                  }
                } else {
                  // remind user that there were incorrect credentials
                  nameET.setHint("Invalid Credentials");
                  passET.setHint("Invalid Credentials");
                }
              }
            }
            // if everyhting worked out then create a success dialog to tell that customer is in
            AlertDialog.Builder succDialog = new AlertDialog.Builder(TellerTerminalMenuActivity.this);
            String message = loginSucc ? "Login Success!" : "Login Failed.";
            succDialog.setMessage(message);
            succDialog.setPositiveButton("OK", null);
            succDialog.show();
          }
        }
      });
      // if canceled then do nothing
      dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          // on cancel
        }
      });
      dialog.show();


    }
  }

  public void giveInterestOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is a valid customer open the list of their accounts to choose from
      Intent intent = new Intent(TellerTerminalMenuActivity.this, ListAccounts.class);
      // send in an extra to tell that only interest should be added
      intent.putExtra("call", 3);
      intent.putExtra("tellerTerminal", tt);
      this.startActivity(intent);
    } else {
      // if there is no valid customer then do nothing
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  public void giveInterestToAllOnClick(View v) {
    if (tt.isCurrentCustomerAuthenticated()) {
      // if there is a valid then give interest to all of their accounts
      try {
        tt.giveInterestToAll();
        // create a success dialog to tell that operation is finished
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Success: Give Interest to All");
        dialog.setMessage("Successfully gave interest to all accounts belonging to the customer");
        dialog.setPositiveButton("OK", null);
        dialog.show();
      } catch (NoAccessToAccountException e) {
        // if there was a problem then create error alert to notify user
        e.printStackTrace();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Give Interest to All Accounts Failed");
        dialog.setMessage("Error: " + e.toString());
        dialog.setPositiveButton("OK", null);
        dialog.show();
      }
    } else {
      // if there is no valid customer then send error alert
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("Can Not Use Function: No Customer Logged In");
      dialog.setMessage("Please log in with a customer to use this functionality.");
      dialog.setPositiveButton("OK", null);
      dialog.show();
    }
  }

  // close the activity to sign out the teller
  public void tellerSignOutOnClick(View v) {
    this.finish();
  }
}
