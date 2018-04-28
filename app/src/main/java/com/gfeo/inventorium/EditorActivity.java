package com.gfeo.inventorium;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EditorActivity extends AppCompatActivity {

	private static int quantityCount;
	private TextView quantityTextView;
	public static final int DECREASE_QUANTITY = 0;
	public static final int INCREASE_QUANTITY = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		setSupportActionBar(findViewById(R.id.editor_toolbar));
		setupQuantityCounter();
	}

	private void setupQuantityCounter() {
		quantityCount = 0;
		quantityTextView = findViewById(R.id.editor_textview_quantity);
		findViewById(R.id.editor_button_minus)
				.setOnClickListener(view -> updateQuantityCount(DECREASE_QUANTITY));
		findViewById(R.id.editor_button_plus)
				.setOnClickListener(view -> updateQuantityCount(INCREASE_QUANTITY));
	}

	private void updateQuantityCount(int operation) {
		if (operation == DECREASE_QUANTITY && quantityCount > 0) {
			quantityCount--;
		} else if (operation == INCREASE_QUANTITY) {
			quantityCount++;
		} else { return; }
		quantityTextView.setText(String.valueOf(quantityCount));
	}

}
