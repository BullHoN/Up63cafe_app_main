package com.example.up63cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.up63cafe.db.SharedPrefNames;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent authIntent = new Intent(this,AuthActivity.class);
        final Intent homeIntent = new Intent(getApplicationContext(),home.class);

        String databaseName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        final String user_name = SharedPrefNames.USER_NAME;
        final String allowAuth = SharedPrefNames.ALLOW_ACCESS;

        final SharedPreferences sharedPreferences =
                getSharedPreferences(databaseName, Context.MODE_PRIVATE);

        new CountDownTimer(500,250){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (sharedPreferences.contains(user_name) && sharedPreferences.contains(allowAuth)){
                    if (sharedPreferences.getBoolean(allowAuth,false)){
                        startActivity(homeIntent);
                    }else {
                        startActivity(authIntent);
                    }
                }else {
                    startActivity(authIntent);
                }
                finish();

            }
        }.start();

    }
}
