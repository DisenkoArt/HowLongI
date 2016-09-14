package com.disenkoart.howlongi.fragments;

import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 14.09.2016.
 */
public class TimersFragment extends Fragment {

    private ArrayList<Timer> mTimerData;

    @BindView(R.id.recycler_view_main_frag)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(view);
        mTimerData = getArguments().getParcelableArrayList(Timer.class.getCanonicalName());
        setRecyclerView();
        return view;
    }

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        TimersAdapter timersAdapter = new TimersAdapter(mTimerData);
        recyclerView.setAdapter(timersAdapter);
    }
}
