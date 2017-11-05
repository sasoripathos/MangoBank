package com.bank.bank;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;

public class AdminCreateAdmin extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_admin_create_admin);
	createAdmin();
  }

  /**
   * Check the input.
   */
  protected void createAdmin() {
	Button createAdminButton = (Button) findViewById(R.id.AdminCreateButton);
	createAdminButton.setOnClickListener(new Button.OnClickListener() {
	  public void onClick(View v) {
		// get user ID
		EditText AdminAge = (EditText) findViewById(R.id.newAdminAge);
		int inputAge = 0;
		try {
		  // If age is string, throw exception.
		  inputAge = Integer.parseInt(AdminAge.getText().toString());
		} catch (Exception e) {
		  AdminCreateAdmin.this.popupMessage("There is something wrong with your age",
                  "Create Fail");
		  return;
		}
		EditText AdminName = (EditText) findViewById(R.id.newAdminName);
		String inputName = AdminName.getText().toString();
        // Check if the name is empty.
		if (inputName.equals("")) {
		  AdminCreateAdmin.this.popupMessage("The name can't be empty.", "Create Fail");
		  return;
		}
		// Check if password is empty.
		EditText AdminPassword = (EditText) findViewById(R.id.newAdminPassword);
		String inputPassword = AdminPassword.getText().toString();
		if (inputPassword.equals("")) {
		  AdminCreateAdmin.this.popupMessage("The password can't be empty.", "Create Fail");
		  return;
		}
		// Check if the address is empty.
		EditText AdminAddress = (EditText) findViewById(R.id.newAdminAddress);
		String inputAddress = AdminAddress.getText().toString();
		if (inputAddress.equals("")) {
		  AdminCreateAdmin.this.popupMessage("The address can't be empty.", "Create Fail");
		  return;
		}
		// Get the role id of the admin.
		int roleId = RolesMap.getInstance().getId(Roles.ADMIN);
        // Get the new id of the new admin.
		long newid = DatabaseDriverAndroidHelper.getInstance().insertNewUser(inputName,
                inputAge, inputAddress, roleId, inputPassword);
        AlertDialog.Builder success = new AlertDialog.Builder(AdminCreateAdmin.this);
        success.setMessage("The new Admin id is " + newid).setTitle("Create Success");
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
	AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminCreateAdmin.this);
	invalidId.setMessage(content).setTitle(title);
	invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {}
	});
	invalidId.create().show();
  }
}
