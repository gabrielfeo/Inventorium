package com.gfeo.inventorium;

import android.content.Context;
import android.database.Cursor;
import android.support.design.button.MaterialButton;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by gabrielfeo on 2018/05/01.
 */

class InventoryCursorAdapter extends CursorAdapter {

	private final int nameColumnIndex;
	private final int sellingPriceColumnIndex;
	private final int quantityColumnIndex;

	public InventoryCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, false);
		nameColumnIndex = cursor.getColumnIndex(ItemTable.COLUMN_NAME_NAME);
		//TODO Differentiate selling from cost price field
		sellingPriceColumnIndex = cursor.getColumnIndex(ItemTable.COLUMN_NAME_COST_PRICE);
		quantityColumnIndex = cursor.getColumnIndex(ItemTable.COLUMN_NAME_STOCK);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		//Inflate the layout
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_inventory, viewGroup,
		                             false);
		//Find views for the ViewHolder
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.nameTextView = view.findViewById(R.id.item_textview_name);
		viewHolder.sellingPriceTextView = view.findViewById(R.id.item_textview_selling_price);
		viewHolder.quantityTextView = view.findViewById(R.id.item_textview_quantity);
		view.setTag(viewHolder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();

		viewHolder.nameTextView.setText(cursor.getString(nameColumnIndex));
		String sellingPrice = "R$ " + cursor.getString(sellingPriceColumnIndex);
		viewHolder.sellingPriceTextView.setText(sellingPrice);
		String quantityText = "  -  " + cursor.getString(quantityColumnIndex) + " left";
		viewHolder.quantityTextView.setText(quantityText);
	}

	private class ViewHolder {
		TextView nameTextView;
		TextView sellingPriceTextView;
		TextView quantityTextView;
	}
}
