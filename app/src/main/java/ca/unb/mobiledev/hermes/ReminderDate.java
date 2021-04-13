package ca.unb.mobiledev.hermes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class ReminderDate extends DialogFragment {
    View currentView;
    Button saveDate, cancelDate;
    DatePicker dp;
    String timeVal, dateVal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.reminder_date, container, false);
        saveDate = currentView.findViewById(R.id.date_save_button);
        cancelDate = currentView.findViewById(R.id.date_cancel_button);
        dp = currentView.findViewById(R.id.dp);

        if (getArguments() != null){
            timeVal = getArguments().getString("time");
            dateVal = getArguments().getString("date");
        }

        //Setting the date picker to previous val if one exists
        if(!dateVal.equalsIgnoreCase("ignore")){
            String[] arr = dateVal.split("-", 3);

            int year = Integer.parseInt(arr[2]);
            int month = Integer.parseInt(arr[1]);
            int day = Integer.parseInt(arr[0]);

            dp.updateDate(year, month, day);
        }

        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateVal = dp.getDayOfMonth() + "-" +( dp.getMonth()+1) + "-" + dp.getYear();
                NoteReminder noteReminder = new NoteReminder();
                Bundle bundle = new Bundle();
                bundle.putString("time",timeVal);
                bundle.putString("date", dateVal);
                noteReminder.setArguments(bundle);
                noteReminder.show(getFragmentManager(), "Relaunching Reminder View");
                dismiss();
            }
        });

        cancelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteReminder noteReminder = new NoteReminder();
                Bundle bundle = new Bundle();
                bundle.putString("time",timeVal);
                bundle.putString("date", dateVal);
                noteReminder.setArguments(bundle);
                noteReminder.show(getFragmentManager(), "Cancelling Date Set");
                dismiss();
            }
        });



        return currentView;
    }
}
