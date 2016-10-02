package com.disenkoart.howlongi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.customView.timersView.ArchiveTimerView;
import com.disenkoart.howlongi.database.Timer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 30.09.2016.
 */
public class ArchiveTimersAdapter extends RecyclerView.Adapter<ArchiveTimersAdapter.ArchiveTimersViewHolder>{

    private ArrayList<Timer> mTimersData;
    private OnDeleteButtonClickListener mOnDeleteButtonClickListener;
    private OnResetButtonClickListener mOnResetButtonClickListener;

    public ArchiveTimersAdapter(ArrayList<Timer> timersData){
        mTimersData = timersData;
    }

    @Override
    public ArchiveTimersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_view, parent, false);
        return new ArchiveTimersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArchiveTimersViewHolder holder, int position) {
        holder.archiveTimerView.setTimerData(mTimersData.get(position));
        holder.archiveTimerView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTimersData.size();
    }

    public void addTimerData(Timer timer, int position) {
        if (position == -1){
            position = mTimersData.size();
        }
        mTimersData.add(position, timer);
        notifyItemInserted(position);
    }

    public class ArchiveTimersViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.archive_timer_view)
        ArchiveTimerView archiveTimerView;

        public ArchiveTimersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            archiveTimerView.setDeleteButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int timerPosition = (int) ((ViewGroup) v.getParent()).getTag();
                    if (mOnDeleteButtonClickListener!= null) {
                        mOnDeleteButtonClickListener.onClick(mTimersData.get(timerPosition), timerPosition);
                    }
                    mTimersData.remove(timerPosition);
                    notifyItemRemoved(timerPosition);
                }
            });

            archiveTimerView.setResetButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int timerPosition = (int) ((ViewGroup) v.getParent()).getTag();
                    if (mOnResetButtonClickListener != null){
                        mOnResetButtonClickListener.onClick(mTimersData.get(timerPosition));
                    }
                    mTimersData.remove(timerPosition);
                    notifyItemRemoved(timerPosition);
                }
            });
        }
    }

    public interface OnDeleteButtonClickListener {
        void onClick(Timer timerData, int position);
    }


    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        mOnDeleteButtonClickListener = listener;
    }

    public interface OnResetButtonClickListener{
        void onClick(Timer timerData);
    }

    public void setOnResetButtonClickListener(OnResetButtonClickListener listener){
        mOnResetButtonClickListener = listener;
    }


}
