package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.DatabaseHelper;
import com.example.myapplication.Model.StudentInformation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SqliteMainScreen extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnShowSqliteData, btnSaveInSQLite, btnFetchDataFromFirebase;
    private TextInputEditText etStudentId, etStudentName, etStudentSurName, etStudentFathersName,
            etStudentNationalID, etStudentDOB, etStudentGender;

    ArrayList<StudentInformation> studentInformations = new ArrayList<>();

    DatabaseHelper databaseHelper;

    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;

    String name, surname, fathername, dob, gender;
    boolean isEdit = false;
    int position;
    String pkey;
    String nationalid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_main_screen);

        etStudentId = findViewById(R.id.etStudentId);
        etStudentName = findViewById(R.id.etStudentName);
        etStudentSurName = findViewById(R.id.etStudentSurName);
        etStudentFathersName = findViewById(R.id.etStudentFathersName);
        etStudentNationalID = findViewById(R.id.etStudentNationalID);
        etStudentDOB = findViewById(R.id.etStudentDOB);
        etStudentGender = findViewById(R.id.etStudentGender);

        databaseHelper = new DatabaseHelper(this);

        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference().child("Students");

        btnShowSqliteData = findViewById(R.id.btnShowSqliteData);
        btnSaveInSQLite = findViewById(R.id.btnSaveInSQLite);
        btnFetchDataFromFirebase = findViewById(R.id.btnFetchDataFromFirebase);

        Intent i = getIntent();
        pkey = i.getStringExtra("pkey");
        isEdit = i.getBooleanExtra("isEdit", false);
        position = i.getIntExtra("position", 0);
        name = i.getStringExtra("name");
        surname = i.getStringExtra("surname");
        fathername = i.getStringExtra("fathername");
        nationalid = i.getStringExtra("nationalid");
        dob = i.getStringExtra("dob");
        gender = i.getStringExtra("gender");

        if (isEdit) {
            btnSaveInSQLite.setText("Update Selected Record in SQLite");
            etStudentId.setEnabled(false);

            etStudentId.setText(pkey);
            etStudentName.setText(name);
            etStudentSurName.setText(surname);
            etStudentFathersName.setText(fathername);
            etStudentNationalID.setText(nationalid.toString());
            etStudentDOB.setText(dob);
            etStudentGender.setText(gender);

        } else {
            btnSaveInSQLite.setText("Save Data To SQLite");
            etStudentId.setEnabled(true);
        }

        btnSaveInSQLite.setOnClickListener(this);
        btnShowSqliteData.setOnClickListener(this);
        btnFetchDataFromFirebase.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveInSQLite:

                if (isEdit) {
                    name = etStudentName.getText().toString();
                    surname = etStudentSurName.getText().toString();
                    fathername = etStudentFathersName.getText().toString();
                    nationalid = etStudentNationalID.getText().toString();
                    dob = etStudentDOB.getText().toString();
                    gender = etStudentGender.getText().toString();

                    updateDataInSQLite(pkey, name, surname, fathername, nationalid, dob, gender, position);
                } else {
                    String id = etStudentId.getText().toString();
                    String name = etStudentName.getText().toString();
                    String surName = etStudentSurName.getText().toString();
                    String fatherName = etStudentFathersName.getText().toString();
                    String nationalId = etStudentNationalID.getText().toString();
                    String dob = etStudentDOB.getText().toString();
                    String gender = etStudentGender.getText().toString();

                    saveDataToSqlite(id, name, surName, fatherName, nationalId, dob, gender);
                }
                break;
            case R.id.btnShowSqliteData:

                startActivity(new Intent(SqliteMainScreen.this, SqliteDataDisplayScreen.class));
                break;
            case R.id.btnFetchDataFromFirebase:

                fetchFirebaseData();
                break;
        }
    }

    private void updateDataInSQLite(String pkey, String name, String surname,
                                    String fathername, String nationalid,
                                    String dob, String gender, int position) {
        databaseHelper.updateDataInSQlite(pkey, name, surname, fathername, nationalid, dob, gender);
        clearFields();
        etStudentId.setEnabled(true);
        btnSaveInSQLite.setText("Save Data To SQLite");
    }

    private void fetchFirebaseData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for (DataSnapshot obj : datasnapshot.getChildren()
                ) {
                    StudentInformation studentInformation = new StudentInformation(null, null, null, null,
                            null, null, null);
                    for (DataSnapshot objVal : obj.getChildren()) {
                        if (objVal.getKey().equals("id")) {
                            studentInformation.setId(objVal.getValue().toString());
                        } else if (objVal.getKey().equals("dob")) {
                            studentInformation.setDob(objVal.getValue().toString());
                        } else if (objVal.getKey().equals("fatherName")) {
                            studentInformation.setFatherName(objVal.getValue().toString());
                        } else if (objVal.getKey().equals("gender")) {
                            studentInformation.setGender(objVal.getValue().toString());
                        } else if (objVal.getKey().equals("name")) {
                            studentInformation.setName(objVal.getValue().toString());
                        } else if (objVal.getKey().equals("nationalId")) {
                            studentInformation.setNationalId(objVal.getValue().toString());
                        } else if (objVal.getKey().equals("surName")) {
                            studentInformation.setSurName(objVal.getValue().toString());
                        }
                    }
                    studentInformations.add(studentInformation);
                }
                for (StudentInformation obj : studentInformations
                ) {
                    saveDataToSqlite(obj.getId(),
                            obj.getName(),
                            obj.getSurName(),
                            obj.getFatherName(),
                            obj.getNationalId(),
                            obj.getDob(),
                            obj.getGender());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SqliteMainScreen.this,
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDataToSqlite(String id, String name, String surName,
                                  String fatherName, String nationalId, String dob, String gender) {
        boolean insertData = databaseHelper.addData(id, name, surName, fatherName, nationalId, dob, gender);
        clearFields();

        if (insertData) {
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void clearFields() {
        etStudentId.setText("");
        etStudentName.setText("");
        etStudentSurName.setText("");
        etStudentFathersName.setText("");
        etStudentNationalID.setText("");
        etStudentDOB.setText("");
        etStudentGender.setText("");
    }
}