package com.gfeo.inventorium;

import android.net.Uri;

class ItemDetails {

	private static final String LOG_TAG = ItemDetails.class.getSimpleName();
	protected final Uri uri;
	protected String name;
	protected String description;
	protected String quantity;
	protected String totalCost;
	protected String totalRevenue;
	protected String totalProfit;
	protected String supplierEmail;
	protected String supplierPhone;
	protected String notes;
	protected String unitCostPrice;
	protected String unitSellingPrice;
	protected String unitProfit;

	ItemDetails(Uri uri) {
		this.uri = uri;
	}

	public Uri getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = String.valueOf(quantity);
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = String.valueOf(totalCost);
	}

	public String getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = String.valueOf(totalRevenue);
	}

	public String getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(double totalProfit) {
		this.totalProfit = String.valueOf(totalProfit);
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getSupplierPhone() {
		return supplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUnitCostPrice() {
		return unitCostPrice;
	}

	public void setUnitCostPrice(double unitCostPrice) {
		this.unitCostPrice = String.valueOf(unitCostPrice);
	}

	public String getUnitSellingPrice() {
		return unitSellingPrice;
	}

	public void setUnitSellingPrice(double unitSellingPrice) {
		this.unitSellingPrice = String.valueOf(unitSellingPrice);
	}

	public String getUnitProfit() {
		return unitProfit;
	}

	public void setUnitProfit(double unitProfit) {
		this.unitProfit = String.valueOf(unitProfit);
	}

}
