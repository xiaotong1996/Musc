package com.example.chenx.musc.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chenx.musc.MyApplication;
import com.example.chenx.musc.R;
import com.example.chenx.musc.model.Action;
import com.example.chenx.musc.model.Record;
import com.example.chenx.musc.model.User;
import com.example.chenx.musc.tool.RecordAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListWorkoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListWorkoutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private SharedPreferences pref;

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private List<Record> recordList;
    //private List<Action> actionList;

    private void initRecords(){
        pref=PreferenceManager.getDefaultSharedPreferences(getContext());
        String email=pref.getString("email","");
        User user=LitePal.where("email = ?",email).find(User.class).get(0);


        //List<Action> actions=LitePal.findAll(Action.class);
        //recordList=new ArrayList<>();
        //actionList=new ArrayList<>();
        //actionList=LitePal.findAll(Action.class);
        recordList=LitePal.where("user_id = ?",String.valueOf(user.getId())).find(Record.class,true);
        /*
        for(Action action : actionList){
            List<Record> records=action.getRecords();
            for(Record record : records){
                record.setAction(action);
                recordList.add(record);
            }
        }*/
    }

    private OnFragmentInteractionListener mListener;

    public ListWorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListWorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListWorkoutFragment newInstance(String param1, String param2) {
        ListWorkoutFragment fragment = new ListWorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    public void refresh()
    {
        initRecords();
        RecordAdapter adapter=new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_workout, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycleViewRecordList) ;
        LinearLayoutManager layoutManager=new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        refresh();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
