/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RepeatPreference extends ListPreference {

	 
	CheckBox chmon,chtue,chwed,chthu,chfri,chsat,chsun;
	Spinner spndays;
	boolean change=false;
    private Alarms.DaysOfWeek mDaysOfWeek = new Alarms.DaysOfWeek();
    private OnRepeatChangedObserver mOnRepeatChangedObserver;

    public interface OnRepeatChangedObserver {
        /** RepeatPrefrence calls this to get initial state */
        public Alarms.DaysOfWeek getDaysOfWeek();

        /** Called when this preference has changed */
        public void onRepeatChanged(Alarms.DaysOfWeek daysOfWeek);
    }

    public RepeatPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setOnRepeatChangedObserver(OnRepeatChangedObserver onRepeatChangedObserver) {
        mOnRepeatChangedObserver = onRepeatChangedObserver;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (change) {
            mOnRepeatChangedObserver.onRepeatChanged(mDaysOfWeek);
            change=false;
        } else {
            /* no change -- reset to initial state */
            mDaysOfWeek.set(mOnRepeatChangedObserver.getDaysOfWeek());
        }
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        CharSequence[] entries = getEntries();
        CharSequence[] entryValues = getEntryValues();

        if (entries == null || entryValues == null) {
            throw new IllegalStateException(
                    "RepeatPreference requires an entries array and an entryValues array.");
        }

        mDaysOfWeek.set(mOnRepeatChangedObserver.getDaysOfWeek());

        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog,null);
	       
        chmon=(CheckBox) dialog.findViewById(R.id.checkmon);
		chtue=(CheckBox) dialog.findViewById(R.id.checktue);
		chwed=(CheckBox) dialog.findViewById(R.id.checkwed);
		chthu=(CheckBox) dialog.findViewById(R.id.checkthu);
		chfri=(CheckBox) dialog.findViewById(R.id.checkfri);
		chsat=(CheckBox) dialog.findViewById(R.id.checksat);
		chsun=(CheckBox) dialog.findViewById(R.id.checksun);
		spndays=(Spinner) dialog.findViewById(R.id.spindays);
		
		spndays.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String sp= spndays.getSelectedItem().toString();
				if(sp.equalsIgnoreCase("Daily"))
				{
					//mDaysOfWeek.set(0,true);
					checkAll();
				}
				else if(sp.equalsIgnoreCase("Weekend"))
				{
					//mDaysOfWeek.set(1,true);
					checkWeekEnd();
				}
				else if(sp.equalsIgnoreCase("Custom"))
				{
					enableAll();
					unCheckAll();
				}
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			

        builder.setView(dialog);
        
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				String sp= spndays.getSelectedItem().toString();
				if(sp.equalsIgnoreCase("Daily"))
				{
					mDaysOfWeek.set(0,true);
					change=true;
				}
				else if(sp.equalsIgnoreCase("Weekend"))
				{
					mDaysOfWeek.set(1,true);
					change=true;
				}
				else if(sp.equalsIgnoreCase("Custom"))
				{
					mDaysOfWeek.set(1, false);
					if(chmon.isChecked())
					{
						mDaysOfWeek.set(2,true);
						change=true;
					}
					if(chtue.isChecked())
					{
						mDaysOfWeek.set(3,true);
						change=true;
					}
					if(chwed.isChecked())
					{
						mDaysOfWeek.set(4,true);
						change=true;
					}
					if(chthu.isChecked())
					{
						mDaysOfWeek.set(5,true);
						change=true;
					}
					if(chfri.isChecked())
					{
						mDaysOfWeek.set(6,true);
						change=true;
					}
					if(chsat.isChecked())
					{
						mDaysOfWeek.set(7,true);
						change=true;
					}
					if(chsun.isChecked())
					{
						mDaysOfWeek.set(8,true);
						change=true;
					}
					change=true;
				}
			}
		});
    }
    
    private void checkAll()
	{
		chmon.setEnabled(false);
		chtue.setEnabled(false);
		chwed.setEnabled(false);
		chthu.setEnabled(false);
		chfri.setEnabled(false);
		chsat.setEnabled(false);
		chsun.setEnabled(false);
		
		chmon.setChecked(true);
		chtue.setChecked(true);
		chwed.setChecked(true);
		chthu.setChecked(true);
		chfri.setChecked(true);
		chsat.setChecked(true);
		chsun.setChecked(true);
	}
	
	private void unCheckAll()
	{
		chmon.setChecked(false);
		chtue.setChecked(false);
		chwed.setChecked(false);
		chthu.setChecked(false);
		chfri.setChecked(false);
		chsat.setChecked(false);
		chsun.setChecked(false);
	}
	
	private void enableAll()
	{
		chmon.setEnabled(true);
		chtue.setEnabled(true);
		chwed.setEnabled(true);
		chthu.setEnabled(true);
		chfri.setEnabled(true);
		chsat.setEnabled(true);
		chsun.setEnabled(true);
		
	}
	
	private void checkWeekEnd()
	{
		chmon.setEnabled(false);
		chtue.setEnabled(false);
		chwed.setEnabled(false);
		chthu.setEnabled(false);
		chfri.setEnabled(false);
		chsat.setEnabled(false);
		chsun.setEnabled(false);
		
		
		chmon.setChecked(false);
		chtue.setChecked(false);
		chwed.setChecked(false);
		chthu.setChecked(false);
		chfri.setChecked(false);
		chsat.setChecked(true);
		chsun.setChecked(true);
	}
	
	private void check()
	{
		if(chmon.isChecked())
		{
			mDaysOfWeek.set(2,true);
		}
		if(chtue.isChecked())
		{
			mDaysOfWeek.set(3,true);
		}
		if(chwed.isChecked())
		{
			mDaysOfWeek.set(4,true);
		}
		if(chthu.isChecked())
		{
			mDaysOfWeek.set(5,true);
		}
		if(chfri.isChecked())
		{
			mDaysOfWeek.set(6,true);
		}
		if(chsat.isChecked())
		{
			mDaysOfWeek.set(7,true);
		}
		if(chsun.isChecked())
		{
			mDaysOfWeek.set(8,true);
		}
	}
	
}


