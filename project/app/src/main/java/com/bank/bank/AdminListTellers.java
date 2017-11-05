package com.bank.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bank.user.Teller;

import java.util.ArrayList;
import java.util.List;

public class AdminListTellers extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_list_tellers);
    Intent intent = getIntent();
    final AdminTerminal newAdmin =
            (AdminTerminal) intent.getSerializableExtra("adminTerminalInstance");
    List<Teller> allTellers = newAdmin.listAllTellers();
    listTellers(allTellers);
  }

  /**
   * List all of tellers after clicking.
   * @param allTellers a list of tellers that is proceeded by allTellers.
   */
  private void listTellers(List<Teller> allTellers) {
    List<String>  tellersInfo = new ArrayList<>();
    for (Teller current:allTellers) {
      tellersInfo.add(current.toString());
    }
    ArrayAdapter<String> tellerList = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1,
            tellersInfo);
    // Put the string of the list into the ListView.
    ListView tellerInfoList = (ListView) findViewById(R.id.TellerList);
    tellerInfoList.setAdapter(tellerList);
  }
}
