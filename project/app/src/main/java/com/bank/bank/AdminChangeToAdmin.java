package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.exceptions.NoAccessToAccountException;
import com.bank.generics.Roles;
import com.bank.generics.RolesMap;
import com.bank.user.User;

public class AdminChangeToAdmin extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_admin_change_to_admin);
	Intent intent = getIntent();
	final AdminTerminal newadmin = (AdminTerminal) intent.
			getSerializableExtra("adminTerminalInstance");
	EnterTellerId(newadmin);
  }

  /**
   * Change the teller to admin.
   * @param newadmin AdminTerminal
   */
  protected void EnterTellerId(final AdminTerminal newadmin) {
	Button changebutton = (Button) findViewById(R.id.changeButton);
	changebutton.setOnClickListener(new Button.OnClickListener() {
	  public void onClick(View v) {
		// get user ID.
		EditText tellerIdView = (EditText) findViewById(R.id.edit_TellerId);
		int tellerId = -1;
        int roleId = -1;
		try {
		  tellerId = Integer.parseInt(tellerIdView.getText().toString());
          roleId = DatabaseDriverAndroidHelper.getInstance().getUserRole(tellerId);
		} catch (Exception e) {
		  // If userId is not given
		  AdminChangeToAdmin.this.popupMessage("Please Enter your teller ID!", "Login Fail");
		  return;
		}
		if (roleId == -1) {
		  // If the user doesn't exist
		  AdminChangeToAdmin.this.popupMessage("There doesn't exist such user", "Login Fail");
		  return;
		}
		// If the roleid is not teller.
		if (RolesMap.getInstance().getId(Roles.TELLER) != roleId ) {
		  AdminChangeToAdmin.this.popupMessage("Your user id is not teller's id", "Login Fail");
		  return;
		}
		try {
		  newadmin.changetoadmin(tellerId);
          String content = "Your teller is successfully changed to admin!";
          String title = "Login Success";
          AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminChangeToAdmin.this);
          invalidId.setMessage(content).setTitle(title);
          // Have the button OK.
          invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              Intent returnIntent = new Intent();
              setResult(RESULT_CANCELED, returnIntent);
              AdminChangeToAdmin.this.finish();
            }
          });
          invalidId.create().show();
		} catch (NoAccessToAccountException e) {
		  AdminChangeToAdmin.this.
				  popupMessage("Your don't have the access to this account", "Login Fail");
		}
	  }
	});
  }

  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
	AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminChangeToAdmin.this);
	invalidId.setMessage(content).setTitle(title);
	// Have the button OK.
	invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {}
	});
	invalidId.create().show();
  }

}
