package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.message.MessageImpl;

public class ReadMessageActivity extends AppCompatActivity {
  MessageImpl message;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_message);
    Intent intent = getIntent();
    // getting a message object through its serial version
    message = (MessageImpl) intent.getSerializableExtra("message");
    // setting the text for the outputs in the xml based on the message
    TextView header = (TextView) findViewById(R.id.readMessageHeader);
    TextView body = (TextView) findViewById(R.id.readMessageBody);
    header.setText("Message id " + message.getMessageId());
    body.setText(message.getContent());
  }

  public void markAsReadOnClick(View v) {
    // mark the message as read on the database
    DatabaseDriverAndroidHelper.getInstance().updateUserMessageState(message.getMessageId());
    finish();
  }

}
