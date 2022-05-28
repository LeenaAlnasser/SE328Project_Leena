package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.FireBaseDataAdapter;
import com.example.myapplication.Model.StudentInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDataDisplayScreen extends AppCompatActivity implements FireBaseDataAdapter.OnViewClickListener {

    RecyclerView rvFireBaseData;
    FireBaseDataAdapter adapter;
    ArrayList<StudentInformation> studentInformations = new ArrayList<>();
    EditText searchByID;
    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_data_display_screen);

        rvFireBaseData = findViewById(R.id.rvFireBaseData);

        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference().child("Students");

        adapter = new FireBaseDataAdapter(this,
                studentInformations, this);
        rvFireBaseData.setAdapter(adapter);

        searchByID = findViewById(R.id.searchMessage);
        initializeData();

        searchByID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchDataByID(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchDataByID(CharSequence charSequence) {
        ArrayList<StudentInformation> mStudents = new ArrayList<>();

        for (StudentInformation student : studentInformations) {
            if (student.getId().toLowerCase().contains(charSequence)) {
                mStudents.add(student);

            }
        }
        adapter = new FireBaseDataAdapter(this,mStudents,this);
        rvFireBaseData.setHasFixedSize(true);
        rvFireBaseData.setLayoutManager(new LinearLayoutManager(this));
        rvFireBaseData.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void initializeData() {

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
                adapter.notifyData(studentInformations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FirebaseDataDisplayScreen.this,
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onViewClick(View view, int position) {
        switch (view.getId()) {
            case R.id.btnDeleteStudentInformation:
                String id = studentInformations.get(position).getId();
                deleteSelectedRecordFromFirebase(id, position);
                break;
            case R.id.btnUpdateStudentInformation:
                String studentID = studentInformations.get(position).getId();
                updateStudentRecord(studentID, position);
                break;
        }
    }

    private void updateStudentRecord(String studentID, int position) {
        Intent i = new Intent(FirebaseDataDisplayScreen.this, FirebaseMainScreen.class);
        i.putExtra("isEdit", true);
        i.putExtra("studentID", studentID);
        i.putExtra("position", position);

        ///SendingValues
        i.putExtra("id", studentInformations.get(position).getId());
        i.putExtra("name", studentInformations.get(position).getName());
        i.putExtra("surName", studentInformations.get(position).getSurName());
        i.putExtra("fathername", studentInformations.get(position).getFatherName());
        i.putExtra("nationalid", studentInformations.get(position).getNationalId());
        i.putExtra("dob", studentInformations.get(position).getDob());
        i.putExtra("gender", studentInformations.get(position).getGender());

        startActivity(i);
    }

    public void deleteSelectedRecordFromFirebase(String id, int position) {
        studentInformations.remove(position);
        databaseReference.child(id).removeValue();
        adapter.notifyData(studentInformations);
        Toast.makeText(this, id + " Deleted Successfully", Toast.LENGTH_SHORT).show();
    }
}