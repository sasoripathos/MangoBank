package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AdminListUsers extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_list_users);
    Intent intent = getIntent();
    final AdminTerminal newAdmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    this.onclickListTellers(newAdmin);
    this.onclickListAdmins(newAdmin);
    this.onclickListCustomers(newAdmin);
  }

  /**
   * Go to ListTellers Activity after clicking
   * @param newAdmin AdminTerminal that is passed from the previous activity.
   */
  private void onclickListTellers(final AdminTerminal newAdmin) {
    Button listTellersButton = (Button) findViewById(R.id.ListTellers);
    listTellersButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminListUsers.this, AdminListTellers.class);
        intent.putExtra("adminTerminalInstance", newAdmin);
        AdminListUsers.this.startActivity(intent);
      }
    });
  }

  /**
   * Go to ListCustomers Activity after clicking.
   * @param newAdmin AdminTerminal that is passed from the previous activity.
   */
  private void onclickListCustomers(final AdminTerminal newAdmin) {
    Button listCustomersButton = (Button) findViewById(R.id.ListCustomers);
    listCustomersButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminListUsers.this, AdminListCustomers.class);
        intent.putExtra("adminTerminalInstance", newAdmin);
        AdminListUsers.this.startActivity(intent);
      }
    });
  }

  /**
   * Go to ListAdmins Activity after clicking.
   * @param newAdmin AdminTerminal that is passed from the previous activity.
   */
  private void onclickListAdmins(final AdminTerminal newAdmin) {
    Button listAdminsButton = (Button) findViewById(R.id.ListAdmins);
    listAdminsButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminListUsers.this, AdminListAdmins.class);
        intent.putExtra("adminTerminalInstance", newAdmin);
        AdminListUsers.this.startActivity(intent);
      }
    });
  }
}
