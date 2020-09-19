/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (C) Copyright IBM Corp. 2018
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 ******************************************************************************/
package tl.hybridhtmlembedded;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tl.uic.Tealeaf;
import com.tl.uic.javascript.JavaScriptInterface;
import com.tl.uic.model.PropertyName;

import java.util.Map;

/**
 * @author ohernandezltmac
 */
public class MyWebView extends WebView {
    private PropertyName webViewId;

    /**
     * @param context
     */
    public MyWebView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Get webview id.
     *
     * @return Webview id.
     */
    public final PropertyName getWebViewId() {
        return webViewId;
    }

    /**
     * Set webview id.
     *
     * @param webviewId Webview id.
     */
    public final void setWebViewId(final PropertyName webviewId) {
        this.webViewId = webviewId;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("NewApi")
    @Override
    public void loadUrl(final String url) {
        loadUrl(url, null);
    }

    /**
     * {@inheritDoc}
     */
    public final void loadUrl(final String url, final Map<String, String> extraHeaders) {
        init(url);

        super.loadUrl(url, extraHeaders);
    }

    /**
     * Initializes WebView.
     */
    private void init(final String url) {
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Tealeaf.setTLCookie(url);
        // Setup Tealeaf bridge
        this.setWebViewId(Tealeaf.getPropertyName(this));
        // This is used to setup javascript bridge to communicate from web to native to collect data
        this.addJavascriptInterface(new JavaScriptInterface(this.getContext(), this, this.getWebViewId().getId()), "tlBridge");

        this.setWebViewClient(new MyWebViewClient());
        this.setWebChromeClient(new MyWebChromeClient());
    }
}
