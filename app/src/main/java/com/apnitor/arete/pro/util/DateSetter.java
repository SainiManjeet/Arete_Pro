package com.apnitor.arete.pro.util;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


import com.apnitor.arete.pro.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateSetter implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText mEditText;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;

    public DateSetter(EditText editText) {
        this.mEditText = editText;
        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            showPicker(view);
        }
    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {

        if (mCalendar == null)
            mCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(view.getContext(), R.style.datepickerCustom, this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.mEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

    }
}