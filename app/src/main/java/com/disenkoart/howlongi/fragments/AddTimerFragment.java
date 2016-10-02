package com.disenkoart.howlongi.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.MainActivity;
import com.disenkoart.howlongi.MainApplication;
import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.customView.gradientsViews.GradientViewGroup;
import com.disenkoart.howlongi.data.DateTimeFormat;
import com.disenkoart.howlongi.database.Timer;
import com.rey.material.widget.Button;

import org.joda.time.DateTime;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 21.09.2016.
 */
public class AddTimerFragment extends Fragment {

    @BindView(R.id.hli_text_layout)
    TextInputLayout mHliTextLayout;

    @BindView(R.id.add_timer_view_howLongI_edit_text)
    EditText mHliEditText;

    @BindView(R.id.date_text_layout)
    TextInputLayout mDateTextLayout;

    @BindView(R.id.add_timer_view_date_edit_text)
    EditText mDateEditText;

    @BindView(R.id.time_text_layout)
    TextInputLayout mTimeTextLayout;

    @BindView(R.id.add_timer_view_time_edit_text)
    EditText mTimeEditText;

    @BindView(R.id.add_timer_view_color_title)
    TextView mColorTitle;

    @BindView(R.id.add_timer_view_gradient_view_group)
    GradientViewGroup mGradientViewGroup;

    @BindView(R.id.add_timer_view_button_add)
    Button mAddButton;

    @BindView(R.id.add_timer_fragment_root)
    LinearLayout mFragmentRootView;

    private Locale mCurrentLocale;
    private boolean isChangeStatus = false;

    private Timer mTimerData;

    public static final String TIMER_ID = "TIMER_ID";

    private OnAddOrUpdateDataListener mOnAddOrUpdateDataListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_timer_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        view.setOnClickListener(onClickListener);
        mCurrentLocale = getResources().getConfiguration().locale;
        ButterKnife.bind(this, view);
        init();
        if (getArguments() != null)
        {
            setArgs();
        } else {
            mTimerData = new Timer(null, "", DateTime.now().getMillis(), 0, -1L);
        }
        updateDateEditText();
        updateTimeEditText();
        mHliEditText.setText(mTimerData.getHliString());
        mGradientViewGroup.setCurrentGradient(mTimerData.getGradientId());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnAddOrUpdateDataListener = (OnAddOrUpdateDataListener) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement mOnAddOrUpdateDataListener");
        }

    }

    private void init(){
        setTypeFace();
        mAddButton.setOnClickListener(addTimerButtonClick);
        mDateEditText.setOnClickListener(dateTextViewClick);
        mTimeEditText.setOnClickListener(timeTextViewClick);
        mHliEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mHliEditText.getText().toString().isEmpty()) {
                    mHliTextLayout.setError("");
                    mHliTextLayout.setErrorEnabled(false);
                    //mToast.show(getResources().getString(R.string.is_empty_text_HLI), null);
                }
            }
        });
    }

    private void setTypeFace(){
        mHliTextLayout.setTypeface(FontManager.UNI_SANS);
        mHliEditText.setTypeface(FontManager.UNI_SANS);
        mDateTextLayout.setTypeface(FontManager.UNI_SANS);
        mDateEditText.setTypeface(FontManager.UNI_SANS);
        mTimeTextLayout.setTypeface(FontManager.UNI_SANS);
        mTimeEditText.setTypeface(FontManager.UNI_SANS);
        mColorTitle.setTypeface(FontManager.UNI_SANS);
        mAddButton.setTypeface(FontManager.UNI_SANS);
    }

    private void setArgs(){
        isChangeStatus = true;
        mAddButton.setText(getResources().getString(R.string.add_timer_view_change_button));
        Long timerId = getArguments().getLong(TIMER_ID);
        mTimerData = MainApplication.getInstance().getDbSession().getTimerDao().load(timerId);
//        mDateTime = new DateTime(getArguments().getLong(TimersFragment.DATE_TIME_KEY));
//        mGradientViewGroup.setCurrentGradient(getArguments().getInt(TimersFragment.COLOR_KEY));
    }

    private void updateDateEditText() {
        if (mCurrentLocale.equals(Locale.US)) {
            mDateEditText.setText(DateTimeFormat.DATE_FORMAT_USA.format(mTimerData.getStartDateTime()));
        } else if (mCurrentLocale.equals(Locale.ENGLISH)) {
            mDateEditText.setText(DateTimeFormat.DATE_FORMAT_EN.format(mTimerData.getStartDateTime()));
        } else {
            mDateEditText.setText(DateTimeFormat.DATE_FORMAT_DEFAULT.format(mTimerData.getStartDateTime()));
        }
    }

    private void updateTimeEditText() {
        if (mCurrentLocale.equals(Locale.US) || mCurrentLocale.equals(Locale.CANADA)) {
            mTimeEditText.setText(DateTimeFormat.timeFormat12.format(mTimerData.getStartDateTime()));
        } else {
            mTimeEditText.setText(DateTimeFormat.timeFormat24.format(mTimerData.getStartDateTime()));
        }
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager keyboard = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentRootView.requestFocus();
            }
        }, 200);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideSoftKeyboard(v);
        }
    };

    private View.OnClickListener addTimerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mHliEditText.getText().toString().isEmpty()) {
                mHliTextLayout.setError(getResources().getString(R.string.is_empty_text_hli));
                //mToast.show(getResources().getString(R.string.is_empty_text_HLI), null);
                return;
            }
            mTimerData.setHliString(mHliEditText.getText().toString());
            mTimerData.setGradientId(mGradientViewGroup.getCurrentGradient().getId());
            if (isChangeStatus) {
                MainApplication.getInstance().getDbSession().getTimerDao().update(mTimerData);
                mOnAddOrUpdateDataListener.onUpdate(mTimerData.getId());
            } else {
                Long newTimerId = MainApplication.getInstance().getDbSession().getTimerDao().insert(mTimerData);
                mOnAddOrUpdateDataListener.onAdd(newTimerId);
            }
//            getActivity().onBackPressed();
        }
    };

    private View.OnClickListener dateTextViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideSoftKeyboard(v);
            DateTime dateTime = new DateTime(mTimerData.getStartDateTime());
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    DateTime oldDateTime = new DateTime(mTimerData.getStartDateTime());
                    mTimerData.setStartDateTime(new DateTime(year, monthOfYear + 1, dayOfMonth, oldDateTime.getHourOfDay(),
                            oldDateTime.getMinuteOfHour(), oldDateTime.getSecondOfMinute()).getMillis());
                    updateDateEditText();
                }
            }, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth()).show();
        }
    };

    private View.OnClickListener timeTextViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideSoftKeyboard(v);
            //TODO:формат отображения диалогов и текста в edittext
            DateTime dateTime = new DateTime(mTimerData.getStartDateTime());
            new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    DateTime oldDateTime = new DateTime(mTimerData.getStartDateTime());
                    mTimerData.setStartDateTime(new DateTime(oldDateTime.getYear(), oldDateTime.getMonthOfYear(), oldDateTime.getDayOfMonth(), hourOfDay,
                            minute, oldDateTime.getSecondOfMinute()).getMillis());
                    updateTimeEditText();
                }
            }, dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), true).show();
        }
    };

    public interface OnAddOrUpdateDataListener{
        void onUpdate(Long timerId);
        void onAdd(Long timerId);
    }
}
