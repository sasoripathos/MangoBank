package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bank.user.Admin;

import java.util.ArrayList;
import java.util.List;

public class AdminListAdmins extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_list_admins);
    Intent intent = getIntent();
    final AdminTerminal newAdmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    List<Admin> allAdmins = newAdmin.listAllAdmins();
    listAdmins(allAdmins);
  }

  /**
   * List all of admins after clicking.
   * @param allAdmins The list of Admin that is proceeded by the allAdmins account.
   */
  private void listAdmins(List<Admin> allAdmins) {
    List<String>  adminsInfo = new ArrayList<>();
    for (Admin current:allAdmins) {
      adminsInfo.add(current.toString());
    }
    ArrayAdapter<String> adminList = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1,
            adminsInfo);
    // Put the string of the list into the ListView.
    ListView adminsInfoList = (ListView) findViewById(R.id.AdminList);
    adminsInfoList.setAdapter(adminList);
  }
}
