package com.f.Flippo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview);

		WebView wv = (WebView) findViewById(R.id.webView);
		wv.loadUrl("file:///android_asset/help.html");
	}
}
