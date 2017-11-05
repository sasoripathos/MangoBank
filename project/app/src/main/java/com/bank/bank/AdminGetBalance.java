package com.bank.bank;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class AdminGetBalance extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_get_balance);
    Intent intent = getIntent();
    final AdminTerminal newadmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    this.onClickCheckTheBalance(newadmin);
    Button getAllBalanceButton = (Button) findViewById(R.id.get_all_balance);
    getAllBalanceButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        AdminGetBalance.this.onclickCheckAllBalance(newadmin);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
    super.onActivityResult(requestCode, resultCode, resultIntent);
    if (requestCode == 1) {
      if (resultCode == Activity.RESULT_CANCELED) {
        AdminGetBalance.this.finish();
      }
    }
  }

  /**
   * Check the balance after clicking.
   * @param newadmin AdminTerminal that will transfer to the next activity.
   */
  protected void onClickCheckTheBalance(final AdminTerminal newadmin) {
    Button getCurrentBalanceButton = (Button) findViewById(R.id.get_current_balance);
    getCurrentBalanceButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(AdminGetBalance.this, AdminGetCurrentBalance.class);
        intent.putExtra("adminTerminalInstance", newadmin);
        AdminGetBalance.this.startActivityForResult(intent, 1);
      }
    });
  }

  /**
   * Do checking all balance after clicking.
   * @param newadmin AdminTerminal
   */
  protected void onclickCheckAllBalance(AdminTerminal newadmin) {
    java.math.BigDecimal totalBalance = newadmin.getallbalance();
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Check the total balance of all users.");
    dialog.setMessage("The balance of the account is " + totalBalance);
    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // on OK.
        AdminGetBalance.this.finish();
      }
    });
    dialog.show();
  }

}
