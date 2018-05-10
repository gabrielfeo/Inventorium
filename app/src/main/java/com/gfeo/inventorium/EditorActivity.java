package com.gfeo.inventorium;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.gfeo.inventorium.InventoryLoaderCallbacks.CursorLoaderCallbacks;
import com.gfeo.inventorium.InventoryLoaderCallbacks.DetailsLoaderCallbacks;
import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;
import com.gfeo.inventorium.databinding.EditorActivityBinding;

public class EditorActivity extends AppCompatActivity {

	private EditorActivityBinding binding;
	private ItemDetails itemDetails;
	private static int quantityCount;
	private static final int DECREASE_QUANTITY = 0;
	private static final int INCREASE_QUANTITY = 1;
	private static final int SET_TO_INPUTTED_QUANTITY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this,
		                                         R.layout.activity_editor);
		Uri itemUri = getIntent().getData();
		if (itemUri == null) {
			showEditorViews();
		} else {
			itemDetails = new ItemDetails(itemUri);
			loadCursor();
		}
		setupToolbar();
		setupQuantityCounterViews();
	}

	private void setupToolbar() {
		setSupportActionBar(binding.editorToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		int titleResourceId = (itemDetails == null) ? R.string.editor_activity_title_adding
		                                            : R.string.editor_activity_title_editing;
		getSupportActionBar().setTitle(titleResourceId);
	}

	private void fillViewsWithPreviousDetails() {
		binding.editorEdittextName.setText(itemDetails.getName());
		binding.editorEdittextDescription.setText(itemDetails.getDescription());
		binding.editorEdittextQuantity.setText(itemDetails.getQuantity());
		binding.editorEdittextCost.setText(itemDetails.getUnitCostPrice());
		binding.editorEdittextSelling.setText(itemDetails.getUnitSellingPrice());
		binding.editorEdittextSupplierEmail.setText(itemDetails.getSupplierEmail());
		binding.editorEdittextSupplierPhone.setText(itemDetails.getSupplierPhone());
		binding.editorEdittextNotes.setText(itemDetails.getNotes());
	}

	private void loadCursor() {
		Bundle args = new Bundle();
		args.putString("uri", itemDetails.getUri().toString());
		getSupportLoaderManager().restartLoader(4, args, cursorLoaderCallbacks);
	}

	private final LoaderManager.LoaderCallbacks cursorLoaderCallbacks =
			new CursorLoaderCallbacks(this) {
				@Override
				public void onLoadFinished(@NonNull Loader loader, Object data) {
					loadDetails((Cursor) data);
				}
			};

	private void loadDetails(Cursor cursor) {
		LoaderManager.LoaderCallbacks detailsLoaderCallbacks = new DetailsLoaderCallbacks(this,
		                                                                                  cursor,
		                                                                                  itemDetails) {
			@Override
			public void onLoadFinished(@NonNull Loader loader, Object data) {
				itemDetails = (ItemDetails) data;
				fillViewsWithPreviousDetails();
				updateQuantityCount(SET_TO_INPUTTED_QUANTITY);
				showEditorViews();
			}
		};
		getSupportLoaderManager().restartLoader(3, null, detailsLoaderCallbacks)
		                         .forceLoad();
	}

	private void showEditorViews() {
		binding.editorProgressbar.setVisibility(View.GONE);
		binding.editorScrollview.setVisibility(View.VISIBLE);
	}

	private void setupQuantityCounterViews() {
		if (itemDetails == null) { quantityCount = 0; }
		binding.editorEdittextQuantity.setText(String.valueOf(quantityCount));
		binding.editorButtonMinus.setOnClickListener(
				view -> updateQuantityCount(DECREASE_QUANTITY));
		binding.editorButtonPlus.setOnClickListener(
				view -> updateQuantityCount(INCREASE_QUANTITY));
		binding.editorEdittextQuantity.setOnEditorActionListener(
				(view, actionId, keyEvent) -> {
					if (actionId == EditorInfo.IME_ACTION_DONE
							|| actionId == EditorInfo.IME_ACTION_NEXT) {
						updateQuantityCount(SET_TO_INPUTTED_QUANTITY);
						view.clearFocus();
						hideSoftKeyboard(view);
						return true;
					} else { return false; }
				});
	}

	private void updateQuantityCount(int operation) {
		if (operation == DECREASE_QUANTITY && quantityCount > 0) {
			quantityCount--;
		} else if (operation == INCREASE_QUANTITY) {
			quantityCount++;
		} else if (operation == SET_TO_INPUTTED_QUANTITY) {
			quantityCount = Integer.valueOf(binding.editorEdittextQuantity.getText().toString());
		} else { return; }
		binding.editorEdittextQuantity.setText(String.valueOf(quantityCount));
	}

	private void hideSoftKeyboard(View requestingView) {
		InputMethodManager imm =
				(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(requestingView.getWindowToken(), 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		if (itemDetails == null) { menu.findItem(R.id.menu_editor_delete).setVisible(false); }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_editor_save:
				onSavePressed();
				break;
			case R.id.menu_editor_delete:
				deleteInDb();
				break;
			default:
				return false;
		}
		return true;
	}

	private void onSavePressed() {
		ContentValues values = getInputtedValues();
		if (hasNoEmptyStrings(values)) {
			if (itemDetails == null) { insertInDb(values); } else { updateInDb(values); }
		}
		onBackPressed();
	}

	private ContentValues getInputtedValues() {
		ContentValues values = new ContentValues();
		values.put(ItemTable.COLUMN_NAME_NAME,
		           binding.editorEdittextName.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_DESCRIPTION,
		           binding.editorEdittextDescription.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_UNIT_COST_PRICE,
		           binding.editorEdittextCost.getText().toString());
		values.put(ItemTable.COLUMN_NAME_UNIT_SELL_PRICE,
		           binding.editorEdittextSelling.getText().toString());
		values.put(ItemTable.COLUMN_NAME_QUANTITY,
		           binding.editorEdittextQuantity.getText().toString());
		values.put(ItemTable.COLUMN_NAME_SUPPLIER_EMAIL,
		           binding.editorEdittextSupplierEmail.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_SUPPLIER_PHONE,
		           binding.editorEdittextSupplierPhone.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_NOTES,
		           binding.editorEdittextNotes.getText().toString());
		return values;
	}

	private boolean hasNoEmptyStrings(ContentValues values) {
		boolean hasEmptyStrings =
				Stream.of(values.valueSet().iterator())
				      .filterNot(mapEntry -> mapEntry.getKey().equals(ItemTable.COLUMN_NAME_NOTES))
				      .anyMatch(mapEntry -> ((String) mapEntry.getValue()).isEmpty());
		if (hasEmptyStrings) {
			Toast.makeText(this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT)
			     .show();
			return false;
		} else { return true; }
	}

	private void insertInDb(ContentValues values) {
		Uri newRowUri = getContentResolver().insert(ItemTable.CONTENT_URI, values);
		if (newRowUri == null) {
			Toast.makeText(this, getString(R.string.toast_error_insert), Toast.LENGTH_SHORT)
			     .show();
		} else { NavUtils.navigateUpFromSameTask(this); }
	}

	private void updateInDb(ContentValues values) {
		int rowsUpdated = getContentResolver().update(itemDetails.getUri(), values, null, null);
		if (rowsUpdated > 0) {
			onBackPressed();
		} else {
			Toast.makeText(this, getString(R.string.toast_error_insert), Toast.LENGTH_SHORT)
			     .show();
		}
	}

	private void deleteInDb() {
		int rowsDeleted = getContentResolver().delete(itemDetails.getUri(), null, null);
		if (rowsDeleted > 0) {
			startActivity(new Intent(this, InventoryActivity.class));
		} else {
			Toast.makeText(this, getString(R.string.toast_error_delete), Toast.LENGTH_SHORT)
			     .show();
		}
	}

}
