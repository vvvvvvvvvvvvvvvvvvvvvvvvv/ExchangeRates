package com.l.usd;

import androidx.appcompat.app.AppCompatActivity;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;



import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private TextView usd;
    private TextView eur;
    private MyAsyncTask task;
    private RequestQueue requestQueue;
    private AdView mAdView;
    private static final String TAG = "MainActivity";


    String[] name;
    String[] currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*MobileAds.initialize(this, getString(R.string.app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice("E3D38324C5EDC3F02497B4D6A70C56DC").
                build();
        mAdView.loadAd(adRequest);*/



        MobileAds.initialize(this, getString(R.string.app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Toast.makeText(MainActivity.this, "Great Connected", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(MainActivity.this, "No internet connections", Toast.LENGTH_SHORT).show();
        }


        usd = (TextView) findViewById(R.id.USD);
        eur = (TextView) findViewById(R.id.USD2);


        requestQueue = Volley.newRequestQueue(this);
        name = new String[]{"USD", "EUR"};
        currency = new String[]{"$", "€"};
        getMovies();
        task = new MyAsyncTask();

        countDownTimer = new CountDownTimer(1000000, 10000) {

            public void onTick(long millisUntilFinished) {
                getMovies();
            }

            public void onFinish() {

            }
        }.start();

    }


    private void getMovies() {

        String url = "https://www.cbr-xml-daily.ru/daily_json.js";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("Valute");

                    JSONObject Object1 = jsonObject.getJSONObject(name[0]);
                    JSONObject Object2 = jsonObject.getJSONObject(name[1]);
                    String usd1 = Object1.getString("Value");
                    String eur2 = Object2.getString("Value");
                    Log.d("test", " " + usd1);
                    usd.setText(currency[0] + usd1);
                    eur.setText(currency[1] + eur2);

                    // String res = Object.getString("Value");
                    //Log.d("hell", "pro" + res);
                    /*for (int i = 0; i < 2; i++) {
                        //Log.d("test", " " + name[i]);
                        JSONObject Object = jsonObject.getJSONObject(name[i]);

                        String res = Object.getString("Value");
                        //usd.setText(res).;
                        usd.append(currency[i] + String.valueOf(res)+"\n");

                        // JSONObject jsonObject = jsonArray.getJSONObject(name[i]);

                       /* String title = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String posterUrl = jsonObject.getString("Poster");
                        Log.d("Test", "h " + title + year );*/

                    // Log.d("Test", "h " + name[i] );
                    //}


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    class MyAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            getMovies();
            return null;
        }
    }

}