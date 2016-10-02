package com.disenkoart.howlongi.fragments.dialogFragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.joda.time.DateTime;

/**
 * Created by Артём on 24.09.2016.
 */
public class DatePickerFragment extends DialogFragment {

    DateTime mDateTime;
    public static final String TAG = "DataPickerFragment";
    public static final String EXTRA_DATE = "com.disenkoart.howlongi.fragments.date";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
        return  null;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        mDateTime = (DateTime)getArguments().getSerializable(EXTRA_DATE);
//        return new DatePickerDialog(getActivity(), /*R.style.DataPickerDialog,*/ dateSetListener, mDateTime.getYear(), mDateTime.getMonthOfYear() - 1,
//                mDateTime.dayOfMonth().get());
//    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, mDateTime.getHourOfDay(),
                    mDateTime.getMinuteOfHour(), mDateTime.getSecondOfMinute());
            sendResult(Activity.RESULT_OK);
        }
    };

    public static DatePickerFragment newInstance(DateTime date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDateTime);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
