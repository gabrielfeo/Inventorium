package com.gfeo.inventorium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class InventoryActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
		setSupportActionBar((Toolbar) findViewById(R.id.inventory_toolbar));
		setupFab();
	}

	private void setupFab() {
		findViewById(R.id.inventory_fab).setOnClickListener(view -> {
			Intent intent = new Intent(this, EditorActivity.class);
			startActivity(intent);
		});
	}
}
