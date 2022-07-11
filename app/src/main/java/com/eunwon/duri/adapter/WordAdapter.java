package com.eunwon.duri.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eunwon.duri.R;
import com.eunwon.duri.activity.WordActivity;
import com.eunwon.duri.model.Word;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    public Context context;
    public ArrayList<Word> wordList;

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.ViewHolder holder, int position) {
        Word word = wordList.get(position);

        holder.englishTV.setText(word.english);
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView englishTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            englishTV = itemView.findViewById(R.id.item_english_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Word word = wordList.get(getAdapterPosition());

                    Intent intent = new Intent(context, WordActivity.class);
                    intent.putExtra("english", word.english);
                    intent.putExtra("korean", word.korean);
                    intent.putExtra("id", word.id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
