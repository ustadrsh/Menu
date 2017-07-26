package hk.ust.aed.menu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 18/07/2017.
 */

public class DownloadApp extends AsyncTask<String,Void,Void> {

    private Context context;
    private MainActivity parent;
    private File apk;
    private boolean swmAlreadyInstalled = true;

    public DownloadApp(MainActivity parent){
        this.parent = parent;
        this.context = parent.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        File filesDir = context.getExternalFilesDir(null);
        String filename = "swm_hk.ust.aed.swm.apk";
        apk = new File(filesDir, filename);
    }

    @Override
    protected Void doInBackground(String... arg0) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("hk.ust.aed.swm", PackageManager.GET_META_DATA);
            Log.e("Downloader", "Package already exists");
            return null;
        } catch (PackageManager.NameNotFoundException e) {}
        
        // DOWNLOAD AND INSTALL PACKAGE
        
        swmAlreadyInstalled = false;
        
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();

            Log.e("Downloader", "DONWLOADED APP!!!");
            
            if(apk.exists()){
                apk.delete();
                Log.e("Downloader", "Deleting existing APK");
            }
            FileOutputStream os = new FileOutputStream(apk.getAbsolutePath());

            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                os.write(buffer, 0, len1);
            }
            os.flush();
            os.close();
            is.close();

            File f = new File(context.getExternalFilesDir(null) + "/swm_hk.ust.aed.swm.apk");

        } catch (Exception e) {
            Log.e("Downloader", "Error");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent launchIntent;
        if(swmAlreadyInstalled){
            launchIntent = new Intent();
        }
        else{
            launchIntent = new Intent(Intent.ACTION_VIEW);
            launchIntent.setDataAndType(Uri.fromFile(new File(apk.getAbsolutePath())), "application/vnd.android.package-archive");
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        }
        launchIntent.putExtra("filename", UploadFirebase.swmGetLatestTrialId() + ".json");
        launchIntent.putExtra("trialDurationMillis", 20000);
        launchIntent.putExtra("numSets", 4);
        launchIntent.putExtra("numBoxes", 6);

        if(swmAlreadyInstalled) {
            parent.getMenuMap().startPackageForResult(launchIntent, "hk.ust.aed.swm", "hk.ust.aed.swm.AndroidLauncher", MenuMap.SWM);
        }
        else {
            context.startActivity(launchIntent);
        }
    }
}