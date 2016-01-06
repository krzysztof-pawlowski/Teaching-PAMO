package com.example.android.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity
        implements WebAddressesFragment.OnWebAddressSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            WebAddressesFragment firstFragment = new WebAddressesFragment();
            firstFragment.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onArticleSelected(int position) {
        MyWebViewFragment webViewFragment = (MyWebViewFragment)
                getFragmentManager().findFragmentById(R.id.webviewfragment);

        if (webViewFragment != null) {
            try {
                WebView webView = webViewFragment.getWebView();
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("http://onet.pl");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyWebViewFragment newFragment = new MyWebViewFragment();
            Bundle args = new Bundle();
            args.putInt(MyWebViewFragment.ARG_POSITION, position);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}