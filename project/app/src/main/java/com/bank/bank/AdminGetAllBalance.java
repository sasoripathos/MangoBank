package com.bank.bank;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


public class AdminGetAllBalance extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_get_all_balance);
  }


  /**
   * Pop up the message.
   * @param content The main content of the message.
   * @param title The title of the message
   */
  private void popupMessage(String content, String title) {
    AlertDialog.Builder invalidId = new AlertDialog.Builder(AdminGetAllBalance.this);
    invalidId.setMessage(content).setTitle(title);
    invalidId.setNegativeButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    });
    invalidId.create().show();
  }
}
