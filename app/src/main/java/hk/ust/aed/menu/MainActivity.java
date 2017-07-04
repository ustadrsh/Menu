package hk.ust.aed.menu;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private hk.ust.aed.menu.MenuMap menuMap;
    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};
    private String mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?";
    private String[] mSelectionArguments = new String[]{"Dad"};
    private String mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    private CursorLoader cursorLoader;
    private int myLoader = 1;

    //ContentResolver
    //URI of contentprovider

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("THIS SHIT WORKED YO", data.getStringExtra("result"));
        Log.e("requestCode", Boolean.toString(requestCode == 1));
        Log.e("requestCode", Integer.toString(resultCode));
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, Splash.class)); //Splash

        menuMap = new MenuMap();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoaderManager().initLoader(myLoader, null, MainActivity.this);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        loadMenu(1);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() { //TODO: FIX LOGIC!!!
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) { //DOESNT WORK
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.test) {
            loadMenu(1);
        } else if (id == R.id.account) {
            loadMenu(2);
        } else if (id == R.id.settings) {
            loadMenu(3);
        } else if (id == R.id.send) {

        } else if (id == R.id.share) {

        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadMenu(int desiredFragmentState){
        /*Tests fragment = new Tests();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "fragment1");
        fragmentTransaction.commit();*/
        MenuFragment menu = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt("state", desiredFragmentState);
        menu.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, menu).commit();
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