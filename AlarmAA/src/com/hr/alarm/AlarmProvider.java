/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hr.alarm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class AlarmProvider extends ContentProvider {
    private SQLiteOpenHelper mOpenHelper;

    private static SQLiteDatabase sqLiteDatabase;
    private static final int ALARMS = 1;
    private static final int ALARMS_ID = 2;
    private static final UriMatcher sURLMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURLMatcher.addURI("com.hr.alarm", "alarm", ALARMS);
        sURLMatcher.addURI("com.hr.alarm", "alarm/#", ALARMS_ID);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "alarms.db";
        private static final int DATABASE_VERSION = 7;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE alarms (" +
                       "_id INTEGER PRIMARY KEY," +
                       "hour INTEGER, " +
                       "minutes INTEGER, " +
                       "daysofweek INTEGER, " +
                       "alarmtime INTEGER, " +
                       "enabled INTEGER, " +
                       "vibrate INTEGER, " +
                       "message TEXT, " +
                       "alert TEXT, "+
                       "snooze INTEGER, " +
                       "alarmtype TEXT,"+
                       "ringbackground TEXT,"+
                       "tone TEXT,"+
                       "vibrate_only INTEGER, " +
                       "volume INTEGER, " +
                       "alerttype TEXT,"+
                       "captcha_snooze INTEGER, "+
                       "captcha_dismiss INTEGER, "+
                       "note TEXT"+
                       
                      
                       "); ");

    
            /*
            // insert default alarms
            String insertMe = "INSERT INTO alarms " +
                    "(hour, minutes, daysofweek, alarmtime, enabled, vibrate, message, alert) " +
                    "VALUES ";
            db.execSQL(insertMe + "(7, 0, 127, 0, 0, 1, '', '');");
            db.execSQL(insertMe + "(8, 30, 31, 0, 0, 1, '', '');");
            db.execSQL(insertMe + "(9, 00, 0, 0, 0, 1, '', '');");
            */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
          if (oldVersion == 6) { //upgrading from alarmingv1
            db.execSQL("ALTER TABLE alarms ADD captcha_snooze INTEGER ");
            db.execSQL("ALTER TABLE alarms ADD captcha_dismiss INTEGER ");
            db.execSQL("UPDATE alarms SET message=name");
            db.execSQL("UPDATE alarms SET name=''");
          }
          else {
            if (Log.LOGV) Log.v(
                    "Upgrading alarms database from version " +
                    oldVersion + " to " + currentVersion +
                    ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS alarms");
            onCreate(db);
          }
        }
        public void open(){
    		sqLiteDatabase = getWritableDatabase();
    	}
        
        public void close(){
    		sqLiteDatabase.close();
    	}
        
        public void updateSetting(int alarmdays,int id)
		{
			ContentValues contentValues  = new ContentValues();
			
			
			contentValues.put("daysofweek", alarmdays);
			sqLiteDatabase.update("alarms", contentValues, "_id ="+id, null);
		}
        
        public void updateNewAlarm(int id,int hour,int minute, int alarmdays, int enable, int vib,String label,String ringtone, int snooze)
        {
        	ContentValues contentValues  = new ContentValues();
			
			
			contentValues.put("daysofweek", alarmdays);
			sqLiteDatabase.update("alarms", contentValues, "_id ="+id, null);
        }
        
        
        
        public Cursor getData(int id){
			Cursor dataCursor = sqLiteDatabase.rawQuery("SELECT hour,minutes,daysofweek,vibrate,message,alert,snooze,alarmtype,ringbackground,alerttype,note,alerttype FROM alarms WHERE _id ="+id+";",null);
		
			if(dataCursor != null){
				dataCursor.moveToFirst();
			}
			return dataCursor;
		}
        
        public Cursor getalldata()
        {
        	Cursor dataCursor=sqLiteDatabase.rawQuery("SELECT * FROM alarms", null);
        	if(dataCursor != null){
				dataCursor.moveToFirst();
			}
			return dataCursor;
        }
        
        public void updateData(int id,int hour,int minute, int alarmdays,Long time, int enable,int vib,int snooze,String lable,String note,String alarmtype,String tone,String alerttype, String ringback)
        {
        	ContentValues contentValues  = new ContentValues();
			
			contentValues.put("hour", hour);
			contentValues.put("minutes", minute);
			contentValues.put("daysofweek", alarmdays);
			contentValues.put("alarmtime", time);
			contentValues.put("enabled", enable);
			contentValues.put("vibrate", vib);
			contentValues.put("message", lable);
			contentValues.put("snooze", snooze);
			contentValues.put("ringbackground", ringback);
			contentValues.put("alarmtype", alarmtype);
			contentValues.put("tone", tone);
			contentValues.put("alerttype", alerttype);
			
			
			contentValues.put("note", note);
			sqLiteDatabase.update("alarms", contentValues, "_id ="+id, null);
        }
    }

    public AlarmProvider() {
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri url, String[] projectionIn, String selection,
            String[] selectionArgs, String sort) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        // Generate the body of the query
        int match = sURLMatcher.match(url);
        switch (match) {
            case ALARMS:
                qb.setTables("alarms");
                break;
            case ALARMS_ID:
                qb.setTables("alarms");
                qb.appendWhere("_id=");
                qb.appendWhere(url.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor ret = qb.query(db, projectionIn, selection, selectionArgs,
                              null, null, sort);

        if (ret == null) {
            if (Log.LOGV) Log.v("Alarms.query: failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), url);
        }

        return ret;
    }

    @Override
    public String getType(Uri url) {
        int match = sURLMatcher.match(url);
        switch (match) {
            case ALARMS:
                return "vnd.android.cursor.dir/alarms";
            case ALARMS_ID:
                return "vnd.android.cursor.item/alarms";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    @Override
    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int count;
        long rowId = 0;
        int match = sURLMatcher.match(url);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (match) {
            case ALARMS_ID: {
                String segment = url.getPathSegments().get(1);
                rowId = Long.parseLong(segment);
                count = db.update("alarms", values, "_id=" + rowId, null);
                break;
            }
            default: {
                throw new UnsupportedOperationException(
                        "Cannot update URL: " + url);
            }
        }
        if (Log.LOGV) Log.v("*** notifyChange() rowId: " + rowId + " url " + url);
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }

    @Override
    public Uri insert(Uri url, ContentValues initialValues) {
        if (sURLMatcher.match(url) != ALARMS) {
            throw new IllegalArgumentException("Cannot insert into URL: " + url);
        }

        ContentValues values;
        if (initialValues != null)
            values = new ContentValues(initialValues);
        else
            values = new ContentValues();

        if (!values.containsKey(Alarms.AlarmColumns.HOUR))
            values.put(Alarms.AlarmColumns.HOUR, 0);

        if (!values.containsKey(Alarms.AlarmColumns.MINUTES))
            values.put(Alarms.AlarmColumns.MINUTES, 0);

        if (!values.containsKey(Alarms.AlarmColumns.DAYS_OF_WEEK))
            values.put(Alarms.AlarmColumns.DAYS_OF_WEEK, 0);

        if (!values.containsKey(Alarms.AlarmColumns.ALARM_TIME))
            values.put(Alarms.AlarmColumns.ALARM_TIME, 0);

        if (!values.containsKey(Alarms.AlarmColumns.ENABLED))
            values.put(Alarms.AlarmColumns.ENABLED, 0);

        if (!values.containsKey(Alarms.AlarmColumns.VIBRATE))
            values.put(Alarms.AlarmColumns.VIBRATE, 1);

        if (!values.containsKey(Alarms.AlarmColumns.MESSAGE))
            values.put(Alarms.AlarmColumns.MESSAGE, "");

        if (!values.containsKey(Alarms.AlarmColumns.ALERT))
            values.put(Alarms.AlarmColumns.ALERT, "");


       
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert("alarms", Alarms.AlarmColumns.MESSAGE, values);
        if (rowId < 0) {
            throw new SQLException("Failed to insert row into " + url);
        }
        if (Log.LOGV) Log.v("Added alarm rowId = " + rowId);

        Uri newUrl = ContentUris.withAppendedId(Alarms.AlarmColumns.CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(newUrl, null);
        return newUrl;
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        long rowId = 0;
        switch (sURLMatcher.match(url)) {
            case ALARMS:
                count = db.delete("alarms", where, whereArgs);
                break;
            case ALARMS_ID:
                String segment = url.getPathSegments().get(1);
                rowId = Long.parseLong(segment);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("alarms", where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete from URL: " + url);
        }

        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }
	
	
}