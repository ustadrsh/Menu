package hk.ust.aed.menu;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hk.ust.aed.menu.MenuMap.Screen.MTT_APP;
import static hk.ust.aed.menu.MenuMap.Screen.SRM_APP;
import static hk.ust.aed.menu.MenuMap.Screen.SWM_APP;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scoreboard extends Fragment {

    private MainActivity parent;
    private static hk.ust.aed.menu.MenuMap menuMap;
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private MenuMap.Screen fragmentState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scoreboard, container, false);
        //Bundle args = getArguments();

        //new GetLocalData(context, app, trialId).getJSONDataObject()
        /*la = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, info);
        lv = (ListView) view.findViewById(R.id.list);
        lv.setAdapter(la);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clicked = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(getActivity(), clicked, Toast.LENGTH_LONG).show();
                    }
                }
        );*/

        List<GroupItem> items = new ArrayList<GroupItem>();

        MenuMap.Screen app = MenuMap.Screen.NULL;
        switch(fragmentState){
            case MTT_SCORES: app = MTT_APP; break;
            case SWM_SCORES: app = SWM_APP; break;
            case SRM_SCORES: app = SRM_APP; break;
        }
        for(long trialNum = 0; trialNum < (long) RunApp.getInputParamsFor(app).get("latestTrialNum"); trialNum++) {
            Map<String, Object> scores = null;
            try {
                scores = (new GetLocalData(parent, app, trialNum)).getMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (scores == null) {
                continue;
            } else {
                GroupItem item = new GroupItem();
                items.add(item);
                item.title = "Trial " + String.valueOf(trialNum + 1);
                Log.e("trial" + trialNum + " ok for", app.name());
                Log.e("Trial " + String.valueOf(trialNum), printObjectWithTabs(scores, 0));
                /*for (Map.Entry<String, Object> entry : RunApp.getInputParamsFor(app).entrySet()) {
                    Log.e(entry.getKey(), String.valueOf(entry.getValue()));
                }*/
                ChildItem child = new ChildItem();
                child.title = "Date unknown";
                if(scores.containsKey("unixTimeMillis")){
                    SimpleDateFormat sdf = new SimpleDateFormat("'Logged at' h:mm a 'on\n'MMMM d, yyyy");
                    child.title = sdf.format(scores.remove("unixTimeMillis"));
                }
                child.hint = printObjectWithTabs(scores, 0);

                item.items.add(child);
            }
        }

        // Populate our list with groups and it's children
        /*for(int i = 1; i < 100; i++) {
            GroupItem item = new GroupItem();

            item.title = "Group " + i;

            for(int j = 0; j < i; j++) {
                ChildItem child = new ChildItem();
                child.title = "Awesome item " + j;
                child.hint = "Too awesome";

                item.items.add(child);
            }

            items.add(item);
        }*/

        adapter = new ExampleAdapter(getContext());
        adapter.setData(items);

        listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        return view;
    }

    private String printObjectWithTabs(Object object, int tabs) {
        String txt = "";
        if (object instanceof HashMap) {
            txt += "\n";
            for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) object).entrySet()) {
                txt += printStringWithTabs(entry.getKey() + ": " + printObjectWithTabs(entry.getValue(), tabs + 2), tabs + 1);
            }
        } else if (object instanceof ArrayList) {
            txt += "\n";
            for (int i = 0; i < ((ArrayList) object).size(); i++) {
                txt += printStringWithTabs(String.valueOf(i) + ": " + printObjectWithTabs(((ArrayList) object).get(i), tabs + 2), tabs + 1);
            }
        } else {
            txt += printStringWithTabs(String.valueOf(object), 0);
        }
        return txt;
    }

    private String printStringWithTabs(String s, int tabs) {
        String txt = "";
        for(int tab = 0; tab < tabs; tab++){
            txt += "\t";
        }
        txt += s + "\n";
        return txt;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        parent = (MainActivity) context;
        menuMap = parent.getMenuMap();
        Bundle args = getArguments();
        fragmentState = MenuMap.Screen.valueOf(args.getString("state", MenuMap.Screen.NULL.name()));
        parent.setTitle(menuMap.getTitle(fragmentState));
    }

    /*
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
    }*/

    /*public void startPackageForResult(Intent i, String pkg, String cls, int requestCode){
        i.setComponent(new ComponentName(pkg, cls));
        parent.startActivityForResult(i, requestCode);
    }*/

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.score_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

}