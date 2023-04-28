package ca.uqac.bubble.Calendrier;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.TimePicker;

import java.time.LocalTime;

import ca.uqac.bubble.MyDatabaseHelper;
import ca.uqac.bubble.R;

public class EventEditActivity extends Activity
{

    private MyDatabaseHelper dbHelper;

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;

    Button timeButton;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.of(12,00,0,0);
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                time = LocalTime.of(hour,minute,0,0);
                eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void saveEventAction(View view) {
        dbHelper = new MyDatabaseHelper(this);
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time,CalendarUtils.selectedDate.atTime(time));
        Event.eventsList.add(newEvent);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", eventName);
        values.put("date", String.valueOf(CalendarUtils.selectedDate));
        values.put("time", String.valueOf(time));
        values.put("dateTime", String.valueOf(CalendarUtils.selectedDate.atTime(time)));

        db.insert("calendrier", null, values);
        dbHelper.close();

        finish();
    }
}