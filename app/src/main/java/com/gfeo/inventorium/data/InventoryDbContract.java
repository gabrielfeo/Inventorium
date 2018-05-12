package com.gfeo.inventorium.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gabrielfeo on 2018/04/28.
 */

@SuppressWarnings("WeakerAccess")
public class InventoryDbContract {

	public static final String DATABASE_NAME = "inventory.db";
	public static final int DATABASE_VERSION = 6;
	public static final String CONTENT_AUTHORITY = "com.gfeo.inventorium";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_INVENTORY = "inventory";

	private InventoryDbContract() {}


	public static final class ItemTable implements BaseColumns {

		public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
		                                                           PATH_INVENTORY);
		public static final String CONTENT_TYPE_PART =
				"/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
		public static final String CONTENT_LIST_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_TYPE_PART;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_TYPE_PART;
		public static final String TABLE_NAME = "inventory";
		public static final String COLUMN_NAME_ID = BaseColumns._ID;
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_NAME_UNIT_COST_PRICE = "unitCostPrice";
		public static final String COLUMN_NAME_UNIT_SELL_PRICE = "unitSellingPrice";
		public static final String COLUMN_NAME_QUANTITY = "stock";
		public static final String COLUMN_NAME_SUPPLIER_PHONE = "supplierPhone";
		public static final String COLUMN_NAME_SUPPLIER_EMAIL = "supplierEmail";
		public static final String COLUMN_NAME_NOTES = "notes";
		public static final String COLUMN_NAME_PICTURE = "picture";

		static final String COMMAND_CREATE_TABLE =
				"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
						+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
						+ COLUMN_NAME_NAME + " TEXT NOT NULL, "
						+ COLUMN_NAME_DESCRIPTION + " TEXT, "
						+ COLUMN_NAME_UNIT_COST_PRICE + " REAL, "
						+ COLUMN_NAME_UNIT_SELL_PRICE + " REAL, "
						+ COLUMN_NAME_QUANTITY + " INTEGER DEFAULT 0, "
						+ COLUMN_NAME_SUPPLIER_PHONE + " TEXT, "
						+ COLUMN_NAME_SUPPLIER_EMAIL + " TEXT, "
						+ COLUMN_NAME_NOTES + " TEXT, "
						+ COLUMN_NAME_PICTURE + " BLOB" + ");";
		static final String COMMAND_DELETE_TABLE =
				"DROP TABLE IF EXISTS " + TABLE_NAME;

		public static final int COLUMN_INDEX_ID = 0;
		public static final int COLUMN_INDEX_NAME = 1;
		public static final int COLUMN_INDEX_DESCRIPTION = 2;
		public static final int COLUMN_INDEX_UNIT_COST_PRICE = 3;
		public static final int COLUMN_INDEX_UNIT_SELL_PRICE = 4;
		public static final int COLUMN_INDEX_QUANTITY = 5;
		public static final int COLUMN_INDEX_SUPPLIER_PHONE = 6;
		public static final int COLUMN_INDEX_SUPPLIER_EMAIL = 7;
		public static final int COLUMN_INDEX_NOTES = 8;
		public static final int COLUMN_INDEX_PICTURE = 9;
	}


}
