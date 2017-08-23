package com.f.Flippo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int DIALOG_DISCLAIMER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Pref.getIsDisclaimerAccepted(this)) {
			startFlippo();
		} else {
			showDialog(DIALOG_DISCLAIMER);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
	}

	private void startFlippo() {
		Intent intent = new Intent(MainActivity.this, Flippo.class);
		MainActivity.this.startActivity(intent);
	}

	private void acceptDisclaimer() {
		Pref.setIsDisclaimerAccepted(this, true);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);

		switch (id) {
			case DIALOG_DISCLAIMER:
				final View disclaimerView = factory.inflate(R.layout.onelinedialog, null);
				TextView disclaimerTextView = (TextView) disclaimerView.findViewById(R.id.one_line_dialog_id);
				disclaimerTextView.setText(getString(R.string.disclaimer_text));
				return new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.disclaimer_title)
						.setView(disclaimerView)
						.setPositiveButton(R.string.btn_accept,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										acceptDisclaimer();
										startFlippo();
									}
								}
						)
						.setNegativeButton(R.string.btn_reject,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										finish();
									}
								}
						)
						// Capture the BACK key as cancel.
						.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								finish();
							}
						})
						.create();
		}

		return null;
	}
}
