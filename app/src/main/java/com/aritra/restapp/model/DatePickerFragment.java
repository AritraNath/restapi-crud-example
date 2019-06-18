package com.aritra.restapp.model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

    final Calendar c = Calendar.getInstance();
    private int year = c.get(Calendar.YEAR);
    private int month = c.get(Calendar.MONTH);
    private int day = c.get(Calendar.DAY_OF_MONTH);


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), (DatePickerDialog.OnDateSetListener) getActivity(),
                year, month, day);
    }

}
