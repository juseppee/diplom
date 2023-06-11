package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {


    private EditText emailLogin;
    private EditText passwordLogin;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailLogin = view.findViewById(R.id.et_email);
        passwordLogin = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view1 -> {

            if (emailLogin.getText().toString().isEmpty() || passwordLogin.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(emailLogin.getText().toString(), passwordLogin.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        user.getIdToken(true)
                                                .addOnCompleteListener(tokenTask -> {
                                                    if (tokenTask.isSuccessful()) {
                                                        String authToken = tokenTask.getResult().getToken();
                                                        System.out.println("ТОКЕН " + authToken);
                                                    } else {
                                                        // Ошибка при получении токена аутентификации
                                                    }
                                                });
                                    } else {
                                        // Ошибка при аутентификации пользователя
                                    }
                                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                                    intent.putExtra("email", emailLogin.getText().toString());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "You have some errors", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}