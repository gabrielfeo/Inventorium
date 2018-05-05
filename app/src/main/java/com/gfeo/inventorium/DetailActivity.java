package com.gfeo.inventorium;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportActionBar(findViewById(R.id.detail_toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
