package hk.ust.aed.menu;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by henry on 7/4/17.
 */

public class UploadFirebase extends AsyncTask<Void, Void, Void> {
    private Context context;

    public UploadFirebase(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.e("UploadFirebase","doInBackground");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Uri uri = Uri.parse(context.getResources().getString(R.string.PM_URI) + dateFormat.format(new Date(System.currentTimeMillis())));
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

        return null;
    }
}
