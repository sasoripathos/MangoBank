package com.bank.bank;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.bank.account.Account;

import com.bank.account.Account;

import java.util.ArrayList;
import java.util.List;

public class AtmMainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_atm_main);
    // get source intent
    Intent fromIntent = getIntent();
    // get the atm instance
    AtmInterface atmInstance = (Atm) fromIntent.getSerializableExtra("atmInstance");
    // get current customer id
    int userId = fromIntent.getIntExtra("customerId", -1);
    // get the user information and display
    String userInfor = atmInstance.display();
    this.displayUserInfor(userInfor);
    // get list of all accounts and display
    List<Account> allAccounts = atmInstance.listAccounts();
    this.displayAccounts(allAccounts, atmInstance);
    // the display button
    this.displayMyMessages(userId);
    // the sign out button
    this.signOut();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
    super.onActivityResult(requestCode, resultCode, resultIntent);
    if (requestCode == 1) {
      if (resultCode == Activity.RESULT_OK) {
        // re-get the atm instance and display the newest information
        AtmInterface atmInstance = (Atm) resultIntent.getSerializableExtra("atmInstance");
        List<Account> allAccounts = atmInstance.listAccounts();
        AtmMainActivity.this.displayAccounts(allAccounts, atmInstance);
      }
    }
  }

  /**
   * Given a string representation of the current customer, display this information.
   * @param infor the name and address of the user
   */
  private void displayUserInfor(String infor) {
    TextView customerInfor = (TextView) findViewById(R.id.CustomerInfor);
    customerInfor.setText(infor);
  }

  /**
   * Given a list of accounts the current customer has and an instance of atm, display the list
   * and set the list view clickable and link to deposit/withdrawal page.
   * @param allAccounts a list of accounts the current customer has
   * @param atmInstance an instance of atm
   */
  private void displayAccounts(final List<Account> allAccounts, final AtmInterface atmInstance) {
    // get string representation of each account
    List<String> accountsInfor = new ArrayList<>();
    for (Account current:allAccounts) {
      accountsInfor.add(current.toString());
    }
    // set the List View
    ArrayAdapter<String> accountList = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1,
        accountsInfor);
    ListView accountInforList = (ListView) findViewById(R.id.CustomerList);
    accountInforList.setAdapter(accountList);
    // Set the list clickable and link to deposit and withdrawal
    accountInforList.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent accountDetail = new Intent(AtmMainActivity.this, AccountDetail.class);
        accountDetail.putExtra("account", allAccounts.get(position));
        accountDetail.putExtra("atmInstance", (Atm) atmInstance);
        AtmMainActivity.this.startActivityForResult(accountDetail, 1);
      }
    });
  }

  /**
   *  Set the view message button onclick move to MessageSelectActivity.
   *
   * @param userId the current customer ID.
   */
  private void displayMyMessages(final int userId) {
    Button customerAllMessages = (Button) findViewById(R.id.CustomerAllMessages);
    customerAllMessages.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent displayMessages = new Intent(AtmMainActivity.this, MessageSelectActivity.class);
        // pass the current customer id to next activity
        displayMessages.putExtra("id", userId);
        AtmMainActivity.this.startActivity(displayMessages);
      }
    });
  }

  /**
   * Set the sign out button.
   */
  private void signOut() {
    Button signOut = (Button) findViewById(R.id.Logout);
    signOut.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        AtmMainActivity.this.finish();
      }
    });
  }

}
