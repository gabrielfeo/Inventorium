package com.gfeo.inventorium;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;
import com.gfeo.inventorium.databinding.DetailActivityBinding;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

	private static final String LOG_TAG = DetailActivity.class.getSimpleName();
	private String itemName;
	private String supplierEmail;
	private String supplierPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DetailActivityBinding binding = DataBindingUtil.setContentView(this,
		                                                               R.layout.activity_detail);
		setupToolbar(binding);
		Cursor cursor = null;
		try {
			cursor = getNewCursor(getIntent().getData());
			fillTextViewsWithCursorData(binding, cursor);
		} finally {
			if (cursor != null) { cursor.close(); }
		}
	}

	private void setupToolbar(DetailActivityBinding binding) {
		setSupportActionBar(binding.detailToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.detail_activity_title);
	}

	private void fillTextViewsWithCursorData(DetailActivityBinding binding, Cursor cursor) {
		cursor.moveToFirst();
		int quantity = cursor.getInt(ItemTable.COLUMN_INDEX_QUANTITY);
		//Unitary values
		double unitCostPrice = cursor.getFloat(ItemTable.COLUMN_INDEX_UNIT_COST_PRICE);
		double unitSellingPrice = cursor.getFloat(ItemTable.COLUMN_INDEX_UNIT_SELL_PRICE);
		double unitProfit = unitSellingPrice - unitCostPrice;
		//Total stock values
		double totalCost = unitCostPrice * quantity;
		double totalRevenue = unitSellingPrice * quantity;
		double totalProfit = unitProfit * quantity;

		//Format currency values
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		String unitCostPriceString = currencyFormat.format(totalCost);
		String unitSellingPriceString = currencyFormat.format(totalRevenue);
		String unitProfitString = currencyFormat.format(totalProfit);
		String totalCostString = currencyFormat.format(totalCost);
		String totalRevenueString = currencyFormat.format(totalRevenue);
		String totalProfitString = currencyFormat.format(totalProfit);

		//Format supplier email and phone and assign to field
		supplierEmail = cursor.getString(ItemTable.COLUMN_INDEX_SUPPLIER_EMAIL).toLowerCase();
		supplierPhone = cursor.getString(ItemTable.COLUMN_INDEX_SUPPLIER_PHONE);
		formatSupplierPhone();

		//Get resource strings filling in placeholders
		itemName = cursor.getString(ItemTable.COLUMN_INDEX_NAME);
		String description = cursor.getString(ItemTable.COLUMN_INDEX_DESCRIPTION);
		String quantityString = getString(R.string.detail_quantity, quantity);
		totalCostString = getString(R.string.detail_total_cost, totalCostString);
		totalRevenueString = getString(R.string.detail_total_revenue, totalRevenueString);
		totalProfitString = getString(R.string.detail_total_profit, totalProfitString, quantity);
		String supplierEmailString = getString(R.string.detail_supplier_email, supplierEmail);
		String supplierPhoneString = getString(R.string.detail_supplier_phone, supplierPhone);
		String notes = cursor.getString(ItemTable.COLUMN_INDEX_NOTES);
		unitCostPriceString = getString(R.string.detail_unit_cost_price, unitCostPriceString);
		unitSellingPriceString = getString(R.string.detail_unit_selling_price,
		                                   unitSellingPriceString);
		unitProfitString = getString(R.string.detail_unit_profit, unitProfitString);

		//Set TextViews text
		binding.detailTextviewName.setText(itemName);
		binding.detailTextviewDescription.setText(description);
		binding.detailTextviewQuantity.setText(quantityString);
		binding.detailTextviewTotalCost.setText(totalCostString);
		binding.detailTextviewTotalRevenue.setText(totalRevenueString);
		binding.detailTextviewTotalProfit.setText(totalProfitString);
		binding.detailTextviewSupplierEmail.setText(supplierEmailString);
		binding.detailTextviewSupplierPhone.setText(supplierPhoneString);
		binding.detailTextviewNotes.setText(notes);
		binding.detailTextviewUnitCost.setText(unitCostPriceString);
		binding.detailTextviewUnitSellingPrice.setText(unitSellingPriceString);
		binding.detailTextviewUnitProfit.setText(unitProfitString);

		//Hide Notes TextViews if applicable
		if (notes.isEmpty()) {
			binding.detailTextviewHeaderNotes.setVisibility(View.GONE);
			binding.detailTextviewNotes.setVisibility(View.GONE);
		}
	}

	private void formatSupplierPhone() {
		String countryCode = Locale.getDefault().getCountry();
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		if (phoneUtil.isPossibleNumber(supplierPhone, countryCode)) {
			try {
				PhoneNumber phoneNumber = phoneUtil.parse(supplierPhone, countryCode);
				supplierPhone =
						phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
			} catch (NumberParseException e) {
				Log.e(LOG_TAG, "Couldn't parse phone number", e);
			}
		}
	}

	private Cursor getNewCursor(Uri itemUri) {
		return getContentResolver().query(itemUri, null, null, null, null);
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
				sendEmailIntentIfResolvable();
				break;
			case R.id.menu_detail_order_phone:
				sendPhoneIntentIfResolvable();
				break;
			case R.id.menu_detail_edit:
				startActivity(new Intent(this, EditorActivity.class));
				break;
			case R.id.menu_detail_delete:
				deleteEntryAndExitActivity();
				break;
			default:
				return false;
		}
		return true;
	}

	private void sendPhoneIntentIfResolvable() {
		Uri phoneUri = Uri.parse("tel:" + supplierPhone);
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, phoneUri);
		if (phoneIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(phoneIntent);
		} else {
			Toast.makeText(this, getString(R.string.toast_error_intent_phone),
			               Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmailIntentIfResolvable() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.putExtra(Intent.EXTRA_EMAIL, supplierEmail);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
		                     getString(R.string.intent_email_subject, itemName));
		if (emailIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(emailIntent);
		} else {
			Toast.makeText(this, getString(R.string.toast_error_intent_email),
			               Toast.LENGTH_SHORT).show();
		}
	}

	private void deleteEntryAndExitActivity() {
		int rowsDeleted = getContentResolver().delete(getIntent().getData(), null, null);
		if (rowsDeleted > 0) { startActivity(new Intent(this, InventoryActivity.class)); }
	}
}
