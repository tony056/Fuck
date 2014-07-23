package com.example.fuck;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
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

	static final String ParseAPPID = "EQkLgw6CynqLuliY0QA6wx4icyh3OH42lPRUB1Os";
	static final String ParseClientKey = "5MzDXux7XnfCXVVK56dz0c3NCBLrTP0AuMKpqOly";
	ListView listView;
	List<String> contentList;
	final Context context = this;
	String username = "";
	String password = "";
	ArrayAdapter<String> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, ParseAPPID, ParseClientKey);
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseAnalytics.trackAppOpened(getIntent());
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		

		listView = (ListView) findViewById(R.id.ListMenu);

		contentList = new ArrayList<String>();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			contentList.add("USERNAME");
		} else {
			contentList.add(currentUser.getUsername());
			String  android_id = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
			installation.put("UniqueId", android_id);
			installation.put("username", currentUser.getUsername());
			installation.saveInBackground();
		}

		contentList.add("SEND FUCK!");
		contentList.add("FUCK! COUNT:");
		contentList.add("SIGN UP");

		arrayAdapter = new ArrayAdapter<String>(getBaseContext(),
				R.layout.mylistview, R.id.textItem, contentList);
		arrayAdapter.notifyDataSetChanged();
		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				if (contentList.get(index).equals("SIGN UP")) {
					// Toast.makeText(getBaseContext(), "" +
					// contentList.get(index), Toast.LENGTH_SHORT).show();
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.signupdialog);
					dialog.setTitle("SIGN UP");

					Button btnSignUp = (Button) dialog
							.findViewById(R.id.buttonSend);
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
										// Toast.makeText(context,
										// ParseUser.getCurrentUser().toString(),
										// Toast.LENGTH_SHORT).show();
										updateData();
										dialog.dismiss();

									}

								}
							});
						}
					});

					Button btnCancel = (Button) dialog
							.findViewById(R.id.buttonCancel);
					btnCancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.show();
				} else if (contentList.get(index).equals("SEND FUCK!")) {
					Intent intent = new Intent(context, FriendActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	private void updateData() {
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("username", username);
		installation.saveInBackground();
		contentList.set(0, username);
		contentList.set(contentList.size() - 1, "LOG OUT");
		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
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
	public void onBackPressed() {
		super.onBackPressed();
	}
}
