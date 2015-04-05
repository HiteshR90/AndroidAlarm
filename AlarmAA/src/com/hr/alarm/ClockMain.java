package com.hr.alarm;

import java.util.Calendar;
import java.util.Date;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.R.string;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ClockMain extends Activity{

	RelativeLayout rdigital,ranalog;

	ImageButton imgc,imga,imgs;
	Typeface tfrobotoreg,tfrobotolig;
	
	TextView txtbattery,txttemp;
	protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
	
        setContentView(R.layout.clockk);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        
        rdigital=(RelativeLayout) findViewById(R.id.layout_digital);
       // ranalog=(RelativeLayout) findViewById(R.id.layout_analog);
        
        tfrobotoreg = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        tfrobotolig = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
    	
//        ranalog.setVisibility(RelativeLayout.GONE);
        txttemp=(TextView) findViewById(R.id.lbltemp);

        txttemp.setTypeface(tfrobotoreg);
        temp();
           
        
        txtbattery=(TextView) findViewById(R.id.txt_clcokk_battery);
        Thread myThread = null;

        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);   
        myThread.start();
        this.registerReceiver(batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        
        

        imga=(ImageButton) findViewById(R.id.img_clock_alarm);
        imgs=(ImageButton) findViewById(R.id.img_clock_setting);
        imgc=(ImageButton) findViewById(R.id.img_clock_clock);
        imgc.setImageResource(R.drawable.clockonhover);
        
        imga.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ClockMain.this,AlarmClock.class);
				startActivity(intent);
			}
		});
        
        imgs.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ClockMain.this,BedClock.class);
				startActivity(intent);
			}
		});

        
        
	}
	
	
	public void temp()
	{
		String s="http://www.google.com/ig/api?weather=ahmedabad";
        try
        {
        	URL website=new URL(s);
        	SAXParserFactory spf=SAXParserFactory.newInstance();
        	SAXParser sp=spf.newSAXParser();
        	XMLReader xr=sp.getXMLReader();
        	
        	HandlingXml doingwork=new HandlingXml();
        	
        	xr.setContentHandler(doingwork);
        	xr.parse(new InputSource(website.openStream()));
        	
        	
        	String temp=doingwork.getTempC();
        	String tempf=doingwork.getTempF();
        	txttemp.setText(temp);
        }
        catch (Exception e) {
			// TODO: handle exception
		}
        
	}
	
	
	public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView txtCurrentTime= (TextView)findViewById(R.id.lbltime);
                    TextView txtampm=(TextView) findViewById(R.id.lblampam);
                    TextView txtdate=(TextView)findViewById(R.id.lbldate);
                    txtCurrentTime.setTypeface(tfrobotoreg);
                    txtampm.setTypeface(tfrobotolig);
                    txtdate.setTypeface(tfrobotolig);
                        Date dt = new Date();
                        int hours = dt.getHours();
                        int minutes = dt.getMinutes();
                        int seconds = dt.getSeconds();
                        int date=dt.getDate();
                        int day=dt.getDay();
                        int month=dt.getMonth();
                        
                        
                        
                        String days = null,months = null;
                        String hor=String.valueOf(hours);
                        String min=String.valueOf(minutes);
                        if(hours>=12 && minutes>=0)
                        {
                        	txtampm.setText("PM");
                        }
                        else
                        {
                        	txtampm.setText("AM");
                        }
                        int minutelength = String.valueOf(minutes).length();
                        int hourlenght= String.valueOf(hours).length();
                        if(minutelength==1)
                        {
                        	min="0"+min;
                        }
                        if(hours==0)
                        {
                        	hor="12";
                        }
                        if(hours>12)
                        {
                        	switch (hours) {
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
                        
                        switch (day) {
                        case 0:
							days="Sunday";
							break;
                        case 1:
							days="Monday";
							break;

						case 2:
							days="Tuesday";
							break;
							
						case 3:
							days="Wednesday";
							break;
						case 4:
							days="Thursday";
							break;
						case 5:
							days="Friday";
							break;
						case 6:
							days="Saturday";
							break;
						

						default:
							break;
						}
                        
                        switch (month) {
						case 1:
							months="January";
							break;
						case 2:
							months="February";
							break;
						case 3:
							months="March";
							break;
						case 4:
							months="April";
							break;
						case 5:
							months="May";
							break;
						case 6:
							months="June";
							break;
						case 7:
							months="July";
							break;
						case 8:
							months="August";
							break;
						case 9:
							months="September";
							break;
						case 10:
							months="October";
							break;
						case 11:
							months="November";
							break;
						case 12:
							months="December";
							break;
						default:
							break;
						}
                        
                        
                        String curTime = hor + ":" + min;
                        txtCurrentTime.setText(curTime);
                      //  SimpleDateFormat dtt=new SimpleDateFormat("YYYY");
                        Calendar c=Calendar.getInstance();
                        int year=c.get(Calendar.YEAR);

                        txtdate.setText(days+","+months+" "+String.valueOf(date)+","+year);
                }catch (Exception e) {}
            }
        });
    }
    
    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    try {
                    doWork();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                    }catch(Exception e){
                    }
                }
        }
    }


  
    
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int level =	intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int maxValue =intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			int chargedPct = (level * 100)/maxValue ;
			
			txtbattery.setText(chargedPct+"%");
		}
		
	};


}
