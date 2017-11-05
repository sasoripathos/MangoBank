package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.validation.MessageValidator;
import com.bank.validation.MessageValidatorPc;

public class AdminLeaveMessage extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_leave_message);
    Intent intent = getIntent();
    final AdminTerminal newadmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    onClickLeaveMessage(newadmin);
  }

  /**
   * Leave the message after clicking the button.
   * @param newadmin AdminTerminal that is passed from the previous acticity.
   */
  private void onClickLeaveMessage(final AdminTerminal newadmin) {
    Button getButton = (Button) findViewById(R.id.getMessageButton);
    getButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        // get user ID
        TextView roleIdView = (TextView) findViewById(R.id.idEdit);
        int customerId = -1;
        int inputId = -1;
        try {
          inputId = Integer.parseInt(roleIdView.getText().toString());
          customerId = DatabaseDriverAndroidHelper.getInstance().getUserRole(inputId);
        } catch (Exception e) {
          // If userId is not given
          AdminLeaveMessage.this.popupMessage("Please Enter your user ID!", "Login Fail");
          return;
        }
        // If the customer id is not the current id, pop up messages.
        if ((customerId == -1) || (inputId == -1)) {
          AdminLeaveMessage.this.popupMessage("There doesn't exist such user", "Login Fail");
          return;
        }
        // If this is the Admin, the
        leaveMessage(newadmin, inputId);
      }
    });
  }

  /**
   * Leave the input user id the message.
   * @param newAdmin AdminTerminal that is passed from the previous acticity.
   * @param userId the id's user id.
   */
  private void leaveMessage(final AdminTerminal newAdmin, final int userId) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Leave Message");
    dialog.setMessage("Please enter the message you want to leave for you customer");
    final EditText edittext = new EditText(this);
    dialog.setView(edittext);
    dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        boolean success = false;
        // use the string the user entered
        String message = edittext.getText().toString();
        MessageValidator m = new MessageValidatorPc();
        if (m.checkContent(message).getResult() && message != null) {
          if (newAdmin.leaveMessage(userId, message)) {
            success = true;
          }
        }
        AlertDialog.Builder succDialog = new AlertDialog.Builder(AdminLeaveMessage.this);
        // If it's successful, print Message successfully sent, else message fail to send.
        String prompt = success ? "Message Successfully Sent!" : "Message Failed to Send.";
        succDialog.setMessage(prompt);
        succDialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
          public void onClick(DialogInterface dialog, int whichButton) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            AdminLeaveMessage.this.finish();
          }
        });
        succDialog.show();
      }
    });
    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // on Cancel
      }
    });
    dialog.show();
  }

  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminLeaveMessage.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }
}
