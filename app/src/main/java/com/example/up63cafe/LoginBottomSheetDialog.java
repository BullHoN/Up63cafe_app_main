package com.example.up63cafe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.up63cafe.auth.LoginPostData;
import com.example.up63cafe.auth.LoginResponseData;
import com.example.up63cafe.db.SharedPrefNames;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginBottomSheetDialog extends BottomSheetDialogFragment {

    private SharedPreferences sharedPreferences;
    private EditText emailView,passwordView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.bottom_sheet_login,container,false);

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
                    Retrofit retrofit = RetroFitClient.getInstance();

                    NetworkApi networkApi = retrofit.create(NetworkApi.class);

                    final LoginPostData data = new LoginPostData(email,password);

                    Call<LoginResponseData> call = networkApi.loginUser(data);

                    call.enqueue(new Callback<LoginResponseData>() {
                        @Override
                        public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {
                            LoginResponseData loginResponseData =
                                    response.body();

                            fetchResponseFromServer(loginResponseData);

                            Log.i("Login",loginResponseData.getUserName()+"");
                        }

                        @Override
                        public void onFailure(Call<LoginResponseData> call, Throwable t) {
                            Log.i("Login","error while posting login",t);
                        }
                    });

                }
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
                    Toast.makeText(getContext(),"Account not verified",Toast.LENGTH_SHORT)
                            .show();
                }
            }else{
                Toast.makeText(getContext(),"Wrong Password",Toast.LENGTH_SHORT)
                        .show();
            }
        }else {
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
