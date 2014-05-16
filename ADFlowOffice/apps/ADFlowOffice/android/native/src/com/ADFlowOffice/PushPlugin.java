package com.ADFlowOffice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;

public class PushPlugin extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		String tokenID = null;
		String userID=null;
		if (action.equals("loginAction")) {
			tokenID = args.getString(0);
			tokenID=tokenID.replaceAll("\\p{Space}", "");
			userID= args.getString(1);
			userID=userID.replaceAll("\\p{Space}", "");
			if (tokenID != null&&tokenID!=""&&userID!=null&&userID!="") {
				System.out.println("tokenID is not null...");
				System.out.println("tokenID:"+tokenID);
				System.out.println("userID:"+userID);
				Intent sendIntent = new Intent("adflow.push.action.login");
				sendIntent.putExtra("tokenID", tokenID);
				sendIntent.putExtra("userID", userID);
				this.cordova.getActivity().startService(sendIntent);
			 }
			}
		
		if(action.equals("deviceID")){
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			md.update(android.os.Build.SERIAL.getBytes());
			byte[] mdbytes = md.digest();

			
			StringBuffer sb = new StringBuffer();
		
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16));
			}
			
			System.out.println("in plug in device ID:"+sb.toString());
			callbackContext.success(sb.toString());
			
		}
		
		;
		return false;
	}
	
/*	  private void savePreferences(String tokenID){
		 SharedPreferences preferences= cordova.getActivity().getSharedPreferences("tokenID", Context.MODE_PRIVATE);
		 SharedPreferences.Editor editor = preferences.edit();
	     editor.putString("tokenID", tokenID);
	     editor.commit();
	  }

	  private String getPreferences(){
		  SharedPreferences preferences= cordova.getActivity().getSharedPreferences("tokenID", Context.MODE_PRIVATE);
		  String tokenID= preferences.getString("tokenID", "");
		  return tokenID;
	    }*/

}
