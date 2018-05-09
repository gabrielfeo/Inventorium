package com.gfeo.inventorium;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.text.NumberFormat;
import java.util.Locale;

class DetailsLoader extends AsyncTaskLoader<ItemDetails> {

	private final static String LOG_TAG = DetailsLoader.class.getSimpleName();
	private Cursor cursor;
	private ItemDetails itemDetails;

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
			setPrices();
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
		String quantityString = getContext().getString(R.string.detail_quantity, quantity);
		itemDetails.setQuantity(quantityString);
	}

	private void setPrices() {
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
		String totalCostString = currencyFormat.format(totalCost);
		String totalRevenueString = currencyFormat.format(totalRevenue);
		String totalProfitString = currencyFormat.format(totalProfit);
		String unitCostPriceString = currencyFormat.format(unitCostPrice);
		String unitSellingPriceString = currencyFormat.format(unitSellingPrice);
		String unitProfitString = currencyFormat.format(unitProfit);
		//Get resource strings filling placeholders with formatted currency values
		totalCostString = getContext().getString(R.string.detail_total_cost, totalCostString);
		totalRevenueString = getContext().getString(R.string.detail_total_revenue,
		                                            totalRevenueString);
		totalProfitString = getContext().getString(R.string.detail_total_profit,
		                                           totalProfitString, quantity);
		unitCostPriceString = getContext().getString(R.string.detail_unit_cost_price,
		                                             unitCostPriceString);
		unitSellingPriceString = getContext().getString(R.string.detail_unit_selling_price,
		                                                unitSellingPriceString);
		unitProfitString = getContext().getString(R.string.detail_unit_profit, unitProfitString);
		//Set to itemDetails
		itemDetails.setTotalCost(totalCostString);
		itemDetails.setTotalRevenue(totalRevenueString);
		itemDetails.setTotalProfit(totalProfitString);
		itemDetails.setUnitCostPrice(unitCostPriceString);
		itemDetails.setUnitSellingPrice(unitSellingPriceString);
		itemDetails.setUnitProfit(unitProfitString);
	}

	private void setSupplierEmail() {
		String email = cursor.getString(ItemTable.COLUMN_INDEX_SUPPLIER_EMAIL)
		                     .toLowerCase();
		email = getContext().getString(R.string.detail_supplier_email, email);
		itemDetails.setSupplierEmail(email);
	}

	private void setSupplierPhone() {
		String phoneString = cursor.getString(ItemTable.COLUMN_INDEX_SUPPLIER_PHONE);
		String countryCode = Locale.getDefault().getCountry();
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		if (phoneUtil.isPossibleNumber(phoneString, countryCode)) {
			try {
				PhoneNumber phoneNumber = phoneUtil.parse(phoneString, countryCode);
				phoneString = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat
						.NATIONAL);
				itemDetails.setSupplierPhone(phoneString);
			} catch (NumberParseException e) {
				Log.e(LOG_TAG, "Couldn't parse phone number", e);
			}
		}
	}

	private void setNotes() {
		String notes = cursor.getString(ItemTable.COLUMN_INDEX_NOTES);
		itemDetails.setNotes(notes);
	}

}
