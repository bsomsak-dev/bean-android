package com.gentech.beanvending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {


    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String usernameKey = "username";
    private static String usernameValue = "";
    private String username;
    private Context context;


    private TextView tvUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        context = getApplicationContext();
        username = loadPreferences(usernameKey);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setText(username);

    }


    public String loadPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(key, "") ;
    }
}
