package com.example.macromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.macromate.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends Fragment {

    private TextView currentDateText;
    private ImageButton previousDayButton, nextDayButton;
    private Calendar selectedCalendar;
    private SimpleDateFormat dateFormat;
    private OnDateChangeListener dateChangeListener;

    public interface OnDateChangeListener {
        void onDateChanged(Date selectedDate);
    }

    public DatePickerFragment() {
        selectedCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
    }

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        initializeViews(view);
        setupDateNavigation();
        updateDateDisplay();

        return view;
    }

    private void initializeViews(View view) {
        currentDateText = view.findViewById(R.id.currentDateText);
        previousDayButton = view.findViewById(R.id.previousDayButton);
        nextDayButton = view.findViewById(R.id.nextDayButton);
    }

    private void setupDateNavigation() {
        previousDayButton.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateDisplay();
            notifyDateChanged();
        });

        nextDayButton.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateDisplay();
            notifyDateChanged();
        });

        currentDateText.setOnClickListener(v -> {
            selectedCalendar = Calendar.getInstance();
            updateDateDisplay();
            notifyDateChanged();
        });
    }

    private void updateDateDisplay() {
        Date selectedDate = selectedCalendar.getTime();
        String formattedDate = dateFormat.format(selectedDate);

        Calendar today = Calendar.getInstance();
        if (isSameDay(selectedCalendar, today)) {
            currentDateText.setText("Danas - " + formattedDate);
        } else {
            currentDateText.setText(formattedDate);
        }
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private void notifyDateChanged() {
        if (dateChangeListener != null) {
            dateChangeListener.onDateChanged(selectedCalendar.getTime());
        }
    }

    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.dateChangeListener = listener;
    }

    public Date getSelectedDate() {
        return selectedCalendar.getTime();
    }

    public void setSelectedDate(Date date) {
        selectedCalendar.setTime(date);
        updateDateDisplay();
    }
}
