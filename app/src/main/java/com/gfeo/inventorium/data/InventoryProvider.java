package com.gfeo.inventorium.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gfeo.inventorium.data.InventoryDbContract.ItemEntry;

/**
 * Created by gabrielfeo on 2018/04/29.
 */

public class InventoryProvider extends ContentProvider {

	private static final String LOG_TAG = InventoryProvider.class.getSimpleName();
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int MATCH_INVENTORY_LIST = 0;
	private static final int MATCH_INVENTORY_ITEM = 1;

	static {
		uriMatcher.addURI(InventoryDbContract.CONTENT_AUTHORITY,
		                  InventoryDbContract.PATH_INVENTORY, MATCH_INVENTORY_LIST);
		uriMatcher.addURI(InventoryDbContract.CONTENT_AUTHORITY,
		                  InventoryDbContract.PATH_INVENTORY + "/#", MATCH_INVENTORY_ITEM);
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String
			selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		SQLiteDatabase db = new InventoryDbHelper(getContext()).getReadableDatabase();
		switch (uriMatcher.match(uri)) {
			case MATCH_INVENTORY_LIST:
				Cursor cursor = db.query(ItemEntry.TABLE_NAME, projection, selection,
				                         selectionArgs,
				                         null, null, sortOrder);
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
				return cursor;
			case MATCH_INVENTORY_ITEM:
				selection = ItemEntry.COLUMN_NAME_ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				return db.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
				                null, null, sortOrder);
			default:
				throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
		}
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		switch (uriMatcher.match(uri)) {
			case MATCH_INVENTORY_LIST:
				return ItemEntry.CONTENT_LIST_TYPE;
			case MATCH_INVENTORY_ITEM:
				return ItemEntry.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("No matching content type for URI: " + uri);
		}
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		SQLiteDatabase db = new InventoryDbHelper(getContext()).getReadableDatabase();
		long newRowId;
		switch (uriMatcher.match(uri)) {
			case MATCH_INVENTORY_LIST:
				newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);
				break;
			default:
				throw new IllegalArgumentException("Couldn't insert values. Unknown URI: " + uri);
		}
		if (newRowId == -1) {
			Log.e(LOG_TAG, "Failed to insert row");
			return null;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return ContentUris.withAppendedId(uri, newRowId);

	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
			selectionArgs) {
		SQLiteDatabase db = new InventoryDbHelper(getContext()).getWritableDatabase();
		int rowsAffected;
		switch (uriMatcher.match(uri)) {
			case MATCH_INVENTORY_LIST:
				rowsAffected = db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case MATCH_INVENTORY_ITEM:
				selection = ItemEntry.COLUMN_NAME_ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				rowsAffected = db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Delete operation failed. Unknown URI: " + uri);
		}
		if (rowsAffected > 0) { getContext().getContentResolver().notifyChange(uri, null); }
		return rowsAffected;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
			selection, @Nullable String[] selectionArgs) {
		SQLiteDatabase db = new InventoryDbHelper(getContext()).getWritableDatabase();
		int rowsAffected;
		switch (uriMatcher.match(uri)) {
			case MATCH_INVENTORY_LIST:
				rowsAffected = db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			case MATCH_INVENTORY_ITEM:
				selection = ItemEntry.COLUMN_NAME_ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				rowsAffected = db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException(" " + uri);
		}
		if (rowsAffected > 0) { getContext().getContentResolver().notifyChange(uri, null); }
		return rowsAffected;
	}
}
