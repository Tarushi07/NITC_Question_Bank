package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Paper_Request_Form extends AppCompatActivity {

    Button submit_request_paper;
    Spinner request_spinner_subject;
    Spinner request_spinner_year;
    Spinner request_spinner_paperType;
    ArrayList<String> request_subject_list;
    ValueEventListener requestValueEventListener;
    DatabaseReference dbref;
    FirebaseDatabase request_dbref;
    String subject_code, subject_name, subject_faculty;

    ArrayAdapter<String> subjectRequestAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_request_form);

        submit_request_paper = (Button)findViewById(R.id.submit_request_paper);
        request_spinner_subject = (Spinner) findViewById(R.id.spinner_subject_list);
        request_subject_list = new ArrayList<String>();
        subjectRequestAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,request_subject_list);
        request_spinner_subject.setAdapter(subjectRequestAdapter);
        dbref = FirebaseDatabase.getInstance().getReference("Subject");
        request_dbref = FirebaseDatabase.getInstance();

        fetchData();

        request_spinner_year = (Spinner) findViewById(R.id.request_spinner_year);
        ArrayAdapter<CharSequence> adapter_course = ArrayAdapter.createFromResource(this,
                R.array.Year, android.R.layout.simple_spinner_item);
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        request_spinner_year.setAdapter(adapter_course);

        request_spinner_paperType = (Spinner) findViewById(R.id.spinner_paper_type);
        ArrayAdapter<CharSequence> adapter_paper_type = ArrayAdapter.createFromResource(this,
                R.array.Paper_Name, android.R.layout.simple_spinner_item);
        adapter_paper_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        request_spinner_paperType.setAdapter(adapter_paper_type);

        submit_request_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String request_subject_name = request_spinner_subject.getSelectedItem().toString();
                FirebaseDatabase iterator = FirebaseDatabase.getInstance();
                iterator.getReference("Subject").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot myData : snapshot.getChildren())
                        {
                            subject_name = myData.child("subjectName").getValue().toString();
                            if(subject_name.compareTo(request_subject_name) == 0)
                            {
                                subject_code = myData.child("subjectCode").getValue().toString();
                                subject_faculty = myData.child("facultyEmail").getValue().toString();
                                break;
                            }
                        }
                        String request_paper_year = request_spinner_year.getSelectedItem().toString();
                        String request_paper_name = request_spinner_paperType.getSelectedItem().toString().trim();
                        if(request_paper_name.compareTo("Mid Term 1")==0)
                        {
                            request_paper_name = "M1";
                        }
                        else if(request_paper_name.compareTo("Mid Term 2")==0)
                        {
                            request_paper_name = "M2";
                        }
                        else
                        {
                            request_paper_name = "ES";
                        }
                        String unique_Id = subject_code+request_paper_name+request_paper_year;
                        unique_Id = unique_Id.replaceAll(" ", "");

                        Request request = new Request(unique_Id,request_subject_name,request_paper_year,request_paper_name,0,subject_faculty);
                        request_dbref.getReference().child("RequestPapers").child(unique_Id).setValue(request);
                        Toast.makeText(getApplicationContext(), "Request Submitted!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public void fetchData()
    {
        requestValueEventListener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    request_subject_list.add(myData.child("subjectName").getValue().toString());
                }
                subjectRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}