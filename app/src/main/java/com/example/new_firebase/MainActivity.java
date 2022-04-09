package com.example.new_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private TextView login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register_button);
        register.setOnClickListener(this);

        login = (TextView) findViewById(R.id.login_button);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.register_button: startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.login_button: startActivity(new Intent(this,Login.class));
                break;
        }
    }
}