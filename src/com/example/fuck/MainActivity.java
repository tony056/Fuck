package com.example.fuck;

import java.util.ArrayList;
import java.util.List;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;

import android.R.integer;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
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

public class MainActivity extends ActionBarActivity {

	ListView listView;
	List<String> contentList;
	final Context context = this;
	String username = "";
	String password = "";
	ArrayAdapter<String> arrayAdapter;
	ParseUser currentUser = null;
	ProgressDialog mProgressDialog;
	int num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ParseAnalytics.trackAppOpened(getIntent());
		new RemoteDataTask().execute();
	}

	private void updateData() {
		deviceInstallation(username);
		contentList.set(0, username);
		contentList.set(contentList.size() - 1, "LOG OUT");
		arrayAdapter.notifyDataSetChanged();
	}

	private void deviceInstallation(String username) {
		ParseInstallation installation = ParseInstallation
				.getCurrentInstallation();
		String android_id = Secure.getString(getApplicationContext()
				.getContentResolver(), Secure.ANDROID_ID);
		installation.put("UniqueId", android_id);
		installation.put("username", ParseUser.getCurrentUser().getUsername());
		installation.put("user", ParseUser.getCurrentUser());
		installation.saveInBackground();

	}

	private void dialogBuilder() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.signupdialog);
		dialog.setTitle("SIGN UP");

		Button btnSignUp = (Button) dialog.findViewById(R.id.buttonSend);
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText etUsername = (EditText) dialog
						.findViewById(R.id.editTextUsername);
				EditText etPassword = (EditText) dialog
						.findViewById(R.id.editTextPassword);

				username = etUsername.getText().toString();
				password = etPassword.getText().toString();

				ParseUser user = new ParseUser();
				user.setUsername(username);
				user.setPassword(password);

				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							currentUser = ParseUser.getCurrentUser();
							updateData();
							dialog.dismiss();

						}

					}
				});
			}
		});

		Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
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
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		mProgressDialog.dismiss();
	}

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(MainActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("FUCK!");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			contentList = new ArrayList<String>();
			if(ParseUser.getCurrentUser().getUsername() == null){
				contentList.add("USERNAME");
				contentList.add("SEND FUCK!");
				contentList.add("FUCK! COUNT: " + 0);
				contentList.add("SIGN UP");
				
			}else {
				
				contentList.add(ParseUser.getCurrentUser().getUsername());
				contentList.add("SEND FUCK!");
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
				query.whereEqualTo("to", ParseUser.getCurrentUser().getUsername());
				query.countInBackground(new CountCallback() {

					@Override
					public void done(int count, ParseException e) {
						if (e == null) {
							contentList.add("FUCK! COUNT: " + count);
							contentList.add("LOG OUT");
							arrayAdapter.notifyDataSetChanged();
						} else {
							Log.e("Parse Error", e.getMessage());
						}
					}
				});
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			listView = (ListView) findViewById(R.id.ListMenu);
			arrayAdapter = new ArrayAdapter<String>(getBaseContext(),
					R.layout.mylistview, R.id.textItem, contentList);
			listView.setAdapter(arrayAdapter);
			mProgressDialog.dismiss();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int index,
						long arg3) {

					if (contentList.get(index).equals("SIGN UP")) {
						dialogBuilder();
					} else if (contentList.get(index).equals("SEND FUCK!")) {
						Intent intent = new Intent(context, FriendActivity.class);
						startActivity(intent);
					}
				}
			});
			// Close the progressdialog
			
		}
	}

}
