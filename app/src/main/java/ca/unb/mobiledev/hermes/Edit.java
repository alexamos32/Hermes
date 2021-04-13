package ca.unb.mobiledev.hermes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteContent;
    Calendar calendar;
    String currentDate, currentTime;
    long noteId;
    TextView htmlPreview;
    MenuItem previewButton, editButton;
    TextView reminderTimeText, reminderDateText;
    String remTime = "ignore", remDate = "ignore";
    private AlarmManager alarmManager;
    private PendingIntent reminderIntent;


    final int DICTATE_REQUEST = 3;

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
        remTime = note.getRemTime();
        remDate = note.getRemDate();

        reminderDateText = findViewById(R.id.rem_date);
        reminderTimeText = findViewById(R.id.rem_time);

        htmlPreview = findViewById(R.id.htmlPreview);
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteDetails);
        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        noteTitle.setText(title);
        noteContent.setText(content);

        //Set the textviews if the note has a stored reminder
        if (!remTime.equalsIgnoreCase("ignore")) {
            reminderTimeText.setText(remTime);
            reminderDateText.setText(remDate);
            reminderTimeText.setVisibility(View.VISIBLE);
            reminderDateText.setVisibility(View.VISIBLE);

        }

        calendar = Calendar.getInstance();
        currentDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: " + currentDate);
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
        Log.d("TIME", "Time: " + currentTime);
    }

    private String pad(int time) {
        if (time < 10)
            return "0" + time;
        return String.valueOf(time);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        editButton = menu.findItem(R.id.editText);
        previewButton = menu.findItem(R.id.preview);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Note note = new Note(noteId, noteTitle.getText().toString(),noteContent.getText().toString(),currentDate,currentTime, getIntent().getLongExtra("parentID", -1), remTime, remDate);

            Log.d("EDITED", "edited: before saving id -> " + note.getId());
            NoteDatabase db = new NoteDatabase(getApplicationContext());
            long id = db.editNote(note);
            Log.d("EDITED", "EDIT: id " + id);

            if (!remTime.equalsIgnoreCase("ignore")){
                //Setting up Calendar object to get alarm delay
                String[] timeArr = remTime.split(":", 2);
                String[] dateArr = remDate.split("-", 3);

                int hour = Integer.parseInt(timeArr[0]);
                int minute = Integer.parseInt(timeArr[1]);
                int day = Integer.parseInt(dateArr[0]);
                int month = Integer.parseInt(dateArr[1])-1;
                int year = Integer.parseInt(dateArr[2]);


                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                Log.i("TIMEUNTIL", String.valueOf(calendar.getTimeInMillis()- System.currentTimeMillis()));
                //Setting up the alarm manager to send the alarm intent
                Intent intent = new Intent(Edit.this, AlarmReceiver.class);
                intent.putExtra("title", noteTitle.getText().toString());
                intent.putExtra("reminderId", id);
                //       intent.putExtra("classname", AddNote.class);
                alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                reminderIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), reminderIntent );
            }


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Note Edited.", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else if (item.getItemId() == R.id.preview) {
            String content = noteContent.getText().toString();
            MarkdownRender mdRenderer = new MarkdownRender();
            String input = mdRenderer.render(content);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                htmlPreview.setText(Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY));
            } else {
                htmlPreview.setText(Html.fromHtml(input));
            }
            noteContent.setVisibility(View.INVISIBLE);
            htmlPreview.setVisibility(View.VISIBLE);
            editButton.setVisible(true);
            item.setVisible(false);
        }
        else if (item.getItemId() == R.id.editText) {
            noteContent.setVisibility(View.VISIBLE);
            htmlPreview.setVisibility(View.INVISIBLE);
            previewButton.setVisible(true);
            item.setVisible(false);
        }
        else if (item.getItemId() == R.id.dictation) {
            Intent dicateIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            try {
                startActivityForResult(dicateIntent, DICTATE_REQUEST);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        }
        else if (item.getItemId() == R.id.notification) {
            launchNotification();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DICTATE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (noteContent.getText().length() > 0) {
                    String temp = noteContent.getText().toString();
                    temp += "\n\n";
                    temp += result.get(0);
                    noteContent.setText(temp);
                } else {
                    noteContent.setText(result.get(0));
                }
            }
        }
    }

    public void launchNotification() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NoteReminder noteReminder = new NoteReminder();

        //Sending the current saved time reminder as arguments
        Bundle bundle = new Bundle();
        bundle.putString("time", reminderTimeText.getText().toString());
        bundle.putString("date", reminderDateText.getText().toString());
        bundle.putString("class","edit");
        noteReminder.setArguments(bundle);
        noteReminder.show(fragmentManager, "Show Fragment");
    }
    public void setReminder(String time, String date){
        remTime = time;
        remDate = date;
        reminderTimeText.setText(time);
        reminderDateText.setText(date);
        reminderTimeText.setVisibility(View.VISIBLE);
        reminderDateText.setVisibility(View.VISIBLE);
    }
    public void deleteReminder(){
        remTime = "ignore";
        remDate = "ignore";
        reminderTimeText.setText("ignore");
        reminderDateText.setText("ignore");
        reminderTimeText.setVisibility(View.INVISIBLE);
        reminderDateText.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        intent.putExtra("title", noteTitle.getText().toString());
        intent.putExtra("reminderId", noteId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0  );
        alarmManager.cancel(alarmIntent);
    }
}
