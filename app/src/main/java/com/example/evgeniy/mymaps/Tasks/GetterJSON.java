package com.example.evgeniy.mymaps.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.evgeniy.mymaps.Interfaces.OnCompleteCallBack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetterJSON extends AsyncTask<String, Void, String> {

    private OnCompleteCallBack callBack;
    private ProgressBar pgLoading;
    private Button btn;
    private String LOG_TAG = "GetterJSON";

    public GetterJSON(ProgressBar pgLoading, Button btn, OnCompleteCallBack callBack) {
        this.pgLoading = pgLoading;
        this.callBack = callBack;
        this.btn = btn;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (pgLoading != null)
            pgLoading.setVisibility(View.VISIBLE);
        if (btn != null)
            btn.setEnabled(false);
    }

    @Override
    protected String doInBackground(String... strings) {
        String Url = strings[0];
        StringBuilder JSon = new StringBuilder();
        byte[] data = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String s;
                while ((s = reader.readLine()) != null) {
                    JSon.append(s);
                }
            } else {
                Log.e(LOG_TAG, "responseCode = " + responseCode);
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка " + e.getMessage());
            return null;
        }
        return JSon.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pgLoading != null)
            pgLoading.setVisibility(View.GONE);     // To Hide ProgressBar
        if (btn != null)
            btn.setEnabled(true);
        callBack.onComplete(s);
    }
}
