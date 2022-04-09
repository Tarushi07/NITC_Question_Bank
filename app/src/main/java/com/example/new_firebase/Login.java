package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button login,newUser;
    private EditText editEmail,editPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newUser = (Button)findViewById(R.id.newUser);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);

        mAuth = FirebaseAuth.getInstance();

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,RegisterUser.class));
            }
        });

        login = (Button)findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String logEmail = editEmail.getText().toString().trim();
                String logPass = editPassword.getText().toString().trim();

                if(logEmail.isEmpty())
                {
                    editEmail.setError("Please enter a valid Email");
                    editEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(logEmail).matches())
                {
                    editEmail.setError("Please enter a valid Email");
                    editEmail.requestFocus();
                    return;
                }
                if(logPass.isEmpty())
                {
                    editPassword.setError("Please Enter a Valid Password");
                    editPassword.requestFocus();
                    return;
                }
                if(logPass.length()<6)
                {
                    editPassword.setError("Min password length should be six");
                    editPassword.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(logEmail,logPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String uid = task.getResult().getUser().getUid();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("Users").child(uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int userType = snapshot.getValue(Integer.class);
                                    if(userType==0)
                                    {
                                        startActivity(new Intent(Login.this,CourseList.class));
                                    }
                                    if(userType==1)
                                    {
                                        FirebaseDatabase dbref_fac;
                                        dbref_fac = FirebaseDatabase.getInstance();
                                        firebaseDatabase.getReference().child("Users").child(uid).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String fac_Name = snapshot.getValue(String.class);
                                                User faculty = new User(uid,fac_Name,"faculty",logEmail,1);
                                                dbref_fac.getReference().child("Faculty").child(uid).setValue(faculty);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        startActivity(new Intent(Login.this,FacultyDashboard.class));
                                    }
                                    if(userType==2)
                                    {
                                        startActivity(new Intent(Login.this,AdminDashboard.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
}