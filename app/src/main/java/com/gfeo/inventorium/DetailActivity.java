package com.gfeo.inventorium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		setSupportActionBar(findViewById(R.id.detail_toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//TODO Implement button actions
		switch (item.getItemId()) {
			case R.id.menu_detail_order_email:
				break;
			case R.id.menu_detail_order_phone:
				break;
			case R.id.menu_detail_edit:
				startActivity(new Intent(this, EditorActivity.class));
				break;
			case R.id.menu_detail_delete:
				break;
			default:
				return false;
		}
		return true;
	}
}
