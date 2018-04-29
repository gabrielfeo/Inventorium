package com.gfeo.inventorium.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gfeo.inventorium.data.InventoryDbContract.ItemEntry;

/**
 * Created by gabrielfeo on 2018/04/28.
 */

class InventoryDbHelper extends SQLiteOpenHelper {

	private static final String COMMAND_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS ("
					+ ItemEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
					+ ItemEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, "
					+ ItemEntry.COLUMN_NAME_DESCRIPTION + " TEXT, "
					+ ItemEntry.COLUMN_NAME_COST + " REAL, "
					+ ItemEntry.COLUMN_NAME_STOCK + " INTEGER DEFAULT 0" + ");";
	private static final String COMMAND_DELETE_TABLE =
			"DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;


	InventoryDbHelper(Context context) {
		super(context, InventoryDbContract.DATABASE_NAME,
		      null, InventoryDbContract.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(COMMAND_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL(COMMAND_DELETE_TABLE);
		onCreate(sqLiteDatabase);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
