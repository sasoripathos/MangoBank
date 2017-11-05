package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;

import java.math.BigDecimal;

public class MakeNewAccountActivity extends AppCompatActivity {

  EditText name;
  EditText balance;
  Spinner spinner;
  TellerTerminal tt;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // create instances of all the input fields in the xml
    setContentView(R.layout.activity_make_new_account);
    spinner = (Spinner) findViewById(R.id.accountTypesSpinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.account_types_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    name = (EditText) findViewById(R.id.makeNewAccountNameField);
    balance = (EditText) findViewById(R.id.makeNewAccountBalanceField);
    Intent start = getIntent();
    // get the serialized version of the teller terminal
    tt = (TellerTerminal) start.getSerializableExtra("tellerTerminal");
  }

  /**
   * Make the account using information that was placed in the edit text boxes.
   * @param v the view that gets passed in
   */
  public void makeAccount(View v) {
    // get the info in the input fields
    String accName = name.getText().toString();
    String accBal = balance.getText().toString();
    String accType = spinner.getSelectedItem().toString();

    // if any field has nothing entered throw up an alert and not allow it to process
    if (accName.equals("")) {
      MakeNewAccountActivity.this.popupMessage("There is something wrong with your name",
          "Create Fail");
      return;
    }
    if (accBal.equals("")) {
      MakeNewAccountActivity.this.popupMessage("There is something wrong with your balance",
          "Create Fail");
      return;
    } else {
      // set the account type to upper case to match enum
      accType = accType.toUpperCase();
      AccountTypes type = AccountTypes.valueOf(accType);
      // make a new account based on the info provided
      tt.makeNewAccount(accName, new BigDecimal(accBal), AccountTypeMap.getInstance().getId(type));

      // create an intent to make a mail activity and send an email
      Intent intent = new Intent(MakeNewAccountActivity.this, MailActivity.class);
      intent.putExtra("name", accName);
      intent.putExtra("bal", accBal);
      intent.putExtra("type", accType);

      this.startActivity(intent);
      finish();
    }
  }

  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(MakeNewAccountActivity.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }
}
