package com.example.todolist;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;


    Button btnAdd;
    EditText etItem;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItems);
        rvItem = findViewById(R.id.rvItem);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the model
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                //create the new activity
                Intent in = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                in.putExtra(KEY_ITEM_TEXT, items.get(position));
                in.putExtra(KEY_ITEM_POSITION, position);

                //display the activity
                startActivityForResult(in, EDIT_TEXT_CODE);

            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItem.setAdapter(itemsAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                items.add(todoItem);

                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    //handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode== EDIT_TEXT_CODE)
        {
            //retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract the original pos of the edited item from key pos
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model at the right pos
            items.set(position, itemText);

            //notify the adapter
            itemsAdapter.notifyItemChanged(position);

            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item has been updated", Toast.LENGTH_SHORT);

        }
        else
        {
            Log.w("MainActivity", "Unkown call to on Activity result");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    //This method will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e) {
            Log.e("Main Activity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        }
        catch(IOException e){
            Log.e("Main Activity", "Error writing items", e);
        }
    }
}