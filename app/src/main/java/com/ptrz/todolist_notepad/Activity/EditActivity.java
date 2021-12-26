package com.ptrz.todolist_notepad.Activity;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.ptrz.todolist_notepad.MainActivity;
import com.ptrz.todolist_notepad.R;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    Boolean important;
    EditText etItem;
    EditText date;
    EditText detail;
    Button btnSave;
    Switch isImportant;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.editItem);
        date = findViewById(R.id.date);
        detail = findViewById(R.id.detail);
        btnSave = findViewById(R.id.btnSave);
        isImportant = findViewById(R.id.isImportant);

        // Setup page
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit item");
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        date.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_DATE));
        detail.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_DETAIL));
        important = getIntent().getExtras().getBoolean(MainActivity.KEY_ITEM_IMPORTANT);
        isImportant.setChecked(important);


        // when user is done editing, they click the save button
        btnSave.setOnClickListener(v -> {
            // create an intent which will contain the results
            Intent intent = new Intent();
            // pass the data (result of editing
            intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
            intent.putExtra(MainActivity.KEY_ITEM_DATE, date.getText().toString());
            intent.putExtra(MainActivity.KEY_ITEM_DETAIL, detail.getText().toString());
            intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
            intent.putExtra(MainActivity.KEY_ITEM_IMPORTANT, important);
            // set the result of the intent
            setResult(RESULT_OK, intent);
            // finish the activity, close the screen, and go back
            finish();
        });

        isImportant.setOnCheckedChangeListener((buttonView, isChecked) -> {
            important = isChecked;
        });
    }
}