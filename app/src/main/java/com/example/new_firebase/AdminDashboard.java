package com.example.new_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener{

    private Button add_subject,admin_handle_request,button_approve_papers;
    ImageView logout;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();
        add_subject = (Button)findViewById(R.id.button_add_subject);
        admin_handle_request = (Button)findViewById(R.id.button_handle_request);
        button_approve_papers = (Button)findViewById(R.id.button_approve_papers);
        logout = (ImageView) findViewById(R.id.logout);

        add_subject.setOnClickListener(this);
        admin_handle_request.setOnClickListener(this);
        button_approve_papers.setOnClickListener(this);
        logout.setOnClickListener(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();
            }
        });

    }

    private void signOutUser() {
        Intent i = new Intent(AdminDashboard.this,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.button_add_subject:
                startActivity(new Intent(AdminDashboard.this,AdminAddSubject.class));
                break;

            case R.id.button_handle_request:
                startActivity(new Intent(AdminDashboard.this,AdminRequest.class));
                 break;

            case R.id.button_approve_papers:
                startActivity(new Intent(AdminDashboard.this,AdminFacultyRequest.class));
                break;
//
//            case R.id.button_logout:
//                break;
        }
    }
}