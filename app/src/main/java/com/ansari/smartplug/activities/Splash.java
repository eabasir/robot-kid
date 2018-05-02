package com.ansari.smartplug.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.ansari.smartplug.R;

/**
 * Created by Eabasir on 7/23/2016.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spalsh);
        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(Demo.createIntent(Splash.this));
                finish();
            }
        }, secondsDelayed * 1000 );
    }
}
