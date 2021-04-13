package ca.unb.mobiledev.hermes;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;



public class AddFolder extends AppCompatActivity {
    Toolbar toolbar;
    EditText folderName;
    Calendar calender;
    String currentDate;
    String currentTime;
    MenuItem saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Folder");

        folderName = findViewById(R.id.folderName);

        folderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        calender = Calendar.getInstance();
        currentDate = calender.get(Calendar.YEAR) + "/" + (calender.get(Calendar.MONTH)+1) + "/" + calender.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: " + currentDate);
        currentTime = pad(calender.get(Calendar.HOUR)) + ":" + pad(calender.get(Calendar.MINUTE));
        Log.d("TIME", "Time: "+ currentTime);

    }

    private String pad(int time){
        if(time < 10){
            return "0"+time;
        }
        return String.valueOf(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_folder_menu,menu);

        saveButton = menu.findItem(R.id.save);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            if(folderName.getText().length() != 0){
                Folder folder = new Folder(getIntent().getIntExtra("parentID", -1), folderName.getText().toString());
                FolderDatabase db = new FolderDatabase(this);
                long id = db.addFolder(folder.getFolderName(), folder.getParentID());
                Folder check = db.getFolder(id);
                Log.d("inserted", "Note: "+ id + ", Title: " + check.getFolderName() + ", Date: " + check.getParentID());
                onBackPressed();

                Toast.makeText(this, "Folder Saved.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Folder name cannot be blank.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Folder name cannot be blank.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("parentID", getIntent().getIntExtra("parentID", -1));
        startActivity(i);
    }
}
