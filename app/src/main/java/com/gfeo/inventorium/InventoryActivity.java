package com.gfeo.inventorium;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.gfeo.inventorium.data.InventoryDbContract;

public class InventoryActivity extends AppCompatActivity {

	private InventoryCursorAdapter cursorAdapter;

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
		if (cursorAdapter == null) {
			cursorAdapter = new InventoryCursorAdapter(this, getNewCursor());
		}
		listView.setAdapter(cursorAdapter);
		//TODO update functionality
		listView.setOnItemClickListener((parent, view, position, id) ->
				                                startActivity(new Intent(this,
				                                                         EditorActivity.class)));
	}

	private Cursor getNewCursor() {
		return getContentResolver().query(InventoryDbContract.ItemTable.CONTENT_URI,
		                                  null, null, null, null);
	}

	private void setupFab() {
		findViewById(R.id.inventory_fab).setOnClickListener(view ->
			startActivity(new Intent(this, DetailActivity.class)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_inventory, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_inventory_delete_all:
				getContentResolver().delete(InventoryDbContract.ItemTable.CONTENT_URI,
				                            null, null);
				//				cursorAdapter.notifyDataSetChanged();
				cursorAdapter.changeCursor(getNewCursor());
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursorAdapter.changeCursor(null);
	}
}
