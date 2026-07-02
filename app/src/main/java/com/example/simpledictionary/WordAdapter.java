package com.example.simpledictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.MyViewHolder> {

    ArrayList<Word> words;
    OnWordClick listener;

    public interface OnWordClick {
        void clickWord(int position);
        void longClickWord(int position);
    }

    public WordAdapter(ArrayList<Word> words, OnWordClick listener) {
        this.words = words;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = words.get(position);

        holder.txtWord.setText(word.getWord());
        holder.txtMeaning.setText(word.getMeaning());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtWord, txtMeaning;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtWord = itemView.findViewById(R.id.txtWord);
            txtMeaning = itemView.findViewById(R.id.txtMeaning);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    listener.clickWord(pos);
                }
            });

            itemView.setOnLongClickListener(v -> {
                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    listener.longClickWord(pos);
                }

                return true;
            });
        }
    }
}