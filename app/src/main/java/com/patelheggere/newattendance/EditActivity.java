package com.patelheggere.newattendance;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private Spinner spinnerSection, spinnerStudent;
    private List<String> secList, studentName;
    private List<StudentModel> studentModels = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String selectedSec;
    private StudentModel studentSelected;
    int pos;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerStudent = findViewById(R.id.spinnerStudent);
        databaseReference = BaseApp.getFireBaseRef();
        secList = new ArrayList<>();
        studentName = new ArrayList<>();
        studentModels = new ArrayList<>();
        databaseReference.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                secList = new ArrayList<>();
                secList.add("Select Section");
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    secList.add(dataSnapshot1.getKey());
                }
                updateSec(secList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                databaseReference = BaseApp.getFireBaseRef();
                selectedSec = secList.get(position);
                if(!selectedSec.equalsIgnoreCase("Select Section")) {
                    databaseReference.child("student").child(secList.get(position)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            studentName = new ArrayList<>();
                            studentName.add("Select Student");
                            studentModels = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                StudentModel studentModel = new StudentModel();
                                studentModel = dataSnapshot1.getValue(StudentModel.class);
                                studentModels.add(studentModel);
                                studentName.add(studentModel.getName());
                            }
                            updateStudentList(studentName);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateStudentList(final List<String> studentName) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, studentName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudent.setAdapter(spinnerArrayAdapter);
        spinnerStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                try {
                        if (!studentName.get(position).equalsIgnoreCase("Select Student")) {
                            pos = position-1;
                            studentSelected = studentModels.get(pos);
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                            builder.setMessage("Do You want to delete?");
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                    alertDialog = null;
                                    finish();
                                }
                            });
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference = BaseApp.getFireBaseRef();
                                    databaseReference.child("student").child(selectedSec).child(studentSelected.getParentNumber()).removeValue();
                                    Toast.makeText(EditActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                            if(alertDialog==null) {
                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateSec(List<String> secList) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, secList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSection.setAdapter(spinnerArrayAdapter);

    }

}
