package com.example.chronoyourself;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ArrayList<String> activities;
	ListView listview;
	ArrayAdapter<String> arrayAdap;
	Context context;
	public static final String prefs_name = "MyPrefsFile";
	SharedPreferences prefs;
	SharedPreferences.Editor prefsEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getSharedPreferences(prefs_name, 0);	
		prefsEdit = prefs.edit();
		activities = new ArrayList<String>();
		int size;
		size = prefs.getInt("Size", 0);
		for(int i=0; i < size; i++){
			activities.add(prefs.getString(Integer.toString(i), "Activity not found"));			
		}
		
		context = this;
		listview = new ListView(context);
		listview = (ListView) findViewById(R.id.listActivities);
		arrayAdap  = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, activities);
		listview.setAdapter(arrayAdap);

		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				Intent i = new Intent(context , ChronoActivity.class);
				Bundle dataBundle = new Bundle();
				dataBundle.putInt("activity", position);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtras(dataBundle);
				startActivity(i);
			}	
		});	
/*		listview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				activities.remove(position);
				arrayAdap.notifyDataSetChanged();			
				return false;
			}
		});
*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    

	public void addActivity(View view){
		EditText ETactivity = new EditText(this);
		ETactivity = (EditText) findViewById(R.id.editText1);
		String activity = ETactivity.getText().toString();
		if(activity != null && activity.length() > 0 && !activities.contains(activity)){
			activities.add(activity);
			arrayAdap.notifyDataSetChanged();
			// Metemos la actividad en preferences con key su posicion en activities
			prefsEdit.putString(Integer.toString(activities.size()-1), activity);
			//Incrementamos el contador de activities
			prefsEdit.putInt("Size", prefs.getInt("Size", 0)+1);
			prefsEdit.commit();
			ETactivity.setText("");
		}
		else
			Toast.makeText(this, R.string.Toast_error_add, Toast.LENGTH_SHORT).show();
	}

}
