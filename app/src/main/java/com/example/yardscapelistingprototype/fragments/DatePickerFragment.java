// Created DatePickerFragment to prompt the user to pick a date, makes entering the date easier
package com.example.yardscapelistingprototype.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year, month, day;
    private EditText dateEditText;


    public DatePickerFragment() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
    }

    public DatePickerFragment(EditText editText) {
        dateEditText = editText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
        this.year = year1;
        this.month = month1 + 1;
        this.day = dayOfMonth;

        dateEditText.setText(String.format("%d/%d/%d", month, dayOfMonth, year));
    }
}
