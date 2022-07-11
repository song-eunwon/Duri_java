package com.eunwon.duri.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eunwon.duri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddWordActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText englishEditText;
    EditText koreanEditText;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        setUpUI();
        action();
    }

    void setUpUI() {
        englishEditText = findViewById(R.id.add_word_english_et);
        koreanEditText = findViewById(R.id.add_word_korean_et);
        doneButton = findViewById(R.id.add_word_done_btn);
    }

    void action() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (englishEditText.getText().toString().equals("")) {
                    Toast.makeText(AddWordActivity.this, "영어 단어를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (koreanEditText.getText().toString().equals("")) {
                    Toast.makeText(AddWordActivity.this, "뜻을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    String userID = pref.getString("userID", "!");

                    Map<String, Object> data = new HashMap<>();
                    data.put("english", englishEditText.getText().toString());
                    data.put("korean", koreanEditText.getText().toString());
                    data.put("created_time", new Timestamp(new Date()));
                    data.put("user_id", userID);

                    db.collection("Words")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    englishEditText.setText("");
                                    koreanEditText.setText("");

                                    Toast.makeText(AddWordActivity.this, "단어가 추가되었습니다", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("firebase", e.toString());
                                }
                            });
                }
            }
        });
    }
}