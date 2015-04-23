package com.samuiinteractive.ati;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ATIWebViewClient extends WebViewClient {

    @Override
    public void onScaleChanged(WebView webview, float oldScale, float newScale) {
        webview.loadUrl("javascript:document.getElementsByTagName('H_content')[0].style.width=window.innerWidth+'px';");
    }

}
