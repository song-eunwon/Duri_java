package com.eunwon.duri.model;


import com.google.firebase.Timestamp;

import java.io.Serializable;

// model class
public class Word {
    // 멤버 변수
    public String id;
    public String english;
    public String korean;
    String userId;
    Timestamp createdTime;

    // 생성자
    public Word(String id, String english, String korean, String userId, Timestamp createdTime) {
        this.id = id;
        this.english = english;
        this.korean = korean;
        this.userId = userId;
        this.createdTime = createdTime;
    }
}
