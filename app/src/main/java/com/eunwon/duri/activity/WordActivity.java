package com.eunwon.duri.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eunwon.duri.R;
import com.eunwon.duri.model.Word;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class WordActivity extends AppCompatActivity {

    Button updateButton;
    Button deleteButton;

    EditText englishEditText;
    EditText koreanEditText;

    // Refactoring - 리팩토링

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        configureUI();
        initData();
        action();
    }

    void configureUI() {
        updateButton = findViewById(R.id.word_update_btn);
        deleteButton = findViewById(R.id.word_delete_btn);

        englishEditText = findViewById(R.id.word_english_et);
        koreanEditText = findViewById(R.id.word_korean_et);
    }

    void initData() {
        Intent intent = getIntent();
        String english = intent.getStringExtra("english");
        englishEditText.setText(english);
        String korean = intent.getStringExtra("korean");
        koreanEditText.setText(korean);
    }

    void action() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                DocumentReference ref = db.collection("Words").document(id);

                ref.update(
                        "english", englishEditText.getText().toString(),
                        "korean", koreanEditText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("FB", e.toString());
                            }
                        });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");

                db.collection("Words").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                            }
                        });
            }
        });
    }
}
