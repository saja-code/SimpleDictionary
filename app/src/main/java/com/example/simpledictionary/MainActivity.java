package com.example.simpledictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;

    ArrayList<Word> words;
    WordAdapter wordAdapter;

    public static final int ADD_WORD = 1;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Simple Dictionary");

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        sharedPreferences = getSharedPreferences("dictionary_data", MODE_PRIVATE);

        words = new ArrayList<>();
        loadWords();

        wordAdapter = new WordAdapter(words, new WordAdapter.OnWordClick() {
            @Override
            public void clickWord(int position) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("word", words.get(position).getWord());
                intent.putExtra("meaning", words.get(position).getMeaning());
                startActivityForResult(intent, ADD_WORD);
            }

            @Override
            public void longClickWord(int position) {
                words.remove(position);
                wordAdapter.notifyItemRemoved(position);
                saveWords();
                Toast.makeText(MainActivity.this, "Word deleted", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wordAdapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivityForResult(intent, ADD_WORD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD && resultCode == RESULT_OK && data != null) {

            String word = data.getStringExtra("word");
            String meaning = data.getStringExtra("meaning");
            int position = data.getIntExtra("position", -1);

            if (position == -1) {
                words.add(new Word(word, meaning));
                wordAdapter.notifyItemInserted(words.size() - 1);
                recyclerView.scrollToPosition(words.size() - 1);
                Toast.makeText(this, "Word added", Toast.LENGTH_SHORT).show();
            } else {
                words.get(position).setWord(word);
                words.get(position).setMeaning(meaning);
                wordAdapter.notifyItemChanged(position);
                Toast.makeText(this, "Word updated", Toast.LENGTH_SHORT).show();
            }

            saveWords();
        }
    }

    private void saveWords() {
        String allWords = "";

        for (Word w : words) {
            allWords += w.getWord() + "##" + w.getMeaning() + "\n";
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("words", allWords);
        editor.apply();
    }

    private void loadWords() {
        String savedData = sharedPreferences.getString("words", "");

        if (!savedData.isEmpty()) {
            String[] lines = savedData.split("\n");

            for (String line : lines) {
                String[] parts = line.split("##");

                if (parts.length == 2) {
                    words.add(new Word(parts[0], parts[1]));
                }
            }
        }
    }
}