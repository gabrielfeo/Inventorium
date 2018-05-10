package com.gfeo.inventorium;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;

class DetailsLoader extends AsyncTaskLoader<ItemDetails> {

	private final static String LOG_TAG = DetailsLoader.class.getSimpleName();
	private final Cursor cursor;
	private final ItemDetails itemDetails;

	DetailsLoader(Context context, Cursor cursor, ItemDetails itemDetails) {
		super(context);
		this.cursor = cursor;
		this.itemDetails = itemDetails;
	}

	@Nullable
	@Override
	public ItemDetails loadInBackground() {
		try {
			cursor.moveToFirst();
			setName();
			setDescription();
			setQuantity();
			calculateAndSetPrices();
			setSupplierEmail();
			setSupplierPhone();
			setNotes();
		} finally {
			cursor.close();
		}
		return itemDetails;
	}

	private void setName() {
		String itemName = cursor.getString(ItemTable.COLUMN_INDEX_NAME);
		itemDetails.setName(itemName);
	}

	private void setDescription() {
		String description = cursor.getString(ItemTable.COLUMN_INDEX_DESCRIPTION);
		itemDetails.setDescription(description);
	}

	private void setQuantity() {
		int quantity = cursor.getInt(ItemTable.COLUMN_INDEX_QUANTITY);
		itemDetails.setQuantity(quantity);
	}

	private void calculateAndSetPrices() {
		int quantity = cursor.getInt(ItemTable.COLUMN_INDEX_QUANTITY);
		//Unitary values
		double unitCostPrice = cursor.getFloat(ItemTable.COLUMN_INDEX_UNIT_COST_PRICE);
		double unitSellingPrice = cursor.getFloat(ItemTable.COLUMN_INDEX_UNIT_SELL_PRICE);
		double unitProfit = unitSellingPrice - unitCostPrice;
		//Total stock values
		double totalCost = unitCostPrice * quantity;
		double totalRevenue = unitSellingPrice * quantity;
		double totalProfit = unitProfit * quantity;
		//Set to itemDetails
		itemDetails.setTotalCost(totalCost);
		itemDetails.setTotalRevenue(totalRevenue);
		itemDetails.setTotalProfit(totalProfit);
		itemDetails.setUnitCostPrice(unitCostPrice);
		itemDetails.setUnitSellingPrice(unitSellingPrice);
		itemDetails.setUnitProfit(unitProfit);
	}

	private void setSupplierEmail() {
		String email = cursor.getString(ItemTable.COLUMN_INDEX_SUPPLIER_EMAIL);
		itemDetails.setSupplierEmail(email);
	}

	private void setSupplierPhone() {
		String phoneString = cursor.getString(ItemTable.COLUMN_INDEX_SUPPLIER_PHONE);
		itemDetails.setSupplierPhone(phoneString);
	}

	private void setNotes() {
		String notes = cursor.getString(ItemTable.COLUMN_INDEX_NOTES);
		itemDetails.setNotes(notes);
	}

}
