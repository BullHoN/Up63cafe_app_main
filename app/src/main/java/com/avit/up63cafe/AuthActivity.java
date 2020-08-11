package com.avit.up63cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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
