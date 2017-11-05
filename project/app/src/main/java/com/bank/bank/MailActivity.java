package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MailActivity extends AppCompatActivity {

  EditText inputBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mail);
    inputBox = (EditText)findViewById(R.id.emailEnter);
  }

  /** Opens up the mail client and preloads information to send as confirmation.
   *
   * @param v The view that gets passed in
   */
  public void sendMail(View v) {
    // create a new email intent
    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

    // get the name, balance, and type of account to set up the email body
    String name = getIntent().getStringExtra("name");
    String bal = getIntent().getStringExtra("bal");
    String type = getIntent().getStringExtra("type");
    String message = "Name of account: " + name + "    Balance: " + bal + "    Type: " + type;
    // Fill the email with the data
    String email = inputBox.getText().toString();
    emailIntent.setType("plain/text");
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Confirmation of New Account");
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

    // Send it off to the Activity-Chooser
    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    finish();
  }
}
