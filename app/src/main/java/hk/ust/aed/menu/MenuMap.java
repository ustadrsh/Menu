package hk.ust.aed.menu;

import android.os.Bundle;
import android.util.Log;

import static hk.ust.aed.menu.MenuMap.Screen.ACCOUNT;
import static hk.ust.aed.menu.MenuMap.Screen.CHANGE_CREDENTIALS;
import static hk.ust.aed.menu.MenuMap.Screen.INIT;
import static hk.ust.aed.menu.MenuMap.Screen.MTT_APP;
import static hk.ust.aed.menu.MenuMap.Screen.MTT_INFO;
import static hk.ust.aed.menu.MenuMap.Screen.MTT_SCORES;
import static hk.ust.aed.menu.MenuMap.Screen.NULL;
import static hk.ust.aed.menu.MenuMap.Screen.PASSIVE_MONITORING_APP;
import static hk.ust.aed.menu.MenuMap.Screen.PASSIVE_MONITORING_INFO;
import static hk.ust.aed.menu.MenuMap.Screen.PRIVACY;
import static hk.ust.aed.menu.MenuMap.Screen.RECALIBRATE;
import static hk.ust.aed.menu.MenuMap.Screen.SCOREBOARD;
import static hk.ust.aed.menu.MenuMap.Screen.SCORES;
import static hk.ust.aed.menu.MenuMap.Screen.SETTINGS;
import static hk.ust.aed.menu.MenuMap.Screen.SRM_APP;
import static hk.ust.aed.menu.MenuMap.Screen.SRM_INFO;
import static hk.ust.aed.menu.MenuMap.Screen.SRM_SCORES;
import static hk.ust.aed.menu.MenuMap.Screen.SWM_APP;
import static hk.ust.aed.menu.MenuMap.Screen.SWM_INFO;
import static hk.ust.aed.menu.MenuMap.Screen.SWM_SCORES;
import static hk.ust.aed.menu.MenuMap.Screen.TESTS;
import static hk.ust.aed.menu.MenuMap.Screen.UNITY_GAME_APP;
import static hk.ust.aed.menu.MenuMap.Screen.UNITY_GAME_INFO;
import static hk.ust.aed.menu.MenuMap.Screen.USAGE_STATISTICS;

/**
 * Created by Administrator on 23/06/2017.
 */

class MenuMap {

    public MainActivity parent;

    public enum Screen {
        NULL,
        INIT,
        TESTS, ACCOUNT, SCORES, SETTINGS,
        SCOREBOARD, USAGE_STATISTICS, RECALIBRATE, CHANGE_CREDENTIALS, PRIVACY,
        SRM_SCORES, SWM_SCORES, MTT_SCORES,
        PASSIVE_MONITORING_INFO, MTT_INFO, SWM_INFO, SRM_INFO, UNITY_GAME_INFO,
        PASSIVE_MONITORING_APP, MTT_APP, SWM_APP, SRM_APP, UNITY_GAME_APP,
    }

    public Screen currentScreen;

    /*public String[][] titles = {
            {"Init", "Tests", "Account", "Settings",
            "Passive Monitoring", "MTT", "SWM", "SRM", "3D Game"},
            {"Scoreboard", "Usage statistics"},
            {"Recalibrate game", "Change username or password", "Privacy settings"}
    };*/

    public MenuMap(MainActivity parent){
        this.parent = parent;
    }

    public String getTitle(Screen state){
        String title = state.name();
        if(true) { //Todo: UPDATE
            title = title.replace("_", " ");
        }
        else{
            char[] arr = title.replace("_", " ").toLowerCase().toCharArray();
            for(int i = 0; i < arr.length; i++){
                if(i == 0){
                    arr[i] = Character.toUpperCase(arr[i]);
                }
                else if(arr[i-1] == ' '){
                    arr[i] = Character.toUpperCase(arr[i]);
                }
            }
            title = new String(arr);
        }
        return title;
    }

