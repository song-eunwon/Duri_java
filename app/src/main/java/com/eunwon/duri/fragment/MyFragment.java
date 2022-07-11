package com.eunwon.duri.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eunwon.duri.R;
import com.eunwon.duri.SpManager;
import com.eunwon.duri.activity.LogInActivity;
import com.eunwon.duri.activity.MainActivity;
import com.eunwon.duri.activity.WebActivity;
import com.eunwon.duri.dialog.LogoutDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MyFragment extends Fragment {
    final int NICKNAME_MIN_LENGTH = 2;
    final int NICKNAME_MAX_LENGTH = 12;

    EditText nicknameEditText;
    Button privacyButton;
    Button serviceButton;
    Button logoutButton;
    Button modifyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        setUpUI(view);
        action();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initNickname();
    }

    void setUpUI(View v) {
        nicknameEditText = v.findViewById(R.id.my_nickname_et);
        privacyButton = v.findViewById(R.id.my_privacy_btn);
        serviceButton = v.findViewById(R.id.my_service_btn);
        logoutButton = v.findViewById(R.id.my_logout_btn);
        modifyButton = v.findViewById(R.id.my_modify_btn);

        initNickname();
    }

    void initNickname() {
        modifyButton.setVisibility(View.GONE);
        nicknameEditText.setText(SpManager.getInstance().getNickname(getContext()));
    }

    void action() {
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("type", "privacy");
                getActivity().startActivity(intent);
            }
        });

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("type", "service");
                getActivity().startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutDialog logoutDialog = new LogoutDialog(getContext(), getActivity());
                logoutDialog.show();
            }
        });

        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO: 12글자가 넘으면 더이상 받지않음

                if (nicknameEditText.getText().toString().equals(SpManager.getInstance().getNickname(getContext()))) {
                    modifyButton.setVisibility(View.GONE);
                } else {
                    modifyButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                SharedPreferences pref = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
                String userID = pref.getString("userID", "");
                db.collection("User")
                        .whereEqualTo("uid", userID) // Query
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                                    db.collection("User")
                                            .document(doc.getId())
                                            .update("nickname", nicknameEditText.getText().toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    SpManager.getInstance().saveNickname(getContext(), nicknameEditText.getText().toString());
                                                    modifyButton.setVisibility(View.GONE);
                                                }
                                            });
                                } else {
                                    Log.d("test", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }
}