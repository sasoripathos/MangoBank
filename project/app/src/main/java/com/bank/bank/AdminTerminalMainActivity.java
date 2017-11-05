package com.bank.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AdminTerminalMainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_terminal_main);
    Intent intent = getIntent();
    int adminid = (int) intent.getSerializableExtra("adminId");
    AdminTerminalInterface admin1 =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    this.getAllBalanceOnclick(admin1);
    this.changeToAdminOnclick(admin1);
    this.createOnClick();
    this.listOnClick(admin1);
    this.messageOnClick(admin1, adminid);
    this.serializeOnClick(admin1);
    this.deserializeOnClick(admin1);
    this.signOut();
  }

  /**
   * Create Admin/Teller/Customer after clicking.
   */
  protected void createOnClick() {
    Button createButton = (Button) findViewById(R.id.createButton);
    createButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminTerminalMainActivity.this, AdminCreate.class);
        AdminTerminalMainActivity.this.startActivity(intent);
      }
    });
  }

  /**
   * Get specific account and all account's balance after clicking.
   * @param newAdmin AdminTerminal that will be passed from the next activity.
   */
  protected void getAllBalanceOnclick(final AdminTerminalInterface newAdmin) {
    Button getbalancebutton = (Button) findViewById(R.id.get_balance);
    getbalancebutton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminTerminalMainActivity.this, AdminGetBalance.class);
        intent.putExtra("adminTerminalInstance", (AdminTerminal) newAdmin);
        AdminTerminalMainActivity.this.startActivity(intent);
      }
    });
  }

  /**
   * Change the admin to teller after clicking.
   * @param newAdmin AdminTerminal that will be passed from the next activity.
   */
  protected void changeToAdminOnclick(final AdminTerminalInterface newAdmin) {
    Button changeToAdminButton = (Button) findViewById(R.id.change_admin);
    changeToAdminButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminTerminalMainActivity.this, AdminChangeToAdmin.class);
        intent.putExtra("adminTerminalInstance", (AdminTerminal) newAdmin);
        AdminTerminalMainActivity.this.startActivity(intent);
      }
    });
  }

  /**
   * List all Tellers/Customers/Admins after clicking.
   * @param newAdmin AdminTerminal that will be passed from the next activity.
   */
  protected void listOnClick(final AdminTerminalInterface newAdmin) {
    Button listButton = (Button) findViewById(R.id.listButton);
    listButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminTerminalMainActivity.this, AdminListUsers.class);
        intent.putExtra("adminTerminalInstance", (AdminTerminal) newAdmin);
        AdminTerminalMainActivity.this.startActivity(intent);
      }
    });
  }

  /**
   * Do the message part after clicking
   * @param newAdmin AdminTerminal that will be passed from the next activity.
   * @param adminid int that will be transfered to the
   */
  protected void messageOnClick(final AdminTerminalInterface newAdmin, final int adminid) {
    Button messageButton = (Button) findViewById(R.id.messageButton);
    messageButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminTerminalMainActivity.this, AdminMessage.class);
        intent.putExtra("adminTerminalInstance", (AdminTerminal) newAdmin);
        intent.putExtra("adminid",adminid);
        AdminTerminalMainActivity.this.startActivity(intent);
      }
    });
  }

  /**
   * Serialize the code after clicking.
   * @param newAdmin AdminTerminal that will be passed from the next activity.
   */
  private void serializeOnClick(final AdminTerminalInterface newAdmin) {
    Button serializeButton = (Button) findViewById(R.id.Serialize);
    serializeButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        newAdmin.serializeDatabase(getApplicationContext());
        AdminTerminalMainActivity.this.popupMessage("The Admin Terminal Serialize successfully",
                "Serialize Success");
      }
    });
  }

  /**
   * Deserialize the code after clicking.
   * @param newAdmin AdminTerminal that will be passed from the next activity.
   */
  private void deserializeOnClick(final AdminTerminalInterface newAdmin) {
    Button deserializeButton = (Button) findViewById(R.id.Deserialize);
    deserializeButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        newAdmin.deserializeDatabse(getApplicationContext());
        AdminTerminalMainActivity.this.popupMessage("The Admin Terminal deserialize successfully",
                "Deserialize Success");
      }
    });
  }

  /**
   * Sign out the user after clicking.
   */
  private void signOut() {
    Button signOut = (Button) findViewById(R.id.exit);
    signOut.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        AdminTerminalMainActivity.this.finish();
      }
    });
  }

  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminTerminalMainActivity.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }
}
