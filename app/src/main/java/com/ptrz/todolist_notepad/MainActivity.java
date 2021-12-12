package com.ptrz.todolist_notepad;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button buttonAdd;
    EditText newItem;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ADD button on main screen
        buttonAdd = findViewById(R.id.buttonAdd);
        // text input box on main screen
        newItem = findViewById(R.id.newItem);
        // list view box on main screen
        rvItem = findViewById(R.id.rvItem);

        loadItems();

        ItemsAdapter.onLongClickListener onLongClickListener = position -> {
            // delete the item from the model
            items.remove(position);
            // notify the adapter
            itemsAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
            saveItems();
        };
        ItemsAdapter.onClickListener onClickListener = position -> {
            // create the new activity
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            // pass the data being edited
            i.putExtra(KEY_ITEM_TEXT, items.get(position));
            i.putExtra(KEY_ITEM_POSITION, position);
            // display the activity
            startActivityForResult(i, EDIT_TEXT_CODE);
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItem.setAdapter(itemsAdapter);
        // show in a vertical line
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(v -> {
            // get the text user input
            String todoItem = newItem.getText().toString();
            // add an item to the model
            items.add(todoItem);
            // notify adapter that an item is inserted
            itemsAdapter.notifyItemInserted(items.size() - 1);
            // reset the text to blank
            newItem.setText("");
            Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
            saveItems();
        });
    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the update text value
            assert data != null;
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract the original position of the edited item from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the position with the new item text
            items.set(position, itemText);
            // notify adapter
            itemsAdapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item update successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.v("MainActivity", "unknown call to onActivityResult");
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    // this function will load items by reading every line of data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // this function save items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}