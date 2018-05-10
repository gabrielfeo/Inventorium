package com.gfeo.inventorium;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.gfeo.inventorium.data.InventoryDbContract.ItemTable;

class DeleteEntryDialogs {

	private DeleteEntryDialogs() { }

	static void showDeleteOneDialog(Context context,
	                                DialogInterface.OnClickListener dialogClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.common_dialog_delete_entry_message);
		builder.setNegativeButton(R.string.common_dialog_delete_entry_negative,
		                          dialogClickListener);
		builder.setPositiveButton(R.string.common_dialog_delete_entry_positive,
		                          dialogClickListener);
		builder.create().show();
	}

	static void showDeleteAllDialog(Context context) {
		DialogInterface.OnClickListener dialogClickListener = (dialog, button) -> {
			if (button == DialogInterface.BUTTON_POSITIVE) {
				context.getContentResolver().delete(ItemTable.CONTENT_URI, null, null);
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.common_dialog_delete_all_message);
		builder.setNegativeButton(R.string.common_dialog_delete_entry_negative,
		                          dialogClickListener);
		builder.setPositiveButton(R.string.common_dialog_delete_entry_positive, dialogClickListener);
		builder.create().show();
	}

}
