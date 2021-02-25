package ca.unb.mobiledev.hermes;


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



public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar calender;
    String currentDate;
    String currentTime;
    TextView htmlPreview;
    MenuItem previewButton, editButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Note");

        htmlPreview = findViewById(R.id.htmlPreview);

        noteDetails = findViewById(R.id.noteDetails);
        noteTitle = findViewById(R.id.noteTitle);

        noteTitle.addTextChangedListener(new TextWatcher() {
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
        inflater.inflate(R.menu.save_menu,menu);

        editButton = menu.findItem(R.id.editText);
        previewButton = menu.findItem(R.id.preview);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            if(noteTitle.getText().length() != 0){
                Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), currentDate, currentTime);
                NoteDatabase db = new NoteDatabase(this);
                long id = db.addNote(note);
                Note check = db.getNote(id);
                Log.d("inserted", "Note: "+ id + ", Title: " + check.getTitle() + ", Date: " + check.getDate() + ", Time: " + check.getTime());
                onBackPressed();

                Toast.makeText(this, "Note Saved.", Toast.LENGTH_SHORT).show();

            }
            else {
                noteTitle.setError("Title cannot be blank.");
            }
        }
        else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        else if (item.getItemId() == R.id.preview){
            String content = noteDetails.getText().toString();
            MarkdownRender mdRenderer = new MarkdownRender();
            String input = mdRenderer.render(content);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                htmlPreview.setText(Html.fromHtml(input,Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                htmlPreview.setText(Html.fromHtml(input));
            }
            noteDetails.setVisibility(View.INVISIBLE);
            htmlPreview.setVisibility(View.VISIBLE);
            editButton.setVisible(true);
            item.setVisible(false);
        }
        else if (item.getItemId() == R.id.editText){
            noteDetails.setVisibility(View.VISIBLE);
            htmlPreview.setVisibility(View.INVISIBLE);
            previewButton.setVisible(true);
            item.setVisible(false);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
