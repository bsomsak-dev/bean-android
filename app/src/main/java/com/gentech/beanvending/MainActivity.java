package com.gentech.beanvending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        String previousUsername = loadUsername(MainActivity.this, usernameKey);

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
                    saveUsername(MainActivity.this, usernameKey, usernameValue);
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public static void saveUsername(Context context, String usernameKey, String usernameValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(usernameKey, usernameValue);
        editor.apply();
    }

    public static String loadUsername(Context context, String usernameKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String previousUsername = sharedPreferences.getString(usernameKey, "");
        return previousUsername;
    }
}
