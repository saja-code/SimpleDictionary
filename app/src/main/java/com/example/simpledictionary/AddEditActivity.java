package com.example.simpledictionary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    EditText txtWord, txtMeaning;
    Button btnSave;
    int index = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        txtWord = findViewById(R.id.etWord);
        txtMeaning = findViewById(R.id.etMeaning);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();

        if (intent.hasExtra("position")) {
            setTitle("Edit Word");
            index = intent.getIntExtra("position", -1);
            txtWord.setText(intent.getStringExtra("word"));
            txtMeaning.setText(intent.getStringExtra("meaning"));
        } else {
            setTitle("Add Word");
        }

        btnSave.setOnClickListener(v -> {
            String word = txtWord.getText().toString().trim();
            String meaning = txtMeaning.getText().toString().trim();

            if (word.isEmpty() || meaning.isEmpty()) {
                Toast.makeText(this, "Please enter word and meaning", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();

            data.putExtra("word", word);
            data.putExtra("meaning", meaning);
            data.putExtra("position", index);

            setResult(RESULT_OK, data);
            finish();
        });
    }
}