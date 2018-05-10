package com.gfeo.inventorium;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

class InventoryLoaderCallbacks {

	private InventoryLoaderCallbacks() {}

	static abstract class CursorLoaderCallbacks implements LoaderManager.LoaderCallbacks {

		public static final String URI_ARGS_KEY = "uri";
		private final Context context;

		CursorLoaderCallbacks(Context context) {
			super();
			this.context = context;
		}

		@NonNull
		@Override
		public Loader onCreateLoader(int id, @Nullable Bundle args) {
			Uri uri = Uri.parse(args.getString("uri"));
			return new CursorLoader(context, uri, null, null, null, null);
		}

		@Override
		public void onLoaderReset(@NonNull Loader loader) { }
	}

	static abstract class DetailsLoaderCallbacks implements LoaderManager.LoaderCallbacks {

		private final Context context;
		private final Cursor cursor;
		private final ItemDetails itemDetails;

		DetailsLoaderCallbacks(Context context, Cursor cursor, @NonNull ItemDetails itemDetails) {
			super();
			this.context = context;
			this.cursor = cursor;
			this.itemDetails = itemDetails;
		}

		@NonNull
		@Override
		public Loader onCreateLoader(int id, @Nullable Bundle args) {
			return new DetailsLoader(context, cursor, itemDetails);
		}

		@Override
		public void onLoaderReset(@NonNull Loader loader) { }
	}

}
