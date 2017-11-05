package com.bank.bank;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bank.account.Account;
import com.bank.databasehelper.DatabaseDriverAndroidHelper;

import java.util.List;

public class AdminCreate extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_admin_create);
	this.CreateAdminOnClick();
	this.CreateTellerOnClick();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
    super.onActivityResult(requestCode, resultCode, resultIntent);
    if ((requestCode == 1) || (requestCode == 2)) {
      if(resultCode == Activity.RESULT_CANCELED){
        AdminCreate.this.finish();
      }
    }
  }

  /**
   * Create the admin after clicking the button.
   */
  protected void CreateAdminOnClick() {
	Button createAdminButton =(Button) findViewById(R.id.create_admin);
	createAdminButton.setOnClickListener(new Button.OnClickListener() {
	  public void onClick(View v) {
		Intent intent = new Intent(AdminCreate.this, AdminCreateAdmin.class);
		AdminCreate.this.startActivityForResult(intent, 1);
	  }
	});
  }

  /**
   * Create the teller after clicking the button.
   */
  protected void CreateTellerOnClick() {
	Button  createTellerButton =(Button) findViewById(R.id.create_teller);
	createTellerButton.setOnClickListener(new Button.OnClickListener() {
	  public void onClick(View v) {
		Intent intent = new Intent(AdminCreate.this, AdminCreateTeller.class);
		AdminCreate.this.startActivityForResult(intent, 2);
	  }
	});
  }
}
