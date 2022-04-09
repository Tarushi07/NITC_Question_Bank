package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAddSubject extends AppCompatActivity
{
    EditText subject_name,subject_code;
    Button button_Subject_Admin;
    DatabaseReference dbref, database_faculty;
    ValueEventListener FacultyValueEventListener;

    Spinner spinner_faculty;
    ArrayList<String> faculty_list;
    ArrayAdapter<String> facultyListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_subject);

        spinner_faculty = (Spinner) findViewById(R.id.spinner_faculty);

        faculty_list = new ArrayList<String>();
        facultyListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,faculty_list);
        spinner_faculty.setAdapter(facultyListAdapter);

        subject_name = (EditText)findViewById(R.id.subject_name);
        subject_code = (EditText)findViewById(R.id.subject_code);
        button_Subject_Admin = (Button) findViewById(R.id.button_Subject_Admin);
        dbref = FirebaseDatabase.getInstance().getReference("Subject");
        database_faculty = FirebaseDatabase.getInstance().getReference("Faculty");
        fetchFacultyData();


        button_Subject_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
    }

    public void insertData()
    {
        String sub_name = subject_name.getText().toString().trim();
        String sub_code = subject_code.getText().toString().trim();
        String faculty_detail = spinner_faculty.getSelectedItem().toString();
        String faculty_email =  faculty_detail.substring(faculty_detail.indexOf("(")+1,faculty_detail.indexOf(")"));
        System.out.println(faculty_email);
        if(sub_name.isEmpty())
        {
            subject_name.setError("Subject Name is Required");
            subject_name.requestFocus();
            return;
        }

        if(sub_code.isEmpty())
        {
            subject_code.setError("Subject Code is Required");
            subject_code.requestFocus();
            return;
        }

        Subject subject = new Subject(sub_name,sub_code,faculty_email);
        dbref.child(sub_code).setValue(subject)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            subject_name.setText("");
                            subject_code.setText("");
                            Toast.makeText(getApplicationContext(), "Subject Added!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void fetchFacultyData()
    {
        FacultyValueEventListener = database_faculty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    String faculty_name = myData.child("userName").getValue().toString();
                    String faculty_email = myData.child("email").getValue().toString();
                    faculty_list.add(faculty_name+"("+faculty_email+")");
                }
                facultyListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}