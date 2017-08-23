package hk.ust.aed.menu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 18/07/2017.
 */

public class RunApp extends DownloadFile {
    public static HashMap<String, Object> mttInputParams = new HashMap<>();
    public static HashMap<String, Object> swmInputParams = new HashMap<>();
    public static HashMap<String, Object> srmInputParams = new HashMap<>();

    Activity parent;
    MenuMap.Screen app;
    private File apk;
    boolean download = true;

    public RunApp(Activity parent, MenuMap.Screen app){
        this.parent =  parent;
        this.app = app;

        PackageManager pm = parent.getApplicationContext().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageAndClass()[0], PackageManager.GET_META_DATA);
            Log.e("Downloader", "Package already exists");
            download = false;
        } catch (PackageManager.NameNotFoundException e) {}

        File filesDir = parent.getApplicationContext().getExternalFilesDir(null);
        String filename = getPackageAndClass()[0] + ".apk";
        apk = new File(filesDir, filename);

        if(download) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ustadrsh-cf116.appspot.com").child(apk.getName());
            downloadFileAndAct(storageReference, apk);
        }
        else{
            onDownloaded(apk);
        }
    }

    @Override
    public void onDownloaded(File file) {
        Intent launchIntent;
        if (download) {
            launchIntent = new Intent(Intent.ACTION_VIEW);
            launchIntent.setDataAndType(Uri.fromFile(new File(file.getAbsolutePath())), "application/vnd.android.package-archive");
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        }
        else{
            launchIntent = new Intent();
        }
        boolean generateNewTrial = false;

        launchIntent.putExtra("filename", UploadFirebase.getLatestTrialId(app, generateNewTrial) + ".json");
        if(app == MenuMap.Screen.UNITY_GAME_APP){
            String jsonPath = "test.json";
            String coinCalibrationAccel = UploadFirebase.getCoinCalibrationAccel();
            JsonObject json = new JsonObject();
            json.add("coinCalibrationAccel", new JsonPrimitive(coinCalibrationAccel));
            String fullJsonPath = writeJsonToPath(json, jsonPath).getAbsolutePath();
                /*launchIntent.putExtra("fileLocation", outFile.getAbsoluteFile());
                launchIntent.putExtra("playerName", "test");
                launchIntent.putExtra("id", "1");
                launchIntent.putExtra("signDurationCalibrated", "0.10");

                launchIntent.putExtra("roadSpeedCalibrated", "18.0");
                launchIntent.putExtra("sensitivity", "200");*/
            //launchIntent.putExtra("coinCalibrationAccel", "3");
            launchIntent.putExtra("jsonPath", fullJsonPath);
            //launchIntent.putExtra("jsonConfigPath", UploadFirebase.getJsonConfigPath());
            launchIntent.setFlags(0);
        }
        else{
            for (Map.Entry<String, Object> entry : getInputParamsFor(app).entrySet())
            {
                Log.e(entry.getKey(), String.valueOf(entry.getValue()));
                try{
                    launchIntent.putExtra(entry.getKey(), (long) entry.getValue());
                }
                catch (Exception e){
                    launchIntent.putExtra(entry.getKey(), (String) entry.getValue());
                }
            }
        }
        /*
        switch(app) {
            case SWM_APP:
                launchIntent.putExtra("trialDurationMillis", 10000);
                launchIntent.putExtra("numSets", 3);
                launchIntent.putExtra("numBoxes", 4);
                break;
            case SRM_APP:
                for (Map.Entry<String, Object> entry : srmInputParams.entrySet())
                {
                    Log.e(entry.getKey(), String.valueOf(entry.getValue()));
                    launchIntent.putExtra(entry.getKey(), (long) entry.getValue());
                }
                launchIntent.putExtra("trialDurationMillis", 2000);
                launchIntent.putExtra("presentationDurationMillis", 3000);
                launchIntent.putExtra("numSets", 2);
                launchIntent.putExtra("numBoxes", 3);
                launchIntent.putExtra("numAlternatives", 2);
                break;
            case MTT_APP:
                //launchIntent.putExtra("trialDurationMillis", 3000);
                //launchIntent.putExtra("numTrials", 3);
                for (Map.Entry<String, Object> entry : mttInputParams.entrySet())
                {
                    Log.e(entry.getKey(), String.valueOf(entry.getValue()));
                    launchIntent.putExtra(entry.getKey(), (long) entry.getValue());
                }
                break;
            case UNITY_GAME_APP:
                String jsonPath = "test.json";
                String coinCalibrationAccel = UploadFirebase.getCoinCalibrationAccel();
                JsonObject json = new JsonObject();
                json.add("coinCalibrationAccel", new JsonPrimitive(coinCalibrationAccel));
                String fullJsonPath = writeJsonToPath(json, jsonPath).getAbsolutePath();
                launchIntent.putExtra("fileLocation", outFile.getAbsoluteFile());
                launchIntent.putExtra("playerName", "test");
                launchIntent.putExtra("id", "1");
                launchIntent.putExtra("signDurationCalibrated", "0.10");

                launchIntent.putExtra("roadSpeedCalibrated", "18.0");
                launchIntent.putExtra("sensitivity", "200");
                //launchIntent.putExtra("coinCalibrationAccel", "3");
                launchIntent.putExtra("jsonPath", fullJsonPath);
                //launchIntent.putExtra("jsonConfigPath", UploadFirebase.getJsonConfigPath());
                launchIntent.setFlags(0);
                break;
            case PASSIVE_MONITORING_APP:
                break;
        }*/
        if (download) {
            parent.getApplicationContext().startActivity(launchIntent);
        }
        else{
            String[] packageClass = getPackageAndClass();
            launchIntent.setComponent(new ComponentName(packageClass[0], packageClass[1]));
            parent.startActivityForResult(launchIntent, app.ordinal());
        }
    }

    public static HashMap<String,Object> getInputParamsFor(MenuMap.Screen app) {
        switch(app){
            case MTT_APP: return RunApp.mttInputParams;
            case SWM_APP: return RunApp.swmInputParams;
            case SRM_APP: return RunApp.srmInputParams;
        }
        return null;
    }

    public static void setInputParamsFor(MenuMap.Screen app, HashMap<String, Object> value) {
        switch(app){
            case MTT_APP: mttInputParams = value; break;
            case SWM_APP: swmInputParams = value; break;
            case SRM_APP: srmInputParams = value; break;
        }
    }

    public String[] getPackageAndClass(){ //Todo: Make a general "GET INFO" for each screen
        switch(app){
            case SWM_APP:
                return new String[]{"hk.ust.aed.swm", "hk.ust.aed.swm.AndroidLauncher"};
            case SRM_APP:
                return new String[]{"hk.ust.aed.srm", "hk.ust.aed.srm.AndroidLauncher"};
            case MTT_APP:
                return new String[]{"hk.ust.aed.mtt", "hk.ust.aed.mtt.MainActivity"};
            case UNITY_GAME_APP:
                return new String[]{"com.hk.ust.aed.multitasking3d", "com.unity3d.player.UnityPlayerActivity"};
            case PASSIVE_MONITORING_APP:
                return new String[]{"hk.ust.aed.alzheimerpassivemonitoring", "hk.ust.aed.alzheimerpassivemonitoring.MainActivity"};
        }
        return null;
    }

    private File writeJsonToPath(JsonObject json, String jsonPath) {
        try {
            File filesDir = parent.getFilesDir();
            File outFile = new File(filesDir, jsonPath);

            OutputStream os = new FileOutputStream(outFile.getAbsolutePath());

            os.write(json.toString().getBytes());
            os.flush();
            os.close();
            return outFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
};

/*public class DownloadApp extends AsyncTask<String,Void,Void> {

    private Context context;
    private MainActivity parent;
    private File apk;
    private boolean alreadyInstalled = true;
    private MenuMap.Screen app;

    public DownloadApp(MainActivity parent, MenuMap.Screen app){
        this.parent = parent;
        this.context = parent.getApplicationContext();
        this.app = app;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        File filesDir = context.getExternalFilesDir(null);
        String filename = getPackageAndClass()[0] + ".apk";
        apk = new File(filesDir, filename);
    }

    @Override
    protected Void doInBackground(String... arg0) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageAndClass()[0], PackageManager.GET_META_DATA);
            Log.e("Downloader", "Package already exists");
            return null;
        } catch (PackageManager.NameNotFoundException e) {}
        
        // DOWNLOAD AND INSTALL PACKAGE
        alreadyInstalled = false;
        
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();

            Log.e("Downloader", "Downloaded app!");
            
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
        if (alreadyInstalled) {
            launchIntent = new Intent();
        } else {
            launchIntent = new Intent(Intent.ACTION_VIEW);
            launchIntent.setDataAndType(Uri.fromFile(new File(apk.getAbsolutePath())), "application/vnd.android.package-archive");
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        }
        launchIntent.putExtra("filename", UploadFirebase.getLatestTrialId() + ".json");
        switch(app) {
            case SWM_APP:
                launchIntent.putExtra("trialDurationMillis", 10000);
                launchIntent.putExtra("numSets", 3);
                launchIntent.putExtra("numBoxes", 4);
                break;
            case SRM_APP:
                launchIntent.putExtra("trialDurationMillis", 2000);
                launchIntent.putExtra("presentationDurationMillis", 3000);
                launchIntent.putExtra("numSets", 2);
                launchIntent.putExtra("numBoxes", 3);
                launchIntent.putExtra("numAlternatives", 2);
                break;
            case MTT_APP:
                break;
            case UNITY_GAME_APP:
                File outFile = new File(parent.getExternalFilesDir(null), "UNITY_PARAMS.json");
                try {
                    OutputStream os = new FileOutputStream(outFile.getAbsolutePath());
                    os.write("{test: 3}".getBytes());
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                launchIntent.putExtra("fileLocation", outFile.getAbsoluteFile());
                launchIntent.putExtra("playerName", "test");
                launchIntent.putExtra("id", "1");
                launchIntent.putExtra("signDurationCalibrated", "0.10");
                launchIntent.putExtra("roadSpeedCalibrated", "18.0");
                launchIntent.putExtra("sensitivity", "200");
                launchIntent.putExtra("coinCalibrationAccel", "3");
                launchIntent.setFlags(0);
                break;
            case PASSIVE_MONITORING_APP:
                break;
        }
        if (alreadyInstalled) {
            String[] packageClass = getPackageAndClass();
            launchIntent.setComponent(new ComponentName(packageClass[0], packageClass[1]));
            parent.startActivityForResult(launchIntent, app.ordinal());
        } else {
            context.startActivity(launchIntent);
        }
    }

    public String[] getPackageAndClass(){ //Todo: Make a general "GET INFO" for each screen
        switch(app){
            case SWM_APP:
                return new String[]{"hk.ust.aed.swm", "hk.ust.aed.swm.AndroidLauncher"};
            case SRM_APP:
                return new String[]{"hk.ust.aed.srm", "hk.ust.aed.srm.AndroidLauncher"};
            case MTT_APP:
                return new String[]{"hk.ust.aed.mtt", "hk.ust.aed.mtt.MainActivity"};
            case UNITY_GAME_APP:
                return new String[]{"com.hk.ust.aed.multitasking3d", "com.unity3d.player.UnityPlayerActivity"};
            case PASSIVE_MONITORING_APP:
                return new String[]{"hk.ust.aed.alzheimerpassivemonitoring", "hk.ust.aed.alzheimerpassivemonitoring.MainActivity"};
        }
        return null;
    }
}*/