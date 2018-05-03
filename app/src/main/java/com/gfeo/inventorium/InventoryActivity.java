package com.gfeo.inventorium;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.gfeo.inventorium.data.InventoryDbContract;

public class InventoryActivity extends AppCompatActivity {

	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
		setSupportActionBar(findViewById(R.id.inventory_toolbar));
		setupListView();
		setupFab();
	}

	private void setupListView() {
		ListView listView = findViewById(R.id.inventory_listview);
		cursor = getContentResolver().query(InventoryDbContract.ItemTable.CONTENT_URI,
		                                           null, null, null, null);
		listView.setAdapter(new InventoryCursorAdapter(this, cursor));
		listView.setOnItemClickListener((parent, view, position, id) ->
				                                startActivity(new Intent(this,
				                                                         EditorActivity.class)));
	}

	private void setupFab() {
		findViewById(R.id.inventory_fab).setOnClickListener(view -> {
			Intent intent = new Intent(this, EditorActivity.class);
			startActivity(intent);
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
	}
}
