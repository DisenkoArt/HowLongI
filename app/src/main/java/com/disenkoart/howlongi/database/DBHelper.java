package com.disenkoart.howlongi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import com.disenkoart.howlongi.MainApplication;

/**
 * Created by Артём on 10.09.2016.
 */
public class DBHelper {
    private SQLiteDatabase mDb = null;
    private DaoSession mSession = null;

    private static final String DB_NAME = "howlongi";
    private String TAG = getClass().getName();

    private DaoMaster getMaster(){
        if (mDb == null){
            mDb = getDatabase(DB_NAME, false);
        }
        return new DaoMaster(mDb);
    }

    public DaoSession getSession(boolean newSession) {
        if (newSession) {
            return getMaster().newSession();
        }
        if (mSession == null) {
            mSession = getMaster().newSession();
        }
        return mSession;
    }

    @Nullable
    private synchronized SQLiteDatabase getDatabase(String name, boolean readOnly) {
        try {
            readOnly = false;
            if (readOnly) {
                Log.i(TAG, "getDB("+name+",readonly=true)");
            } else {
                Log.i(TAG, "getDB("+name+",readonly=false)");
            }

            SQLiteOpenHelper helper = new MyOpenHelper(MainApplication.getInstance().getApplicationContext(), name, null);
            if (readOnly) {
                return helper.getReadableDatabase();
            } else {
                return helper.getWritableDatabase();
            }
        } catch (Exception ex) {
            if (readOnly) {
                Log.e(TAG, "getDB("+name+",readonly=true)", ex);
            } else {
                Log.e(TAG, "getDB("+name+",readonly=false)", ex);
            }
            return null;
        } catch (Error err) {
            if (readOnly) {
                Log.e(TAG, "getDB("+name+",readonly=true)", err);
            } else {
                Log.e(TAG, "getDB("+name+",readonly=false)", err);
            }
            return null;
        }
    }

    private class MyOpenHelper extends DaoMaster.OpenHelper {

        private static final String GRADIENTS_TABLE = "GRADIENT";
        public static final String START_COLOR = "START_COLOR";
        public static final String END_COLOR = "END_COLOR";

        public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Create DB-Schema (version " + Integer.toString(DaoMaster.SCHEMA_VERSION) + ")");
            super.onCreate(db);
            setGradientDB(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Update DB-Schema to version: " + Integer.toString(oldVersion) + "->" + Integer.toString(newVersion));
//            switch (oldVersion) {
//                case 1:
//                    db.execSQL(SQL_UPGRADE_1To2);
//                case 2:
//                    db.execSQL(SQL_UPGRADE_2To3);
//                    break;
//                default:
//                    break;
//            }
        }

        private void setGradientDB(SQLiteDatabase db) {
            //1
            addGradient(db, Color.argb(255, 102, 45, 145), Color.argb(255, 239, 65, 54));
            //2
            addGradient(db, Color.argb(255, 242, 204, 71), Color.argb(255, 239, 54, 54));
            //3
            addGradient(db, Color.argb(255, 201, 224, 64), Color.argb(255, 50, 189, 61));
            //4
            addGradient(db, Color.argb(255, 241, 239, 165), Color.argb(255, 52, 184, 135));
            //5
            addGradient(db, Color.argb(255, 8, 232, 191), Color.argb(255, 35, 94, 149));
            //6
            addGradient(db, Color.argb(255, 255, 66, 76), Color.argb(255, 184, 97, 245));
            //7
            addGradient(db, Color.argb(255, 219, 58, 126), Color.argb(255, 255, 217, 76));
            //8
            addGradient(db, Color.argb(255, 250, 235, 84), Color.argb(255, 130, 219, 33));
            //9
            addGradient(db, Color.argb(255, 0, 186, 223), Color.argb(255, 239, 224, 161));
            //10
            addGradient(db, Color.argb(255, 30, 151, 208), Color.argb(255, 204, 40, 228));
            //11
            addGradient(db, Color.argb(255, 255, 150, 124), Color.argb(255, 219, 66, 89));
            //12
            addGradient(db, Color.argb(255, 255, 255, 166), Color.argb(255, 255, 191, 38));
            //13
            addGradient(db, Color.argb(255, 252, 250, 33), Color.argb(255, 31, 143, 196));
            //14
            addGradient(db, Color.argb(255, 255, 219, 171), Color.argb(255, 105, 74, 252));
            //15
            addGradient(db, Color.argb(255, 255, 89, 153), Color.argb(255, 30, 151, 208));
//        //16
//        addGradient(Color.argb(255, 94, 87, 148), Color.argb(255, 255, 107, 176));
//        //17
//        addGradient(Color.argb(255, 227, 173, 89), Color.argb(255, 214, 64, 138));
//        //18
//        addGradient(Color.argb(255, 255, 142, 69), Color.argb(255, 130, 184, 120));
//        //19
//        addGradient(Color.argb(255, 255, 142, 69), Color.argb(255, 130, 184, 120));
//        //20
//        addGradient(Color.argb(255, 255, 142, 69), Color.argb(255, 130, 184, 120));
        }

        /**
         * Добавляет градиент в БД.
         * @param db БД.
         * @param startColor Начальный цвет градиента.
         * @param finishColor Конечный цвет градиента.
         */
        private void addGradient(SQLiteDatabase db, int startColor, int finishColor) {
            ContentValues cv = new ContentValues();
            cv.put(START_COLOR, startColor);
            cv.put(END_COLOR, finishColor);
            db.insert(GRADIENTS_TABLE, null, cv);
        }
    }
}
