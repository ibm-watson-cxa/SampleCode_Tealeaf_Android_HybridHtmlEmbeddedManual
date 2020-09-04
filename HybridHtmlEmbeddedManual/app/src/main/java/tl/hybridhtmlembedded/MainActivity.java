/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (C) Copyright IBM Corp. 2018
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 ******************************************************************************/
package tl.hybridhtmlembedded;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ZoomButtonsController;

import com.ibm.eo.util.LogInternal;
import com.tl.uic.Tealeaf;

import java.lang.reflect.Method;

public class MainActivity extends Activity {
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize tealeaf with a reference to application
		@SuppressWarnings("unused")
        Tealeaf tealeaf = new Tealeaf(this.getApplication());
        Tealeaf.enable();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createWebView(R.id.myWebView1, "file:///android_asset/mobile_domcap/embeddedGesturesMenu.html");
        createWebView(R.id.myWebView2, "file:///android_asset/mobile_domcap/embeddedGesturesMenu.html");

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(getOnClickListener());
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
        checkBox.setOnClickListener(getOnClickListener());

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });


        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
	}
	
	@SuppressLint("SetJavaScriptEnabled")
    private void createWebView(final int id, final String url) {
	    final MyWebView myWebView = (MyWebView) findViewById(id);
        myWebView.clearCache(true);
        
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        
        // Remove the zuto zoom
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
          // Use the API 11+ calls to disable the controls
          // Use a separate class to obtain 1.6 compatibility
          new Runnable() {
            public void run() {
                myWebView.getSettings().setDisplayZoomControls(false);
            }
          }.run();
        } else {
            try {
                Method zoomMethod = myWebView.getClass().getMethod("getZoomButtonsController");
                if (zoomMethod != null) {
                    Object[] value = null;
                    final ZoomButtonsController zoom_controll = (ZoomButtonsController) zoomMethod.invoke(myWebView, value);
                    zoom_controll.getContainer().setVisibility(View.GONE);
                }
            } catch (Exception e) {
                LogInternal.logException("HybridHTMLEmbedded", e);
            }
        }
        
        myWebView.loadUrl(url);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
	}
	
	public static OnClickListener getOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				Tealeaf.logEvent(view);
			}
		};
		return onClickListener;
	}

    /* Add touch event to collect gestures for Tealeaf.
     * 
     * (non-Javadoc)
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    public boolean dispatchTouchEvent(MotionEvent e)
    {
        //detector.onTouchEvent(e);
        Tealeaf.dispatchTouchEvent(this, e);
        return super.dispatchTouchEvent(e);
    }
}
