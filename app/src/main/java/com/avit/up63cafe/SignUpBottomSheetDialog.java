package com.avit.up63cafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avit.up63cafe.auth.RegisterPostData;
import com.avit.up63cafe.auth.RegisterResponseData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpBottomSheetDialog extends BottomSheetDialogFragment {

    private EditText emailView;
    private EditText nameView;
    private ProgressBar progressBar;
    private Button signUpButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.bottom_sheet_signup,container,false);

        root.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBottomSheetDialog loginBottomSheetDialog = new
                        LoginBottomSheetDialog();
                loginBottomSheetDialog.show(getFragmentManager(),"login_model");
                dismiss();
            }
        });

        root.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeHelpCall();
            }
        });

        nameView = root.findViewById(R.id.name);
        emailView = root.findViewById(R.id.email);
        progressBar = root.findViewById(R.id.progress);
        signUpButton = root.findViewById(R.id.signup_button);

        final CheckBox policyBox = root.findViewById(R.id.policy);
        final EditText passwordView = root.findViewById(R.id.password);
//        passwordView.requestFocus();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        root.findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString().trim();
                String email = emailView.getText().toString().trim();
                String password = passwordView.getText().toString().trim();

                if (!email.matches(emailPattern)){
                    Toast.makeText(getContext(),"Invalid Email",Toast.LENGTH_SHORT)
                            .show();
                }else if(name.length() < 5){
                    Toast.makeText(getContext(),"Please Enter a valid Email",Toast.LENGTH_SHORT)
                            .show();
                }else if(password.length() < 6){
                    Toast.makeText(getContext(),"Password should be at least 6 digit",Toast.LENGTH_SHORT)
                            .show();
                }else if(!policyBox.isChecked()){
                    Toast.makeText(getContext(),"Please Check The Box",Toast.LENGTH_SHORT)
                            .show();
                }else {
                    // send to database
                    progressBar.setVisibility(View.VISIBLE);
                    signUpButton.setClickable(false);

                    Retrofit retrofit = RetroFitClient.getInstance();

                    NetworkApi networkApi = retrofit.create(NetworkApi.class);

                    RegisterPostData data = new RegisterPostData(email,password,name);

                    Call<RegisterResponseData> call = networkApi.registerUser(data);

                    call.enqueue(new Callback<RegisterResponseData>() {
                        @Override
                        public void onResponse(Call<RegisterResponseData> call, Response<RegisterResponseData> response) {
                            RegisterResponseData registerResponseData =
                                    response.body();

                            progressBar.setVisibility(View.INVISIBLE);
                            fetchResponseFromServer(registerResponseData);
                            Log.i("Register",registerResponseData.isAlreadyUser()
                                + " " + registerResponseData.isAccountVerified());

                        }

                        @Override
                        public void onFailure(Call<RegisterResponseData> call, Throwable t) {
                            Toast.makeText(getContext(),"Some error occured",Toast.LENGTH_SHORT)
                                    .show();
                            Log.i("Register","error in register",t);
                        }
                    });

                }
            }
        });

        return root;
    }

    private void fetchResponseFromServer(RegisterResponseData data){

        Intent intent = new Intent(getContext(),OtpActivity.class);

        intent.putExtra("email",emailView.getText().toString().trim());
        intent.putExtra("name",nameView.getText().toString().trim());

        if (!data.isAlreadyUser()){
            Toast.makeText(getContext(),"new User",Toast.LENGTH_SHORT)
                    .show();
            startActivity(intent);
        }else {
            if (data.isAccountVerified()){
                Toast.makeText(getContext(),"This email is already registered",Toast.LENGTH_SHORT)
                        .show();
                signUpButton.setClickable(true);
            }else {
                Toast.makeText(getContext(),"Verify your account", Toast.LENGTH_SHORT)
                        .show();
                startActivity(intent);
            }
        }
    }

    private void makeHelpCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "9532455040"));
        startActivity(intent);
    }

}
