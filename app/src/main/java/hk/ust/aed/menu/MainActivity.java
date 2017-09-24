package hk.ust.aed.menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private hk.ust.aed.menu.MenuMap menuMap;
    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};
    private String mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?";
    private String[] mSelectionArguments = new String[]{"Dad"};
    private String mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    private int myLoader = 1;

    private FirebaseStorage storage;
    StorageReference storageRef;

    private List<Integer> coinPerformanceStringToList(String str) {
        List<Integer> result = new ArrayList<Integer>();
        int currInt = 1;
        String[] strArray = str.split(",");
        for (int i = 0; i < strArray.length; ++i) {
            int leng = Integer.parseInt(strArray[i]);
            for (int j = 0; j < leng; ++j) {
                result.add(currInt);
            }
            if (currInt == 0) currInt = 1;
            else currInt = 0;
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        menuMap.screensStack.pop();
        if(resultCode == Activity.RESULT_OK){
            MenuMap.Screen app = MenuMap.Screen.values()[requestCode];
            switch(app){
                case SWM_APP:
                    String SWMresult = data.getStringExtra("result");
                    Log.e("SWM RETURNED", SWMresult);
                    break;
                case SRM_APP:
                    Log.e("SRM RETURNED", data.getStringExtra("result"));
                    break;
                case UNITY_GAME_APP:
                    Log.d("TAG", "OK");
                    //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    //DatabaseReference ref = mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "3dgame" + UploadFirebase.getLatestTrialId(MenuMap.Screen.UNITY_GAME_APP, true));
                    //ref.setValue(jsonPath);
                    //users/uid/3dgame/trial id
                    //ref = mDatabase.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "3dgame/params");
                    //Put this in users/uid/3dgame/params
                    //ref.setValue(paramsPath);
                    String signDurationCalibrated = data.getStringExtra("signDurationCalibrated");
                    String roadSpeedCalibrated = data.getStringExtra("roadSpeedCalibrated");
                    String sensitivity = data.getStringExtra("sensitivity");
                    String signDurationAtTime = data.getStringExtra("signDurationAtTime");
                    String signPerformanceTime = data.getStringExtra("signPerformanceTime");
                    String signPerformance = data.getStringExtra("signPerformance");
                    String coinPerformance = data.getStringExtra("coinPerformance");
                    List<Integer> coinPerformaceList = coinPerformanceStringToList(coinPerformance);
                    Log.d("TAG", signDurationCalibrated);
                    Log.d("TAG", roadSpeedCalibrated);
                    Log.d("TAG", sensitivity);
                    Log.d("TAG", signDurationAtTime);
                    Log.d("TAG", signPerformanceTime);
                    Log.d("TAG", signPerformance);
                    Log.d("TAG", coinPerformance);
                    Log.d("TAG", "" + coinPerformaceList);
                    Log.d("TAG", sensitivity);
                    break;
            }
            new UploadFirebase(this, app).execute();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadFirebase.maybeProduceListeners();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //Start UploadScheduler if there is no alarm of it
        Intent pendingService = new Intent(getApplicationContext(),UploadScheduler.class);
        boolean alarmNotExist = (PendingIntent.getService(getApplicationContext(),0,pendingService,PendingIntent.FLAG_NO_CREATE) == null);
        if (alarmNotExist) {
            Log.e("MainActivity","No Alarm");
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent.getService(this, 0, pendingService, 0);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30 * 1000, alarmIntent);
        }
        else{
            Log.e("MainActivity", "ALARM");
        }

        menuMap = new MenuMap(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menuMap.newScreen(MenuMap.Screen.NULL, 0);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Log.e("menu", "BP");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            menuMap.backPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement=
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.test) {
            menuMap.newScreen(MenuMap.Screen.INIT, 0);
        } else if (id == R.id.account) {
            menuMap.newScreen(MenuMap.Screen.INIT, 1);
        } else if (id == R.id.scores) {
            menuMap.newScreen(MenuMap.Screen.INIT, 2);
        } else if (id == R.id.send) {

        } else if (id == R.id.share) {

        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            while(getSupportFragmentManager().getBackStackEntryCount() > 0){
                Log.e("frags", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
                getSupportFragmentManager().popBackStackImmediate();
            }
            startActivity(new Intent(MainActivity.this, Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public MenuMap getMenuMap(){
        return menuMap;
    }

    //LOADER methods
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if(id == myLoader){
            return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI, mColumnProjection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            while(data.moveToNext()){
                Log.e(data.getString(0), data.getString(1));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
    //END LOADER
}