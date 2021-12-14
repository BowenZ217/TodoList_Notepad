package com.ptrz.todolist_notepad;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ptrz.todolist_notepad.Activity.EditActivity;
import com.ptrz.todolist_notepad.Adapter.ItemAdapter;
import com.ptrz.todolist_notepad.Model.Item;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_DATE = "item_date";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final String KEY_ITEM_IMPORTANT = "item_important";
    public static final int EDIT_TEXT_CODE = 20;

    Button buttonAdd;
    EditText newItem;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    Gson gson = new Gson();

    List<String> save_list = new ArrayList<>();
    List<Item> item_list = new ArrayList<>();

    Comparator<Item> compareByImportant = Comparator.comparing(Item::getImportant);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* -------------------- Initialization --------------------*/
        // ADD button on main screen
        buttonAdd = findViewById(R.id.buttonAdd);
        // text input box on main screen
        newItem = findViewById(R.id.newItem);
        // list view box on main screen
        recyclerView = findViewById(R.id.recycler_view);


        loadItems();
        /*
        // -------------------- test case --------------------
        item_list.add(new Item("event 1", "2021/2/2"));
        item_list.add(new Item("event 2", "2021/3/1"));
        item_list.add(new Item("event 3", "2021/2/23"));
        item_list.add(new Item("event 4", "2021/3/16"));
        item_list.add(new Item("event 5", "2021/6/20"));
        item_list.add(new Item("event 6", "2021/9/30"));
         */



        ItemAdapter.onLongClickListener onLongClickListener = position -> {
            // delete the item from the model
            item_list.remove(position);
            // notify the adapter
            itemAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
            saveItems();
        };
        ItemAdapter.onClickListener onClickListener = position -> {
            // create the new activity
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            // pass the data being edited
            i.putExtra(KEY_ITEM_TEXT, item_list.get(position).getEvent());
            i.putExtra(KEY_ITEM_DATE, item_list.get(position).getDate());
            i.putExtra(KEY_ITEM_POSITION, position);
            i.putExtra(KEY_ITEM_IMPORTANT, item_list.get(position).getImportant());
            // display the activity
            startActivityForResult(i, EDIT_TEXT_CODE);
        };

        itemAdapter = new ItemAdapter(item_list, onLongClickListener, onClickListener);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);

        buttonAdd.setOnClickListener(v -> {
            // get the text user input
            String todoItem = newItem.getText().toString();
            if (!"".equals(todoItem)) {
                // add an item to the model
                item_list.add(new Item(todoItem));
                // notify adapter that an item is inserted
                itemAdapter.notifyItemInserted(item_list.size() - 1);
                // reset the text to blank
                newItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            } else {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this)
                        .setTitle("Cannot add blank events")
                        .setMessage("Please add some text")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("Confirmed",
                                (dialogInterface, i) ->
                                        Toast.makeText(MainActivity.this,
                                                "Please add some text", Toast.LENGTH_SHORT).show());
                alertDialog2.show();
            }
        });
    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the update text value
            assert data != null;
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            String itemDate = data.getStringExtra(KEY_ITEM_DATE);
            Boolean important = data.getExtras().getBoolean(KEY_ITEM_IMPORTANT);
            // extract the original position of the edited item from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the position with the new item text
            item_list.get(position).setEvent(itemText);
            item_list.get(position).setDate(itemDate);
            item_list.get(position).setImportant(important);

            // sort the array
            Collections.sort(item_list, compareByImportant);
            // notify adapter
            itemAdapter.notifyItemRangeChanged(0, item_list.size());


            Item.reverse(item_list);
            // notify adapter
            itemAdapter.notifyItemRangeChanged(0, item_list.size());
            // itemAdapter.notifyItemChanged(position);

            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item update successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.v("MainActivity", "unknown call to onActivityResult");
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }

    // /*
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    // this function will load items by reading every line of data file
    private void loadItems() {
        try {
            item_list = new ArrayList<>();
            save_list = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            for (String json : save_list) {
                item_list.add(gson.fromJson(json, Item.class));
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            item_list = new ArrayList<>();
        }
    }
    // this function save items by writing them into the data file
    private void saveItems() {
        try {
            save_list = new ArrayList<>();
            for (Item i : item_list) {
                save_list.add(gson.toJson(i));
            }
            FileUtils.writeLines(getDataFile(), save_list);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
    // */
}