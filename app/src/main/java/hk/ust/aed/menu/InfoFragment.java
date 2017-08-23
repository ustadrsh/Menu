package hk.ust.aed.menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import hk.ust.aed.menu.adapters.RecyclerViewDataAdapter;
import hk.ust.aed.menu.models.SectionDataModel;
import hk.ust.aed.menu.models.SingleItemModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    MainActivity parent;
    MenuMap menuMap;
    public MenuMap.Screen fragmentState;

    ArrayList<SectionDataModel> allSampleData;

    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //set global params from getArguments()
        }
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_screen, container, false);

        RecyclerView my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);

        //TextView instructions = (TextView) view.findViewById(R.id.instructionsTxt);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        Toast toast = Toast.makeText(getContext(), "Button clicked", Toast.LENGTH_SHORT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (MainActivity) context;
        menuMap = parent.getMenuMap();
        Bundle args = getArguments();
        fragmentState = MenuMap.Screen.valueOf(args.getString("state", MenuMap.Screen.NULL.name()));
        parent.setTitle(menuMap.getTitle(fragmentState));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadData() {
        allSampleData = new ArrayList<SectionDataModel>();
        /*** ONLY ONE MODEL ADDED ***/

        SectionDataModel dm = new SectionDataModel();
        dm.setHeaderTitle("Screenshots");

        String[] pictureCaptions = {};
        String pictureName = "_screenshot_";

        Log.e("Preimagenameset:::", fragmentState.name());
        switch(fragmentState){
            case SRM_INFO:
                pictureName = "srm" + pictureName;
                pictureCaptions = new String[]{"1", "2"};
                break;
            case SWM_INFO:
                pictureName = "swm" + pictureName;
                pictureCaptions = new String[]{"1", "2", "3"};
                break;
            case MTT_INFO:
                pictureName = "mtt" + pictureName;
                pictureCaptions = new String[]{"1", "2", "3"};
                break;
            case PASSIVE_MONITORING_INFO:
                pictureName = "pm" + pictureName;
                pictureCaptions = new String[]{"1", "2", "3"};
                break;
            case UNITY_GAME_INFO:
                pictureName = "adm" + pictureName;
                pictureCaptions = new String[]{"1", "2", "3", "4"};
                break;
        }

        int[] defaultImageResources = {};

        ArrayList<SingleItemModel> section = new ArrayList<SingleItemModel>();
        for (int itemIndex = 0; itemIndex < pictureCaptions.length; itemIndex++) {
            section.add(new SingleItemModel(pictureCaptions[itemIndex], pictureName + String.valueOf(itemIndex) + ".png", R.drawable.android));
        }

        dm.setAllItemsInSection(section);
        /*** ***/

        allSampleData.add(dm);
    }

    public void startGame(){
        menuMap.newScreen(fragmentState, 0);
    }
}
