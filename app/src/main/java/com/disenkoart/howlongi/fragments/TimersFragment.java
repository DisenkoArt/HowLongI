package com.disenkoart.howlongi.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.adapters.TimersAdapter;
import com.disenkoart.howlongi.database.Timer;

import java.util.ArrayList;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 14.09.2016.
 */
public class TimersFragment extends Fragment {

    private ArrayList<Timer> mTimerData;

    @BindView(R.id.recycler_view_main_frag)
    RecyclerView recyclerView;

    private java.util.Timer mAnimTimer;
    private Handler mUpdateTimeHandler = new Handler();
    private TimerTask mUpdateTimerTask;
    private static final long INTERVAL_UPDATE_VIEW = 1000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        mTimerData = getArguments().getParcelableArrayList(Timer.class.getCanonicalName());
        setRecyclerView();
        return view;
    }

    private void setRecyclerView(){
        mTimerData = getArguments().getParcelableArrayList(Timer.class.getCanonicalName());
        TimersAdapter timersAdapter = new TimersAdapter(mTimerData, null);
//            mTimersAdapter.setOnSendButtonClickListener(onSendButtonClickListener);
//            mTimersAdapter.setOnChangeButtonClickListener(changeButtonClickListener);
//            mTimersAdapter.setOnDeleteButtonClickListener(onDeleteButtonClickListener);
        timersAdapter.setOnChangeIndexOfOpenPanelListener(onChangeIndexOfOpenPanelListener);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(timersAdapter);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int)(getResources().getDimension(R.dimen.card_view_vertical_space) * scale + 0.5f)));
        scheduleTimerUpdate();
    }


    private void scheduleTimerUpdate() {
        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
        }
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                mUpdateTimeHandler.post(updateTimerViewRunnable);
            }
        };
        mAnimTimer = new java.util.Timer();
        mAnimTimer.schedule(mUpdateTimerTask, 0, INTERVAL_UPDATE_VIEW);
    }

    private final Runnable updateTimerViewRunnable = new Runnable() {
        @Override
        public void run() {
            if (recyclerView.getChildCount() != 0){
                updateTimersProgress();
            }
        }
    };

    private void updateTimersProgress(){
        GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        for (int i = 0; i <= last - first; i++){
            TimersAdapter.TimerViewHolder viewHolder = (TimersAdapter.TimerViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (viewHolder != null)
                viewHolder.invalidate();
        }
    }

    private void stopRun(){
        if (mAnimTimer != null){
            mAnimTimer.cancel();
            mAnimTimer = null;
        }
        GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        for (int i = 0; i <= last - first; i++){
            TimersAdapter.TimerViewHolder viewHolder = (TimersAdapter.TimerViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (viewHolder != null)
                viewHolder.stopUpdate();
        }
    }

    public void startRun(){
        scheduleTimerUpdate();
        GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        if (first < 0 || last <0)
            return;
        for (int i = 0; i <= last - first; i++){
            TimersAdapter.TimerViewHolder viewHolder = (TimersAdapter.TimerViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (viewHolder != null)
                viewHolder.startUpdate();
        }
    }

    /* -- START OVERRIDE METHODS -- */

    @Override
    public void onStop() {
        //TODO:
        stopRun();
        super.onStop();
    }

//    @Override
//    public void onPause() {
//        stopRun();
//        super.onPause();
//    }

    @Override
    public void onStart() {
        super.onStart();
        startRun();
    }

    /* -- END OVERRIDE METHODS -- */

    TimersAdapter.OnChangeIndexOfOpenPanelListener onChangeIndexOfOpenPanelListener = new TimersAdapter.OnChangeIndexOfOpenPanelListener() {
        @Override
        public void onChangeIndex(int position) {
            GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
            int first = layoutManager.findFirstVisibleItemPosition();
            int last = layoutManager.findLastVisibleItemPosition();
            if (position < 0)
                return;

            if (position < first || position > last ){
                recyclerView.getAdapter().notifyItemChanged(position);
            } else {
                TimersAdapter.TimerViewHolder viewHolder = (TimersAdapter.TimerViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                viewHolder.setStatusLeftPanel(false);
            }
//            TimersAdapter.TimerViewHolder viewHolder =
//                    (TimersAdapter.TimerViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
//            if (viewHolder != null) {
//                viewHolder.setStatusLeftPanel(false);
//            }


        }
    };

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{
        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mVerticalSpaceHeight;
            } else {
                outRect.bottom = mVerticalSpaceHeight/2;
            }
            if (position == 0){
                outRect.top = mVerticalSpaceHeight/2;
            }
        }
    }
}
