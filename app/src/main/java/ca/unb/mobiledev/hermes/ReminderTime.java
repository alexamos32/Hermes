package ca.unb.mobiledev.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ReminderTime extends DialogFragment {
    View currentView;
    Button saveTime, cancelTime;
    TimePicker tp;
    String timeVal, dateVal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.reminder_time, container, false );
        saveTime = currentView.findViewById(R.id.time_save_button);
        cancelTime = currentView.findViewById(R.id.time_cancel_button);
        /*
        Note that the time picker in this layout will handle the selection of time values
        All we need to do is set an initial value if one exists, and handle cancel and saving
         */

        tp = currentView.findViewById(R.id.tp);

        if (getArguments() != null){
            timeVal = getArguments().getString("time");
            dateVal = getArguments().getString("date");
        }

        if (!timeVal.equalsIgnoreCase("ignore")){
            String[] arr = timeVal.split(":", 2);
            int hour = Integer.parseInt(arr[0]);
            int minute = Integer.parseInt(arr[1]);

            tp.setHour(hour);
            tp.setMinute(minute);
        }

        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeVal = tp.getHour() + ":";

                if (tp.getMinute() < 10) {
                    timeVal += "0" + tp.getMinute();
                }
                else {
                    timeVal += tp.getMinute();
                }

                NoteReminder noteReminder = new NoteReminder();
                Bundle bundle = new Bundle();
                bundle.putString("time", timeVal);
                bundle.putString("date", dateVal);
                noteReminder.setArguments(bundle);
                noteReminder.show(getFragmentManager(), "Relaunching Reminder View");

                dismiss();
            }
        });

        cancelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteReminder noteReminder = new NoteReminder();
                Bundle bundle = new Bundle();
                bundle.putString("time", timeVal);
                bundle.putString("date", dateVal);
                noteReminder.setArguments(bundle);
                noteReminder.show(getFragmentManager(), "Cancelled Reminder Time Set");
                dismiss();
            }
        });


        return currentView;
    }
}
