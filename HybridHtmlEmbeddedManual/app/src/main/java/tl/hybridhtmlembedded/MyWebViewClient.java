/********************************************************************************************
# Copyright (C) 2018 Acoustic, L.P. All rights reserved.
# 
# NOTICE: This file contains material that is confidential and proprietary to
# Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
# industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
# Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
# prohibited.
********************************************************************************************/
package tl.hybridhtmlembedded;

import android.webkit.WebView;
import android.webkit.WebViewClient;


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
    }
}
