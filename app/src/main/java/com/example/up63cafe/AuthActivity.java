package com.example.up63cafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        findViewById(R.id.login_model).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBottomSheetDialog loginBottomSheetDialog =
                        new LoginBottomSheetDialog();
                loginBottomSheetDialog.show(getSupportFragmentManager(),"login_model");
            }
        });

        findViewById(R.id.signup_model).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpBottomSheetDialog signUpBottomSheetDialog =
                        new SignUpBottomSheetDialog();
                signUpBottomSheetDialog.show(getSupportFragmentManager(),"signup_model");
            }
        });

    }
}
