package com.eunwon.duri.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eunwon.duri.R;
import com.eunwon.duri.SpManager;
import com.eunwon.duri.model.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LogInActivity extends AppCompatActivity {
    //button
    private Button logInButton;
    private Button signUpButton;

    //editText
    private EditText emailEditText;
    private EditText pwEditText;

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setUpUI();
        action();
    }

    void setUpUI() {
        logInButton = findViewById(R.id.main_login_btn);
        signUpButton = findViewById(R.id.main_signup_btn);

        emailEditText = findViewById(R.id.main_user_email_ed);
        pwEditText = findViewById(R.id.main_pw_ed);
    }

    void action() {
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEditText.getText().toString().isEmpty() || pwEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    logIn();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    void logIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailEditText.getText().toString(), pwEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection("User")
                                    .whereEqualTo("uid", auth.getCurrentUser().getUid()) // Query
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                                                SpManager.getInstance().logIn(getApplicationContext(), doc.getString("uid"), doc.getString("nickname"));

                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.d("test", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(getApplicationContext(), "존재하지 않는 유저입니다", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
