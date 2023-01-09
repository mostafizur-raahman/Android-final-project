package com.mostafiz.myfirstweatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    EditText edCity;
    TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String apiid = "ce231728888b2e72f0c3f5620895d78b";
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);

    }

    public void getWeatherdetails(View view) {
        String tempurl = "";
        String city = edCity.getText().toString().trim();
        if( city.equals("")){
            tvResult.setText("City name must required");
        }
        else{
            tempurl = url + "?q=" + city + "&appid=" + apiid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelslike = jsonObjectMain.getDouble("feels_like")-273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    output += "  Currenr weather of " + cityName + "(" + countryName + ")" + "\n  Temp "
                     + df.format(temp)  + "°C \n  Feels like " + df.format(feelslike) + "c \n   Humidity " +
                    humidity + "%\n   Description :" +description + "\n" +
                    "   wind speed : " + wind + "  m/s" + "\n " +
                    "  Cloudyness : " + clouds + " %" + "\n" +
                    "   pressure : " + pressure + " hpa";

                    tvResult.setText(output);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}