package hk.ust.aed.menu;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by henry on 7/4/17.
 */

public class UploadFirebase extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String filesDir;
    private String directoryPM;
    private static final String FIREBASE_URL = "https://ustadrsh-cf116.firebaseio.com/users/";

    public UploadFirebase(Context context) {
        this.context = context;

        filesDir = context.getFilesDir().toString();

        File dirPM = new File(filesDir,"/PassiveMon");
        if(!dirPM.exists()) dirPM.mkdir();
        directoryPM = dirPM.toString();
        Log.e("UploadFirebase", "ONCREATE");
        Log.i("PMdatadirectory",directoryPM);
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.e("Calling","doInBackground");
        boolean PM = false;
        if(PM) {
            String[] dataPM = passiveMonGetData(context);
            dataUploadPM(dataPM[0], dataPM[1], "user123456");
        }
        else{
            String[] dataSWM = swmGetData(context);
            Log.e("Retrieved JSON", dataSWM[0]);
            //dataUploadSWM(dataPM[0], dataPM[1], "user123456");
        }
        return null;
    }

    private String[] passiveMonGetData(Context context){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String trialID = dateFormat.format(new Date(System.currentTimeMillis()));
        Uri uri = Uri.parse(context.getResources().getString(R.string.PM_URI) + trialID);
        InputStream is = null;
        StringBuilder result = new StringBuilder();
        try {
            is = context.getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = r.readLine()) != null) {
                result.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (is != null) is.close(); } catch (IOException e) { }
        }

        Log.e("TestJson",result.toString());
        String[] data = {result.toString(),trialID};
        return data;
    }

    private String[] swmGetData(Context context){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String trialID = "20170613";
        Uri uri = Uri.parse(context.getResources().getString(R.string.SWM_URI) + trialID);
        InputStream is = null;
        StringBuilder result = new StringBuilder();
        try {
            is = context.getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = r.readLine()) != null) {
                result.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (is != null) is.close(); } catch (IOException e) { }
        }

        Log.e("TestJson",result.toString());
        String[] data = {result.toString(),trialID};
        return data;
    }

    //true if file store successfully
    private boolean dataStorePM(String data, String fileName){
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(directoryPM, fileName + ".json");

        return file.length()> 1;
    }

    private boolean dataUploadPM(String data, String trialID, String userID){
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, data);
        Response response = null;
        Request request = new Request.Builder()
                .url(FIREBASE_URL + userID + "/passiveMon/" + trialID + ".json")
                .post(body)
                .build();
        try{
            response = client.newCall(request).execute();
        }
        catch (IOException e){

        }

        if(response == null) return false;
        if(!response.isSuccessful()) return false;
        return true;
    }

    private boolean dataUploadSWM(String data, String trialID, String userID){
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, data);
        Response response = null;
        Request request = new Request.Builder()
                .url(FIREBASE_URL + userID + "/SWM/scores" + trialID + ".json")
                .post(body)
                .build();
        try{
            response = client.newCall(request).execute();
        }
        catch (IOException e){

        }

        if(response == null) return false;
        if(!response.isSuccessful()) return false;
        return true;
    }
}
