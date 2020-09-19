/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (C) Copyright IBM Corp. 2018
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 ******************************************************************************/
package tl.hybridhtmlembedded;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tl.uic.Tealeaf;
import com.tl.uic.util.CustomAsyncTaskCompatUtil;
import com.tl.uic.util.DialogLogScreenTask;


public class MyWebViewClient extends WebViewClient {

    public MyWebViewClient() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        view.loadUrl(url);
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onPageFinished(final WebView view, final String url) {
        view.loadUrl("javascript:TLT.registerBridgeCallbacks([ "
            + "{enabled: true, cbType: 'screenCapture', cbFunction: function (){tlBridge.screenCapture();}},"
            + "{enabled: true, cbType: 'messageRedirect', cbFunction: function (data){tlBridge.addMessage(data);}}]);");

//        Good place to log layout, only need to log screen once per Activity
//        Tealeaf.logScreenLayout((Activity)view.getContext(), "Main", 1000);
    }
}
