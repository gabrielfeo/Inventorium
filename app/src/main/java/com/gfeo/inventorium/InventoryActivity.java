package com.gfeo.inventorium;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;
import com.gfeo.inventorium.data.InventoryLoaderCallbacks;

public class InventoryActivity extends AppCompatActivity {

	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
		setSupportActionBar(findViewById(R.id.inventory_toolbar));
		setupListView();
		setupFab();
		newInventoryCursorLoader();
	}

	private void setupListView() {
		ListView listView = findViewById(R.id.inventory_listview);
		//onClick listener
		listView.setOnItemClickListener((parent, view, position, id) -> {
			Uri itemUri = ContentUris.withAppendedId(ItemTable.CONTENT_URI, id);
			startActivity(new Intent(Intent.ACTION_VIEW, itemUri, this, DetailActivity.class));
		});
	}

	private void setupFab() {
		findViewById(R.id.inventory_fab).setOnClickListener(
				view -> startActivity(new Intent(this, EditorActivity.class)));
	}

	private void newInventoryCursorLoader() {
		Bundle args = new Bundle();
		args.putString(InventoryLoaderCallbacks.URI_ARGS_KEY, ItemTable.CONTENT_URI.toString());
		getSupportLoaderManager().restartLoader(0, args, loaderCallbacks);
	}

	private void setListViewAdapter() {
		ListView listView = findViewById(R.id.inventory_listview);
		CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
		if (adapter == null) {
			listView.setAdapter(new InventoryCursorAdapter(this,
			                                               cursor));
		} else { adapter.swapCursor(cursor); }

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
				getContentResolver().delete(ItemTable.CONTENT_URI,
				                            null, null);
				newInventoryCursorLoader();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
	}

	private LoaderCallbacks loaderCallbacks = new InventoryLoaderCallbacks(this) {
		@Override
		public void onLoadFinished(@NonNull Loader loader, Object data) {
			cursor = (Cursor) data;
			setListViewAdapter();
		}
	};

}
