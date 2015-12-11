package com.everystudio.test001;

import java.io.IOException;
import java.io.InputStream;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TestNotificationReceiver extends BroadcastReceiver{
	

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Unity", "TestNotificationReceiver.onRecieve");

        //値の取得
        Integer primary_key = intent.getIntExtra("PRIMARY_KEY", 0);
        String ticker = intent.getStringExtra("TICKER");
        String content_title = intent.getStringExtra("CONTENT_TITLE");
        String content_text = intent.getStringExtra ("CONTENT_TEXT");
        String sound_path = intent.getStringExtra("SOUND_PATH");
        
        if( sound_path.equals("stop") ){
            Log.i("Unity", "TestNotificationReceiver.stop");
        	if( m != null ){
	            m.stop();
	            m.release();
	            m = new MediaPlayer();
	            return;
        	}
        }

        // intentからPendingIntentを作成
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // LargeIcon の Bitmap を生成
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
          applicationInfo = pm.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
          e.printStackTrace();
          return;
        }
        final int appIconResId = applicationInfo.icon;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), appIconResId);
        
        // NotificationBuilderを作成
        //NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context);
        android.support.v4.app.NotificationCompat.Builder builder = new  android.support.v4.app.NotificationCompat.Builder(context);
        
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pendingIntent);
        builder.setTicker(ticker);                    //通知到着時に通知バーに表示(4.4まで)
        builder.setSmallIcon(appIconResId);           //アイコン
        builder.setContentTitle(content_title);       // タイトル
        builder.setContentText(content_text);         // 本文（サブタイトル）
        builder.setLargeIcon(largeIcon);              //開いた時のアイコン
        builder.setWhen(System.currentTimeMillis());  //通知に表示される時間(※通知時間ではない！)
        //Log.i("Unity", "TestNotificationReceiver.onRecieve6");

        // 通知時の音・バイブ・ライト
        /*
        if( sound_path.equals("Default") ){
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }else{
            builder.setSound( Uri.parse(sound_path));
        }
        */

        /*
        Activity activity = UnityPlayer.currentActivity;
        AssetManager assetManager = context.getResources().getAssets();
        Log.i("Unity", "activity.getResources().getAssets() end");
        String[] fileList = null;
        try {
          fileList = assetManager.list("ttest");
        } catch (IOException e) {
          e.printStackTrace();
        }
        for( int i = 0 ; i < fileList.length ; i++ ){
            Log.i("Unity", "1:" + fileList[i]);        
        }
        */

        
        //builder.setDefaults(Notification.DEFAULT_VIBRATE);
        //builder.setDefaults(Notification.DEFAULT_LIGHTS);
        //Log.i("Unity", sound_path);
        //Log.i("Unity", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString());
        //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        //builder.setSound(Uri.parse(sound_path));
        //builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        //builder.setSound(Uri.parse("file://data/data/test.everystudio.alarm.school/files/sample"));
        //builder.setSound(Uri.parse("http://ad.xnosserver.com/apps/myzoo_data/sample.mp3"));
        
        //String use_path = context.getFilesDir().getPath() + "/ttest/sample.mp3";
        //builder.setSound(Uri.parse( use_path));
                
        builder.setAutoCancel(true);

        // NotificationManagerを取得
        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Notificationを作成して通知
        manager.notify(primary_key, builder.build());
        
        try{
        	if( m == null ){
                Log.i("Unity" , "create MediaPlayer" );
	            m = new MediaPlayer();
        	}
	        if (m.isPlaying()) {
                Log.i("Unity" , "create MediaPlayer.isPlaying() == true" );
	            m.stop();
	            m.release();
	            m = new MediaPlayer();
	        }else{
                Log.i("Unity" , "create MediaPlayer.isPlaying() == false" );
	        }
	
	        AssetFileDescriptor descriptor = context.getAssets().openFd("ttest/sample.mp3");
	        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        descriptor.close();
	
	        m.prepare();
	        m.setVolume(1f, 1f);
	        m.setLooping(false);
	        m.start();        
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
    }
    
	public void test(){
        Log.i("Unity" , "test()" );
    	if( m != null ){
	    	if( m.isPlaying()){
	            m.stop();
	            m.release();
	            m = new MediaPlayer();	    		
	    	}
    	}
	}

    
    static MediaPlayer m;

}
