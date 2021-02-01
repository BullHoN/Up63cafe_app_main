package com.avit.up63cafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avit.up63cafe.auth.RegisterPostData;
import com.avit.up63cafe.db.SharedPrefNames;
import com.avit.up63cafe.notification.NotificationResponseData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInButton signInButton;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private int RC_SIGN_IN = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

//        findViewById(R.id.login_model).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginBottomSheetDialog loginBottomSheetDialog =
//                        new LoginBottomSheetDialog();
//                loginBottomSheetDialog.show(getSupportFragmentManager(),"login_model");
//            }
//        });
//
//        findViewById(R.id.signup_model).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SignUpBottomSheetDialog signUpBottomSheetDialog =
//                        new SignUpBottomSheetDialog();
//                signUpBottomSheetDialog.show(getSupportFragmentManager(),"signup_model");
//            }
//        });

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;

        sharedPreferences = getSharedPreferences(dbName, Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        progressBar = findViewById(R.id.progress);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sign_in_button:
                        signIn();
                    break;
                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            // Register the user
            Retrofit retrofit = RetroFitClient.getInstance();
            NetworkApi networkApi = retrofit.create(NetworkApi.class);

            final String email = account.getEmail();
            final String name = account.getDisplayName();

            Log.i(TAG, "handleSignInResult: " + email + " " + name);

            RegisterPostData data = new RegisterPostData(email,"123",name);
            Call<NotificationResponseData> call = networkApi.googleSignUp(data);

            progressBar.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<NotificationResponseData>() {
                @Override
                public void onResponse(Call<NotificationResponseData> call, Response<NotificationResponseData> response) {
                    NotificationResponseData data = response.body();

                    if(data.isStatus()){
                        // Save the user's data in shared prefrences
                        saveDataToPrefrences(name,email);

                        Toast.makeText(getApplicationContext(),"Successfully LoggedIn",Toast.LENGTH_SHORT)
                                .show();

                        Intent intent = new Intent(getApplicationContext(),home.class);
                        startActivity(intent);

                        finish();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Some Error Occurred",Toast.LENGTH_SHORT)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<NotificationResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_SHORT)
                            .show();
                }
            });


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void saveDataToPrefrences(String name,String email){

        String userName = SharedPrefNames.USER_NAME;
        String userEmail = SharedPrefNames.EMAIL;
        String userAuth = SharedPrefNames.ALLOW_ACCESS;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userName,name);
        editor.putString(userEmail,email);
        editor.putBoolean(userAuth,true);

        editor.apply();

    }

}
