package hk.ust.aed.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by henry on 2017-07-09.
 */

public class BroadcastReceiverBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent startServiceIntent = new Intent(context, UploadScheduler.class);
            context.startService(startServiceIntent);
        }*/
    }
}
