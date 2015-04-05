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

import java.util.Calendar;

import android.R.style;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.hr.alarm.AlarmProvider.DatabaseHelper;
import com.hr.alarm.Alarms.DaysOfWeek;
import com.hr.alarm.R;

/**
 * Manages each alarm
 */
public class SetAlarm extends Activity
   {

   
    DatabaseHelper db;
  //  private SeekBarPreference mCrescendoPref;
 //   private SeekBarPreference mDelayPref;
    
    private Spinner spntype,spnsoundpicker,spnringbackground,spnsnooze,spnalert,spndaily;
    private Button btnsave,btncancel,btnaddtime;
    private EditText etlabel,etnote; 
    private static final int SELECT_PICTURE = 1;
    String message,alert,alarmtype,alerttype,ringback,note,back;
    int hour,minute,daysofweek,vibrate,lable,snooze,duration,captcha;
    
    Alarms.DaysOfWeek dow;
    private int mId;
    static final int TIME_DIALOG_ID = 999;
     /**
     * Set an alarm.  Requires an Alarms.ID to be passed in as an
     * extra
     */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.setalarm);
      
        
        spntype=(Spinner) findViewById(R.id.spin_setalarm_type);
        spnsoundpicker=(Spinner) findViewById(R.id.spin_setalarm_soundpicker);
        spnringbackground=(Spinner) findViewById(R.id.spin_setalarm_ringbackground);
        spnsnooze=(Spinner) findViewById(R.id.spin_setalarm_snooze);
        spnalert=(Spinner) findViewById(R.id.spin_setalarm_alert);
       
        spndaily=(Spinner) findViewById(R.id.spin_setalarm_time);
        
        btnsave=(Button) findViewById(R.id.btn_setalarm_save);
        btncancel=(Button) findViewById(R.id.btn_setalarm_cancel);
        btnaddtime=(Button) findViewById(R.id.btn_setalarm_time);
        
        etlabel=(EditText) findViewById(R.id.edit_setalarm_label);
        etnote=(EditText) findViewById(R.id.edit_setalarm_note);
        
        
        
        Intent i = getIntent();
        mId = i.getIntExtra(Alarms.ID, -1);
        if (Log.LOGV) Log.v("In SetAlarm, alarm id = " + mId);

        /* load alarm details from database */
       // Alarms.getAlarm(getContentResolver(), this, mId);
        /* This should never happen, but does occasionally with the monkey.
         * I believe it's a race condition where a deleted alarm is opened
         * before the alarm list is refreshed. */
        db=new DatabaseHelper(SetAlarm.this);
        db.open();
        
       
        
        Cursor c=db.getData(mId);
        hour=c.getInt(0);
        minute=c.getInt(1);
        daysofweek=c.getInt(2);
        vibrate=c.getInt(3);
        message=c.getString(4);
        alert=c.getString(5);
        snooze=c.getInt(6);
        alarmtype=c.getString(7);
        ringback=c.getString(8);
        alerttype=c.getString(9);
        note=c.getString(10);
        alerttype=c.getString(11);
        c.close();
        
        if(note==null)
        {
        	note="";
        }
        if(ringback==null)
        {
        	ringback="None";
        }
       
        dow=new Alarms.DaysOfWeek(daysofweek);
        
        
