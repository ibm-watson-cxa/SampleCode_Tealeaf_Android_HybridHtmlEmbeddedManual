package tl.hybridhtmlembedded;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.eo.EOCore;
import com.tl.uic.Tealeaf;
import com.tl.uic.TealeafEOLifecycleObject;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        final EditText postMessageUrl = findViewById(R.id.postMessageUrl);
        postMessageUrl.setText(EOCore.getConfigItemString(Tealeaf.TLF_POST_MESSAGE_URL, TealeafEOLifecycleObject.getInstance()));

        final EditText appKey = findViewById(R.id.appKey);
        appKey.setText(EOCore.getConfigItemString(Tealeaf.TLF_APP_KEY, TealeafEOLifecycleObject.getInstance()));

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean configModified = false;
                if (!postMessageUrl.getText().equals((EOCore.getConfigItemString(Tealeaf.TLF_POST_MESSAGE_URL, TealeafEOLifecycleObject.getInstance())))) {
                    EOCore.updateConfig(Tealeaf.TLF_POST_MESSAGE_URL, postMessageUrl.getText().toString(), TealeafEOLifecycleObject.getInstance());
                    configModified = true;
                }
                if (!appKey.getText().equals((EOCore.getConfigItemString(Tealeaf.TLF_APP_KEY, TealeafEOLifecycleObject.getInstance())))) {
                    EOCore.updateConfig(Tealeaf.TLF_APP_KEY, appKey.getText().toString(), TealeafEOLifecycleObject.getInstance());
                    configModified = true;
                }

                if (configModified) {
                    Tealeaf.disable();
                    Tealeaf.enable();
                }

                Tealeaf.logFormCompletion(true);

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

            }
        });

    }
}
