package com.patelheggere.newattendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private UserModel userModel;
    private ArrayList<StudentModel> studentModelList;
    private DatabaseReference databaseReference;
    private ListView listView;
    private Button submitButton, sec1Button, sec2Button;
    private MyCustomAdapter dataAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getData();
    }

    private void initViews() {
        listView = findViewById(R.id.listView);
        submitButton = findViewById(R.id.submit);
        sec1Button = findViewById(R.id.sec1);
        sec2Button = findViewById(R.id.sec2);
        progressBar = findViewById(R.id.progress_circular);
        sec1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sec1Button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                sec2Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                getDataFromFB(userModel.getSec());
            }
        });

        sec2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sec2Button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                sec1Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                if(userModel.getSec_2()!=null)
                getDataFromFB(userModel.getSec_2());
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<StudentModel> studentArrayList1 = dataAdapter.studentArrayList;
                for (int i = 0; i < studentArrayList1.size(); i++)
                {
                    if (!studentArrayList1.get(i).isSelected()) {
                        String otp = "Your ward "+studentArrayList1.get(i).getName() +" bearing Reg NO."+studentArrayList1.get(i).getRegNo()+" Absent for today";
                        String url = "http://bulksms.srushti.info/api/sendhttp.php?authkey=98994ArUXXszM0HlY57fb9c32&mobiles="+studentArrayList1.get(i).getParentNumber()+"&message="+otp+"&sender=PRAJIV&route=4&country=India";
                        Log.d(TAG, "onClick: "+url);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: "+response);
                                        // Display the first 500 characters of the response string.
                                     //   textView.setText("Response is: "+ response.substring(0,500));
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "onErrorResponse: "+error.getLocalizedMessage());
                                //textView.setText("That didn't work!");
                            }
                        });


                        //creating a request queue
                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                        //adding the string request to request queue
                        requestQueue.add(stringRequest);
                    }
                }
                Toast.makeText(MainActivity.this, "Report sent", Toast.LENGTH_LONG).show();
            }
        });

        // Assign adapter to ListView

    }

    private void getData() {
        userModel = getIntent().getParcelableExtra("DATA");
        if (userModel != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(userModel.getUsername()+" Attendance");
            getDataFromFB(userModel.getSec());
            sec1Button.setText(userModel.getSection_1());
            sec2Button.setText(userModel.getSection_2());
        }

    }

    private void getDataFromFB(String sec) {
        progressBar.setVisibility(View.VISIBLE);
        studentModelList = new ArrayList<>();
        databaseReference = BaseApp.getFireBaseRef();
        databaseReference = databaseReference.child("student").child(sec);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentModel studentModel = new StudentModel();
                    studentModel.setRegNo((long) snapshot.child("regNo").getValue());
                    studentModel.setName((String) snapshot.child("name").getValue());
                    studentModel.setParentNumber((String) snapshot.child("parentNumber").getValue());
                    studentModel.setSemester((String) snapshot.child("semester").getValue());
                    studentModelList.add(studentModel);
                }
                progressBar.setVisibility(View.GONE);
                dataAdapter = new MyCustomAdapter(MainActivity.this, R.layout.activity_student, studentModelList);
                listView.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private class MyCustomAdapter extends ArrayAdapter<StudentModel> {

        private ArrayList<StudentModel> studentArrayList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<StudentModel> countryList) {
            super(context, textViewResourceId, countryList);
            this.studentArrayList = new ArrayList<StudentModel>();
            this.studentArrayList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.activity_student, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        StudentModel country = (StudentModel) cb.getTag();
                        country.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StudentModel student = studentArrayList.get(position);
            holder.code.setText(" (" + student.getRegNo() + ")");
            holder.name.setText(student.getName());
            holder.name.setChecked(student.isSelected());
            holder.name.setTag(student);

            return convertView;

        }

    }
}
