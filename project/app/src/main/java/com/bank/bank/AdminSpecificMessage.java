package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;

public class AdminSpecificMessage extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_admin_specific_message);
	Intent intent = getIntent();
	final AdminTerminal newadmin = (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
	OnClickGetMessage(newadmin);
  }

  /**
   * Get and check the message id and pop ip the message
   * @param newadmin AdminTerminal that is passed from the previous activity.
   */
  protected void OnClickGetMessage(final AdminTerminal newadmin) {
	Button getButton = (Button) findViewById(R.id.GetMessageButton);
	getButton.setOnClickListener(new Button.OnClickListener() {
	  public void onClick(View v) {
		// get user ID
		TextView messageIdView = (TextView) findViewById(R.id.MessageUserEdit);
		int messageid = -1;
		try {
		  messageid = Integer.parseInt(messageIdView.getText().toString());
		} catch (Exception e) {
		  // If userId is not given
		  AdminSpecificMessage.this.popupMessage("Please Enter your message ID!", "Login Fail");
		  return;
		}
		// If the message id doesn't exist.
		String message = DatabaseDriverAndroidHelper.getInstance().getSpecificMessage(messageid);
		if (message == null) {
		  AdminSpecificMessage.this.popupMessage("There doesn't exist such message id",
                  "Login Fail");
		  return;
		}
        AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminSpecificMessage.this);
        invalidId.setMessage(message).setTitle("Message Content");
        invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            AdminSpecificMessage.this.finish();
          }
        });
        invalidId.create().show();
	  }
	});
  }

  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
	AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminSpecificMessage.this);
	invalidId.setMessage(content).setTitle(title);
	invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {}
	});
	invalidId.create().show();
  }
}
