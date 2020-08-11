package com.avit.up63cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.up63cafe.auth.OptResponseData;
import com.avit.up63cafe.auth.OtpPostData;
import com.avit.up63cafe.auth.ResendOtpResponseData;
import com.avit.up63cafe.db.SharedPrefNames;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView resendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeHelpCall();
            }
        });

        resendView = findViewById(R.id.resend);

        resendOtp();

        String databaseName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;

        sharedPreferences = getSharedPreferences(databaseName, Context.MODE_PRIVATE);

        final String email = getIntent().getStringExtra("email");
        final String name = getIntent().getStringExtra("name");
        TextView textView = findViewById(R.id.otp_email);

        textView.setText("Otp has been send to email " + email);

        final EditText optView = findViewById(R.id.otp);

        findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opt = optView.getText().toString().trim();

                if (opt.length() < 1){
                    Toast.makeText(getApplicationContext(),"Please Enter Otp",Toast.LENGTH_SHORT)
                            .show();
                }else {
                    Retrofit retrofit = RetroFitClient.getInstance();

                    NetworkApi networkApi = retrofit.create(NetworkApi.class);

                    OtpPostData data = new OtpPostData(name,email,opt);

                    Call<OptResponseData> call = networkApi.validateOtp(data);

                    call.enqueue(new Callback<OptResponseData>() {
                        @Override
                        public void onResponse(Call<OptResponseData> call, Response<OptResponseData> response) {
                            OptResponseData responseData =
                                    response.body();

                            fetchResponseFromServer(responseData);
                            Log.i("Otp",responseData.isValidOtp()+"");
                        }

                        @Override
                        public void onFailure(Call<OptResponseData> call, Throwable t) {
                            Log.i("Otp","otp request fail",t);
                        }
                    });

                }
            }
        });
    }

    private void resendOtp(){

        final String email = getIntent().getStringExtra("email");

        resendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = RetroFitClient.getInstance();

                NetworkApi networkApi = retrofit.create(NetworkApi.class);

                Call<ResendOtpResponseData> call = networkApi.resendOtp(email);

                call.enqueue(new Callback<ResendOtpResponseData>() {
                    @Override
                    public void onResponse(Call<ResendOtpResponseData> call, Response<ResendOtpResponseData> response) {
                        ResendOtpResponseData data = response.body();

                        if(data.isOtpSend()){
                            Toast.makeText(getApplicationContext(),"Otp is send",Toast.LENGTH_SHORT)
                                    .show();
                        }else {
                            Toast.makeText(getApplicationContext(),"some error occured",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResendOtpResponseData> call, Throwable t) {

                        Log.i("ResendOtp","error on 2 otp",t);

                        Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
    }

    private void fetchResponseFromServer(OptResponseData data){
        if (data.isValidOtp()){
            Toast.makeText(getApplicationContext(),"Successfully registered",Toast.LENGTH_SHORT)
                    .show();

            saveDataToPrefrences();

            Intent intent = new Intent(getApplicationContext(),home.class);
            startActivity(intent);

            finish();
        }else {
            Toast.makeText(getApplicationContext(),"Not Valid Otp",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void saveDataToPrefrences(){

        String userName = SharedPrefNames.USER_NAME;
        String userEmail = SharedPrefNames.EMAIL;
        String userAuth = SharedPrefNames.ALLOW_ACCESS;
        String email = getIntent().getStringExtra("email");
        String name = getIntent().getStringExtra("name");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userName,name);
        editor.putString(userEmail,email);
        editor.putBoolean(userAuth,true);

        editor.apply();

    }

    private void makeHelpCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "9532455040"));
        startActivity(intent);
    }

}
