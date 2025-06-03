package com.example.realestate.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.realestate.activites.LoginActivity;
import com.example.realestate.activites.WelcomeActivity;
import com.example.realestate.models.JsonParser;

public class ConnectionAsyncTask extends AsyncTask<String, Void, String> {

    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        //((WelcomeActivity) activity).setButtonText("Connecting...");
        //((WelcomeActivity) activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        //((WelcomeActivity) activity).setProgress(false);


        Toast.makeText(activity, "Raw: " + result, Toast.LENGTH_LONG).show();
        //Toast.makeText(activity, "Parsed: " + JsonParser.parse(result,activity), Toast.LENGTH_SHORT).show();
        if (result != null && JsonParser.parse(result, activity)) {
            //((WelcomeActivity) activity).setButtonText("Connected ✔");
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {
            //((WelcomeActivity) activity).setButtonText("Connect");
            Toast.makeText(activity, "Failed to connect to server!", Toast.LENGTH_LONG).show();
        }
    }
}
