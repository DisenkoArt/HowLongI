package com.disenkoart.howlongi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.disenkoart.howlongi.database.DaoSession;
import com.disenkoart.howlongi.fragments.AboutFragment;
import com.disenkoart.howlongi.fragments.AddTimerFragment;
import com.disenkoart.howlongi.fragments.ArchiveFragment;
import com.disenkoart.howlongi.fragments.TimersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimersFragment.OnChangeTimerDataListener, AddTimerFragment.OnAddOrUpdateDataListener{


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    DaoSession mDbSession;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDbSession = MainApplication.getInstance().getDbSession();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mNavigationDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                if(backStackEntryCount > 0) {
                    InputMethodManager keyboard = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    onBackPressed();
                }else if (mNavigationDrawer.isDrawerOpen(GravityCompat.START))
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                else
                    mNavigationDrawer.openDrawer(GravityCompat.START);
            }
        });
        mNavigationDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null)
        {
            navigationView.getMenu().performIdentifierAction(R.id.hli_menu_item, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            mNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
        mDrawerToggle.syncState();
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
        Bundle bundle = new Bundle();
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.hli_menu_item:
                fragmentClass = TimersFragment.class;
                break;
            case R.id.archive_menu_item:
                fragmentClass = ArchiveFragment.class;
                break;
            case R.id.about_menu_item:
                fragmentClass = AboutFragment.class;
                break;
        }
        changeFragment(fragmentClass, bundle, false);
    }

    public void changeFragment(Class fragmentClass, Bundle arguments, boolean isBackStack){
        try {
            assert fragmentClass != null;
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (isBackStack){
                transaction.add(R.id.frame_content, fragment).addToBackStack(null).commit();
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                mNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            } else {
                transaction.replace(R.id.frame_content, fragment).commit();
                mNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
            mDrawerToggle.syncState();


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void changeTimerData(Long timerId) {
        Bundle bundle = new Bundle();
        bundle.putLong(AddTimerFragment.TIMER_ID, timerId);
        changeFragment(AddTimerFragment.class, bundle, true);
    }

    @Override
    public void onUpdate(Long timerId) {
        onBackPressed();
        TimersFragment fragment = (TimersFragment) getSupportFragmentManager().findFragmentById(R.id.frame_content);
        fragment.updateTimerData(timerId);
//        getSupportFragmentManager().getFragments()
    }

    @Override
    public void onAdd(Long timerId) {
        onBackPressed();
        TimersFragment fragment = (TimersFragment) getSupportFragmentManager().findFragmentById(R.id.frame_content);
        fragment.addTimerData(timerId);
    }
}
