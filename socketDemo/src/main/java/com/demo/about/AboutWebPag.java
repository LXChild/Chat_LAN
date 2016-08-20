package com.demo.about;

import com.example.socketdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutWebPag extends Activity {

	private WebView wv_about;
	private String URI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		setContentView(R.layout.layout_about_webpage);

		wv_about = (WebView) findViewById(R.id.wv_about);
	}

	private void initData() {
		URI = "http://www.baidu.com";
		wv_about.setWebViewClient(new WebViewClient() {
		});
		wv_about.loadUrl(URI);
	}
}
