package com.gfeo.inventorium;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.gfeo.inventorium.InventoryLoaderCallbacks.CursorLoaderCallbacks;
import com.gfeo.inventorium.InventoryLoaderCallbacks.DetailsLoaderCallbacks;
import com.gfeo.inventorium.databinding.DetailActivityBinding;

public class DetailActivity extends AppCompatActivity {

	private static final String LOG_TAG = DetailActivity.class.getSimpleName();
	private static final int CURSOR_LOADER_ID = 1;
	private static final int DETAILS_LOADER_ID = 2;
	private DetailActivityBinding binding;
	private FormattedItemDetails itemDetails;
	private final LoaderCallbacks cursorLoaderCallbacks = new CursorLoaderCallbacks(this) {
		@Override
		public void onLoadFinished(@NonNull Loader loader, Object data) {
			loadDetails((Cursor) data);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		itemDetails = new FormattedItemDetails(this, getIntent().getData());
		binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
		setupToolbar(binding);
	}

	private void setupToolbar(DetailActivityBinding binding) {
		setSupportActionBar(binding.detailToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.activity_title_detail);
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadCursor();
	}

	private void loadCursor() {
		Bundle args = new Bundle();
		args.putString(CursorLoaderCallbacks.URI_ARGS_KEY, itemDetails.getUri().toString());
		getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, args, cursorLoaderCallbacks);
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
				sendEmailIntent();
				break;
			case R.id.menu_detail_order_phone:
				sendPhoneIntent();
				break;
			case R.id.menu_detail_edit:
				startActivity(new Intent(Intent.ACTION_VIEW, itemDetails.getUri(),
				                         this, EditorActivity.class));
				break;
			case R.id.menu_detail_delete:
				showDeleteEntryDialog();
				break;
			default:
				return false;
		}
		return true;
	}

	private void sendEmailIntent() {
		if (itemDetails.getSupplierEmail() == null) { return; }
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		emailIntent.setData(Uri.parse("mailto:"));
		String[] recipients = {itemDetails.getPlainSupplierEmail()};
		emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
		                     getString(R.string.intent_email_subject, itemDetails.getName()));
		if (emailIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(emailIntent);
		} else {
			Toast.makeText(this, getString(R.string.toast_error_intent_email),
			               Toast.LENGTH_SHORT).show();
		}
	}

	private void sendPhoneIntent() {
		if (itemDetails.getSupplierPhone() == null) { return; }
		Uri phoneUri = Uri.parse("tel:" + itemDetails.getPlainSupplierPhone());
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, phoneUri);
		if (phoneIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(phoneIntent);
		} else {
			Toast.makeText(this, getString(R.string.toast_error_intent_phone),
			               Toast.LENGTH_SHORT).show();
		}
	}

	private void showDeleteEntryDialog() {
		DialogInterface.OnClickListener dialogClickListener = (dialog, button) -> {
			if (button == DialogInterface.BUTTON_POSITIVE) { deleteInDb(); }
		};
		DeleteEntryDialogs.showDeleteOneDialog(this, dialogClickListener);
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

	private void loadDetails(Cursor cursor) {
		LoaderCallbacks detailsLoaderCallbacks = new DetailsLoaderCallbacks(this, cursor,
		                                                                    itemDetails) {
			@Override
			public void onLoadFinished(@NonNull Loader loader, Object data) {
				itemDetails = (FormattedItemDetails) data;
				fillTextViewsWithDetails();
				fillImageView();
				hideNotesViewIfEmpty();
				showDetailsViews();
			}
		};
		getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID, null, detailsLoaderCallbacks)
		                         .forceLoad();
	}

	private void fillTextViewsWithDetails() {
		binding.detailTextviewName.setText(itemDetails.getName());
		binding.detailTextviewDescription.setText(itemDetails.getDescription());
		binding.detailTextviewQuantity.setText(itemDetails.getQuantity());
		binding.detailTextviewTotalCost.setText(itemDetails.getTotalCost());
		binding.detailTextviewTotalRevenue.setText(itemDetails.getTotalRevenue());
		binding.detailTextviewTotalProfit.setText(itemDetails.getTotalProfit());
		binding.detailTextviewSupplierEmail.setText(itemDetails.getSupplierEmail());
		binding.detailTextviewSupplierPhone.setText(itemDetails.getSupplierPhone());
		binding.detailTextviewNotes.setText(itemDetails.getNotes());
		binding.detailTextviewUnitCost.setText(itemDetails.getUnitCostPrice());
		binding.detailTextviewUnitSellingPrice.setText(itemDetails.getUnitSellingPrice());
		binding.detailTextviewUnitProfit.setText(itemDetails.getUnitProfit());
	}

	private void fillImageView() {
		binding.detailImageviewPicture.setImageBitmap(itemDetails.getPicture());
	}

	private void hideNotesViewIfEmpty() {
		if (itemDetails.getNotes().isEmpty()) {
			binding.detailTextviewHeaderNotes.setVisibility(View.GONE);
			binding.detailTextviewNotes.setVisibility(View.GONE);
		}
	}

	private void showDetailsViews() {
		binding.detailProgressbar.setVisibility(View.GONE);
		binding.detailScrollview.setVisibility(View.VISIBLE);
	}
}
