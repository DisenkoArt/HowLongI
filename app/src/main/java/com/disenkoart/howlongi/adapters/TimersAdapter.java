package com.disenkoart.howlongi.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.customView.timersView.FullTimersView;
import com.disenkoart.howlongi.customView.timersView.TimersView;
import com.disenkoart.howlongi.database.Timer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 14.09.2016.
 */
public class TimersAdapter extends RecyclerView.Adapter<TimersAdapter.TimerViewHolder> {

    private int mIndexOfOpenPanel = -1;
    private ArrayList<Integer> mSelectedTimerIndex = new ArrayList<>();
    private ArrayList<Timer> mTimersData;

    public static final String INDEX_OF_OPEN_LEFT_PANEL_KEY = "INDEX_OF_OPEN_LEFT_PANEL_KEY";
    public static final String INDEX_SELECT_PROGRESS_BAR_LIST_KEY = "INDEX_SELECT_PROGRESS_BAR_LIST_KEY";

    public TimersAdapter(ArrayList<Timer> timersData, Bundle arguments) {
        mTimersData = timersData;
        if (arguments != null) {
            mIndexOfOpenPanel = arguments.getInt(INDEX_OF_OPEN_LEFT_PANEL_KEY);
            mSelectedTimerIndex = (ArrayList<Integer>) arguments.getSerializable(INDEX_SELECT_PROGRESS_BAR_LIST_KEY);
        }
        while (mSelectedTimerIndex.size() < timersData.size()) {
            mSelectedTimerIndex.add(-1);
        }
    }

    OnChangeButtonClickListener mOnChangeButtonClickListener;
    OnChangeIndexOfOpenPanelListener mOnChangeIndexOfOpenPanelListener;
    OnDeleteButtonClickListener mOnDeleteButtonClickListener;

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timers_view, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimerViewHolder holder, int position) {
        holder.fullTimersView.setData(mTimersData.get(position));
        holder.fullTimersView.setTag(position);
        holder.fullTimersView.setSelectedTimerIndex(mSelectedTimerIndex.get(position));
        holder.fullTimersView.setOpenedLeftPanel(mIndexOfOpenPanel == position, false);
        //holder.fullTimersView.setSendButtonOnClickListener(sendButtonOnClickListener);
    }


    @Override
    public int getItemCount() {
        return mTimersData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    FullTimersView.OnChangeOpenStatusListener onChangeOpenStatusListener = new FullTimersView.OnChangeOpenStatusListener() {
        @Override
        public void onChangeOpenStatus(View v, boolean isOpen) {
            int previousPosition = mIndexOfOpenPanel;
            mIndexOfOpenPanel = isOpen ? (Integer) ((ViewGroup) v.getParent()).getTag() : -1;
            if (mOnChangeIndexOfOpenPanelListener != null) {
                mOnChangeIndexOfOpenPanelListener.onChangeIndex(previousPosition);
            }
        }
    };

    TimersView.OnChangeSelectedTimerListener onChangeSelectedTimerListener = new TimersView.OnChangeSelectedTimerListener() {
        @Override
        public void onChangeSelectedTimerIndex(View v, int selectedTimerIndex) {
            mSelectedTimerIndex.set((Integer) ((ViewGroup) v.getParent().getParent()).getTag(), selectedTimerIndex);
        }
    };

    public void updateTimerData(Long timerId) {
        int position = 0;
        for (int i = 0; i < mTimersData.size(); i++) {
            Timer timer = mTimersData.get(i);
            if (timer.getId().equals(timerId)) {
                position = i;
            }
        }
        notifyItemChanged(position);
    }

    public void addTimerData(Timer timer, int position) {
        if (position == -1){
            position = mTimersData.size();
        }
        mTimersData.add(position, timer);
        mSelectedTimerIndex.add(-1);
        notifyItemInserted(position);
    }

     /* -- START INTERFACES -- */

    public interface OnDeleteButtonClickListener {
        void onClick(Timer timerData, int position);
    }


    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        mOnDeleteButtonClickListener = listener;
    }

    public static interface OnChangeButtonClickListener {
        public void onClick(Long timerId);
    }

    public void setOnChangeButtonClickListener(OnChangeButtonClickListener listener) {
        mOnChangeButtonClickListener = listener;
    }

    public static interface OnChangeIndexOfOpenPanelListener {
        public void onChangeIndex(int position);
    }

    public void setOnChangeIndexOfOpenPanelListener(OnChangeIndexOfOpenPanelListener listener) {
        mOnChangeIndexOfOpenPanelListener = listener;
    }

    /* -- END INTERFACES -- */

    public class TimerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timers_view)
        FullTimersView fullTimersView;

        public TimerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            fullTimersView.setChangeOpenStatusListener(onChangeOpenStatusListener);
            fullTimersView.setChangeSelectedTimerIndex(onChangeSelectedTimerListener);
            fullTimersView.setOnChangeButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int timerPosition = (int) ((ViewGroup) v.getParent().getParent()).getTag();
                    if (mOnChangeButtonClickListener != null) {
                        mOnChangeButtonClickListener.onClick(mTimersData.get(timerPosition).getId());
                    }
                }
            });
            fullTimersView.setOnDeleteButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int timerPosition = (int) ((ViewGroup) v.getParent().getParent()).getTag();
                    Timer deletedTimer = mTimersData.get(timerPosition);
                    mTimersData.remove(timerPosition);
                    notifyItemRemoved(timerPosition);
                    mIndexOfOpenPanel = -1;
                    if (mOnDeleteButtonClickListener != null) {
                        mOnDeleteButtonClickListener.onClick(deletedTimer, timerPosition);
                    }
                }
            });
        }

        public void invalidate() {
            fullTimersView.invalidateProgressBar();
        }

        public void stopUpdate() {
            fullTimersView.onStopUpdate();
        }

        public void startUpdate() {
            fullTimersView.onStartUpdate();
        }

        public void setStatusLeftPanel(boolean isOpen) {
            fullTimersView.setOpenedLeftPanel(isOpen, true);
        }
    }


}
