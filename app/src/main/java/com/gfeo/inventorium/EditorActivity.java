package com.gfeo.inventorium;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;

public class EditorActivity extends AppCompatActivity {

	private static int quantityCount;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private EditText costEditText;
	private EditText quantityEditText;
	private static final int DECREASE_QUANTITY = 0;
	private static final int INCREASE_QUANTITY = 1;
	private static final int SET_TO_INPUTTED_QUANTITY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		setSupportActionBar(findViewById(R.id.editor_toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		findEditTextViews();
		setupQuantityCounter();
	}

	private void findEditTextViews() {
		nameEditText = findViewById(R.id.editor_edittext_name);
		descriptionEditText = findViewById(R.id.editor_edittext_description);
		costEditText = findViewById(R.id.editor_edittext_cost);
		quantityEditText = findViewById(R.id.editor_edittext_quantity);
	}

	private void setupQuantityCounter() {
		//TODO Change for "update" functionality
		quantityCount = 0;
		quantityEditText.setText(String.valueOf(quantityCount));
		findViewById(R.id.editor_button_minus)
				.setOnClickListener(view -> updateQuantityCount(DECREASE_QUANTITY));
		findViewById(R.id.editor_button_plus)
				.setOnClickListener(view -> updateQuantityCount(INCREASE_QUANTITY));
		quantityEditText.setOnEditorActionListener(
				(view, actionId, keyEvent) -> {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
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
			quantityCount = Integer.valueOf(quantityEditText.getText().toString());
		} else { return; }
		quantityEditText.setText(String.valueOf(quantityCount));
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
		           nameEditText.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_DESCRIPTION,
		           descriptionEditText.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_COST_PRICE,
		           costEditText.getText().toString().trim());
		values.put(ItemTable.COLUMN_NAME_STOCK,
		           quantityEditText.getText().toString());
		return values;
	}

	private boolean hasNoEmptyStrings(ContentValues values) {
		boolean hasEmptyStrings = values.getAsString(ItemTable.COLUMN_NAME_NAME).isEmpty()
				|| values.getAsString(ItemTable.COLUMN_NAME_DESCRIPTION).isEmpty()
				|| values.getAsString(ItemTable.COLUMN_NAME_COST_PRICE).isEmpty();
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
