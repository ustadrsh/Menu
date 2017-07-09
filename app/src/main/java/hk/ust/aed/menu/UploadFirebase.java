package hk.ust.aed.menu;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by henry on 7/4/17.
 */

public class UploadFirebase extends AsyncTask<Void, Void, Boolean> {
    private Context context;

    public UploadFirebase(Context context) {
        this.context = context;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(context.getResources().getString(R.string.PM_URI_PhoneUsage)),null,null,null,null);

        if(cursor == null) return null;
        if(cursor.getCount() < 1) return null;

        cursor.moveToFirst();

        Log.e("CONTENT RESOLVER",cursor.getString(1));
        return null;
    }
}