//        if (!mReportAlarmCalled) {
//            Log.e("reportAlarm never called!");
//            finish();
//        }

        if(daysofweek==127)
        {
        	spndaily.setSelection(0);
        }
        else if(daysofweek==96)
        {
        	spndaily.setSelection(1);
        }
        else
        {
        	spndaily.setSelection(2);
        }
        
        if(alarmtype.equalsIgnoreCase("None") || alarmtype.equalsIgnoreCase("10"))
        {
        	spntype.setSelection(0);
        }
        else if(alarmtype.equalsIgnoreCase("Floating Snooze"))
        {
        	spntype.setSelection(1);
        }
        else if(alarmtype.equalsIgnoreCase("Mathematical"))
        {
        	spntype.setSelection(2);
        }
        
        //set ring back
       
        	if(ringback.equalsIgnoreCase("None"))
        	{
        		spnringbackground.setSelection(0);
        	}
        	else if(ringback.equalsIgnoreCase("Images"))
        	{
        		spnringbackground.setSelection(1);
        	}
        //set snooze
        if(snooze==5)
        {
        	spnsnooze.setSelection(0);
        }
        else if(snooze==10)
        {
        	spnsnooze.setSelection(1);
        }
        else if (snooze==15) {
        	spnsnooze.setSelection(2);
		}
        else if (snooze==20) {
        	spnsnooze.setSelection(3);
		}
        else if (snooze==30) {
        	spnsnooze.setSelection(4);
		}
        
        //setalert
        if(alerttype.equalsIgnoreCase("Ring"))
        {
        	spnalert.setSelection(0);
        }
        else
        {
        	spnalert.setSelection(1);
        }
      
        
        etlabel.setText(message.toString());
        etnote.setText(note.toString());
        
       
        String setadd=setTime(hour, minute);
        btnaddtime.setText(setadd);
        
        
        spntype.setOnItemSelectedListener(new OnItemSelectedListener() {

        	int i=0;
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String stype=spntype.getSelectedItem().toString();
				if(i>0)
				{
					if(stype.equalsIgnoreCase("None"))
					{
						
					}
					else if(stype.equalsIgnoreCase("Floating Snooze"))
					{
						
					}
					else if(stype.equalsIgnoreCase("Floating Snooze"))
					{
						
					}
				}
				i++;
			
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        spnsoundpicker.setOnItemSelectedListener(new OnItemSelectedListener() {

        	int i=0;
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String ssoundpicker=spnsoundpicker.getSelectedItem().toString();
				if(i>0)
				{
					if(ssoundpicker.equalsIgnoreCase("Voice"))
					{
						
					}
					else if(ssoundpicker.equalsIgnoreCase("Ring Tone"))
					{
						Intent intent = new Intent();
						intent.setType("audio/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, "Select Ring"),SELECT_PICTURE);
					}
				}
				i++;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        btnaddtime.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
		});

        btnsave.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int vib=1;
				String label=etlabel.getText().toString();
		        String note=etnote.getText().toString();
		        
				if(spnalert.getSelectedItem().toString().equalsIgnoreCase("Ring"))
				{
					vib=0;
				}
				else
				{
					vib=1;
				}
				
				String alarmtype=spntype.getSelectedItem().toString();
				String ringback=spnringbackground.getSelectedItem().toString();
				String snoo=spnsnooze.getSelectedItem().toString();
				int snoozz=Integer.parseInt(snoo);
				long time=Alarms.calculateAlarm(hour, minute,dow).getTimeInMillis();
				String tone=null;
				String alerttype=spnalert.getSelectedItem().toString();
				db.open();
				db.updateData(mId, hour, minute, daysofweek, time, 1, vib, snoozz, label, note, alarmtype, tone, alerttype, ringback);
				db.close();
				Intent intent=new Intent(SetAlarm.this,AlarmClock.class);
				startActivity(intent);
				
			}
		});
        db.close();
    }


    
    
 // display current time
 		public void setCurrentTimeOnView() {

 			

 			final Calendar c = Calendar.getInstance();
 			hour = c.get(Calendar.HOUR_OF_DAY);
 			minute = c.get(Calendar.MINUTE);

 		}
 	
 	protected Dialog onCreateDialog(int id)
     {
     	switch (id) {
 		case TIME_DIALOG_ID:
 			// set time picker as current time
 			return new TimePickerDialog(this, timePickerListener, hour, minute,false);

 		}
 		return null;
     	
     }
  
 	private TimePickerDialog.OnTimeSetListener timePickerListener=new TimePickerDialog.OnTimeSetListener() {
 		
 		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
 			
 			hour=hourOfDay;
 			SetAlarm.this.minute=minute;
 			
 			String time=setTime(hourOfDay, minute);
 			 
 			btnaddtime.setText(time.toString());
 			
 			
 	       
 	        //Toast.makeText(getBaseContext(), timeh+timem+ampm1, 7000).show();
 	        
 	        
 		}
 	};
    
 	private String setTime(int hourOfDay,int minute)
 	{
 		String hor = null,min=null,ampm=null;
			min=new Integer(minute).toString();
			hor=new Integer(hourOfDay).toString();
 		int minutelength = String.valueOf(min).length();
 		if(hourOfDay>=12 && minute>=0)
        {
        	ampm="PM";
        }
        else
        {
       	 ampm="AM";
        }
		 if(hourOfDay<10)
		 {
			 hor="0"+hor;
		 }
		 if(minutelength==1)
       {
       	min="0"+min;
       }
       if(hourOfDay==0)
       {
       	hor="12";
       }
		if(hourOfDay>12)
       {
       	switch (hourOfDay) {
			case 13:
				hor="01";
				break;

			case 14:
				hor="02";
				break;
			case 15:
				hor="03";
				break;
			case 16:
				hor="04";
				break;
			case 17:
				hor="05";
				break;
			case 18:
				hor="06";
				break;
			case 19:
				hor="07";
				break;
			case 20:
				hor="08";
				break;
			case 21:
				hor="09";
				break;
			case 22:
				hor="10";
				break;
			case 23:
				hor="11";
				break;
			case 24:
				hor="12";
				break;
			default:
				break;
			}
       }
		
		return hor+":"+min+" "+ampm;
 	}
 	
 
}
