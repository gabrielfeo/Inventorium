package com.gfeo.inventorium.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gabrielfeo on 2018/04/28.
 */

class InventoryDbHelper extends SQLiteOpenHelper {

	InventoryDbHelper(Context context) {
		super(context, InventoryDbContract.DATABASE_NAME,
		      null, InventoryDbContract.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(InventoryDbContract.ItemTable.COMMAND_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL(InventoryDbContract.ItemTable.COMMAND_DELETE_TABLE);
		onCreate(sqLiteDatabase);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
