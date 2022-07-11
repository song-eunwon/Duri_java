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
import com.eunwon.duri.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    //Button
    Button signUpButton;

    //editText
    EditText nicknameEditText;
    EditText emailEditText;
    EditText pwEditText;
    EditText pwConfirmEditText;

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpUI();
        action();
    }

    void setUpUI() {
        signUpButton = findViewById(R.id.signup_signup_btn);

        nicknameEditText = findViewById(R.id.signup_user_id_ed);
        emailEditText = findViewById(R.id.signup_email_ed);
        pwEditText = findViewById(R.id.signup_pw_ed);
        pwConfirmEditText = findViewById(R.id.signup_pw_confirm_ed);
    }

    void action() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nicknameEditText.getText().toString().isEmpty() || pwEditText.getText().toString().isEmpty() || pwConfirmEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "항목을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (!pwEditText.getText().toString().equals(pwConfirmEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {
                    signUp();
                }
            }
        });
    }

    void signUp() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), pwEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("nickname", nicknameEditText.getText().toString());
                            data.put("email", emailEditText.getText().toString());
                            data.put("uid", auth.getCurrentUser().getUid());

                            db.collection("User")
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            SpManager.getInstance().logIn(getApplicationContext(), auth.getCurrentUser().getUid(), nicknameEditText.getText().toString());

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("test", e.toString());
                                        }
                                    });
                        } else {
                            Log.d("AAA", task.getException().toString());
                        }
                    }
                });
    }
}