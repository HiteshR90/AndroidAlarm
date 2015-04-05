package com.hr.alarm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;

import com.hr.alarm.R;
import com.hr.widget.NumberPicker;

public class MathCaptcha extends Dialog implements CaptchaInterface
{
  private int mAnswer;
  TextView tv1,tv2;
  private NumberPicker mAnswerHundreds, mAnswerTens, mAnswerOnes;
  CaptchaInterface.OnCorrectListener mCorrectListener;

  public void setOnCorrectListener(CaptchaInterface.OnCorrectListener listener) {
    mCorrectListener = listener;
  }

  public MathCaptcha(Context context) { 
    super(context); 
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
  }

  public void onCreate(Bundle icicle) {
	  tv1=(TextView) findViewById(R.id.txtrandom11);
      tv2=(TextView) findViewById(R.id.txtrandom22);
      final EditText et=(EditText) findViewById(R.id.edittotal2);
      
      final Button btnsnooze=(Button) findViewById(R.id.btn_addtwonumbers_snooze);
      final Button btnstop=(Button) findViewById(R.id.btn_addtwonumbers_stop);
      
      btnsnooze.setEnabled(false);
      btnstop.setEnabled(false);
      
      Random r=new Random();
      int r1=r.nextInt(5);
      int r2=r.nextInt(4);
      
      String st1=String.valueOf(r1);
      String st2=String.valueOf(r2);
      
      tv1.setText(st1.toString());
      tv2.setText(st2.toString());
      
     final int total=r1+r2;
     final String totals=String.valueOf(total);
     
     
    btnsnooze.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	});
     
    
    et.addTextChangedListener(new TextWatcher() {
		
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			String str= et.getText().toString();
			if(totals.equalsIgnoreCase(str))
			{
				et.setVisibility(View.GONE);
				tv1.setVisibility(View.GONE);
				tv2.setVisibility(View.GONE);
				btnsnooze.setEnabled(true);
				btnstop.setEnabled(true);
				btnsnooze.setBackgroundColor(Color.BLUE);
				btnstop.setBackgroundColor(Color.BLUE);
			}
		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
    
    
    
  }

  private int get_answer()
  {
    return mAnswerHundreds.getCurrent()*100 
      + mAnswerTens.getCurrent()*10 
      + mAnswerOnes.getCurrent();
  }

  private boolean check_answer()
  {
    return get_answer() == mAnswer;
  }
}
