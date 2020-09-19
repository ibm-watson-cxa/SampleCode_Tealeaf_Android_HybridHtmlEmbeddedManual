package tl.hybridhtmlembedded;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.tl.uic.Tealeaf;
import com.tl.uic.javascript.JavaScriptInterface;
import com.tl.uic.util.CustomAsyncTaskCompatUtil;
import com.tl.uic.util.DialogLogScreenTask;

public class WebDialog extends Dialog {

    private static final String TAG = WebDialog.class.getSimpleName();

    private WebView mWebnotiView;

    private String mUrl;

    private Context mContext;

    /**
     * 진행바
     */

    public WebDialog(@NonNull Context context) {
        super(context, R.style.FullscreenDialog);
        mContext = context;
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    @JavascriptInterface
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_web_notice);

        mWebnotiView = findViewById(R.id.web_view_dialog);
        Button dialogButton = (Button) findViewById(R.id.btnClose);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebDialog.this.dismiss();
            }
        });
        WebSettings webSettings = mWebnotiView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        JavaScriptInterface jsi = new JavaScriptInterface(mContext, mWebnotiView, Tealeaf.getPropertyName(mWebnotiView).getId());
        mWebnotiView.addJavascriptInterface(jsi, "tlBridge");

        mWebnotiView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Need to register callbacks for Native SDK to collect data from WebView
                view.loadUrl("javascript:TLT.registerBridgeCallbacks([ "
                        + "{enabled: true, cbType: 'screenCapture', cbFunction: function (){tlBridge.screenCapture();}},"
                        + "{enabled: true, cbType: 'messageRedirect', cbFunction: function (data){tlBridge.addMessage(data);}}]);");

                DialogLogScreenTask dialogLogScreenTask = new DialogLogScreenTask((Activity) mContext, "WebNoticeDialog_onPageFinished_DialogLogScreenTask", WebDialog.this, Tealeaf.getCurrentSessionId());
                CustomAsyncTaskCompatUtil.executeParallel(dialogLogScreenTask);
            }
        });
        mWebnotiView.setWebChromeClient(new WebChromeClient());

        String path = mUrl;
        if (path != null) {
            loadUrl(path);
        }
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    private void loadUrl(String url) {
        if (mWebnotiView == null) {
            return;
        }

        mWebnotiView.post(() -> {
            if (mWebnotiView != null) {
                mWebnotiView.loadUrl(url);
            }
        });
    }
}
