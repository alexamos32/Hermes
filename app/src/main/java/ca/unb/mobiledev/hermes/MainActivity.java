package ca.unb.mobiledev.hermes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    TextView noNotesText;
    NoteDatabase db;

    FolderDatabase folderDB;
    int parentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // NoteDatabase db = new NoteDatabase(this);
      //  db.onUpgrade();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (parentID != -1) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        else
            actionBar.setDisplayHomeAsUpEnabled(false);

        parentID = getIntent().getIntExtra("parentID", -1);


        noNotesText = findViewById(R.id.noNotes);
        db = new NoteDatabase(this);
        folderDB = new FolderDatabase(this);

        // get notes in folder
        List<Note> allNotes = db.getNotesInFolder(parentID);


        List<Folder> folders = folderDB.getFolderByParent(parentID);
        recyclerView = findViewById(R.id.allNotesList);

        if (allNotes.isEmpty()) {
            noNotesText.setVisibility(View.VISIBLE);
        }
        else {
            noNotesText.setVisibility(View.GONE);
            displayList(allNotes, folders);
        }
    }

    private void displayList(List<Note> allNotes, List<Folder> folders) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, allNotes, folders);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.right_menu, menu);
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            Toast.makeText(this, "Add New Note", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,AddNote.class);
            i.putExtra("parentID", parentID);
            startActivity(i);
        }
        if(item.getItemId() == R.id.add_folder){
            Toast.makeText(this, "Add New Folder", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,AddFolder.class);
            i.putExtra("parentID", parentID);
            startActivity(i);
        }
        if(item.getItemId() == android.R.id.home){
            Toast.makeText(this, "Returning ...", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("parentID", folderDB.getFolder(parentID).getParentID());
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        parentID = getIntent().getIntExtra("parentID", -1);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (parentID != -1) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        else
            actionBar.setDisplayHomeAsUpEnabled(false);

        List<Note> getAllNotes = db.getNotesInFolder(parentID);
        List<Folder> folders = folderDB.getFolderByParent(parentID);
        if (getAllNotes.isEmpty() && folders.isEmpty()) {
            noNotesText.setVisibility(View.VISIBLE);
        } else {
            noNotesText.setVisibility(View.GONE);
            displayList(getAllNotes, folders);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("parentID", parentID);
        startActivity(i);
    }
}