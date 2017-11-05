package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MakeNewCustomerActivity extends AppCompatActivity {

  TellerTerminal tt;
  EditText name;
  EditText address;
  EditText age;
  EditText password;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_make_new_customer);
    Intent start = getIntent();
    // get a serialized version of the teller terminal
    tt = (TellerTerminal) start.getSerializableExtra("tellerTerminal");
    // set up instances of all the input fields
    name = (EditText) findViewById(R.id.makeCustomerNameField);
    address = (EditText) findViewById(R.id.makeCustomerAddressField);
    age = (EditText) findViewById(R.id.makeCustomerAgeField);
    password = (EditText) findViewById(R.id.makeCustomerPasswordField);
  }

  /**
   * Makes a Customer given the button press.
   * @param v the View.
   */
  public void makeCustomer(View v){
      // retrieve all the info from the input fields
      String cusName = name.getText().toString();
      String cusAdd = address.getText().toString();
      String cusAge = age.getText().toString();
      String cusPass = password.getText().toString();

      // if any of the fields are empty, send an alert and not allow processing
      if(cusName.equals("")){
          MakeNewCustomerActivity.this.popupMessage("There is something wrong with your name", "Create Fail");
          return;
      }
      else if(cusAdd.equals("")){
          MakeNewCustomerActivity.this.popupMessage("There is something wrong with your address", "Create Fail");
          return;
      }
      else if(cusAge.equals("")){
          MakeNewCustomerActivity.this.popupMessage("There is something wrong with your age", "Create Fail");
          return;
      }
      else if(cusPass.equals("")){
          MakeNewCustomerActivity.this.popupMessage("There is something wrong with your password", "Create Fail");
          return;
      }
      else {
          // get a customer id by adding the new customer to database
          Integer customerid = tt.makeNewUser(cusName, Integer.parseInt(cusAge), cusAdd, cusPass);
          // if the returned id was -1, then there was an issue
          if (customerid == -1) {
              AlertDialog.Builder dialog = new AlertDialog.Builder(this);
              dialog.setTitle("Failure");
              dialog.setMessage("Could not make new customer.");
              dialog.setPositiveButton("OK", null);
              dialog.show();
              // display error message and exit activity
              finish();
          } else {
              // if everything worked as it was supposed to, then sent success alert
              AlertDialog.Builder dialog = new AlertDialog.Builder(this);
              dialog.setTitle("Success");
              dialog.setMessage("New customer made.");
              dialog.setPositiveButton("OK", null);
              dialog.show();
              finish();
          }
      }

  }

  private void popupMessage(String content, String title) {
      AlertDialog.Builder invalidId = new AlertDialog.Builder(MakeNewCustomerActivity.this);
      invalidId.setMessage(content).setTitle(title);
      invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {}
      });
      invalidId.create().show();
  }
}
