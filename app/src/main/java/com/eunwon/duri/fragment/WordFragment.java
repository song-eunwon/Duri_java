package com.eunwon.duri.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eunwon.duri.R;
import com.eunwon.duri.activity.AddWordActivity;
import com.eunwon.duri.adapter.WordAdapter;
import com.eunwon.duri.model.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WordFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private RecyclerView wordRecyclerView;
    private TextView defaultTextView;

    private WordAdapter wordAdapter;
    private ArrayList<Word> wordList;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, container, false);
        setUpUI(view);
        initRecyclerView();
        action();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    void setUpUI(View view) {
        defaultTextView = view.findViewById(R.id.default_tv);
        floatingActionButton = view.findViewById(R.id.word_floating_button);
        wordRecyclerView = view.findViewById(R.id.word_recyclerView);

        defaultTextView.setVisibility(View.GONE);
    }

    void initRecyclerView() {
        wordList = new ArrayList<>();

        wordAdapter = new WordAdapter();
        wordAdapter.wordList = wordList;
        wordAdapter.context = getActivity();

        wordRecyclerView.setAdapter(wordAdapter);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    void fetchData() {
        SharedPreferences pref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String userID = pref.getString("userID", "");

        db = FirebaseFirestore.getInstance();
        db.collection("Words")
                .whereEqualTo("user_id", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            wordList.clear();
                            // 데이터 파싱 - Parsing
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id = doc.getId();
                                String english = doc.getString("english");
                                String korean = doc.getString("korean");
                                String userId = doc.getString("user_id");
                                Timestamp createdTime = doc.getTimestamp("created_time");

                                Word word = new Word(id, english, korean, userId, createdTime);
                                wordList.add(word);
                            }

                            wordAdapter.wordList = wordList;
                            wordAdapter.notifyDataSetChanged();

                            if (wordList.size() == 0) {
                                defaultTextView.setVisibility(View.VISIBLE);
                            } else {
                                defaultTextView.setVisibility(View.GONE);
                            }
                        } else {
                            Log.d("aaa", task.getException().toString());
                        }
                    }
                });
    }

    void action() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddWordActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}