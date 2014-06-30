package uk.co.alt236.memoryoverlay.monitor;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public abstract class BaseMonitor implements SharedPreferences.OnSharedPreferenceChangeListener{
	private final Service mService;


	public BaseMonitor(Service context){
		mService = context;
	}

	public abstract void destroy();

	public Context getApplicationContext(){
		return mService.getApplicationContext();
	}

	public Context getContext(){
		return mService;
	}

	public String getString(int resId){
		return mService.getString(resId);
	}

    public Object getSystemService(String name){
		return mService.getSystemService(name);
	}

	protected void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		getContext().registerReceiver(receiver, filter);
	}

	public abstract boolean start();

	protected void startActivity(Intent intent){
		getContext().startActivity(intent);
	}

	protected void startForeground(int id, Notification notification) {
		mService.startForeground(id, notification);
	}

	protected void unregisterReceiver(BroadcastReceiver receiver) {
		getContext().unregisterReceiver(receiver);
	}
}
