package com.disenkoart.howlongi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.customView.timersView.FullTimersView;
import com.disenkoart.howlongi.database.Timer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 14.09.2016.
 */
public class TimersAdapter extends RecyclerView.Adapter<TimersAdapter.TimerViewHolder> {


    private ArrayList<Timer> mTimersData;

    public TimersAdapter(ArrayList<Timer> timersData){
        mTimersData = timersData;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timers_layout, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimerViewHolder holder, int position) {
        holder.fullTimersView.setData(mTimersData.get(position));
    }

    @Override
    public int getItemCount() {
        return mTimersData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class TimerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.timers_view)
        FullTimersView fullTimersView;
        public TimerViewHolder(View view) {
            super(view);
            ButterKnife.bind(fullTimersView);
        }
    }
}
