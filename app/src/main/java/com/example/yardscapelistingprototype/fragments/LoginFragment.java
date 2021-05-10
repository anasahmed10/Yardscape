package com.example.yardscapelistingprototype.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yardscapelistingprototype.R;
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

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private Button btnLogin, btnRegister, btnForgot;
    private EditText emailView, passView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SignInButton btnGoogleLogin;
    private GoogleSignInAccount googleSignInAccount;
    private final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 120;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure Google Sign In

//        mUser = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext() ,gso);
        mAuth = FirebaseAuth.getInstance();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_login, container, false);

        btnLogin = (Button) v.findViewById(R.id.loginButton);
        btnRegister = (Button) v.findViewById(R.id.registerButton);
        btnForgot = (Button) v.findViewById(R.id.forgotButton);
        emailView = (EditText) v.findViewById(R.id.emailEntry);
        passView = (EditText) v.findViewById(R.id.passwordEntry);
        btnGoogleLogin = (SignInButton) v.findViewById(R.id.googleloginButton);
        btnGoogleLogin.setSize(SignInButton.SIZE_STANDARD);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

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
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                androidx.fragment.app.Fragment fragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Opens forgot account fragment
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                ForgotFragment fragment = new ForgotFragment();
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        return v;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void loginUser(String email, final String password) {
        // Attempts to sign the user in or logs the appropriate message in case of error
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(getActivity().findViewById(R.id.flContent), "Login Successful!", Snackbar.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        // FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.popBackStackImmediate();
                    } else {
                        Snackbar.make(getActivity().findViewById(R.id.flContent), "Login Failed!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Snackbar.make(getActivity().findViewById(R.id.flContent), e.getLocalizedMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AuthSuccess", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AuthFailed", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GsignSuccess", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GsignError", "Google sign in failed", e);
            }
        }
    }
}