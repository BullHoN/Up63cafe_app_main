package com.avit.up63cafe.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avit.up63cafe.R;
import com.avit.up63cafe.db.SharedPrefNames;

public class AddressActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        sharedPreferences = getSharedPreferences(dbName, Context.MODE_PRIVATE);

        final EditText phoneView = findViewById(R.id.phoneNo);
        final EditText addressView = findViewById(R.id.address);
        final EditText nearByView = findViewById(R.id.near_by);

        final String addressName = SharedPrefNames.ADDRESS;
        final String nearByName = SharedPrefNames.NEAR_ADDRESS;
        final String phoneNoName = SharedPrefNames.PH_NUMBER;

        if(sharedPreferences.contains(addressName)){
            phoneView.setText(sharedPreferences.getString(phoneNoName,""));
            addressView.setText(sharedPreferences.getString(addressName,""));
            nearByView.setText(sharedPreferences.getString(nearByName,""));
        }

        findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phoneView.getText().toString().trim();
                String address = addressView.getText().toString().trim();
                String nearBy = nearByView.getText().toString().trim();

                if (phoneNo.length() != 10){
                    Toast.makeText(getApplicationContext(),"Invalid Phone number",Toast.LENGTH_SHORT)
                            .show();
                }else if(address.length() <5  || nearBy.length() <5){
                    Toast.makeText(getApplicationContext(),"Address or nearBy cannot be empty",Toast.LENGTH_SHORT)
                            .show();
                }else {

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(addressName,address);
                    editor.putString(nearByName,nearBy);
                    editor.putString(phoneNoName,phoneNo);

                    editor.apply();

                    Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT)
                            .show();

                    finish();
                }

            }
        });


    }
}
