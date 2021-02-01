package com.avit.up63cafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.avit.up63cafe.db.SharedPrefNames;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private int UPDATE_REQUEST_CODE = 132;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callInAppUpdate();

        final Intent authIntent = new Intent(this,AuthActivity.class);
        final Intent homeIntent = new Intent(getApplicationContext(),home.class);

        // Shared Prefrences Data
        String databaseName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        final String user_name = SharedPrefNames.USER_NAME;
        final String allowAuth = SharedPrefNames.ALLOW_ACCESS;

        final SharedPreferences sharedPreferences =
                getSharedPreferences(databaseName, Context.MODE_PRIVATE);

        new CountDownTimer(1500,750){

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

    @Override
    protected void onResume() {
        super.onResume();
        callInAppUpdate();
    }

    private void callInAppUpdate(){
        // Creates instance of the manager.
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE
                        ,MainActivity.this,UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null) return;

        if(requestCode == UPDATE_REQUEST_CODE){
            Toast.makeText(getApplicationContext(),"Download Started",Toast.LENGTH_SHORT)
                    .show();

        }

    }
}
