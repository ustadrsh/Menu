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
        switch(newFragmentState) {
            case -1: break;
            case MenuMap.PASSIVE_MONITORING_APP:
                Intent i = new Intent(); //parent.getPackageManager().getLaunchIntentForPackage("com.test.openable");
                i.setComponent(new ComponentName("com.test.openable","com.test.openable.MainActivity"));
                parent.startActivityForResult(i, 1);//null pointer check in case package name was not found
                break;
            default:
                if(newFragmentState <= 3) {
                    args.putInt("state", newFragmentState);
                    menu.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, menu).commit();
                }
        }
    }
}
