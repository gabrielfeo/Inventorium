package com.gfeo.inventorium.data;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public abstract class InventoryLoaderCallbacks implements LoaderManager.LoaderCallbacks {

	public static final String URI_ARGS_KEY = "uri";
	private Context context;

	public InventoryLoaderCallbacks(Context context) {
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
