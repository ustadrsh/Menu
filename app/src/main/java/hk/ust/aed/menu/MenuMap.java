package hk.ust.aed.menu;

/**
 * Created by Administrator on 23/06/2017.
 */

class MenuMap {

    public static final int PASSIVE_MONITORING_APP = 4;
    public static final int GAME_1 = 5;

    private String[] menuTitles = {"Home", "Tests", "Account", "Settings"};
    private String[][] menuItems = {
            {"Tests", "Account", "Settings"},
            {"Game 1", "Game 2", "Game 3", "Passive Monitoring"},
            {"Scoreboard", "Usage statistics"},
            {"Recalibrate game", "Change username or password", "Privacy settings"}
    };
    private int[][] linkTo = {
            {1, 2, 3},
            {2, -1, -1, PASSIVE_MONITORING_APP},
            {3, -1},
            {-1, -1}
    };

/*    public String[] nextMenu(int fragmentState, int position){
        int state = linkTo[fragmentState][position];
        if(state == -1) return null;
        return menuItems[state];
    }*/

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