    public Screen[] getLinks(Screen state){
        switch (state){
            case NULL: return (new Screen[] {INIT});
            case INIT: return (new Screen[]{TESTS, ACCOUNT, SCORES, SETTINGS});
            case TESTS: return (new Screen[]{PASSIVE_MONITORING_INFO, MTT_INFO, SWM_INFO, SRM_INFO, UNITY_GAME_INFO});
            case ACCOUNT: return (new Screen[]{SCOREBOARD, USAGE_STATISTICS});
            case SCORES: return (new Screen[] {SWM_SCORES, SRM_SCORES, MTT_SCORES});
            case SETTINGS: return (new Screen[]{RECALIBRATE, CHANGE_CREDENTIALS, PRIVACY});
            case PASSIVE_MONITORING_INFO: return (new Screen[] {PASSIVE_MONITORING_APP});
            case MTT_INFO: return (new Screen[] {MTT_APP});
            case SWM_INFO: return (new Screen[] {SWM_APP});
            case SRM_INFO: return (new Screen[] {SRM_APP});
            case UNITY_GAME_INFO: return (new Screen[] {UNITY_GAME_APP});
            /*case SCOREBOARD: break;
            case USAGE: break;
            case RECALIBRATE: break;
            case CHANGE_CREDENTIALS: break;
            case PRIVACY: break;*/
        }
        return null;
    }

    public void newScreen(Screen currentScreen, int selectedItemIndex){
        Bundle args = new Bundle();
        final Screen newScreen = getLinks(currentScreen)[selectedItemIndex];
        //parent.setTitle(getTitle(newScreen));
        args.putString("state", newScreen.name());

        if( newScreen.ordinal() <= PRIVACY.ordinal() ){
            MenuFragment menu = new MenuFragment();

            String[] menuTitles = new String[getLinks(newScreen).length];
            for (int i = 0; i < menuTitles.length; i++){
                menuTitles[i] = getTitle(getLinks(newScreen)[i]);
            }

            args.putStringArray("menu", menuTitles);
            menu.setArguments(args);
            parent.getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame, menu)
                    .commit();
        }
        else if( newScreen.ordinal() <= MTT_SCORES.ordinal() ){
            Scoreboard scoreboard = new Scoreboard();
            scoreboard.setArguments(args);
            parent.getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame, scoreboard)
                    .commit();
            Log.e("Opening", "Score fragrenage");
        }
        else if( newScreen.ordinal() <= UNITY_GAME_INFO.ordinal() ){
            InfoFragment info = new InfoFragment();
            info.setArguments(args);
            parent.getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame, info)
                    .commit();
        }
        else if( newScreen.ordinal() <= UNITY_GAME_APP.ordinal() ){
            //startPackageForResult(newScreen);
            new RunApp(parent, newScreen);
        }
        currentScreen = newScreen;
        Log.e("menuMap going to...", currentScreen.name());
    }

    public void backPressed(){
        if(currentScreen == NULL || currentScreen == INIT) {
            parent.finish();
        }
        else{
            if (parent.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                parent.getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void startPackageForResult(Screen app){
        /*switch(app){
            case SWM_APP:
                d.execute("https://www.dl.dropboxusercontent.com/s/ufktun87uvppimv/swm_hk.ust.aed.swm.apk?dl=0");
                return;
            case SRM_APP:
                d.execute("https://www.dl.dropboxusercontent.com/s/vdi2ryo6ezd7f1f/hk.ust.aed.srm.apk?dl=0");
                //pkg = "hk.ust.aed.srm";
                //cls = "hk.ust.aed.srm.AndroidLauncher";
                break;
            case MTT_APP:
                d.execute("https://www.dl.dropboxusercontent.com/s/zg6qjuek52x58b6/hk.ust.aed.mtt.apk?dl=0");
                break;
            case PASSIVE_MONITORING_APP:
                d.execute("https://www.dl.dropboxusercontent.com/s/8ce07wf6utzgufy/hk.ust.aed.alzheimerpassivemonitoring.apk?dl=0");
                //pkg = "com.test.openable";
                //cls = "com.test.openable.MainActivity";
                break;
            case UNITY_GAME_APP:
                d.execute("https://www.dl.dropboxusercontent.com/s/m51a30bx919f59r/com.hk.ust.aed.multitasking3d.apk?dl=0");
                //pkg = "com.hk.ust.aed.multitasking3d";
                //cls = "com.unity3d.player.UnityPlayerActivity";
                break;
        }*/
    }

    public Screen getScreenFromOrdinal(){
        return null;
    }
}