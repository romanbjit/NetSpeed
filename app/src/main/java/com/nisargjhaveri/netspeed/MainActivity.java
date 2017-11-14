package com.nisargjhaveri.netspeed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public final class MainActivity extends AppCompatActivity {

    private SharedPreferences mSharedPref;

    private final OnSharedPreferenceChangeListener mSettingsListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Settings.KEY_INDICATOR_ENABLED)) {
                if (mSharedPref.getBoolean(Settings.KEY_INDICATOR_ENABLED, true)) {
                    startIndicatorService();
                } else {
                    stopIndicatorService();
                }
            } else if (key.equals(Settings.KEY_SHOW_SETTINGS_BUTTON)) {
                startIndicatorService();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (mSharedPref.getBoolean(Settings.KEY_INDICATOR_ENABLED, true)) {
            startIndicatorService();
        }
    }

    @Override
    protected void onResume() {
       super.onResume();
        mSharedPref.registerOnSharedPreferenceChangeListener(mSettingsListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPref.unregisterOnSharedPreferenceChangeListener(mSettingsListener);
    }

    private void startIndicatorService() {
        Intent serviceIntent = new Intent(this, NetSpeedIndicatorService.class);
        serviceIntent.putExtra(
                Settings.KEY_SHOW_SETTINGS_BUTTON,
                mSharedPref.getBoolean(Settings.KEY_SHOW_SETTINGS_BUTTON, false)
        );

        startService(serviceIntent);
    }

    private void stopIndicatorService() {
        stopService(new Intent(this, NetSpeedIndicatorService.class));
    }
}
