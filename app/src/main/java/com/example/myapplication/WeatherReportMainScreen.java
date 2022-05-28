package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherReportMainScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivWeatherImage;
    private TextInputEditText etCityName;
    private MaterialButton btnShowReport;
    private TextView tvWeatherReport;
    LottieAnimationView animation_loading, animation_status;

    private final String appid = "9132ca75460af89754292b585004e06b";
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_report_main_screen);

        etCityName = findViewById(R.id.etCityName);
        btnShowReport = findViewById(R.id.btnShowReport);
        tvWeatherReport = findViewById(R.id.tvWeatherReport);
        animation_loading = findViewById(R.id.animation_loading);
        animation_status = findViewById(R.id.animation_status);

        btnShowReport.setEnabled(true);
        btnShowReport.setOnClickListener(this);

    }

    public void getWeatherDetails(View view) {
        String cityName = etCityName.getText().toString().toLowerCase().trim();
        String tempUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + appid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                animation_loading.setVisibility(View.GONE);
                etCityName.setText("");
                String result = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");

                    JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectCloud.getString("all");

                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");

                    tvWeatherReport.setTextColor(Color.rgb(68, 134, 199));

                    if (Integer.valueOf(clouds) <= 30){
                        animation_status.setAnimation(R.raw.cleardayanimation);
                    }
                    if (Integer.valueOf(clouds) > 30 && Integer.valueOf(clouds) <= 70){
                        animation_status.setAnimation(R.raw.littlecloudy);
                    }
                    if (Integer.valueOf(clouds) > 70 && Integer.valueOf(clouds) <= 100){
                        animation_status.setAnimation(R.raw.sureclouds);
                    }

                    result += "Current weather of " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";

                    tvWeatherReport.setText(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShowReport:
                animation_loading.setVisibility(View.VISIBLE);
                getWeatherDetails(view);
                break;
        }
    }
}