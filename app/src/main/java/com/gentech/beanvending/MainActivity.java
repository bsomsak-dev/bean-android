package com.gentech.beanvending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String usernameKey = "username";
    private static String usernameValue = "";

    private Button btLogin;
    private EditText edUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogin = (Button) findViewById(R.id.btLogin);
        edUsername = (EditText) findViewById(R.id.edUsername);
        String previousUsername = loadPreferences(usernameKey);

        if(!previousUsername.isEmpty()){
            edUsername.setText(previousUsername);
        }

        btLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Username!!", Toast.LENGTH_SHORT).show();
                }else{
                    usernameValue = username;
                    savePreferences(usernameKey, usernameValue);
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String loadPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(key, "") ;
    }
}
