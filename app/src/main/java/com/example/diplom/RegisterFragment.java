package com.example.diplom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private EditText nameRegister;
    private EditText emailRegister;
    private EditText passwordRegister;
    private Button btnRegister;

    private FirebaseAuth mAuth;

    public RegisterFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();

        nameRegister = view.findViewById(R.id.et_name);
        emailRegister = view.findViewById(R.id.et_email);
        passwordRegister = view.findViewById(R.id.et_password);
        btnRegister = view.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(view1 -> {

            if (nameRegister.getText().toString().isEmpty() || emailRegister.getText().toString().isEmpty()
                    || passwordRegister.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(emailRegister.getText().toString(), passwordRegister.getText().toString())
                .addOnCompleteListener(task -> {
                    System.out.println(emailRegister.getText().toString() + " " + passwordRegister.getText().toString() + "у дача");

                    if (task.isSuccessful()){

                        System.out.println("Нудача");
                        Intent intent = new Intent(getActivity(), MainActivity.class);

                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "You have some errors", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}