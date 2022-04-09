package com.example.new_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class CourseList extends AppCompatActivity implements View.OnClickListener{

    private Button view_paper,handle_request;
    ImageView student_logout;
    FirebaseAuth mAuth;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        mAuth = FirebaseAuth.getInstance();

        view_paper = (Button) findViewById(R.id.button_view_paper);
        handle_request = (Button) findViewById(R.id.button_request);
        handle_request.setOnClickListener(this);
        student_logout = (ImageView) findViewById(R.id.student_logout);

        view_paper.setOnClickListener(this);
        student_logout.setOnClickListener(this);
        student_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();
            }
        });
    }

    private void signOutUser() {
        Intent i = new Intent(CourseList.this,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.button_view_paper:
                startActivity(new Intent(CourseList.this,SubjectList.class));
                break;

            case R.id.button_request:
                startActivity(new Intent(CourseList.this,Paper_Request_Form.class));
                break;
        }
    }
}