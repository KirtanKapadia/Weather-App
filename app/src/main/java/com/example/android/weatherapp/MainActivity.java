package com.example.android.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView info;

    public void giveWeatherInfo(View view){

        String city = cityName.getText().toString();
        if(city.equals(null)){
            Toast.makeText(this,"Please Enter a CIty", Toast.LENGTH_SHORT).show();
        }
        else {

            DownloadTask task = new DownloadTask();
            // 4bfdb6da93c99325bb85f1b105b17766
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=4bfdb6da93c99325bb85f1b105b17766";
            String result = String.valueOf(task.execute(url));
        }
    }





    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder();
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char current = (char)data;
                    stringBuilder.append(current);
                    data = reader.read();
                }
                return stringBuilder.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherinfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherinfo);
                String main;
                String description;
                String message = "";

                for(int i = 0; i<arr.length(); i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message = main + ":" + description + "\r\n";
                    }
                }
                if(!message.equals("")){
                    info.setText(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        info = findViewById(R.id.infoTextView);
    }
}
