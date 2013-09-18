package com.example.chronoyourself;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class ChronoActivity extends Activity {

	Chronometer chrono;
	private long paused;
	private String state;
	String activity;
	SharedPreferences prefs;
	SharedPreferences.Editor prefsEd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chrono);

		prefs = getSharedPreferences(MainActivity.prefs_name, 0);
		prefsEd = prefs.edit();
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			activity = prefs.getString(Integer.toString(extras.getInt("activity")), "No prefs work");
			TextView textAct = (TextView) findViewById(R.id.textView1);
			textAct.setText(activity);
		}
		else
			finish();
		
		
		state = "stopped";		
		chrono = new Chronometer(this);
		chrono = (Chronometer) findViewById(R.id.chronometer1);	
		chrono.setText(prefs.getString(activity.toString(), "00:00"));
	}
	
	@Override
	public void onStop(){
		super.onStop();
		saveTime(chrono.getText().toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void clickStart(View view){
		if(state.equals("stopped")){
			int time = 0;
			time = getTime();
			chrono.setBase(SystemClock.elapsedRealtime() - time);
			chrono.start();
			state = "counting";
		}
		else
			if(state.equals("paused")){	
				chrono.setBase(chrono.getBase() + SystemClock.elapsedRealtime() - paused);
				chrono.start();
				//Toast.makeText(this, Integer.toString(time/1000), Toast.LENGTH_SHORT).show();
				state = "counting";
			}
	}
	
	public void clickPause(View view){
		if(state.equals("counting")){	
			paused = SystemClock.elapsedRealtime();
			chrono.stop();
			state = "paused";
		}
	}
	
	public void saveTime(String time){
		prefsEd.putString(activity, time);
		prefsEd.commit();
	}
	
	public int getTime(){
		int time = 0;
		String sTime;
		String[] times;
		sTime = prefs.getString(activity, "00:00");
		times = sTime.split(":");
	      if (times.length == 2) {
	        time = Integer.parseInt(times[0]) * 60 * 1000
	            + Integer.parseInt(times[1]) * 1000;
	      } else if (times.length == 3) {
	        time = Integer.parseInt(times[0]) * 60 * 60 * 1000 
	            + Integer.parseInt(times[1]) * 60 * 1000
	            + Integer.parseInt(times[2]) * 1000;
	     }		
		return time;
	}
}
