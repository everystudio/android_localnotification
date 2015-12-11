package com.everystudio.test001;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

public class TestNotificationReceiver extends BroadcastReceiver{
	// staticはもはや意味が無いかも
	MediaPlayer m;
	
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Unity", "TestNotificationReceiver.onRecieve");

        //値の取得
        Integer primary_key = intent.getIntExtra("PRIMARY_KEY", 0);
        String ticker = intent.getStringExtra("TICKER");
        String content_title = intent.getStringExtra("CONTENT_TITLE");
        String content_text = intent.getStringExtra ("CONTENT_TEXT");
        String sound_path = intent.getStringExtra("SOUND_PATH");
        
        Log.i("Unity", "sound_path:" + sound_path );

        // 同じイベントを利用して音を止める（stopというサウンドファイルは使えません）
        if( sound_path.equals("stop") ){
            Log.i("Unity", "TestNotificationReceiver.stop");
        	if( m != null ){
	            m.stop();
	            m.release();
	            m = new MediaPlayer();
        	}
            return;
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
        android.support.v4.app.NotificationCompat.Builder builder = new  android.support.v4.app.NotificationCompat.Builder(context);
        
        builder.setContentIntent(pendingIntent);
        builder.setTicker(ticker);                    //通知到着時に通知バーに表示(4.4まで)
        builder.setSmallIcon(appIconResId);           //アイコン
        builder.setContentTitle(content_title);       // タイトル
        builder.setContentText(content_text);         // 本文（サブタイトル）
        builder.setLargeIcon(largeIcon);              //開いた時のアイコン
        builder.setWhen(System.currentTimeMillis());  //通知に表示される時間(※通知時間ではない！)
        
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        builder.setAutoCancel(true);

        if( sound_path.equals("Default") ){
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        else {
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
		
		        AssetFileDescriptor descriptor = context.getAssets().openFd(sound_path);
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
        // NotificationManagerを取得
        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Notificationを作成して通知
        manager.notify(primary_key, builder.build());
    }

}
