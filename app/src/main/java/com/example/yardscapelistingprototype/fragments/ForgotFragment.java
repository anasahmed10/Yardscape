package com.example.yardscapelistingprototype.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.yardscapelistingprototype.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotFragment extends Fragment {

    private EditText recoveryEmail, confirmRecoveryEmail;
    private Button btnRecover;

    public ForgotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForgotFragment.
     */
    public static ForgotFragment newInstance() {
        ForgotFragment fragment = new ForgotFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot, container, false);

        recoveryEmail = (EditText) v.findViewById(R.id.forgotEmail);
        confirmRecoveryEmail = (EditText) v.findViewById(R.id.confirmForgotEmail);
        btnRecover = (Button) v.findViewById(R.id.recoverAccountButton);

        // On button click the recovery email is sent to the user's email
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recoveryEmail.getText().toString() != confirmRecoveryEmail.getText().toString()){
                    recoveryEmail.setError("Please ensure that both emails are the same");
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(recoveryEmail.toString());
                Snackbar.make(requireView(), "Your recovery message has been sent to your email", Snackbar.LENGTH_LONG);
            }
        });

        return v;
    }
}