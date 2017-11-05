package com.bank.bank;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bank.account.Account;
import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.CannotWithdrawlException;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

import java.math.BigDecimal;

public class AccountDetail extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_detail);
    Intent fromIntent = getIntent();
    Account account = (Account) fromIntent.getSerializableExtra("account");
    AtmInterface atmInstance = (Atm) fromIntent.getSerializableExtra("atmInstance");
    this.hindWithdrawalButton(account);
    this.displayDetails(account);
    this.makeDeposit(account, atmInstance);
    this.makeWithdrwal(account, atmInstance);
    this.goBack(atmInstance);
  }



  private void displayDetails(Account account) {
    TextView oneAccountInformation = (TextView) findViewById(R.id.OneAccountInformation);
    String infor = account.getName()
            + " (" + AccountTypeMap.getInstance().getAccount(account.getType()).toString() + ")\n";
    infor += "$" + account.getBalance().toString();
    oneAccountInformation.setText(infor);
  }

  private void hindWithdrawalButton(Account account) {
    Button atmWithdrawalButton = (Button) findViewById(R.id.AtmWithdrawalButton);
    if (AccountTypeMap.getInstance().getAccount(account.getType())
            == AccountTypes.RESTRICTEDSAVINGS) {
      atmWithdrawalButton.setVisibility(View.INVISIBLE);
    }
  }

  private void makeDeposit(final Account account, final AtmInterface atmInstance) {
    Button atmDepositButton = (Button) findViewById(R.id.AtmDepositButton);
    atmDepositButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountDetail.this);
        dialog.setTitle("Deposit");
        dialog.setMessage("Please enter the amount");
        final EditText edittext = new EditText(AccountDetail.this);
        edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setView(edittext);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // get the amount
            String amountString = edittext.getText().toString();
            BigDecimal amount = null;
            try {
              amount = new BigDecimal(amountString);
            } catch (Exception e) {
              // if amount illegal
              AccountDetail.this.popupMessage("Please enter a non-negative amount.",
                      "Deposit Fail");
              return;
            }

            // else try to make the deposit
            boolean success = false;
            try {
              success = atmInstance.makeDeposit(amount, account.getId());
            } catch (NoAccessToAccountException e) {
              AccountDetail.this.popupMessage("You have no access to the account.", "Deposit Fail");
              return;
            } catch (IllegalAmountException e) {
              AccountDetail.this.popupMessage("The amount must be positive.", "Deposit Fail");
              return;
            }
            // if not sucess,
            if (!success) {
              AccountDetail.this.popupMessage("Something wrong with database connection.",
                      "Deposit Fail");
            }  else { // else display the new account information
              Account newAccount = DatabaseDriverAndroidHelper.getInstance()
                      .getOneAccountDetails(account.getId());
              AccountDetail.this.displayDetails(newAccount);
            }
          }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // on cancel
          }
        });
        dialog.show();
      }
    });
  }

  private void makeWithdrwal(final Account account, final AtmInterface atmInstance) {
    Button atmWithdrawalButton = (Button) findViewById(R.id.AtmWithdrawalButton);
    atmWithdrawalButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountDetail.this);
        dialog.setTitle("Withdrawal");
        dialog.setMessage("Please enter the amount");
        final EditText edittext = new EditText(AccountDetail.this);
        edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setView(edittext);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // get the amount
            String amountString = edittext.getText().toString();
            BigDecimal amount = null;
            try {
              amount = new BigDecimal(amountString);
            } catch (Exception e) {
              // if amount illegal
              AccountDetail.this.popupMessage("Please enter a non-negative amount.",
                      "Withdrawal Fail");
              return;
            }

            // else try to make the deposit
            boolean success = false;
            try {
              success = atmInstance.makeWithdrawal(amount, account.getId());
            } catch (NoAccessToAccountException e) {
              AccountDetail.this.popupMessage("You have no access to the account.",
                      "Withdrawal Fail");
              return;
            } catch (IllegalAmountException e) {
              AccountDetail.this.popupMessage("The amount must be positive.", "Withdrawal Fail");
              return;
            } catch (InsufficientFundsException e) {
              AccountDetail.this.popupMessage("You don't have enough money.", "Withdrawal Fail");
              return;
            } catch (CannotWithdrawlException e) {
              AccountDetail.this.popupMessage("You cannot do withdrawal on this account.",
                      "Withdrawal Fail");
              return;
            }
            // if not sucess,
            if (!success) {
              AccountDetail.this.popupMessage("Something wrong with database connection",
                      "Withdrawal Fail");
            }  else { // else display the new account information
              Account newAccount = DatabaseDriverAndroidHelper.getInstance()
                      .getOneAccountDetails(account.getId());
              AccountDetail.this.displayDetails(newAccount);
            }
          }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // on cancel
          }
        });
        dialog.show();
      }
    });
  }

  private void goBack(final AtmInterface atmInstance) {
    Button backButton = (Button) findViewById(R.id.BackButton);
    backButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("atmInstance",(Atm) atmInstance);
        setResult(Activity.RESULT_OK, returnIntent);
        AccountDetail.this.finish();
      }
    });
  }

  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(AccountDetail.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }
}
