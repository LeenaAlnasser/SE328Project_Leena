package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnFirebaseScreen, btnSqlLiteScreen, btnCheckWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFirebaseScreen = findViewById(R.id.btnFirebaseScreen);
        btnSqlLiteScreen = findViewById(R.id.btnSqlLiteScreen);
        btnCheckWeather = findViewById(R.id.btnCheckWeather);

        btnFirebaseScreen.setOnClickListener(this);
        btnSqlLiteScreen.setOnClickListener(this);
        btnCheckWeather.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFirebaseScreen:
                Toast.makeText(MainActivity.this, "Moving To Firebase Screen ",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MainActivity.this, FirebaseMainScreen.class));

                break;
            case R.id.btnSqlLiteScreen:
                Toast.makeText(MainActivity.this, "Moving To SQLite Screen ",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SqliteMainScreen.class));
                break;
            case R.id.btnCheckWeather:
                Toast.makeText(MainActivity.this, "Moving To Weather Screen ",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, WeatherReportMainScreen.class));
                break;
        }
    }
}