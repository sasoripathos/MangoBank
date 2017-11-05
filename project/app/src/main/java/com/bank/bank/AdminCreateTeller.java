package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;

public class AdminCreateTeller extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_create_teller);
    createTeller();
  }

  private void createTeller() {
    Button createTellerButton = (Button) findViewById(R.id.tellerCreateButton);
    createTellerButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        // get user ID
        EditText tellerAge = (EditText) findViewById(R.id.newTellerAge);
        int inputAge = 0;
        try {
          inputAge = Integer.parseInt(tellerAge.getText().toString());
        } catch (Exception e) {
          AdminCreateTeller.this.popupMessage("There is something wrong with your age",
                  "Create Fail");
          return;
        }
        EditText tellerName = (EditText) findViewById(R.id.newtellername);
        String inputName = tellerName.getText().toString();
        // Check if the teller is empty.
        if (inputName.equals("")) {
          AdminCreateTeller.this.popupMessage("The name can't be empty.", "Create Fail");
          return;
        }
        EditText tellerPassword = (EditText) findViewById(R.id.newTellerPassword);
        String inputPassword = tellerPassword.getText().toString();
        // Check if the password is empty.
        if (inputPassword.equals("")) {
          AdminCreateTeller.this.popupMessage("The password can't be empty.", "Create Fail");
          return;
        }
        // Check if the address is empty.
        EditText tellerAddress = (EditText) findViewById(R.id.newTellerAddress);
        String inputAddress = tellerAddress.getText().toString();
        if (inputAddress.equals("")) {
          AdminCreateTeller.this.popupMessage("The address can't be empty.", "Create Fail");
          return;
        }
        // Get the role is of the Teller.
        int roleId = RolesMap.getInstance().getId(Roles.TELLER);
        long newid = DatabaseDriverAndroidHelper.getInstance().insertNewUser(inputName,
                inputAge, inputAddress, roleId, inputPassword);
        AlertDialog.Builder success = new AlertDialog.Builder(AdminCreateTeller.this);
        success.setMessage("The new Teller id is " + newid).setTitle("Create Success");
        success.setNegativeButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
          }
        });
        success.create().show();
      }
    });
  }

  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminCreateTeller.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }
}
