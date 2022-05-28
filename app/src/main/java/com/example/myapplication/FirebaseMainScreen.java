package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.StudentInformation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseMainScreen extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etStudentId, etStudentName, etStudentSurName, etStudentFathersName,
            etStudentNationalID, etStudentDOB, etStudentGender;
    private MaterialButton btnSaveInFirebase, btnShowFirebaseData;

    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;

    StudentInformation information;
    boolean isEdit = false;
    String studentID;
    Integer position;

    ArrayList<StudentInformation> studentInformations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_main_screen);

        etStudentId = findViewById(R.id.etStudentId);
        etStudentName = findViewById(R.id.etStudentName);
        etStudentSurName = findViewById(R.id.etStudentSurName);
        etStudentFathersName = findViewById(R.id.etStudentFathersName);
        etStudentNationalID = findViewById(R.id.etStudentNationalID);
        etStudentDOB = findViewById(R.id.etStudentDOB);
        etStudentGender = findViewById(R.id.etStudentGender);

        btnShowFirebaseData = findViewById(R.id.btnShowFirebaseData);
        btnSaveInFirebase = findViewById(R.id.btnSaveInFirebase);

        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference();

        Intent intentForEdit = getIntent();
        isEdit = intentForEdit.getBooleanExtra("isEdit", false);
        studentID = intentForEdit.getStringExtra("studentID");
        position = intentForEdit.getIntExtra("position", 0);

        if (isEdit) {
            btnSaveInFirebase.setText("Update Record");
            etStudentId.setText(intentForEdit.getStringExtra("id"));
            etStudentName.setText(intentForEdit.getStringExtra("name"));
            etStudentSurName.setText(intentForEdit.getStringExtra("surName"));
            etStudentFathersName.setText(intentForEdit.getStringExtra("fathername"));
            etStudentNationalID.setText(String.valueOf(intentForEdit.getStringExtra("nationalid")));
            etStudentDOB.setText(intentForEdit.getStringExtra("dob"));
            etStudentGender.setText(intentForEdit.getStringExtra("gender"));
        } else {
            btnSaveInFirebase.setText("Save Record");
        }

        btnSaveInFirebase.setOnClickListener(this);
        btnShowFirebaseData.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShowFirebaseData:

                Toast.makeText(FirebaseMainScreen.this, "Moving To List Screen of Firebase Data ",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(FirebaseMainScreen.this, FirebaseDataDisplayScreen.class));
                break;
            case R.id.btnSaveInFirebase:

                if (validate()) {

                    if (isEdit) {
                        String id = etStudentId.getText().toString();
                        String name = etStudentName.getText().toString();
                        String surName = etStudentSurName.getText().toString();
                        String fatherName = etStudentFathersName.getText().toString();
                        String nationalId = etStudentNationalID.getText().toString();
                        String dob = etStudentDOB.getText().toString();
                        String gender = etStudentGender.getText().toString();

                        StudentInformation updatedInformation = new StudentInformation(id, name, surName,
                                fatherName, nationalId, dob, gender);
                        updateFirebaseData(studentID, studentInformations, position, updatedInformation);

                    } else {
                        String id = etStudentId.getText().toString();
                        String name = etStudentName.getText().toString();
                        String surName = etStudentSurName.getText().toString();
                        String fatherName = etStudentFathersName.getText().toString();
                        String nationalId = etStudentNationalID.getText().toString();
                        String dob = etStudentDOB.getText().toString();
                        String gender = etStudentGender.getText().toString();

                        information = new StudentInformation(id, name, surName, fatherName, nationalId, dob, gender);

                        saveDataInFirebase(id, name, surName, fatherName, nationalId, dob, gender, information);
                    }
                }

                break;
        }
    }

    private void updateFirebaseData(String studentID, ArrayList<StudentInformation> studentInformations, Integer position,
                                    StudentInformation updatedInformation) {
        databaseReference.child("Students").child(studentID).setValue(updatedInformation);
        Toast.makeText(this, studentID + " Data Updated Successfully", Toast.LENGTH_SHORT).show();
        clearFields();
        btnSaveInFirebase.setText("Save Record");
    }


    public void saveDataInFirebase(String id, String name, String surName, String fatherName,
                                   String nationalId, String dob, String gender,
                                   StudentInformation informationObj) {

        information.setId(id);
        information.setName(name);
        information.setSurName(surName);
        information.setFatherName(fatherName);
        information.setNationalId(nationalId);
        information.setDob(dob);
        information.setGender(gender);

        databaseReference.child("Students").child(id).setValue(informationObj);

        clearFields();

        Toast.makeText(FirebaseMainScreen.this,
                "Save Inserted Successfully", Toast.LENGTH_SHORT).show();

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

    private boolean validate() {
        boolean goHead = true;

        if (TextUtils.isEmpty(etStudentId.getText().toString())) {
            etStudentId.setError("Please Enter ID");
            goHead = false;
        }
        if (TextUtils.isEmpty(etStudentName.getText().toString())) {
            etStudentName.setError("Please Enter Name");
            goHead = false;
        }
        if (TextUtils.isEmpty(etStudentSurName.getText().toString())) {
            etStudentSurName.setError("Please Enter SurName");
            goHead = false;
        }
        if (TextUtils.isEmpty(etStudentFathersName.getText().toString())) {
            etStudentFathersName.setError("Please Enter Father's Name");
            goHead = false;
        }
        if (TextUtils.isEmpty(etStudentNationalID.getText().toString())) {
            etStudentNationalID.setError("Please Enter National ID");
            goHead = false;
        }
        if (TextUtils.isEmpty(etStudentDOB.getText().toString())) {
            etStudentDOB.setError("Please Enter Date of Birth");
            goHead = false;
        }
        if (TextUtils.isEmpty(etStudentGender.getText().toString())) {
            etStudentGender.setError("Please Enter Gender");
            goHead = false;
        }
        return goHead;
    }

}