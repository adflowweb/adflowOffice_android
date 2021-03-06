package com.ADFlowOffice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class MqttPushServiceReceiver extends BroadcastReceiver {

	// Debug TAG
	public static final String DEBUGTAG = "Mqtt푸시서비스리시버";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(DEBUGTAG, "onReceive시작(context=" + context + "||intent=" + intent
				+ ")");
		try {
			// String deviceID = Secure.getString(context.getContentResolver(),
			// Secure.ANDROID_ID);
			// Log.d(DEBUGTAG, "deviceID::" + deviceID);
			// MqttPushService.setDeviceID(deviceID);

			Log.d(DEBUGTAG, "action=" + intent.getAction());

			if (intent.getAction() != null
					&& intent.getAction().equals(
							"android.intent.action.BOOT_COMPLETED")) {
				// ComponentName cName = new ComponentName(ctx.getPackageName(),
				// GPSLogger);
				// ComponentName svcName = ctx.startService(new
				// Intent().setComponent(cName);
				// if (svc == null) {
				// Log.e(TAG, "Could not start service " + cName.toString());
				// }
				// } else {
				// Log.e(TAG, "Received unexpected intent " +
				// intent.toString());
				// }

				// 토큰을 저장소 에서 가져와 토큰이 있으면 알람을 설정 없으면 skip
				SharedPreferences sp = context.getSharedPreferences("token",
						context.MODE_PRIVATE);
				String tokenID = sp.getString("tokeID", null);

				if (tokenID != null) {
					// set alarm
					AlarmManager alarmManager = (AlarmManager) context
							.getSystemService(Context.ALARM_SERVICE);
					Intent i = new Intent(context,
							MqttPushServiceReceiver.class);
					PendingIntent pending = PendingIntent.getBroadcast(context,
							0, i, PendingIntent.FLAG_CANCEL_CURRENT);
					// Calendar cal = Calendar.getInstance();
					// start 30 seconds after boot completed
					// cal.add(Calendar.SECOND, 1);
					// fetch every 30 seconds
					// InexactRepeating allows Android to optimize the energy
					// consumption
					alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					/* cal.getTimeInMillis() */0, 1000 * 240, pending);
					Log.d(DEBUGTAG, "푸시알람이설정되었습니다");
				}

			} else {
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				WakeLock wakeLock = pm.newWakeLock(
						PowerManager.PARTIAL_WAKE_LOCK,
						"MqttPushServiceReceiverWakeLock");
				MqttPushService.setWakeLock(wakeLock);
				Log.d(DEBUGTAG, "웨이크락상태::" + wakeLock.isHeld());
				if (!wakeLock.isHeld()) {
					wakeLock.acquire(10000);
					// ..screen will stay on during this section..
					Log.d(DEBUGTAG, "웨이크락::" + wakeLock);
					Intent alarmIntent = new Intent(context,
							MqttPushService.class);
					alarmIntent.setAction("adflow.push.action.checkMqttClient");
					context.startService(alarmIntent);
					// wakeLock.release();
				}
			}
		} catch (Exception e) {
			Log.e(DEBUGTAG, "예외상황발생", e);
		}
		Log.d(DEBUGTAG, "onReceive종료()");
	}
}
