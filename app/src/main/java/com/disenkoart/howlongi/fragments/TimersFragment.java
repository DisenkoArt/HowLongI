package com.disenkoart.howlongi.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.MainActivity;
import com.disenkoart.howlongi.MainApplication;
import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.adapters.TimersAdapter;
import com.disenkoart.howlongi.customView.EmptyView;
import com.disenkoart.howlongi.customView.StyleSnackBar;
import com.disenkoart.howlongi.database.Timer;
import com.disenkoart.howlongi.database.TimerDao;

import java.util.ArrayList;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Артём on 14.09.2016.
 */
public class TimersFragment extends Fragment {

    private ArrayList<Timer> mTimerData;

    @BindView(R.id.recycler_view_main_frag)
    RecyclerView recyclerView;

    @BindView(R.id.add_timer_fab)
    FloatingActionButton addButton;

    @BindView(R.id.main_view_empty_view)
    EmptyView mEmptyView;

    private java.util.Timer mAnimTimer;
    private Handler mUpdateTimeHandler = new Handler();
    private TimerTask mUpdateTimerTask;
    private static final long INTERVAL_UPDATE_VIEW = 1000;

    private OnChangeTimerDataListener mChangeTimerDataListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        mEmptyView.setText(getResources().getString(R.string.main_view_is_empty));
        mTimerData = (ArrayList<Timer>) MainApplication.getInstance().getDbSession().getTimerDao().queryBuilder().
                where(TimerDao.Properties.IsArchived.eq(0)).list();
        setRecyclerView();
        changeVisibleEmptyTextView();
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) addButton.getLayoutParams();
//            p.setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
//            addButton.setLayoutParams(p);
//        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mChangeTimerDataListener = (OnChangeTimerDataListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @OnClick(R.id.add_timer_fab)
    public void onClick(View v) {
      //  saveFragmentArgs();
        ((MainActivity) getActivity()).changeFragment(AddTimerFragment.class, null, true);
    }

    private void setRecyclerView(){
        TimersAdapter timersAdapter = new TimersAdapter(mTimerData, null);
        timersAdapter.setOnChangeButtonClickListener(new TimersAdapter.OnChangeButtonClickListener() {
            @Override
            public void onClick(Long timerId) {
                mChangeTimerDataListener.changeTimerData(timerId);
            }
        });
//            mTimersAdapter.setOnSendButtonClickListener(onSendButtonClickListener);
//            mTimersAdapter.setOnChangeButtonClickListener(changeButtonClickListener);
//            mTimersAdapter.setOnDeleteButtonClickListener(onDeleteButtonClickListener);
        timersAdapter.setOnChangeIndexOfOpenPanelListener(onChangeIndexOfOpenPanelListener);
        timersAdapter.setOnDeleteButtonClickListener(onDeleteButtonClickListener);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(timersAdapter);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int)(getResources().getDimension(R.dimen.card_view_vertical_space) * scale + 0.5f)));
        scheduleTimerUpdate();
    }

    private void changeVisibleEmptyTextView(){
        if (recyclerView.getAdapter().getItemCount() == 0){
            mEmptyView.setVisibility(View.VISIBLE);
//            mAddButton.setAlpha(1f);
//            mAddButton.setClickable(true);
            return;
        } else {
            mEmptyView.setVisibility(View.INVISIBLE);
        }
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
        if (recyclerView.getChildCount() == 0)
            return;
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
        if (recyclerView.getChildCount() == 0)
            return;
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
        if (recyclerView.getChildCount() == 0)
            return;
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

    public void updateTimerData(Long timerId){
//        ((TimersAdapter.TimerViewHolder) recyclerView.findViewHolderForAdapterPosition(position)).stopUpdate();
        ((TimersAdapter) recyclerView.getAdapter()).updateTimerData(timerId);
    }

    public void addTimerData(Long timerId){
        ((TimersAdapter) recyclerView.getAdapter()).addTimerData(MainApplication.getInstance().getDbSession().getTimerDao().load(timerId), -1);
        changeVisibleEmptyTextView();
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
            if (recyclerView.getChildCount() == 0)
                return;
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

    TimersAdapter.OnDeleteButtonClickListener onDeleteButtonClickListener = new TimersAdapter.OnDeleteButtonClickListener() {
        @Override
        public void onClick(Timer timerData, int position) {
            //MainApplication.getInstance().getDbSession().getTimerDao().deleteByKey(timerId);
            timerData.setIsArchived(1);
            MainApplication.getInstance().getDbSession().getTimerDao().update(timerData);
            changeVisibleEmptyTextView();
            Snackbar snackbar = StyleSnackBar.createSnackBar(getView(), getResources().getString(R.string.undo_title), Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo_button, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Long id = (Long) ((View) v.getParent()).getTag(R.id.timer_id);
                            int index = (int) ((View) v.getParent()).getTag(R.id.position);
                            Timer timer = MainApplication.getInstance().getDbSession().getTimerDao().queryBuilder().
                            where(TimerDao.Properties.Id.eq(id)).unique();
                            timer.setIsArchived(0);
                            MainApplication.getInstance().getDbSession().getTimerDao().update(timer);
                            ((TimersAdapter) recyclerView.getAdapter()).addTimerData(timer, index);
                            changeVisibleEmptyTextView();
                            if (index == 0 || recyclerView.getAdapter().getItemCount() == index + 1)
                                recyclerView.scrollToPosition(index);
                        }
                    });
            snackbar.getView().setTag(R.id.timer_id, timerData.getId());
            snackbar.getView().setTag(R.id.position, position);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorError));
            snackbar.show();
            changeVisibleEmptyTextView();
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

    public interface OnChangeTimerDataListener {
        void changeTimerData(Long timerId);
    }

}
