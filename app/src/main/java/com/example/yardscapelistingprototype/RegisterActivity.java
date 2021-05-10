package com.example.yardscapelistingprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailView, passView, confirmView;
    Button btnCreate;
    private FirebaseAuth mAuth;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailView = (EditText) findViewById(R.id.registerEmail);
        passView = (EditText) findViewById(R.id.registerPassword);
        confirmView = (EditText) findViewById(R.id.registerConfirmPassword);
        btnCreate = (Button) findViewById(R.id.registerAccountButton);
        relativeLayout = (RelativeLayout) findViewById(R.id.registerLayout);

        mAuth = FirebaseAuth.getInstance();

/*        try {
            String email = getIntent().getStringExtra("email");
            emailView.setText(email);
        } catch (Exception e) {
            Log.e("emailError", e.getMessage(), e);
        }*/

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        if((passView.getText().toString().compareTo(confirmView.getText().toString())) == 1) {
            confirmView.setError("Please confirm your password");
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailView.toString(), passView.toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Error, Account was not created", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}