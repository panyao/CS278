package org.magnum.soda.example.maint;

import org.magnum.soda.android.AndroidSoda;
import org.magnum.soda.android.AndroidSodaConnectionException;
import org.magnum.soda.android.AndroidSodaListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SodaConnectionService extends Service implements AndroidSodaListener{
	private static final String TAG = "SodaConnectionService";
	private static final String mHost = "192.168.173.1";
	private AndroidSoda as;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		AndroidSoda.init(this, mHost, 8081, this);
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	
	@Override
	public void connected(final AndroidSoda s) {
		Log.d(TAG, "connected");
		this.as = s;
	}
	
	 @Override
     public void connectionFailure(AndroidSoda s,
                     AndroidSodaConnectionException ex) {
             Toast.makeText(this, "Unable to connect to server.", Toast.LENGTH_LONG).show();
     }


}
