package com.example.sodacloudsmsexampleclient;

import org.magnum.soda.example.sms.SMSManager;
import org.magnum.soda.example.sms.SMSManagerImpl;

import android.content.Context;

public class MyModule extends ModuleImpl {

    public MyModule(Context ctx){
           setComponent(SMSManager.class, new SMSManagerImpl(ctx));
           setComponent(ObjRefExtractor.class, new QRCodeObjRefExtractor());
     }
	
}