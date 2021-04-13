package ca.unb.mobiledev.hermes;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;



public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar calender;
    String currentDate;
    String currentTime;
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
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Note");

        htmlPreview = findViewById(R.id.htmlPreview);

        noteDetails = findViewById(R.id.noteDetails);
        noteTitle = findViewById(R.id.noteTitle);

        reminderDateText = findViewById(R.id.rem_date);
        reminderTimeText = findViewById(R.id.rem_time);





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
                Log.d("hhhhhhhh", "Note: "+ getIntent().getIntExtra("parentID", -1));
                long folderID = getIntent().getIntExtra("parentID", -1);
                Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), currentDate, currentTime, folderID, remTime, remDate);

                NoteDatabase db = new NoteDatabase(this);
                long id = db.addNote(note);
                Note check = db.getNote(id);
                Log.d("inserted", "Note: "+ id + ", Title: " + check.getTitle() + ", Date: " + check.getDate() + ", Time: " + check.getTime());

                //Setting the reminder if an actual time date has been selected
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
                    Intent intent = new Intent(AddNote.this, AlarmReceiver.class);
                    intent.putExtra("title", noteTitle.getText().toString());
                    intent.putExtra("reminderId", id);
                    //       intent.putExtra("classname", AddNote.class);
                    alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    reminderIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), reminderIntent );
                }

                //Closing window.
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
        else if (item.getItemId() == R.id.dictation){
            Intent dicateIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            try{
                startActivityForResult(dicateIntent, DICTATE_REQUEST);
            }
            catch (ActivityNotFoundException e){
                e.printStackTrace();
            }
        }
        else if(item.getItemId() == R.id.notification){
            launchNotification();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DICTATE_REQUEST && resultCode == RESULT_OK) {
            if (data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (noteDetails.getText().length()>0){
                    String temp = noteDetails.getText().toString();
                    temp += "\n\n";
                    temp += result.get(0);
                    noteDetails.setText(temp);
                }
                else {
                    noteDetails.setText(result.get(0));
                }
            }
        }
    }

    //Launch notification Reminder window
    public void launchNotification(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        NoteReminder noteReminder = new NoteReminder();

        //Sending the current saved time reminder as arguments
        Bundle bundle = new Bundle();
        bundle.putString("time", reminderTimeText.getText().toString());
        bundle.putString("date", reminderDateText.getText().toString());
        bundle.putString("class" , "add");
        noteReminder.setArguments(bundle);
        noteReminder.show(fragmentManager, "Show Fragment");
    }
    //Set the Reminder textviews with selected values
    //This method is called from NoteReminder class
    //TODO Change the notificaton setting to after the note is saved to db
    // We can use the note id as uniqure notificaton ID so that each note can have its own reminder
    public void setReminder(String time, String date){
        remTime = time;
        remDate = date;
        reminderTimeText.setText(time);
        reminderDateText.setText(date);
        reminderTimeText.setVisibility(View.VISIBLE);
        reminderDateText.setVisibility(View.VISIBLE);
    }
    //Only need to do this part for add note.
    //Since the not has yet to be created there will be note existing notification set.
    //Setting reminder string back to ignore will ensure no reminder is set during save
    public void deleteReminder(){
        remTime = "ignore";
        remDate = "ignore";
        reminderTimeText.setText("ignore");
        reminderDateText.setText("ignore");
        reminderTimeText.setVisibility(View.INVISIBLE);
        reminderDateText.setVisibility(View.INVISIBLE);
    }

}
