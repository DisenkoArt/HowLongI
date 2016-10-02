package com.disenkoart.howlongi.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.MainApplication;
import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.adapters.ArchiveTimersAdapter;
import com.disenkoart.howlongi.adapters.TimersAdapter;
import com.disenkoart.howlongi.customView.EmptyView;
import com.disenkoart.howlongi.customView.StyleSnackBar;
import com.disenkoart.howlongi.database.Timer;
import com.disenkoart.howlongi.database.TimerDao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 01.10.2016.
 */
public class ArchiveFragment extends Fragment {

    private ArrayList<Timer> mTimerData;

    @BindView(R.id.archive_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.archive_view_empty_view)
    EmptyView mEmptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.archive_fragment, container, false);
        ButterKnife.bind(this, view);
        mEmptyView.setText(getResources().getString(R.string.archive_is_empty_string));
        mTimerData = (ArrayList<Timer>) MainApplication.getInstance().getDbSession().getTimerDao().queryBuilder().
                where(TimerDao.Properties.IsArchived.eq(1)).list();
        setRecyclerView();
        changeVisibleEmptyTextView();
        return view;
    }

    private void setRecyclerView(){
        ArchiveTimersAdapter adapter = new ArchiveTimersAdapter(mTimerData);
        adapter.setOnDeleteButtonClickListener(mOnDeleteButtonClickListener);
        adapter.setOnResetButtonClickListener(mOnResetButtonClickListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int)(getResources().getDimension(R.dimen.card_view_vertical_space) * scale + 0.5f)));
    }

    private void changeVisibleEmptyTextView(){
        if (mRecyclerView.getAdapter().getItemCount() == 0){
            mEmptyView.setVisibility(View.VISIBLE);
//            mAddButton.setAlpha(1f);
//            mAddButton.setClickable(true);
            return;
        } else {
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }

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

    ArchiveTimersAdapter.OnDeleteButtonClickListener mOnDeleteButtonClickListener = new ArchiveTimersAdapter.OnDeleteButtonClickListener() {
        @Override
        public void onClick(Timer timerData, int position) {
            MainApplication.getInstance().getDbSession().getTimerDao().deleteByKey(timerData.getId());
            changeVisibleEmptyTextView();
            Snackbar snackbar = StyleSnackBar.createSnackBar(getView(), getResources().getString(R.string.undo_title), Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo_button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hliText = (String) ((View) v.getParent()).getTag(R.id.hli_text);
                    Long dateTime = (Long) ((View) v.getParent()).getTag(R.id.date_time);
                    Long gradientId = (Long) ((View) v.getParent()).getTag(R.id.gradient_id);
                    int index = (int) ((View) v.getParent()).getTag(R.id.position);
                    Timer timer = new Timer();
                    timer.setHliString(hliText);
                    timer.setStartDateTime(dateTime);
                    timer.setGradientId(gradientId);
                    timer.setIsArchived(1);
                    MainApplication.getInstance().getDbSession().getTimerDao().insert(timer);
                    ((ArchiveTimersAdapter) mRecyclerView.getAdapter()).addTimerData(timer, index);
                    changeVisibleEmptyTextView();
                    if (index == 0 || mRecyclerView.getAdapter().getItemCount() == index + 1)
                        mRecyclerView.scrollToPosition(index);
                }
            });
            snackbar.getView().setTag(R.id.position, position);
            snackbar.getView().setTag(R.id.hli_text, timerData.getHliString());
            snackbar.getView().setTag(R.id.date_time, timerData.getStartDateTime());
            snackbar.getView().setTag(R.id.gradient_id, timerData.getGradientId());
            snackbar.setActionTextColor(getResources().getColor(R.color.colorError));
            snackbar.show();
        }
    };

    ArchiveTimersAdapter.OnResetButtonClickListener mOnResetButtonClickListener = new ArchiveTimersAdapter.OnResetButtonClickListener() {
        @Override
        public void onClick(Timer timerData) {
            timerData.setIsArchived(0);
            MainApplication.getInstance().getDbSession().getTimerDao().update(timerData);
        }
    };
}
