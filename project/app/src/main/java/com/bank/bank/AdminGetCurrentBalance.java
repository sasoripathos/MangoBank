package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;

public class AdminGetCurrentBalance extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_get_current_balance);
    // get the admin terminal passed from the AdminGetBalance.
    Intent intent = getIntent();
    final AdminTerminal newadmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    enterCustomerId(newadmin);
  }

  /**
   * Enter the id of the customer
   * @param newadmin AdminTerminal that is passed from AdminGetBalance activity.
   */
  protected void enterCustomerId(final AdminTerminal newadmin) {
    Button getButton = (Button) findViewById(R.id.getButton);
    getButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        // get user ID
        TextView roleIdView = (TextView) findViewById(R.id.CustomerIdEdit);
        int customerId = -1;
        try {
          int inputId = Integer.parseInt(roleIdView.getText().toString());
          customerId = DatabaseDriverAndroidHelper.getInstance().getUserRole(inputId);
        } catch (Exception e) {
          // If userId is not given
          AdminGetCurrentBalance.this.popupMessage("Please Enter your customer ID!", "Login Fail");
          return;
        }
        // If we can't get the user from the roleId.
        if (customerId == -1) {
          AdminGetCurrentBalance.this.popupMessage("There doesn't exist such user", "Login Fail");
          return;
        }
        // If the role is not customer.
        int roleid = RolesMap.getInstance().getId(Roles.CUSTOMER);
        if (DatabaseDriverAndroidHelper.getInstance().getUserRole(customerId) != roleid) {
          AdminGetCurrentBalance.this.popupMessage("Your user id is not customer's id",
                  "Login Fail");
          return;
        }
        java.math.BigDecimal totalBalance = newadmin.getuserbalance(customerId);
        // Pop up the message of the total balance.
        AlertDialog.Builder dialog = new AlertDialog.Builder(AdminGetCurrentBalance.this);
        dialog.setTitle("Check the total balance of the current user.");
        dialog.setMessage("The balance of the account is " + totalBalance);
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            AdminGetCurrentBalance.this.finish();
          }
        });
        dialog.show();
      }
    });
  }

  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminGetCurrentBalance.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }

}
