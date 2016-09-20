package com.disenkoart.howlongi;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.disenkoart.howlongi.database.DaoSession;
import com.disenkoart.howlongi.database.Timer;
import com.disenkoart.howlongi.fragments.AboutFragment;
import com.disenkoart.howlongi.fragments.TestFragment;
import com.disenkoart.howlongi.fragments.TimersFragment;

import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    DaoSession mDbSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDbSession = MainApplication.getInstance().getDbSession();
        setSupportActionBar(toolbar);
        mDbSession.getTimerDao().deleteAll();
        for (int i = 0; i < 7; i++){
            Timer timerins = new Timer();
            timerins.setHliString("HOW LONG I " + i);
            Log.d(getLocalClassName(), timerins.getHliString());
            timerins.setGradientId(i + 1);
            timerins.setStartDateTime(DateTime.now().minusHours(i + 2).getMillis());
            timerins.setIsArchived(0);
            mDbSession.getTimerDao().insert(timerins);
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selectDrawerItem(item);
        item.setChecked(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectDrawerItem(MenuItem menuItem){
        Fragment fragment;
        Bundle bundle = new Bundle();
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.hli_menu_item:
                fragmentClass = TimersFragment.class;
                bundle.putParcelableArrayList(Timer.class.getCanonicalName(), (ArrayList<Timer>) mDbSession.getTimerDao().loadAll());
                break;
            case R.id.archive_menu_item:
                break;
            case R.id.about_menu_item:
                fragmentClass = AboutFragment.class;
                break;
        }
        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
