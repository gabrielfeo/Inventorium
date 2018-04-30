package com.gfeo.inventorium.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gabrielfeo on 2018/04/28.
 */

public class InventoryDbContract {

	public static final String DATABASE_NAME = "inventory.db";
	public static final int DATABASE_VERSION = 1;
	public static final String CONTENT_AUTHORITY = "com.gfeo.inventorium";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_INVENTORY = "inventory";

	private InventoryDbContract() {}

	public static final class ItemEntry implements BaseColumns {

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
		public static final String COLUMN_NAME_COST = "costPrice";
		public static final String COLUMN_NAME_STOCK = "stock";

	}


}
