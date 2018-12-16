package com.example.chenx.musc.fragment;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.chenx.musc.R;

import com.example.chenx.musc.model.Action;
import com.example.chenx.musc.model.Record;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.litepal.LitePal;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalyseWorkoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalyseWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyseWorkoutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerType;
    private Spinner spinnerItem;

    private ArrayList<String> data1;
    private ArrayList<String> data2;

    private List<Record> recordList;
    private List<Action> actionList;

    private PieChart pieChart;
    private PieChart groupChart;
    private BarChart actionChart;

    private RelativeLayout relativeLayout;

    private OnFragmentInteractionListener mListener;

    public AnalyseWorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalyseWorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalyseWorkoutFragment newInstance(String param1, String param2) {
        AnalyseWorkoutFragment fragment = new AnalyseWorkoutFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_analyse_workout, container, false);

        spinnerType=view.findViewById(R.id.spinnerType);
        spinnerItem=view.findViewById(R.id.spinnerItem);

        relativeLayout=view.findViewById(R.id.showChart);

        initSpinner1();
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,data1);
        spinnerType.setAdapter(adapter1);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initSpinner2(spinnerType.getSelectedItem().toString());
                ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, data2);
                spinnerItem.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerType.getSelectedItem().toString())
                {
                    case "All":{
                        //replaceFragment(new PieCharFragment());
                        showPieChart();break;
                    }
                    case "Group":{
                        showGroupChart(spinnerItem.getSelectedItem().toString());break;
                    }
                    case "Action":{
                        showActionChart(spinnerItem.getSelectedItem().toString());break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void showActionChart(String item){
        actionChart=new BarChart((getContext()));
        initActionChart(item);
        relativeLayout.removeAllViews();
        relativeLayout.addView(actionChart,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void showGroupChart(String item){
        groupChart=new PieChart(getContext());
        initGroupChart(item);
        relativeLayout.removeAllViews();
        relativeLayout.addView(groupChart,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void showPieChart(){
        pieChart=new PieChart(getContext());
        initPieChart();
        relativeLayout.removeAllViews();
        relativeLayout.addView(pieChart,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }



    private void initRecords(){
        // List<Action> actions=LitePal.findAll(Action.class);
        recordList=new ArrayList<>();
        actionList=new ArrayList<>();
        actionList=LitePal.findAll(Action.class);
        //recordList=LitePal.findAll(Record.class);
        for(Action action : actionList){
            List<Record> records=action.getRecords();
            for(Record record : records){
                record.setAction(action);
                recordList.add(record);
            }
        }
    }

    private void initPieChart()
    {
        List<PieEntry> entries = new ArrayList<>();
        int total=LitePal.findAll(Action.class).size();

        ArrayList<Integer> colors = new ArrayList<>();

        List<Float> values=new ArrayList<>() ;
        List<String> labels=new ArrayList<>();

        Random random = new Random();
        initRecords();
        for(Record r : recordList)
        {
            String label=r.getAction().getGroup();
            if(!labels.contains(label)){
                labels.add(label);
                int result=LitePal.where("group = ?",label).find(Action.class).size();
                float value=(float)result/(float)total;
                values.add(value);

                entries.add(new PieEntry(value, label));
                int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                colors.add(ranColor);
            }
        }


        PieDataSet set = new PieDataSet(entries, "Muscles");
        set.setColors(colors);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh

    }

    private void initGroupChart(String item)
    {
        List<PieEntry> entries = new ArrayList<>();
        int total=LitePal.where("group = ?",item ).find(Action.class).size();

        ArrayList<Integer> colors = new ArrayList<>();

        List<Float> values=new ArrayList<>() ;
        List<String> labels=new ArrayList<>();

        Random random = new Random();
        initRecords();

        for(Record r : recordList)
        {
            if(r.getAction().getGroup().equals(item)){
                String label=r.getAction().getName();
                if(!labels.contains(label)){
                    labels.add(label);
                    int result=LitePal.where("name = ?",label).find(Action.class).size();
                    float value=(float)result/(float)total;
                    values.add(value);

                    entries.add(new PieEntry(value, label));
                    int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                    colors.add(ranColor);
                }
            }
        }


        PieDataSet set = new PieDataSet(entries, "Muscles");
        set.setColors(colors);
        PieData data = new PieData(set);
        groupChart.setData(data);
        groupChart.invalidate(); // refresh

    }

    private void initActionChart(String item)
    {
        List<Integer> times=new ArrayList<>();
        List<Float> weights=new ArrayList<>();
        List<String> dates=new ArrayList<>();

        List<BarEntry> entriesTimes=new ArrayList<>();
        List<BarEntry> entriesWeights=new ArrayList<>();

        initRecords();
        for(Record r : recordList) {
            if (r.getAction().getName().equals(item)) {
                times.add(Integer.valueOf(r.getTimes()));
                weights.add(Float.valueOf(r.getWoWeight()));
                SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM");
                dates.add(sdfr.format(r.getDate()));
            }
        }

        for(int i=0;i<times.size();i++){
            entriesTimes.add(new BarEntry(i,times.get(i).intValue()));
            entriesWeights.add(new BarEntry(i,weights.get(i).floatValue()));
        }

        BarDataSet set1=new BarDataSet(entriesTimes,"Times");
        BarDataSet set2=new BarDataSet(entriesWeights,"Weights");

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset

        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        actionChart.setData(data);
        actionChart.groupBars(1980f, groupSpace, barSpace); // perform the "explicit" grouping

        XAxis xAxis = actionChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        actionChart.invalidate(); // refresh


    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.showChart,fragment);
        transaction.commit();
    }

    public void initSpinner1(){
        data1=new ArrayList<String>();
        data1.add("All");
        data1.add("Group");
        data1.add("Action");
    }

    public void initSpinner2(String x){
        data2=new ArrayList<String>();
        switch (x){
            case "All":
            {data2.add("null");break;}
            case "Group": {
                XmlResourceParser parser = getResources().getXml(R.xml.action);
                int type;
                try {
                    type = parser.getEventType();
                    while (type != XmlPullParser.END_DOCUMENT) {
                        if (type == XmlPullParser.START_TAG) {
                            String nodeName = parser.getName();
                            if ("group".equals(nodeName)) {
                                data2.add(parser.getAttributeValue(0));
                            }
                        }
                        type = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "Action": {
                XmlResourceParser parser = getResources().getXml(R.xml.action);
                int type;
                try {
                    type = parser.getEventType();
                    while (type != XmlPullParser.END_DOCUMENT) {
                        if (type == XmlPullParser.START_TAG) {
                            String nodeName = parser.getName();
                            if ("action".equals(nodeName)) {
                                data2.add(parser.nextText());
                            }
                        }
                        type = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
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
