package com.gfeo.inventorium;

import android.content.ContentValues;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;
import com.gfeo.inventorium.databinding.EditorActivityBinding;

public class EditorActivity extends AppCompatActivity {

	private EditorActivityBinding binding;
	private static int quantityCount;
	private static final int DECREASE_QUANTITY = 0;
	private static final int INCREASE_QUANTITY = 1;
	private static final int SET_TO_INPUTTED_QUANTITY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this,
		                                         R.layout.activity_editor);

		setupToolbar(true);
		setupQuantityCounter();
	}

	private void setupToolbar(boolean isNewEntry) {
		setSupportActionBar(binding.editorToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		int titleResourceId = (isNewEntry) ? R.string.editor_activity_title_adding
		                                   : R.string.editor_activity_title_editing;
		getSupportActionBar().setTitle(titleResourceId);
	}

	private void setupQuantityCounter() {
		//TODO Change for "update" functionality
		quantityCount = 0;
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_editor_save:
				ContentValues values = getInputtedValues();
				if (hasNoEmptyStrings(values)) { insertInDb(values); }
				break;
			case R.id.menu_editor_delete:
				//TODO Delete menu button action
				break;
			default:
				return false;
		}
		return true;
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

}
