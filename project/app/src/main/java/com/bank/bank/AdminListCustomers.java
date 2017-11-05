package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bank.user.Customer;

import java.util.ArrayList;
import java.util.List;

public class AdminListCustomers extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_list_customers);
    Intent intent = getIntent();
    final AdminTerminal newAdmin = (AdminTerminal)
            intent.getSerializableExtra("adminTerminalInstance");
    List<Customer> allCustomers = newAdmin.listAllCustomers();
    listCustomers(allCustomers);
  }

  /**
   * List all of customers after clicking.
   * @param allCustomers The list of Customers that is proceeded by the admin's listAllCustomers.
   */
  private void listCustomers(List<Customer> allCustomers) {
    List<String> customersInfo = new ArrayList<>();
    for (Customer current:allCustomers) {
      customersInfo.add(current.toString());
    }
    ArrayAdapter<String> customerList = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1,
            customersInfo);
    // Put the string of the list into the ListView.
    ListView customerInfoList = (ListView) findViewById(R.id.CustomerList);
    customerInfoList.setAdapter(customerList);
  }

}
