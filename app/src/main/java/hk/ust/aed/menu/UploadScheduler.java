package hk.ust.aed.menu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by henry on 2017-07-09.
 */

public class UploadScheduler extends Service {
    private static final int UPLOAD_INTERVAL = 86400; //seconds
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AlarmManager alarmManager;

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Log.e("UploadScheduler","ONCREATE");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new UploadFirebase(this).execute();
        //editor.putString("LastUpload",Long.toString(System.currentTimeMillis()));

        Intent nextUploadFirebase = new Intent(getApplicationContext(), this.getClass());
        PendingIntent pendingNextUploadFirebase= PendingIntent.getService(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + UPLOAD_INTERVAL * 1000,pendingNextUploadFirebase);

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
