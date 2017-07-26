package hk.ust.aed.menu;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 23/06/2017.
 */

class MenuMap {

    public MainActivity parent;
    public static final int INIT_STATE = 0;
    public static final int PASSIVE_MONITORING = 4;
    public static final int MTT = 5;
    public static final int SWM = 6;
    public static final int SRM = 7;
    public static final int UNITY_GAME = 8;

    private String[] menuTitles = {"Home", "Tests", "Account", "Settings"};
    private String[][] menuItems = {
            {"Tests", "Account", "Settings"},
            {"Passive Monitoring", "MTT", "SWM", "SRM", "3D Game"},
            {"Scoreboard", "Usage statistics"},
            {"Recalibrate game", "Change username or password", "Privacy settings"}
    };
    private int[][] linkTo = {
            {1, 2, 3},
            {PASSIVE_MONITORING, MTT, SWM, SRM, UNITY_GAME},
            {3, -1},
            {-1, -1}
    };

    public MenuMap(MainActivity parent){
        this.parent = parent;
    }

    public String getMenuTitle(int fragmentState){
        if(fragmentState >= 0 && fragmentState < menuTitles.length){
            return menuTitles[fragmentState];
        }
        return null;
    }

    public String[] getCurrentMenu(int fragmentState){
        return menuItems[fragmentState];
    }

    public int getNewState(int currentState, int selectedItemIndex){
        return linkTo[currentState][selectedItemIndex];
    }

    public void newScreen(int currentState, int selectedItemIndex){
        int newState = getNewState(currentState, selectedItemIndex);
        Intent launchIntent = new Intent();
        Bundle args = new Bundle();
        switch (newState) {
            case -1: break;
            case MenuMap.MTT:
                startPackageForResult(launchIntent, "hk.ust.aed.mtt", "hk.ust.aed.mtt.MainActivity", MenuMap.MTT);
                break;
            case MenuMap.SWM:
                DownloadApp d = new DownloadApp(parent);
                d.execute("https://www.dl.dropboxusercontent.com/s/ufktun87uvppimv/swm_hk.ust.aed.swm.apk?dl=0");
                break;
            case MenuMap.SRM:
                startPackageForResult(launchIntent, "hk.ust.aed.srm", "hk.ust.aed.srm.AndroidLauncher", MenuMap.SRM);
                break;
            case MenuMap.PASSIVE_MONITORING:
                startPackageForResult(launchIntent, "com.test.openable", "com.test.openable.MainActivity", MenuMap.PASSIVE_MONITORING);
                break;
            case MenuMap.UNITY_GAME:
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
                startPackageForResult(launchIntent, "com.hk.ust.aed.multitasking3d", "com.unity3d.player.UnityPlayerActivity", MenuMap.UNITY_GAME);
            default:
                if(newState <= 3) {
                    parent.setTitle(menuTitles[newState]);
                    args.putInt("state", newState);
                    args.putStringArray("menu", getCurrentMenu(newState));
                    MenuFragment menu = new MenuFragment();
                    menu.setArguments(args);
                    parent.getSupportFragmentManager().beginTransaction().replace(R.id.frame, menu).commit();
                }
        }
    }

    public void startPackageForResult(Intent i, String pkg, String cls, int requestCode){
        i.setComponent(new ComponentName(pkg, cls));
        parent.startActivityForResult(i, requestCode);
    }
}