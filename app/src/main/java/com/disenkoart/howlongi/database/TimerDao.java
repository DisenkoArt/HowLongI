package com.disenkoart.howlongi.database;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TIMER".
*/
public class TimerDao extends AbstractDao<Timer, Long> {

    public static final String TABLENAME = "TIMER";

    /**
     * Properties of entity Timer.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HliString = new Property(1, String.class, "hliString", false, "HLI_STRING");
        public final static Property StartDateTime = new Property(2, long.class, "startDateTime", false, "START_DATE_TIME");
        public final static Property IsArchived = new Property(3, int.class, "isArchived", false, "IS_ARCHIVED");
        public final static Property GradientId = new Property(4, long.class, "gradientId", false, "GRADIENT_ID");
    }

    private DaoSession daoSession;


    public TimerDao(DaoConfig config) {
        super(config);
    }
    
    public TimerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TIMER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HLI_STRING\" TEXT NOT NULL ," + // 1: hliString
                "\"START_DATE_TIME\" INTEGER NOT NULL ," + // 2: startDateTime
                "\"IS_ARCHIVED\" INTEGER NOT NULL ," + // 3: isArchived
                "\"GRADIENT_ID\" INTEGER NOT NULL );"); // 4: gradientId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TIMER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Timer entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getHliString());
        stmt.bindLong(3, entity.getStartDateTime());
        stmt.bindLong(4, entity.getIsArchived());
        stmt.bindLong(5, entity.getGradientId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Timer entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getHliString());
        stmt.bindLong(3, entity.getStartDateTime());
        stmt.bindLong(4, entity.getIsArchived());
        stmt.bindLong(5, entity.getGradientId());
    }

    @Override
    protected final void attachEntity(Timer entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Timer readEntity(Cursor cursor, int offset) {
        Timer entity = new Timer( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // hliString
            cursor.getLong(offset + 2), // startDateTime
            cursor.getInt(offset + 3), // isArchived
            cursor.getLong(offset + 4) // gradientId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Timer entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHliString(cursor.getString(offset + 1));
        entity.setStartDateTime(cursor.getLong(offset + 2));
        entity.setIsArchived(cursor.getInt(offset + 3));
        entity.setGradientId(cursor.getLong(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Timer entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Timer entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Timer entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getGradientDao().getAllColumns());
            builder.append(" FROM TIMER T");
            builder.append(" LEFT JOIN GRADIENT T0 ON T.\"GRADIENT_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Timer loadCurrentDeep(Cursor cursor, boolean lock) {
        Timer entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Gradient gradient = loadCurrentOther(daoSession.getGradientDao(), cursor, offset);
         if(gradient != null) {
            entity.setGradient(gradient);
        }

        return entity;    
    }

    public Timer loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Timer> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Timer> list = new ArrayList<Timer>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Timer> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Timer> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
