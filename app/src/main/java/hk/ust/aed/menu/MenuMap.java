package hk.ust.aed.menu;

/**
 * Created by Administrator on 23/06/2017.
 */

class MenuMap {

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

    public String getMenuTitle(int fragmentState){
        if(fragmentState >= 0 && fragmentState < menuTitles.length){
            return menuTitles[fragmentState];
        }
        return null;
    }

    public String[] getCurrentMenu(int fragmentState){
        return menuItems[fragmentState];
    }

    public int getNewState(int fragmentState, int clickedIndex){
        return linkTo[fragmentState][clickedIndex];
    }
}