/* 
 **
 ** Copyright 2013, Jules White
 **
 ** 
 */
package org.magnum.soda.example.sms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.magnum.soda.proxy.SodaAsync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSManagerImpl implements SMSManager, SMSSender {

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private Set<SMSListener> listeners_  = new HashSet<SMSListener>();


	private static final String MESSAGES = "pdus";

	public SMSManagerImpl(Context ctx){
		
		IntentFilter f = new IntentFilter(SMS_RECEIVED);
		f.setPriority(100);
		
		ctx.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// get the data associated with the intent
				Bundle bundle = intent.getExtras();

				// extract the list of sms messages from the data
				Object messages[] = (Object[]) bundle.get(MESSAGES);
				List<String> cmds = new ArrayList<String>();

				// iterate through the sms messages and look for any
				// commands that need to be executed
				for (int i = 0; i < messages.length; i++) {
					SmsMessage msg = SmsMessage.createFromPdu((byte[]) messages[i]);
					
					SMS sms = new SMS();
					sms.setContent(msg.getMessageBody());
					sms.setFrom(msg.getOriginatingAddress());
					//sms.setTo(msg.);
					received(sms);
					/**
					 * Asgn Step 1: This code should construct
					 * a new SMS message using the data stored
					 * in "msg" and invoke the the received() method 
					 * 
					 */
				}
			}
		}, f);
	}
	
	
/**
 * 
 * Asgn Step 2: Fill in the following three methods using the Observer
 * pattern. This class should store a list of listeners and notify them when
 * a new SMS message is received. You need to call
 * l.smsSenderAdded(this) after adding a listener
 * to your list.
 */
	
	@Override
	public void addListener(SMSListener l) {
		listeners_.add(l);
		l.smsSenderAdded(this);
	}

	@Override
	public void removeListener(SMSListener l) {
		listeners_.remove(l);
	}
	
	public void received(SMS sms){
		SMSEvent smsEvent = new SMSEvent(SMSEvent.EVENT_TYPE.RECEIVE, sms);
		
		for(SMSListener l:listeners_){
			l.smsEvent(smsEvent);
		}
	}
	
	
	
	/**
	 * Asgn Step 3: Update this class so that it
	 * properly implements the SMSSender interface.
	 * Make sure that your send() method is annotated
	 * with the SodaAsync annotation.
	 * 
	 */
	@SodaAsync
	public void send(String to, String msg) {
		Log.d("sms","send " + to + " msg:" + msg);
		
		SMS sms = new SMS();
		sms.setContent(msg);
		sms.setTo(to);
		
		sendSMS(sms);	
	}
	
	
	public void sendSMS(SMS sms) {
		
		SmsManager mgr = SmsManager.getDefault();
		mgr.sendTextMessage(sms.getTo(), null, sms.getContent(), null, null);
	}

}
