package com.example.fuck;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
		currentUser = ParseUser.getCurrentUser();
		
		friendList = new ArrayList<String>();
		
		updateData();
		
		arrayAdapter = new ArrayAdapter<String>(context, R.layout.mylistview, R.id.textItem, friendList);
		
		listView.setAdapter(arrayAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String command = friendList.get(arg2);
				if(command.equals("+")){
					dialogBuilder();
				}else{
					sendPush(command);
				}
			}
			
		});
		
	}
	
	private void dialogBuilder(){
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.addfrienddialog);
	
		Button btnAddFriend = (Button) dialog.findViewById(R.id.buttonAdd);
		Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);
		
		btnAddFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText friendIdEditText = (EditText) dialog.findViewById(R.id.editTextFriednId);
				addFriend(friendIdEditText.getText().toString());
				dialog.dismiss();
			}

		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
	
	private void addFriend(final String targetUser) {
		Toast.makeText(this, "adding", Toast.LENGTH_SHORT).show();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", targetUser);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if(e == null){
					if(objects.size() == 1){
						ParseUser friend = objects.get(0);
						Log.e("DONE", "get user");
						ParseRelation<ParseUser> relation = currentUser.getRelation("Friend");
						relation.add(friend);
						currentUser.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								updateData();
							}
						});
					}
				}else{
					Log.e("Error", e.getMessage());
				}
				
			}
		});
		
	}
	
	private void sendPush(String target){
		ParseObject message = new ParseObject("Message");
		message.put("from", currentUser.getUsername());
		message.put("to", target);
		message.saveInBackground();
		Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
	}
	
	private void updateData(){
		friendList.clear();
		friendList.add("+");
		ParseRelation<ParseUser> relation = currentUser.getRelation("Friend");
		ParseQuery<ParseUser> query = relation.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if(objects.size() > 0){
					for(ParseUser obj : objects){
						friendList.add(obj.getUsername());
					}
					
				}
				arrayAdapter.notifyDataSetChanged();
			}
		});
		
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
		finish();
	}
	
}
