package com.example.sodacloudsmsexampleclient;

import org.magnum.soda.proxy.ObjRef;

public class ExternalObjRefImpl implements ExternalObjRef {
	
	private ObjRef objRef_;
	private String host_;
	
	public ExternalObjRefImpl(ObjRef objRef, String host){
		objRef_ = objRef;
	}
	
	@Override
	public ObjRef getObjRef() {

		return objRef_;
	}

	@Override
	public String getPubSubHost() {

		return host_;
	}
	/**
	 * The toString() implementation should return
	 * a String in the following format:
	 * 
	 * getPubSubHost()+"|"+getObjRef().getUri()
	 * 
	 * 
	 * @return
	 */
	public String toString(){
		 return getPubSubHost()+"|"+getObjRef().getUri();
	}
}
