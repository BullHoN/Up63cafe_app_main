package com.avit.up63cafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avit.up63cafe.auth.LoginPostData;
import com.avit.up63cafe.auth.LoginResponseData;
import com.avit.up63cafe.db.SharedPrefNames;
import com.avit.up63cafe.forgetPassword.ForgetPasswordPostData;
import com.avit.up63cafe.forgetPassword.ForgetResponseData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginBottomSheetDialog extends BottomSheetDialogFragment {

    private SharedPreferences sharedPreferences;
    private EditText emailView,passwordView;
    private ProgressBar progressBar;
    private Button signInButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.bottom_sheet_login,container,false);


        root.findViewById(R.id.signup_model).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpBottomSheetDialog signUpBottomSheetDialog =
                        new SignUpBottomSheetDialog();
                signUpBottomSheetDialog.show(getFragmentManager(),"signUp_model");
                dismiss();
            }
        });



        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;

        sharedPreferences = getActivity().getSharedPreferences(dbName, Context.MODE_PRIVATE);

        progressBar = root.findViewById(R.id.progress);
        signInButton = root.findViewById(R.id.login_button);

        emailView = root.findViewById(R.id.email);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        passwordView = root.findViewById(R.id.password);

        root.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailView.getText().toString().trim();
                final String password = passwordView.getText().toString().trim();

                if (!email.matches(emailPattern)){
                    emailView.requestFocus();
                    Toast.makeText(getContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
                }else if(password.length() < 6){
                    passwordView.requestFocus();
                    Toast.makeText(getContext(),"Password Should be at least 6 characters",Toast.LENGTH_SHORT).show();
                }else {
                    // send to database
                    progressBar.setVisibility(View.VISIBLE);
                    signInButton.setClickable(false);

                    Retrofit retrofit = RetroFitClient.getInstance();

                    NetworkApi networkApi = retrofit.create(NetworkApi.class);

                    final LoginPostData data = new LoginPostData(email,password);

                    Call<LoginResponseData> call = networkApi.loginUser(data);

                    call.enqueue(new Callback<LoginResponseData>() {
                        @Override
                        public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {
                            LoginResponseData loginResponseData =
                                    response.body();

                            progressBar.setVisibility(View.INVISIBLE);
                            fetchResponseFromServer(loginResponseData);

                        }

                        @Override
                        public void onFailure(Call<LoginResponseData> call, Throwable t) {
                            Toast.makeText(getContext(),"Try Again Later",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }
            }
        });

        final Button button = root.findViewById(R.id.login_button);
        final TextView forgetView = root.findViewById(R.id.forget_password);
        final TextView loginData = root.findViewById(R.id.login_data);

        // signup page
        forgetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginData.setText("Forget Password");
                forgetView.setVisibility(View.INVISIBLE);
                passwordView.setVisibility(View.GONE);
                button.setText("Change Password");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = emailView.getText().toString().trim();
                        if(email.length() == 0){
                            emailView.requestFocus();
                            Toast.makeText(getContext(),"Enter Email",Toast.LENGTH_SHORT)
                                    .show();
                        }else if (!email.matches(emailPattern)){
                            emailView.requestFocus();
                            Toast.makeText(getContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
                        }else {
                            Retrofit retrofit = RetroFitClient.getInstance();
                            NetworkApi networkApi = retrofit.create(NetworkApi.class);
                            ForgetPasswordPostData data = new ForgetPasswordPostData(email);
                            Call<ForgetResponseData> call = networkApi.sendForgetPasswordRequest(data);
                            call.enqueue(new Callback<ForgetResponseData>() {
                                @Override
                                public void onResponse(Call<ForgetResponseData> call, Response<ForgetResponseData> response) {
                                    ForgetResponseData responseData = response.body();
                                    if(responseData.isStatus()){
                                        Toast.makeText(getContext(),"Instructions Are send to Email To Change The Password",Toast.LENGTH_LONG)
                                                .show();
                                    }else {
                                        Toast.makeText(getContext(),"Email is Not Registered",Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    dismiss();
                                }

                                @Override
                                public void onFailure(Call<ForgetResponseData> call, Throwable t) {
                                    Toast.makeText(getContext(),"Some Error Occured Try Again Later", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });

                        }
                    }
                });
            }
        });

        return root;
    }

    private void fetchResponseFromServer(LoginResponseData data){
        if (data.isValidEmail()){
            if (data.isPasswordCorrect()){
                if (data.isAccountVerified()){

                    saveDataToPrefrences(data.getUserName());

                    Intent intent = new Intent(getContext(),home.class);
                    startActivity(intent);

                    dismiss();
                    getActivity().finish();

                }else {
                    signInButton.setClickable(true);
                    Toast.makeText(getContext(),"Account not verified Please Register Again",Toast.LENGTH_SHORT)
                            .show();
                }
            }else{
                signInButton.setClickable(true);
                Toast.makeText(getContext(),"Wrong Password",Toast.LENGTH_SHORT)
                        .show();
            }
        }else {
            signInButton.setClickable(true);
            Toast.makeText(getContext(),"Email is not registered",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void saveDataToPrefrences(String name){

        String userName = SharedPrefNames.USER_NAME;
        String userEmail = SharedPrefNames.EMAIL;
        String userAuth = SharedPrefNames.ALLOW_ACCESS;
        String email = emailView.getText().toString().trim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userName,name);
        editor.putString(userEmail,email);
        editor.putBoolean(userAuth,true);

        editor.apply();

    }

}
