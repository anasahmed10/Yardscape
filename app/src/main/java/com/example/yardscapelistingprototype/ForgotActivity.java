package com.example.yardscapelistingprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    private EditText recoveryEmail, confirmRecoveryEmail;
    private Button btnRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        recoveryEmail = (EditText) findViewById(R.id.forgotEmail);
        confirmRecoveryEmail = (EditText) findViewById(R.id.confirmForgotEmail);
        btnRecover = (Button) findViewById(R.id.recoverAccountButton);

/*        try {
            recoveryEmail.setText(getIntent().getStringExtra("email"));
        } catch (Exception e) {
            Log.e("emailError", e.getMessage(), e);
        }*/

        // On button click the recovery email is sent to the user's email
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recoveryEmail.getText().toString() != confirmRecoveryEmail.getText().toString()){
                    recoveryEmail.setError("Please ensure that both emails are the same");
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(recoveryEmail.toString());
                Toast.makeText(ForgotActivity.this, "Recovery Email has been sent to your Email Address", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
            }
        });
    }
}