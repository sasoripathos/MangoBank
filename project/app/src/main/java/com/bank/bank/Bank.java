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
import com.bank.generics.AccountTypeMap;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.user.User;

import java.math.BigDecimal;

public class Bank extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bank);
    //Initialize connection to an exist database or create a new empty database
    DatabaseDriverAndroidHelper.initialize(getApplicationContext());
    // If empty, insert default user and account to the database
    if (DatabaseDriverAndroidHelper.getInstance().getAllRoles().size() == 0) {
      this.createNewDatabase();
    }
    this.login();
  }

  /**
   * The method is only used on
   */
  private void createNewDatabase() {
    DatabaseDriverAndroidHelper db = null;
    try {
      db = DatabaseDriverAndroidHelper.getInstance();
    } catch (Exception e) {
      return;
    }
    for (Roles current: Roles.values()) {
      db.insertRole(current.toString());
    }
    for (AccountTypes current: AccountTypes.values()) {
      db.insertAccountType(current.toString(), new BigDecimal("0.2"));
    }
    RolesMap map = RolesMap.getInstance();
    //map.update();
    db.insertNewUser("admin1", 30, "UTSC", map.getId(Roles.ADMIN), "123");
    db.insertNewUser("teller1", 30, "UTSC", map.getId(Roles.TELLER), "123");
    db.insertNewUser("customer1", 30, "UTSC", map.getId(Roles.CUSTOMER), "123");

    db.insertAccount("saving1", new BigDecimal("2000.0"),
            AccountTypeMap.getInstance().getId(AccountTypes.SAVING));
    db.insertUserAccount(3, 1);
    db.insertAccount("restrictSaving1", new BigDecimal("2000.0"),
            AccountTypeMap.getInstance().getId(AccountTypes.RESTRICTEDSAVINGS));
    db.insertUserAccount(3, 2);
    db.insertAccount("chequing1", new BigDecimal("2000.0"),
            AccountTypeMap.getInstance().getId(AccountTypes.CHEQUING));
    db.insertUserAccount(3, 3);
    db.insertAccount("tfsa1", new BigDecimal("2000.0"),
            AccountTypeMap.getInstance().getId(AccountTypes.TFSA));
    db.insertUserAccount(3, 4);
    db.insertAccount("balanceOwing1", new BigDecimal("0.0"),
            AccountTypeMap.getInstance().getId(AccountTypes.BALANCEOWING));
    db.insertUserAccount(3, 5);
  }

  private void login() {
    Button loginButton = (Button) findViewById(R.id.LoginButton);
    loginButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        // get user ID
        EditText userIdView = (EditText) findViewById(R.id.userId);
        int userId = -1;
        try {
          userId = Integer.parseInt(userIdView.getText().toString());
        } catch (Exception e) {
          // If userId is not given
          Bank.this.popupMessage("Please Enter your user ID!", "Login Fail");
          Bank.this.cleanContent();
          return;
        }
        // get password
        EditText passwordView = (EditText) findViewById(R.id.password);
        String password = passwordView.getText().toString();
        int roleId = DatabaseDriverAndroidHelper.getInstance().getUserRole(userId);
        if (roleId == -1) {
          // If the user does not exist
          Bank.this.popupMessage("The given userID does not exist!", "Invalid userID");
          Bank.this.cleanContent();
        } else if (RolesMap.getInstance().getRole(roleId) == Roles.ADMIN) { // an admin
          //get the admin
          User admin = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(userId);
          if (!admin.authenticate(password)) {
            //If authenticate fail
            Bank.this.popupMessage("Invalid password!", "Login Fail");
            Bank.this.cleanContent();
          } else {
            //If authenticate success, move to admin terminal main page
            Intent intent = new Intent(Bank.this, AdminTerminalMainActivity.class);
            AdminTerminalInterface adminTerminalInstance = new AdminTerminal(userId, password);
            intent.putExtra("adminTerminalInstance",
                (AdminTerminal) adminTerminalInstance);
            intent.putExtra("adminId", userId);
            Bank.this.startActivity(intent);
          }

        } else if (RolesMap.getInstance().getRole(roleId) == Roles.TELLER) { // a teller
          //get the teller
          User teller = DatabaseDriverAndroidHelper.getInstance().getOneUserDetails(userId);
          if (!teller.authenticate(password)) {
            //If authenticate fail
            Bank.this.popupMessage("Invalid password!", "Login Fail");
            Bank.this.cleanContent();
          } else {
            //If authenticate success, move to admin terminal main page
            Intent intent = new Intent(Bank.this, TellerTerminalMenuActivity.class);
            TellerTerminalInterface tellerTerminalInstance = new TellerTerminal(userId, password);
            intent.putExtra("tellerTerminal", (TellerTerminal) tellerTerminalInstance);
            Bank.this.startActivity(intent);
          }
        } else { // a customer
          AtmInterface atmInstance = new Atm(userId);
          if (atmInstance.authenticate(userId, password)) {
            Intent intent = new Intent(Bank.this, AtmMainActivity.class);
            intent.putExtra("atmInstance", (Atm) atmInstance);
            intent.putExtra("customerId", userId);
            Bank.this.startActivity(intent);
          } else {
            // if authenticate fail
            Bank.this.popupMessage("Invalid password!", "Login Fail");
            Bank.this.cleanContent();
          }
        }
      }
    });
  }

  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(Bank.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }

  private void cleanContent() {
    EditText userIdView = (EditText) findViewById(R.id.userId);
    EditText passwordView = (EditText) findViewById(R.id.password);
    userIdView.setText("");
    passwordView.setText("");
  }

}
