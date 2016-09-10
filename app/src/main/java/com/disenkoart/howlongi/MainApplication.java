package com.disenkoart.howlongi;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.disenkoart.howlongi.data.DateTimeUnits;
import com.disenkoart.howlongi.database.DBHelper;
import com.disenkoart.howlongi.database.DaoSession;
import com.disenkoart.howlongi.database.GradientDao;

import java.util.List;

/**
 * Created by Артём on 16.08.2016.
 */
public class MainApplication extends Application {

    private static MainApplication instance;
    private DaoSession dbSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FontManager.init();
        DateTimeUnits.setResources(getResources());
        DBHelper dbHelper = new DBHelper();
        dbSession = dbHelper.getSession(false);
        GradientDao gradient =  dbSession.getGradientDao();
        List gradientList = gradient.loadAll();
        Log.d("Count: ", String.valueOf(gradientList.size()));
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public DaoSession getDbSession(){
        return dbSession;
    }

}
