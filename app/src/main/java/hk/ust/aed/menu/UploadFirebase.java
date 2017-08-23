package hk.ust.aed.menu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.HashMap;

import static hk.ust.aed.menu.MenuMap.Screen.MTT_APP;
import static hk.ust.aed.menu.MenuMap.Screen.SRM_APP;
import static hk.ust.aed.menu.MenuMap.Screen.SWM_APP;

/**
 * Created by henry on 7/4/17.
 */

public class UploadFirebase extends AsyncTask<Void, Void, Void> {
    private static boolean listenersNotYetProduced = true;

    private static int SRMlatestTrialNum;
    private static int SWMlatestTrialNum;
    private static int PMlatestTrialNum;
    private static int MTTlatestTrialNum;

    public static String getCoinCalibrationAccel() {
        return coinCalibrationAccel;
    }

    private static String coinCalibrationAccel;

    private Context context;
    private String filesDir;
    private String directoryPM;
    private MenuMap.Screen app;

    private static final String FIREBASE_URL = "https://ustadrsh-cf116.firebaseio.com/users/";

    public UploadFirebase(Context context, MenuMap.Screen app) {
        this.context = context;
        this.app = app;

        filesDir = context.getFilesDir().toString();

        /*File dirPM = new File(filesDir,"/PassiveMon");
        if(!dirPM.exists()) dirPM.mkdir();
        directoryPM = dirPM.toString();
        Log.e("UploadFirebase", "ONCREATE");
        Log.i("PMdatadirectory",directoryPM);*/
    }


