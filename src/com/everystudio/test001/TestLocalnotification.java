package com.everystudio.test001;

import java.util.Calendar;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestLocalnotification {
	public void test(){
        Log.d("Unity Native","text");
	}
    /**
     * 時間指定でローカル通知を投げる
     * @param unixtime
     * @param primary_key
     * @param ticker
     * @param content_title
     * @param content_text
     * @param sound_path
     **/
    public void sendNotification(long unixtime, int primary_key, String ticker, String content_title, String content_text, String sound_path)
    {
        Log.i("Unity", "SendNotificationStart");

        // インテント作成
        Activity activity = UnityPlayer.currentActivity;
        Context context = activity.getApplicationContext();
        Intent intent = new Intent(context, TestNotificationReceiver.class);
        //渡す値
        intent.putExtra("PRIMARY_KEY", primary_key);
        intent.putExtra("TICKER", ticker);
        intent.putExtra("CONTENT_TITLE", content_title);
        intent.putExtra("CONTENT_TEXT", content_text);
        intent.putExtra("SOUND_PATH", sound_path);

        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(unixtime);
        Log.i("Unity" , ""+unixtime );
        calendar.setTimeInMillis(System.currentTimeMillis()); // 現在時刻を取得
        calendar.add(Calendar.SECOND, (int)unixtime); // 現時刻より15秒後を設定
        
        PendingIntent sender = PendingIntent.getBroadcast(context,  primary_key, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , sender);
        Log.i("Unity", "SendNotificationEnd");
    }
    /**
     * 時間指定でローカル通知を投げる
     * @param unixtime
     * @param primary_key
     * @param ticker
     * @param content_title
     * @param content_text
     **/
    public void sendNotification(long unixtime, int primary_key, String ticker, String content_title, String content_text)
    {
        sendNotification(unixtime, primary_key, ticker, content_title, content_text,"Default");
    }
    /**
     * 時間指定でローカル通知を投げる
     * @param unixtime
     * @param primary_key
     * @param content_title
     * @param content_text
     **/
    public void sendNotification(long unixtime, int primary_key,  String content_title, String content_text)
    {
        sendNotification(unixtime, primary_key, content_title, content_title, content_text);
    }
    /**
     * 時間指定でローカル通知を投げる
     * @param unixtime
     * @param primary_key
     * @param content_title
     **/
    public void sendNotification(long unixtime, int primary_key,  String content_title)
    {
        sendNotification(unixtime, primary_key, content_title, content_title, "");
    }
    public void clearNotification(int primary_key){
        Log.i("Unity", "ClearNotificationStart");

        // インテント作成
        Activity activity = UnityPlayer.currentActivity;
        Context context = activity.getApplicationContext();
        Intent intent = new Intent(context,TestNotificationReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(context,  primary_key, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
        sender.cancel();
    }
}
