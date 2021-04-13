package ca.unb.mobiledev.hermes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
This class opens a popup window that allows users to set
a reminder using a date and time picker.
 */
public class NoteReminder extends DialogFragment {
    View currentView;
    TextView timeText, dateText;
    Button saveButton, deleteButton;
    ImageButton backButton;
    String timeVal, dateVal, className;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.reminder_view, container, false);
        timeText = currentView.findViewById(R.id.time_text);
        dateText = currentView.findViewById(R.id.date_text);
        saveButton = currentView.findViewById(R.id.save_reminder);
        backButton = currentView.findViewById(R.id.back_button);
        deleteButton = currentView.findViewById(R.id.delete_reminder);

        if (getArguments() != null) {
            timeVal = getArguments().getString("time");
            dateVal = getArguments().getString("date");
            className = getArguments().getString("class");
        }
        if (timeVal.equalsIgnoreCase("ignore") && dateVal.equalsIgnoreCase("ignore")){
            //Use two formats to get both time and date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm", Locale.CANADA);
            timeVal = dateFormat1.format(new Date());

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
            dateVal = dateFormat2.format(new Date());
        }
        timeText.setText(timeVal);
        dateText.setText(dateVal);

        //Setting onClick listeners for both the time and date textviews.
        //This will open respective popups for settng each.
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager timeFragment = getFragmentManager();
                ReminderTime reminderTime = new ReminderTime();

                //Send Reminder time as an argument;
                Bundle bundle = new Bundle();
                bundle.putString("time", timeVal);
                bundle.putString("date", dateVal);
                bundle.putString("class" ,className);
                reminderTime.setArguments(bundle);
                reminderTime.show(timeFragment,"Time Pick Fragment");
                dismiss();

            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager dateFragment = getFragmentManager();
                ReminderDate reminderDate = new ReminderDate();

                Bundle bundle = new Bundle();
                bundle.putString("time", timeVal);
                bundle.putString("date", dateVal);
                bundle.putString("class" ,className);
                reminderDate.setArguments(bundle);
                reminderDate.show(dateFragment, "Date Pick Fragment");
                dismiss();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /*
        TODO Make the save button properly save a reminder
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reminder set", Toast.LENGTH_SHORT).show();
                if(className.equalsIgnoreCase("add")){
                    AddNote addNote = (AddNote)getActivity();
                    addNote.setReminder(timeVal, dateVal);
                }
                else{
                    Edit edit = (Edit)getActivity();
                    edit.setReminder(timeVal, dateVal);
                }
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reminder deleted", Toast.LENGTH_SHORT).show();
                if(className.equalsIgnoreCase("add")){
                    AddNote addNote = (AddNote)getActivity();
                    addNote.deleteReminder();
                }
                else {
                    Edit edit = (Edit) getActivity();
                    edit.deleteReminder();
                }
                dismiss();
            }
        });


        return currentView;
    }
}
