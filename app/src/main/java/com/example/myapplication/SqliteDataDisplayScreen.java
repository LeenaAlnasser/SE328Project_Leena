package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.SQLiteDataAdapter;
import com.example.myapplication.Model.DatabaseHelper;
import com.example.myapplication.Model.StudentInformation;

import java.util.ArrayList;

public class SqliteDataDisplayScreen extends AppCompatActivity implements SQLiteDataAdapter.OnViewClickListener {

    RecyclerView rvSqliteData;
    SQLiteDataAdapter adapter;
    ArrayList<StudentInformation> studentInformations = new ArrayList<>();

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_data_display_screen);

        rvSqliteData = findViewById(R.id.rvSqliteData);

        databaseHelper = new DatabaseHelper(this);

        adapter = new SQLiteDataAdapter(this,
                studentInformations, this);
        rvSqliteData.setAdapter(adapter);
        initializeData();
    }

    private void initializeData() {
        studentInformations = databaseHelper.getAllDataFromSQlite();
        adapter.notifyData(studentInformations);
    }

    @Override
    public void onViewClick(View view, int position) {
        switch (view.getId()) {
            case R.id.btnDeleteStudentInformation:
                String pkey = studentInformations.get(position).getId();
                databaseHelper.deleteDataFromSQlite(pkey);
                studentInformations.clear();
                studentInformations = databaseHelper.getAllDataFromSQlite();
                adapter.notifyData(studentInformations);
                break;
            case R.id.btnUpdateStudentInformation:
                studentInformations.clear();
                studentInformations = databaseHelper.getAllDataFromSQlite();
                Intent i = new Intent(this, SqliteMainScreen.class);
                i.putExtra("pkey", studentInformations.get(position).getId());
                i.putExtra("isEdit", true);
                i.putExtra("position", position);
                i.putExtra("name", studentInformations.get(position).getName());
                i.putExtra("surname", studentInformations.get(position).getSurName());
                i.putExtra("fathername", studentInformations.get(position).getFatherName());
                i.putExtra("nationalid", studentInformations.get(position).getNationalId());
                i.putExtra("dob", studentInformations.get(position).getDob());
                i.putExtra("gender", studentInformations.get(position).getGender());

                startActivity(i);
                break;
        }
    }
}