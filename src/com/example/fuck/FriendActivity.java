package com.example.fuck;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseUser;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FriendActivity extends ActionBarActivity{
	
	ListView listView;
	List<String> friendList;
	ArrayAdapter<String> arrayAdapter;
	Context context = this;
	ParseUser currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendview);
		listView = (ListView) findViewById(R.id.listViewFriend);
		
		friendList = new ArrayList<String>();
		friendList.add("+");
		
		arrayAdapter = new ArrayAdapter<String>(context, R.layout.mylistview, R.id.textItem, friendList);
		
		listView.setAdapter(arrayAdapter);
		Toast.makeText(context, ParseUser.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		//moveTaskToBack(true);
		super.onBackPressed();
		FriendActivity.this.finish();
	}
	
}
