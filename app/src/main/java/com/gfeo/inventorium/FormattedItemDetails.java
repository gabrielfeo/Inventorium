package com.gfeo.inventorium;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.text.NumberFormat;
import java.util.Locale;

class FormattedItemDetails extends ItemDetails {

	private static final String LOG_TAG = FormattedItemDetails.class.getSimpleName();
	private final Context context;
	private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	private int rawQuantity;

	FormattedItemDetails(Context context, Uri uri) {
		super(uri);
		this.context = context;
		//		currencyFormat = NumberFormat.getCurrencyInstance();
	}

	@Override
	public void setQuantity(int quantity) {
		rawQuantity = quantity;
		this.quantity = context.getString(R.string.detail_quantity, quantity);
	}

	@Override
	public void setTotalCost(double totalCost) {
		String totalCostString = currencyFormat.format(totalCost);
		this.totalCost = context.getString(R.string.detail_total_cost, totalCostString);
	}

	@Override
	public void setTotalRevenue(double totalRevenue) {
		String totalRevenueString = currencyFormat.format(totalRevenue);
		this.totalRevenue = context.getString(R.string.detail_total_revenue,
		                                      totalRevenueString);
	}

	@Override
	public void setTotalProfit(double totalProfit) {
		String totalProfitString = currencyFormat.format(totalProfit);
		this.totalProfit = context.getString(R.string.detail_total_profit,
		                                     totalProfitString,
		                                     rawQuantity);
	}

	@Override
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = context.getString(R.string.detail_supplier_email,
		                                       supplierEmail.toLowerCase());
	}

	@Override
	public void setSupplierPhone(String supplierPhone) {
		String countryCode = Locale.getDefault().getCountry();
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		if (phoneUtil.isPossibleNumber(supplierPhone, countryCode)) {
			try {
				PhoneNumber phoneNumber = phoneUtil.parse(supplierPhone, countryCode);
				supplierPhone = phoneUtil.format(phoneNumber,
				                                 PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
			} catch (NumberParseException e) {
				Log.e(LOG_TAG, "Couldn't parse phone number", e);
			}
		} else { supplierPhone = "Invalid number"; }
		this.supplierPhone = context.getString(R.string.detail_supplier_phone, supplierPhone);
	}

	@Override
	public void setUnitCostPrice(double unitCostPrice) {
		String unitCostPriceString = currencyFormat.format(unitCostPrice);
		this.unitCostPrice = context.getString(R.string.detail_unit_cost_price,
		                                       unitCostPriceString);
	}

	@Override
	public void setUnitSellingPrice(double unitSellingPrice) {
		String unitSellingPriceString = currencyFormat.format(unitSellingPrice);
		this.unitSellingPrice = context.getString(R.string.detail_unit_selling_price,
		                                          unitSellingPriceString);
	}

	@Override
	public void setUnitProfit(double unitProfit) {
		String unitProfitString = currencyFormat.format(unitProfit);
		this.unitProfit = context.getString(R.string.detail_unit_profit, unitProfitString);

	}
}
