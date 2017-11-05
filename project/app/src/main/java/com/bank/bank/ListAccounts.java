package com.bank.bank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bank.account.Account;
import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.user.Customer;

import java.math.BigDecimal;
import java.util.List;


public class ListAccounts extends AppCompatActivity {
  List<Account> accounts;
  TellerTerminal tt;
  String[] accs;
  private final Context mcontext = this;
  Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_accounts);
    intent = getIntent();
  }

  @Override
  protected void onResume() {
    // everytime the process is resumed
    super.onResume();
    // create a new teller in the class by taking in the serialization from the intent
    tt = (TellerTerminal) intent.getSerializableExtra("tellerTerminal");
    // set up a customer based on the teller terminal and get their accounts
    Customer cuss = (Customer) DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(
        tt.getCurrentCustomerId());
    accounts = cuss.getAccounts();
    accs = new String[accounts.size()];
    // set up a string array based on the accounts of the customer
    for (int i = 0; i < accounts.size(); i++) {
      accs[i] = accounts.get(i).getName() + " - "
          + DatabaseDriverAndroidHelper.getInstance().getAccountTypeName(accounts.get(i).getType())
          + ":     $" + accounts.get(i).getBalance();
    }

    // see which button was used to get to this screen based on what extra was passed in
    int i = intent.getIntExtra("call", 1);
    TextView funcDisplay = (TextView) findViewById(R.id.currFunction);
    // change the message dispalyed based on the functionality
    if (i == 0) {
      funcDisplay.setText("Displaying All Accounts");
    } else if (i == 1) {
      funcDisplay.setText("Choose Account to Deposit to");
    } else if (i == 2) {
      funcDisplay.setText("Choose Account to Withdraw from");
    } else if (i == 3) {
      funcDisplay.setText("Choose Account to Give Interest");
    }
    // set up the list view to show all the accounts
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this, android.R.layout.simple_list_item_1, accs);
    final ListView nameList = (ListView) findViewById(R.id.test_list);
    nameList.setAdapter(adapter);
    nameList.setClickable(true);

    // check when an item on the list view has been pressed
    nameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> adapter, View v, int position, long other) {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog
            .Builder(mcontext);
        // set a message for the alert that will pop up
        if (intent.getIntExtra("call", 0) == 2) {
          dialog.setTitle("Enter Amount to Withdraw");
        } else if (intent.getIntExtra("call", 0) == 1) {
          dialog.setTitle("Enter Amount to Deposit");
        }

        // check if adding interest was called so no alert needs to display
        if (intent.getIntExtra("call", 0) == 3) {
          try {
            // add interest to the selected account
            tt.giveInterest(accounts.get(position).getId());
            ListAccounts.this.popupMessage("Transaction Complete.", "Success");
            onResume();
            return;
          } catch (NoAccessToAccountException e) {
            // display message for when errors are caught
            ListAccounts.this.popupMessage("Cannot access account.", "Fail");
            return;
          }
        } else {
          // set up the alert to ask for amount of money
          LayoutInflater inflater = getLayoutInflater();
          View rootView = inflater.inflate(R.layout.enter_amount_layout, null);
          dialog.setView(rootView);
          final EditText amountEt = (EditText) rootView.findViewById(R.id.enterAmountBox);
          final int accPos = position;
          dialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
              // when the enter button is pressed
              // get the amount that is in the text box to see how much to add or subtract
              if (amountEt.getText().toString().equals("")) {
                // in case nothing is entered
                ListAccounts.this.popupMessage("Invalid Amount.", "Fail");
                return;
              }
              // create a new big decimal to do the amount
              BigDecimal amount = new BigDecimal(amountEt.getText().toString());
              if (amount.compareTo(new BigDecimal("0")) < 0) {
                // if negative is entered
                ListAccounts.this.popupMessage("Invalid Amount.", "Fail");
                return;
              }
              try {
                // withdraw or deposit based on what the intent was
                if (intent.getIntExtra("call", 0) == 2) {
                  tt.makeWithdrawal(amount, accounts.get(accPos).getId());
                } else if (intent.getIntExtra("call", 0) == 1) {
                  tt.makeDeposit(amount, accounts.get(accPos).getId());
                }
              } catch (NoAccessToAccountException e) {
                // display messages to tell when an exception was thrown
                ListAccounts.this.popupMessage("Cannot access account.", "Fail");
                return;
              } catch (InsufficientFundsException e) {
                ListAccounts.this.popupMessage("Not enough funds.", "Fail");
                return;
              } catch (IllegalAmountException e) {
                ListAccounts.this.popupMessage("The amount must be positive.", "Fail");
                return;
              }
              ListAccounts.this.popupMessage("Transaction Complete.", "Success");
              // reload the screen with the new information
              onResume();
              return;
            }
          });
          // when the Cancel command is chosen, do nothing
          dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              // on cancel
            }
          });
          // if the intent wasn't to just list or add interest, show a display
          if (!((intent.getIntExtra("call", 0) == 0) || (intent.getIntExtra("call", 0) == 3))) {
            dialog.show();
          }
        }
      }

    });
  }

  private void popupMessage(String content, String title) {
    // sets up an alert message that can display whatever the user wants
    AlertDialog.Builder invalidId = new AlertDialog.Builder(ListAccounts.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    invalidId.create().show();
  }
}

