package com.patelheggere.newattendance;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    private static final String TAG = "AddStudentActivity";
    private DatabaseReference databaseReference;
    private List<String> sectionList;
    private Spinner spinner;
    private String selectedSection;
    private Button addStudent;
    private EditText editTextName, editTextClass, editTextNumber, editTextReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        initViews();
    }
    private void initViews()
    {
        editTextName = findViewById(R.id.studentName);
        editTextClass = findViewById(R.id.className);
        editTextNumber = findViewById(R.id.parentNumber);
        editTextReg = findViewById(R.id.registerNo);

        addStudent = findViewById(R.id.addStudent);
        spinner = findViewById(R.id.spinnerSection);
        databaseReference = BaseApp.getFireBaseRef();
        databaseReference = databaseReference.child("student");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sectionList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    sectionList.add(dataSnapshot1.getKey());
                }
                if(sectionList!=null)
                createSectionList(sectionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentModel studentModel = new StudentModel();
                if(editTextName.getText()!=null && editTextName.getText().toString().trim().length()!=0)
                {
                    studentModel.setName(editTextName.getText().toString().trim());
                }
                else
                {
                    editTextName.setError("Enter Name");
                    return;
                }

                if(editTextClass.getText()!=null && editTextClass.getText().toString().trim().length()!=0)
                {
                    studentModel.setSemester(editTextClass.getText().toString().trim());
                }
                else
                {
                    editTextClass.setError("Enter Class");
                    return;
                }

                if(editTextNumber.getText()!=null && editTextNumber.getText().toString().trim().length()!=0)
                {
                    studentModel.setParentNumber(editTextNumber.getText().toString().trim());
                }
                else
                {
                    editTextNumber.setError("Enter Number");
                    return;
                }
                if(editTextReg.getText()!=null && editTextReg.getText().toString().trim().length()!=0)
                {
                    studentModel.setRegNo(Long.parseLong(editTextReg.getText().toString().trim()));
                }
                else
                {
                    editTextReg.setError("Enter Reg No.");
                    return;
                }
                databaseReference = BaseApp.getFireBaseRef();
                databaseReference.child("student").child(selectedSection).child(studentModel.getParentNumber()).setValue(studentModel);
                Toast.makeText(AddStudentActivity.this, "added Student Details", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void createSectionList(final List<String> sectionList)
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectionList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSection = sectionList.get(position);
                //Toast.makeText(getApplicationContext(),sectionList.get(position) , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
