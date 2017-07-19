package hk.ust.aed.menu;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private final int PASSIVE_MONITORING_APP = 100;

    private MainActivity parent;
    private static hk.ust.aed.menu.MenuMap menuMap;
    private ListAdapter la;
    private ListView lv;
    int fragmentState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);
        la = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, menuMap.getCurrentMenu(fragmentState));
        lv = (ListView) view.findViewById(R.id.list);
        lv.setAdapter(la);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clicked = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(getActivity(), clicked, Toast.LENGTH_LONG).show();
                        newFragment(position);
                    }
                }
        );
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        parent = (MainActivity) context;
        Bundle args = getArguments();
        fragmentState = args.getInt("state", 0);
        menuMap = parent.getMenuMap();
        String menuTitle = menuMap.getMenuTitle(fragmentState);
        if(menuTitle != null){
            parent.setTitle(menuTitle);
        }
    }

    public void newFragment(int clickedIndex){
        MenuFragment menu = new MenuFragment();
        Bundle args = new Bundle();
        int newFragmentState = menuMap.getNewState(fragmentState, clickedIndex);
        Intent launchIntent = new Intent();
        switch (newFragmentState) {
            case -1: break;
            case MenuMap.MTT:
                startPackageForResult(launchIntent, "hk.ust.aed.mtt", "hk.ust.aed.mtt.MainActivity", MenuMap.MTT);
                break;
            case MenuMap.SWM:
                DownloadApp d = new DownloadApp(parent.getApplicationContext(), this);
                d.execute("https://www.dl.dropboxusercontent.com/s/ufktun87uvppimv/swm_hk.ust.aed.swm.apk?dl=0");
                break;
            case MenuMap.SRM:
                startPackageForResult(launchIntent, "hk.ust.aed.srm", "hk.ust.aed.srm.AndroidLauncher", MenuMap.SRM);
                break;
            case MenuMap.PASSIVE_MONITORING:
                startPackageForResult(launchIntent, "com.test.openable", "com.test.openable.MainActivity", MenuMap.PASSIVE_MONITORING);
                break;
            case MenuMap.UNITY_GAME:
                File outFile = new File(getContext().getExternalFilesDir(null), "UNITY_PARAMS.json");
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
                if(newFragmentState <= 3) {
                    args.putInt("state", newFragmentState);
                    menu.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, menu).commit();
                }
        }
    }

    public void startPackageForResult(Intent i, String pkg, String cls, int requestCode){
        i.setComponent(new ComponentName(pkg, cls));
        parent.startActivityForResult(i, requestCode);
    }
}