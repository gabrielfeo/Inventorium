package com.gfeo.inventorium;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;

import com.gfeo.inventorium.InventoryLoaderCallbacks.CursorLoaderCallbacks;
import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;
import com.gfeo.inventorium.databinding.InventoryActivityBinding;

public class InventoryActivity extends AppCompatActivity {

	private InventoryActivityBinding binding;
	private Cursor cursor;
	private final LoaderCallbacks loaderCallbacks = new CursorLoaderCallbacks(this) {
		@Override
		public void onLoadFinished(@NonNull Loader loader, Object data) {
			cursor = (Cursor) data;
			setListViewAdapter();
			showListView();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory);
		setSupportActionBar(binding.inventoryToolbar);
		setupListView();
		setupFab();
		loadCursor();
	}

	private void setupListView() {
		binding.inventoryListview.setOnItemClickListener((parent, view, position, id) -> {
			Uri itemUri = ContentUris.withAppendedId(ItemTable.CONTENT_URI, id);
			startActivity(new Intent(Intent.ACTION_VIEW, itemUri, this, DetailActivity.class));
		});
	}

	private void setupFab() {
		binding.inventoryFab.setOnClickListener(
				view -> startActivity(new Intent(this, EditorActivity.class)));
	}

	private void loadCursor() {
		Bundle args = new Bundle();
		args.putString(CursorLoaderCallbacks.URI_ARGS_KEY, ItemTable.CONTENT_URI.toString());
		getSupportLoaderManager().restartLoader(0, args, loaderCallbacks);
	}

	private void setListViewAdapter() {
		CursorAdapter adapter = (CursorAdapter) binding.inventoryListview.getAdapter();
		if (adapter == null) {
			binding.inventoryListview.setAdapter(new InventoryCursorAdapter(this,
			                                                                cursor));
		} else { adapter.swapCursor(cursor); }

	}

	private void showListView() {
		binding.inventoryProgressbar.setVisibility(View.GONE);
		binding.inventoryListview.setVisibility(View.VISIBLE);
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
				loadCursor();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
	}

}
