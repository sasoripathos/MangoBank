package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AdminMessage extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_message);
    Intent intent = getIntent();
    int adminid = (int) intent.getSerializableExtra("adminid");
    final AdminTerminal newAdmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    this.onclickLeaveMessage(newAdmin);
    this.onclickOwnMessage(newAdmin,adminid);
    this.onclickSpecificMessage(newAdmin);
  }

  /**
   * Do the LeaveMessage activity after clicking
   * @param newAdmin AdminTerminal that is passed from the previous activity.
   */
  private void onclickLeaveMessage(final AdminTerminal newAdmin) {
    Button leaveMessageButton = (Button) findViewById(R.id.leaveMessage);
    leaveMessageButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminMessage.this, AdminLeaveMessage.class);
        intent.putExtra("adminTerminalInstance", newAdmin);
        AdminMessage.this.startActivity(intent);
      }
    });
  }

  /**
   * See the Admin's own message activity after clicking
   * @param newAdmin AdminTerminal that is passed from the previous activity.
   * @param adminid the adminid that is transferred
   */
  private void onclickOwnMessage(final AdminTerminal newAdmin, final int adminid) {
    Button viewOwnMessageButton = (Button) findViewById(R.id.viewMessage);
    viewOwnMessageButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminMessage.this, MessageSelectActivity.class);
        intent.putExtra("id", adminid);
        AdminMessage.this.startActivity(intent);
      }
    });
  }

  /**
   * Do the Check Specific message after clicking.
   * @param newAdmin AdminTerminal that is passed from the previous activity.
   */
  private void onclickSpecificMessage(final AdminTerminal newAdmin) {
    Button viewSpecificMessageButton = (Button) findViewById(R.id.viewSpecificMessage);
    viewSpecificMessageButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminMessage.this, AdminSpecificMessage.class);
        intent.putExtra("adminTerminalInstance", newAdmin);
        AdminMessage.this.startActivity(intent);
      }
    });
  }

}
