package com.gfeo.inventorium;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;

/**
 * Created by gabrielfeo on 2018/05/01.
 */

class InventoryCursorAdapter extends CursorAdapter {

	public InventoryCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, false);
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
		viewHolder.infoTextView = view.findViewById(R.id.item_textview_info);
		viewHolder.sellButton = view.findViewById(R.id.item_button_sell);
		view.setTag(viewHolder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		int currentQuantity = cursor.getInt(ItemTable.COLUMN_INDEX_QUANTITY);
		Uri itemUri = ContentUris.withAppendedId(ItemTable.CONTENT_URI,
		                                         cursor.getInt(ItemTable.COLUMN_INDEX_ID));
		FormattedItemDetails itemDetails = new FormattedItemDetails(context, itemUri);
		itemDetails.setUnitSellingPrice(cursor.getInt(ItemTable.COLUMN_INDEX_UNIT_SELL_PRICE));
		String info = context.getString(R.string.item_item_info,
		                                itemDetails.getPlainUnitSellingPrice(),
		                                currentQuantity);

		viewHolder.nameTextView.setText(cursor.getString(ItemTable.COLUMN_INDEX_NAME));
		viewHolder.infoTextView.setText(info);
		viewHolder.sellButton.setOnClickListener(buttonView -> sellItem(context, itemUri,
		                                                                currentQuantity));
	}

	private void sellItem(Context context, Uri itemUri, int currentQuantity) {
		if (currentQuantity <= 0) { return; }
		ContentValues newValues = new ContentValues();
		newValues.put(ItemTable.COLUMN_NAME_QUANTITY, currentQuantity - 1);
		context.getContentResolver().update(itemUri, newValues, null, null);
	}

	private class ViewHolder {
		TextView nameTextView;
		TextView infoTextView;
		Button sellButton;
	}
}
