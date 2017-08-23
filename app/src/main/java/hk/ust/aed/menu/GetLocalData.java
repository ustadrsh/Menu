package hk.ust.aed.menu;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 12/08/2017.
 */

public class GetLocalData {

    private JSONObject data;
    private boolean validFile = true;
    String trialId = "";

    public GetLocalData(Context context, MenuMap.Screen app, long trialNum){
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
            case MTT_APP:
                contentProviderUri = R.string.MTT_URI;
                break;
        }
        trialId = FirebaseAuth.getInstance().getCurrentUser().getUid() + UploadFirebase.getGameName(app) + String.valueOf(trialNum);
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
        } catch (Exception e) {
            validFile = false;
            e.printStackTrace();
        } finally {
            try { if (is != null) is.close(); } catch (IOException e) { }
        }
        if(validFile) {
            try {
                data = new JSONObject(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getDataString(){
        return data.toString();
    }

    public JSONObject getData(){
        return data;
    }

    public Map<String, Object> getMap() throws JSONException {
        if(!validFile) return null;

        Map<String, Object> retMap = new HashMap<String, Object>();
        if(data != JSONObject.NULL) {
            retMap = toMap(data);
        }
        return retMap;
    }

    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
