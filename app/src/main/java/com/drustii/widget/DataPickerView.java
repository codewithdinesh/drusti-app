package com.drustii.widget;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.drustii.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataPickerView implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText mEditText;
    private Calendar mCalender;
    private SimpleDateFormat dateFormat;

    public DataPickerView(EditText editText,String format){
        this.mEditText=editText;
        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnClickListener(this);
        dateFormat=new SimpleDateFormat(format, Locale.getDefault());

    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalender.set(Calendar.YEAR,year);
        mCalender.set(Calendar.MONTH,month);
        mCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        this.mEditText.setText(dateFormat.format(mCalender.getTime()));

    }

    @Override
    public void onClick(View v) {
        showPicker(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            showPicker(v);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void showPicker(View v) {
        if (mCalender==null)
            mCalender=Calendar.getInstance();

        int day=mCalender.get(Calendar.DAY_OF_MONTH);
        int month=mCalender.get(Calendar.MONTH);
        int year=mCalender.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog=new DatePickerDialog(v.getContext(),this,year,month,day);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.blue);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.red);
    }
}
