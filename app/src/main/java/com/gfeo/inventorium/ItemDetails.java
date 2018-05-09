package com.gfeo.inventorium;

import android.net.Uri;

class ItemDetails {

	private static final String LOG_TAG = ItemDetails.class.getSimpleName();
	private final Uri uri;
	private String name;
	private String description;
	private String quantity;
	private String totalCost;
	private String totalRevenue;
	private String totalProfit;
	private String supplierEmail;
	private String supplierPhone;
	private String notes;
	private String unitCostPrice;
	private String unitSellingPrice;
	private String unitProfit;

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

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(String totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public String getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(String totalProfit) {
		this.totalProfit = totalProfit;
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

	public void setUnitCostPrice(String unitCostPrice) {
		this.unitCostPrice = unitCostPrice;
	}

	public String getUnitSellingPrice() {
		return unitSellingPrice;
	}

	public void setUnitSellingPrice(String unitSellingPrice) {
		this.unitSellingPrice = unitSellingPrice;
	}

	public String getUnitProfit() {
		return unitProfit;
	}

	public void setUnitProfit(String unitProfit) {
		this.unitProfit = unitProfit;
	}

}