    @Override
    protected Void doInBackground(Void... params) {

        boolean generateNewTrial = true;
        String trialId = getLatestTrialId(app, generateNewTrial);
        try {
            dataUpload(trialId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLatestTrialId(MenuMap.Screen app, boolean generateNewTrail){
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid() + getGameName(app) + String.valueOf(getLatestTrialNum(app, generateNewTrail));
        Log.e("TRIAL ID has" + (generateNewTrail ? "" : " not") + " been incremented!", id);
        return id;
    }

    public static long getLatestTrialNum(MenuMap.Screen app, final boolean generateNewTrial){
        long returnNum = 0;
        returnNum = (long) RunApp.getInputParamsFor(app).get("latestTrialNum");
        if(generateNewTrial){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String directory = "";
            switch(app){
                case PASSIVE_MONITORING_APP:
                    directory = "/passiveMon/";
                    break;
                case SWM_APP:
                    directory = "/swm/";
                    break;
                case SRM_APP:
                    directory = "/srm/";
                    break;
                case MTT_APP:
                    directory = "/mtt/";
                    break;
            }
            DatabaseReference ref = mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + directory + "config/latestTrialNum");
            ref.setValue(returnNum + 1);
        }
        return returnNum;
    }

    public static String getGameName(MenuMap.Screen app){
        switch (app){
            case SWM_APP: return "SWM";
            case SRM_APP: return "SRM";
            case MTT_APP: return "MTT";
        }
        return null;
    }

    /*private String getData(Context context, String trialId){
        int contentProviderUri = 0;
        switch(app) {
            case SWM_APP:
                contentProviderUri = R.string.SWM_URI;
                break;
            case PASSIVE_MONITORING_APP:
                contentProviderUri = R.string.PM_URI;
                break;
            case SRM_APP:
                contentProviderUri = R.string.SRM_URI;
                break;
        }
        Uri uri = Uri.parse(context.getResources().getString(contentProviderUri) + trialId);
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

        Log.e("JSON FOUND",result.toString());
        return result.toString();
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
    */

    private boolean dataUpload(String trialId) throws JSONException {
        /*OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, data);
        Response response = null;

        String directory = "";
        switch(app){
            case PASSIVE_MONITORING_APP:
                directory = "/passiveMon/";
                break;
            case SWM_APP:
                directory = "/swm/scoreboard/";
                break;
            case SRM_APP:
                directory = "/srm/scoreboard/";
                break;
        }

        String url = FIREBASE_URL + FirebaseAuth.getInstance().getCurrentUser().getUid() + directory + trialId + ".json";

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        try{
            response = client.newCall(request).execute();
        }
        catch (IOException e){

        }

        if(response == null) return false;
        if(!response.isSuccessful()) return false;*/
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String directory = "";
        switch(app){
            case PASSIVE_MONITORING_APP:
                directory = "/passiveMon/";
                break;
            case SWM_APP:
                directory = "/swm/scores/";
                break;
            case SRM_APP:
                directory = "/srm/scores/";
                break;
            case MTT_APP:
                directory = "/mtt/scores/";
                break;
        }
        DatabaseReference ref = mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + directory + trialId);
        boolean generateNewId = false;
        GetLocalData localDataGetter = new GetLocalData(context, app, getLatestTrialNum(app, generateNewId));
        Log.e("Data to upload", localDataGetter.getDataString());
        //Log.e("Trying to upload", localDataGetter.getDataString());
        //Gson gson = new Gson();
        //SRMTrialData trialData = gson.fromJson(localDataGetter.getDataString(), SRMTrialData.class);
        //JsonObject data
        /*SRMTrialData test = new SRMTrialData();
        test.numSets = 4;
        test.sets = new ArrayList<>();
        SRMTrialData.Set set = test.new Set();
        set.correctness = new ArrayList<>(Arrays.asList(false, false));
        set.latencies = new ArrayList<>(Arrays.asList(10, 24));
        set.numAlternatives = 3;
        set.numBoxes = 4;
        set.presentationDurationMillis = 5;
        set.trialDurationMillis = 1;
        set.xDivisions = 0;
        set.yDivisions = -3;
        test.sets.add(set);*/

        ref.setValue(localDataGetter.getMap());//localDataGetter.getData());
        return true;
    }

    /*public static void updateLocalTrialId(MenuMap.Screen app, int value) {
        switch(app){
            case PASSIVE_MONITORING_APP:
                PMlatestTrialNum = value;
                break;
            case SWM_APP:
                SWMlatestTrialNum = value;
                break;
            case MTT_APP:
                MTTlatestTrialNum = value;
                break;
            case SRM_APP:
                SRMlatestTrialNum = value;
                break;
        }
    }*/

    public static void maybeProduceListeners() {
        if (listenersNotYetProduced) {
            Log.e("Creating", "listeners");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            for (final MenuMap.Screen app : new MenuMap.Screen[]{SWM_APP, SRM_APP, MTT_APP}) {
                String directory = "";
                switch (app) {
                    case PASSIVE_MONITORING_APP:
                        directory = "/passiveMon/";
                        break;
                    case SWM_APP:
                        directory = "/swm/";
                        break;
                    case SRM_APP:
                        directory = "/srm/";
                        break;
                    case MTT_APP:
                        directory = "/mtt/";
                        break;
                }

                final DatabaseReference ref = mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + directory + "config/");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //HashMap<String, Object> inputParams = getInputParamsFor(app);
                            RunApp.setInputParamsFor(app, (HashMap<String, Object>) dataSnapshot.getValue());
                            Log.e("latesttrial for " + app.name(), String.valueOf(RunApp.getInputParamsFor(app).get("latestTrialNum")));
                            //String value = String.valueOf(dataSnapshot.getValue());
                            //updateLocalTrialId(app, Integer.parseInt(String.valueOf(value)));
                            //Log.e("Updated " + app.name() + " to", value);
                        } else {
                            HashMap<String, Object> newConfig = new HashMap<>();
                            newConfig.put("latestTrialNum", 0);
                            ref.setValue(newConfig);
                            //Log.e("Snapshot for " + app.name(), "doesn't exist, setting to 0.");
                            //ref.setValue(0);
                            //updateLocalTrialId(app, 0);
                        }
                        //notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });
            }
            /*{
                final DatabaseReference ref = mDatabase.child("config/3dgame/coinCalibrationAccel");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            coinCalibrationAccel = (String) dataSnapshot.getValue();
                            Log.e("coin Updated! to", coinCalibrationAccel);
                        } else {

                        }
                        //notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });
            }*/
            listenersNotYetProduced = false;
        }
    }
}