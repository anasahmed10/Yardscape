package com.example.yardscapelistingprototype.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yardscapelistingprototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    EditText emailView, passView, confirmView;
    Button btnCreate;
    private FirebaseAuth mAuth;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

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
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        emailView = (EditText) v.findViewById(R.id.registerEmail);
        passView = (EditText) v.findViewById(R.id.registerPassword);
        confirmView = (EditText) v.findViewById(R.id.registerConfirmPassword);
        btnCreate = (Button) v.findViewById(R.id.registerAccountButton);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        return v;
    }

    private void createAccount() {
        if(passView.getText().toString().compareTo(confirmView.getText().toString()) == 0) {
            confirmView.setError("Please confirm your password");
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailView.toString(), passView.toString()).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Snackbar.make(requireView(), "Account Created Successfully!", Snackbar.LENGTH_SHORT);
                }
                else {
                    Snackbar.make(requireView(), "Error, Account was not created", Snackbar.LENGTH_SHORT);
                }
            }
        });
    }
}