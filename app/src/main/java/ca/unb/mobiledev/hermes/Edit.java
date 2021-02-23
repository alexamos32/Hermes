package ca.unb.mobiledev.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteContent;
    Calendar calendar;
    String currentDate, currentTime;
    long noteId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        noteId = intent.getLongExtra("ID", 0);
        NoteDatabase db = new NoteDatabase(this);
        Note note = db.getNote(noteId);

        final String title = note.getTitle();
        String content = note.getContent();
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteDetails);
        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() !=0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        noteTitle.setText(title);
        noteContent.setText(content);

        calendar = Calendar.getInstance();
        currentDate = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: "+currentDate);
        currentTime = pad(calendar.get(Calendar.HOUR))+":"+pad(calendar.get(Calendar.MINUTE));
        Log.d("TIME", "Time: "+currentTime);
    }

    private String pad(int time) {
        if(time < 10)
            return "0"+time;
        return String.valueOf(time);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Note note = new Note(noteId, noteTitle.getText().toString(),noteContent.getText().toString(),currentDate,currentTime);

            Log.d("EDITED", "edited: before saving id -> " + note.getId());
            NoteDatabase db = new NoteDatabase(getApplicationContext());
            long id = db.editNote(note);
            Log.d("EDITED", "EDIT: id " + id);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Note Edited.", Toast.LENGTH_SHORT).show();
        }

        else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
