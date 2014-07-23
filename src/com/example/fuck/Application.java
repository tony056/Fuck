package com.example.fuck;

import android.provider.Settings.Secure;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;


public class Application extends android.app.Application{
	
	static final String ParseAPPID = "EQkLgw6CynqLuliY0QA6wx4icyh3OH42lPRUB1Os";
	static final String ParseClientKey = "5MzDXux7XnfCXVVK56dz0c3NCBLrTP0AuMKpqOly";
	
	public Application() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the Parse SDK.
		Parse.initialize(this, ParseAPPID, ParseClientKey);
		ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);

		// Specify an Activity to handle all pushes by default.
		PushService.setDefaultPushCallback(this, MainActivity.class);
	}
}
