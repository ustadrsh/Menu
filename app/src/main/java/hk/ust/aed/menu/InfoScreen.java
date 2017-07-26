package hk.ust.aed.menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hk.ust.aed.menu.models.SectionDataModel;
import hk.ust.aed.menu.models.SingleItemModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoScreen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoScreen extends Fragment {
    ArrayList<SectionDataModel> allSampleData;

    private OnFragmentInteractionListener mListener;

    public InfoScreen() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InfoScreen newInstance() {
        InfoScreen fragment = new InfoScreen();
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
        createDummyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.info_screen, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    public void createDummyData() {
        for (int i = 1; i <= 1; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Screenshots");

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
            for (int j = 0; j <= 0; j++) {
                singleItem.add(new SingleItemModel("Item " + j, "URL " + j, R.drawable.android));
            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);
        }
    }
}
