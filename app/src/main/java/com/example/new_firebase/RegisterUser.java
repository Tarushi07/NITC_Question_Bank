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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity{

    private EditText editUsername,editRoll,editEmail,editPass;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        Button backtologin = findViewById(R.id.backtologin);
        Button register = findViewById(R.id.registerButton);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editRoll = (EditText) findViewById(R.id.editRoll);
        editEmail = (EditText) findViewById(R.id.editRegisterEmail);
        editPass = (EditText) findViewById(R.id.editRegisterPassword);

        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterUser.this,Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailValid = editEmail.getText().toString().trim();
                String passValid = editPass.getText().toString().trim();
                String nameValid = editUsername.getText().toString().trim();
                String rollValid = editRoll.getText().toString().trim();

                if(nameValid.isEmpty())
                {
                    editUsername.setError("Full Name is Required");
                    editUsername.requestFocus();
                    return;
                }
                if(rollValid.isEmpty())
                {
                    editRoll.setError("Roll Number is Required");
                    editRoll.requestFocus();
                    return;
                }
                if(emailValid.isEmpty())
                {
                    editEmail.setError("Email is Required");
                    editEmail.requestFocus();
                    return;
                }
                if(passValid.isEmpty())
                {
                    editPass.setError("Password is Required");
                    editPass.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailValid).matches())
                {
                    editEmail.setError("Please enter a valid Email");
                    editEmail.requestFocus();
                    return;
                }
                if(passValid.length()<6)
                {
                    editPass.setError("Min password length should be six");
                    editPass.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailValid,passValid)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    String uid = task.getResult().getUser().getUid();
                                    User user = new User(uid,nameValid,rollValid,emailValid,0);
                                    firebaseDatabase.getReference().child("Users").child(uid).setValue(user);
                                    startActivity(new Intent(RegisterUser.this,Login.class));
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