package com.example.yardscapelistingprototype;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister, btnForgot;
    private EditText emailView, passView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SignInButton btnGoogleLogin;
    private GoogleSignInAccount googleSignInAccount;
    private final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 120;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        setContentView(R.layout.fragment_login);

        btnLogin = (Button) findViewById(R.id.loginButton);
        btnRegister = (Button) findViewById(R.id.registerButton);
        btnForgot = (Button) findViewById(R.id.forgotButton);
        emailView = (EditText) findViewById(R.id.emailEntry);
        passView = (EditText) findViewById(R.id.passwordEntry);
        btnGoogleLogin = (SignInButton) findViewById(R.id.googleloginButton);
        btnGoogleLogin.setSize(SignInButton.SIZE_STANDARD);

        mAuth = FirebaseAuth.getInstance();

        // Checks to see if the user is already logged in
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mUser != null) {
                    startActivity(new Intent(LoginActivity.this, HostActivity.class));
                    finish();
                }
            }
        }, 1000);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(emailView.toString(), passView.toString());
            }
        });

        // Opens register account fragment
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
/*                if(emailView.toString() != ""){
                    intent.putExtra("email", emailView.toString());
                }*/
                startActivity(intent);
            }
        });

        // Opens forgot account fragment
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
/*                if(emailView.toString() != ""){
                    intent.putExtra("email", emailView.toString());
                }*/
                startActivity(intent);
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        // Configure Google Sign In

//        mUser = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext() ,gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void loginUser(String email, final String password) {
        // Attempts to sign the user in or logs the appropriate message in case of error
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        // Brings user to the main page
                        startActivity(new Intent(LoginActivity.this, HostActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.loginView), e.getLocalizedMessage(), Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception exception = task.getException();
            if(task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("GsignSuccess", "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("GsignError", "Google sign in failed", e);
                }
            } else {
                Log.w("GsignError", exception.toString());
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AuthSuccess", "signInWithCredential:success");
                            mUser = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HostActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AuthFailed", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }

}